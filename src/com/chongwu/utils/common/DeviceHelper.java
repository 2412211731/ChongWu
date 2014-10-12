package com.chongwu.utils.common;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

import com.chongwu.utils.common.MD5_AES.Data;

public class DeviceHelper
{
  private Context context;
  private static DeviceHelper deviceHelper;

  public DeviceHelper(Context context)
  {
    this.context = context.getApplicationContext();
  }

  public static DeviceHelper getInstance(Context c) {
    if (deviceHelper == null) {
      deviceHelper = new DeviceHelper(c);
    }
    return deviceHelper;
  }

  public boolean isRooted()
  {
    return false;
  }

  public String getMacAddress() {
    WifiManager wifi = (WifiManager)this.context.getSystemService("wifi");
    if (wifi == null) {
      return null;
    }

    WifiInfo info = wifi.getConnectionInfo();
    if (info != null) {
      String mac = info.getMacAddress();
      return mac == null ? null : mac;
    }
    return null;
  }

  public String getModel()
  {
    return Build.MODEL;
  }

  public String getFactory()
  {
    return Build.MANUFACTURER;
  }

  public String getDeviceId()
  {
    TelephonyManager phone = (TelephonyManager)this.context
      .getSystemService("phone");
    if (phone == null) {
      return null;
    }

    String deviceId = phone.getDeviceId();
    String backId = "";
    if (deviceId != null) {
      backId = new String(deviceId);
      backId = backId.replace("0", "");
    }

    if (((deviceId == null) || (backId.length() <= 0)) && (Build.VERSION.SDK_INT >= 9)) {
      try {
        Class c = Class.forName("android.os.SystemProperties");
        Method get = c.getMethod("get", new Class[] { String.class, String.class });
        deviceId = (String)get.invoke(c, new Object[] { "ro.serialno", "unknown" });
      } catch (Throwable t) {
        t.printStackTrace();
        deviceId = null;
      }
    }

    return deviceId;
  }

  public String getDeviceData()
  {
    String data = getModel() + "|" + getOSVersion() + "|" + getFactory() + "|" + getCarrier() + "|" + getScreenSize();
    String deviString = getDeviceKey();
    return Base64AES(data, deviString.substring(0, 16));
  }

  public String Base64AES(String msg, String key)
  {
    String result = null;
    try {
      result = Base64.encodeToString(Data.AES128Encode(key, msg), 0);

      if (result.contains("\n"))
        result = result.replace("\n", "");
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    return result;
  }

  public String getOSVersion()
  {
    return String.valueOf(Build.VERSION.SDK_INT);
  }

  public String getScreenSize()
  {
    DisplayMetrics dm2 = this.context.getResources().getDisplayMetrics();

    if (this.context.getResources().getConfiguration().orientation == 1) {
      return dm2.widthPixels + "x" + dm2.heightPixels;
    }

    return dm2.heightPixels + "x" + dm2.widthPixels;
  }

  public String getCarrier()
  {
    TelephonyManager telephonyManager = (TelephonyManager)this.context.getSystemService("phone");
    String operator = telephonyManager.getSimOperator();
    if (TextUtils.isEmpty(operator)) {
      operator = "-1";
    }
    return operator;
  }

  public String getNetworkType()
  {
    ConnectivityManager conn = (ConnectivityManager)this.context
      .getSystemService("connectivity");
    if (conn == null) {
      return null;
    }

    NetworkInfo network = conn.getActiveNetworkInfo();
    if ((network == null) || (!network.isAvailable())) {
      return null;
    }

    int type = network.getType();
    if (1 == type) {
      return "wifi";
    }

    if (type == 0)
    {
      String proxy = Proxy.getDefaultHost();
      String wap = "";
      if ((proxy != null) && (proxy.length() > 0)) {
        wap = " wap";
      }

      return (isFastMobileNetwork() ? "3G" : "2G") + wap;
    }

    return null;
  }

  public int getPlatformCode()
  {
    return 1;
  }

  private boolean isFastMobileNetwork() {
    TelephonyManager phone = (TelephonyManager)this.context
      .getSystemService("phone");
    if (phone == null) {
      return false;
    }

    switch (phone.getNetworkType()) {
    case 7:
      return false;
    case 4:
      return false;
    case 2:
      return false;
    case 5:
      return true;
    case 6:
      return true;
    case 1:
      return false;
    case 8:
      return true;
    case 10:
      return true;
    case 9:
      return true;
    case 3:
      return true;
    case 14:
      return true;
    case 12:
      return true;
    case 15:
      return true;
    case 11:
      return false;
    case 13:
      return true;
    case 0:
      return false;
    }
    return false;
  }

  public JSONArray getRunningApp() {
    JSONArray appNmes = new JSONArray();

    ActivityManager am = (ActivityManager)this.context
      .getSystemService("activity");
    if (am == null) {
      return appNmes;
    }

    List<RunningAppProcessInfo> apps = am.getRunningAppProcesses();
    if (apps == null) {
      return appNmes;
    }

    for (ActivityManager.RunningAppProcessInfo app : apps) {
      appNmes.put(app.processName);
    }

    return appNmes;
  }

  public String getRunningAppStr() throws JSONException {
    JSONArray apps = getRunningApp();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < apps.length(); i++) {
      if (i > 0) {
        sb.append(',');
      }
      sb.append(String.valueOf(apps.get(i)));
    }
    return sb.toString();
  }

  public String getDeviceKey() {
    try {
      String mac = getMacAddress();
      String udid = getDeviceId();
      String model = getModel();
      String data = mac + ":" + udid + ":" + model;
      byte[] bytes = Data.SHA1(data);
      return Data.byteToHex(bytes);
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

  public String getPackageName()
  {
    return this.context.getPackageName();
  }

  public String getAppName()
  {
    String appName = this.context.getApplicationInfo().name;
    if (appName != null) {
      return appName;
    }

    int appLbl = this.context.getApplicationInfo().labelRes;
    if (appLbl > 0) {
      appName = this.context.getString(appLbl);
    }

    return appName;
  }

  public int getAppVersion()
  {
    try {
      PackageManager pm = this.context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(this.context.getPackageName(), 0);
      return pi.versionCode;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return 0;
  }

  public String getAppVersionName()
  {
    try {
      PackageManager pm = this.context.getPackageManager();
      PackageInfo pi = pm.getPackageInfo(this.context.getPackageName(), 0);
      return pi.versionName;
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return "1.0";
  }

  public ArrayList<HashMap<String, String>> getInstalledApp(boolean includeSystemApp)
  {
    ArrayList apps = 
      new ArrayList();
    try {
      PackageManager pm = this.context.getPackageManager();
      List<PackageInfo> pis = pm.getInstalledPackages(0);
      for (PackageInfo pi : pis) {
        if ((!includeSystemApp) && (isSystemApp(pi)))
        {
          continue;
        }
        HashMap app = new HashMap();
        app.put("pkg", pi.packageName);
        app.put("name", pi.applicationInfo.loadLabel(pm).toString());
        app.put("version", pi.versionName);
        apps.add(app);
      }
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return apps;
  }

  private boolean isSystemApp(PackageInfo pi) {
    boolean isSysApp = (pi.applicationInfo.flags & 0x1) == 1;
    boolean isSysUpd = (pi.applicationInfo.flags & 0x80) == 1;
    return (isSysApp) || (isSysUpd);
  }

  public String getNetworkOperator() {
    TelephonyManager telephonyManager = (TelephonyManager)this.context.getSystemService("phone");
    String operator = telephonyManager.getNetworkOperator();
    return operator;
  }

  public String getTopTaskPackageName() {
    try {
      ActivityManager am = (ActivityManager)this.context.getSystemService(
        "activity");
      ActivityManager.RunningTaskInfo task = (ActivityManager.RunningTaskInfo)am.getRunningTasks(1).get(0);
      return task.topActivity.getPackageName();
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return null;
  }

  public boolean getSdcardState()
  {
    return "mounted".equals(
      Environment.getExternalStorageState());
  }

  public String getSdcardPath()
  {
    return Environment.getExternalStorageDirectory().getAbsolutePath();
  }
}