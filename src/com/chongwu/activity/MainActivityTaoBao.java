package com.chongwu.activity;

import java.io.File;
import java.util.Date;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.json.JSONException;
import org.json.JSONObject;

import com.chongwu.R;
import com.chongwu.activity.MainActivityTaoBao;
import com.chongwu.config.Config;
import com.chongwu.config.Constants;
import com.chongwu.utils.common.AppSharedPreference;
import com.taobao.top.android.Installation;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.TopParameters;
import com.taobao.top.android.api.ApiError;
import com.taobao.top.android.api.FileItem;
import com.taobao.top.android.api.TopApiListener;
import com.taobao.top.android.api.TopTqlListener;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthActivity;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityTaoBao extends AuthActivity {

	private static final String TAG = "MainActivity";
	private TextView topResult;
	private Long userId;
	private String nick;
	private TopAndroidClient client=TopAndroidClient.getAndroidClientByAppKey(Config.app_key);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_taobao);
        
        Button btn=(Button)this.findViewById(R.id.button1);
        topResult = (TextView)this.findViewById(R.id.topResult);
        if(nick!=null){
        	this.setTopResultText(nick);
        }
        btn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				client.authorize(MainActivityTaoBao.this);
				MainActivityTaoBao.this.finish();
			}});
        
        Button apibtn=(Button)this.findViewById(R.id.button2);
        apibtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				TopParameters params=new TopParameters();
				params.setMethod("taobao.user.buyer.get");
				params.addFields("user_id","nick","sex","buyer_credit");
				if(userId==null){
					Toast t=Toast.makeText(MainActivityTaoBao.this,"请先授权", Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				client.api(params, userId, new TopApiListener(){

					@Override
					public void onComplete(JSONObject json) {
						Log.e(TAG, json.toString());
						Toast t=Toast.makeText(MainActivityTaoBao.this,json.toString(), Toast.LENGTH_SHORT);
						t.show();
						setTopResultText(json.toString());
					}

					@Override
					public void onError(ApiError error) {
						Log.e(TAG, error.toString());
						Toast t=Toast.makeText(MainActivityTaoBao.this,error.toString(), Toast.LENGTH_SHORT);
						t.show();
						setTopResultText(error.toString());
					}

					@Override
					public void onException(Exception e) {
						setTopResultText(e.getMessage());
						
					}}, false);
			}});
        
        Button uuidbtn=(Button)this.findViewById(R.id.button3);
        uuidbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String uuid=Installation.id(MainActivityTaoBao.this);
				Toast t=Toast.makeText(MainActivityTaoBao.this,uuid, Toast.LENGTH_SHORT);
				t.show();
				
			}});
        
        Button asyncapibtn=(Button)this.findViewById(R.id.button4);
        asyncapibtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				TopParameters params=new TopParameters();
				params.setMethod("taobao.user.buyer.get");//API方法
				params.addFields("nick","sex");//返回字段
				if(userId==null){
					Toast t=Toast.makeText(MainActivityTaoBao.this,"请先授权", Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				client.api(params, userId, new TopApiListener(){

					@Override
					public void onComplete(JSONObject json) {
						Log.e(TAG, json.toString());
						setTopResultText(json.toString());
						
					}

					@Override
					public void onError(ApiError error) {
						Log.e(TAG, error.toString());
						setTopResultText(error.toString());
					}

					@Override
					public void onException(Exception e) {
						setTopResultText(e.getMessage());
						
					}}, true);
				
			
			
			}});
        
        Button refbtn=(Button)this.findViewById(R.id.button5);
        refbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				client.refreshToken(userId, new AuthorizeListener(){

					@Override
					public void onComplete(AccessToken accessToken) {
						Log.e(TAG, accessToken.getStartDate().toGMTString());
						String r2=accessToken.getAdditionalInformation().get(AccessToken.KEY_R2_EXPIRES_IN);
						setTopResultText(accessToken.getStartDate().toGMTString()+" r2:"+r2);
					}

					@Override
					public void onError(AuthError e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAuthException(AuthException e) {
						// TODO Auto-generated method stub
						
					}}, false);
			}});
        
        Button arefbtn=(Button)this.findViewById(R.id.button6);
        arefbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				client.refreshToken(userId, new AuthorizeListener(){

					@Override
					public void onComplete(AccessToken accessToken) {
						Log.e(TAG, accessToken.getStartDate().toGMTString());
						String r2=accessToken.getAdditionalInformation().get(AccessToken.KEY_R2_EXPIRES_IN);
						setTopResultText(accessToken.getStartDate().toGMTString()+" r2:"+r2);
					}

					@Override
					public void onError(AuthError error) {
						setTopResultText(error.getErrorDescription());
						
					}

					@Override
					public void onAuthException(AuthException e) {
						setTopResultText(e.getMessage());
						
					}}, true);
			}});
        
        Button asynctql=(Button)this.findViewById(R.id.button7);
        asynctql.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String ql="{select num,price,type,stuff_status,title from item where num_iid=15435709307}";
				ql+="{select num,price,type,stuff_status,title from item where num_iid=17546088521}";
				client.tql(ql, userId, new TopTqlListener(){

					@Override
					public void onComplete(String result) {
						if (TextUtils.isEmpty(result)) {
							return;
						}
						String[] array = result.split("\r\n");
						String title = "";
						for (int i = 0; i < array.length; i++) {
							JSONObject json;
							try {
								json = new JSONObject(array[i]);
								JSONObject j = json
										.optJSONObject("item_get_response");
								if (j != null) {
									j = j.optJSONObject("item");
									if (j != null) {
										String t = j.optString("title");
										title += t;
										title += "\n";
									}
								}
							} catch (JSONException e) {
								Log.e(TAG, e.getMessage(), e);
							}

						}

						setTopResultText(title);

					}

					@Override
					public void onException(Exception e) {
						// TODO Auto-generated method stub
						
					}}, true);
			}});
        
        Button tql=(Button)this.findViewById(R.id.button8);
        tql.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String ql="select num,price,type,stuff_status,title from item where num_iid=1500009902569";
				client.tql(ql, userId, new TopTqlListener(){

					@Override
					public void onComplete(String result) {
						setTopResultText(result);
						
					}

					@Override
					public void onException(Exception e) {
						// TODO Auto-generated method stub
						
					}}, false);
			}});
        
        Button showAuth=(Button)this.findViewById(R.id.button9);
        showAuth.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				ConcurrentHashMap<Long, AccessToken>  map=client.getTokenStore();
				Set<Long> keys=map.keySet();
				String ret="";
				for(Long key:keys){
					ret+=key.toString();
					ret+="=";
					ret+=map.get(key).getValue();
					ret+=";";
				}
				setTopResultText(ret);
			}
        });
        
        Button commitBtn=(Button)this.findViewById(R.id.button10);
        commitBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				EditText et=(EditText)findViewById(R.id.editText1);
				String id=et.getText().toString();
				if(TextUtils.isDigitsOnly(id)){
					userId=Long.parseLong(id);
				}
			}
        });
        
        Button uploadbtn=(Button)this.findViewById(R.id.button11);
        uploadbtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				TopParameters params=new TopParameters();
				params.setMethod("taobao.item.img.upload");//api方法
				params.addParam("num_iid", "1500009902569");//业务参数
				File file=new File(getFilesDir(), "test.jpg");
				if(!file.exists()){
					Toast t=Toast.makeText(MainActivityTaoBao.this,"请添加test.jpg", Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				FileItem image=new FileItem(file);
				params.addAttachment("image", image);//设置附件
				if(userId==null){
					Toast t=Toast.makeText(MainActivityTaoBao.this,"请先授权", Toast.LENGTH_SHORT);
					t.show();
					return;
				}
				client.api(params, userId, new TopApiListener(){

					@Override
					public void onComplete(JSONObject json) {
						setTopResultText(json.toString());
//						Toast t=Toast.makeText(MainActivity.this,json.toString(), Toast.LENGTH_SHORT);
//						t.show();
						
					}

					@Override
					public void onError(ApiError error) {
						setTopResultText(error.toString());
						
					}

					@Override
					public void onException(Exception e) {
						// TODO Auto-generated method stub
						
					}}, true);
			}
        });
        
        
        
        Button btn12=(Button)this.findViewById(R.id.button12);
        btn12.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				tryApi();
			}
        });
        
        Button btn13=(Button)this.findViewById(R.id.button13);
        btn13.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				userId = getUserIdFromSharePrerence(MainActivityTaoBao.this);
			}
        });
        
        Button btn14=(Button)this.findViewById(R.id.button14);
        btn14.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 //跳转
				Intent intent = new Intent(MainActivityTaoBao.this, WebViewActivity.class);
				intent.putExtra("loginUrl", "http://a.m.tmall.com/i35493797747.htm?ali_trackid=2:mm_43134341_0_0:1385274538_6k1_1538393487");
				startActivity(intent);
			}
        });
        
    }
    
   
	private void setTopResultText(final String ret) {
		runOnUiThread(new Runnable() {

            @Override
            public void run() {
            	topResult.setText(ret);
            }
        });
	}

	@Override
	protected TopAndroidClient getTopAndroidClient() {
		return client;
	}

	@Override
	protected AuthorizeListener getAuthorizeListener() {
		// TODO Auto-generated method stub
		return new AuthorizeListener(){

			@Override
			public void onComplete(AccessToken accessToken) {
				Log.d(TAG, "callback");
				String id=accessToken.getAdditionalInformation().get(AccessToken.KEY_SUB_TAOBAO_USER_ID);
				if(id==null){
					id=accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_ID);
				}
				MainActivityTaoBao.this.userId=Long.parseLong(id);
				nick=accessToken.getAdditionalInformation().get(AccessToken.KEY_SUB_TAOBAO_USER_NICK);
				if(nick==null){
					nick=accessToken.getAdditionalInformation().get(AccessToken.KEY_TAOBAO_USER_NICK);
				}
				String r2_expires = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_R2_EXPIRES_IN);
				Date start = accessToken.getStartDate();
				Date end = new Date(start.getTime()
						+ Long.parseLong(r2_expires) * 1000L);
			}

			@Override
			public void onError(AuthError e) {
				Log.e(TAG, e.getErrorDescription());
				
			}

			@Override
			public void onAuthException(AuthException e) {
				Log.e(TAG, e.getMessage());
				
			}};
	}

	//////////////////////////////// self - defined interfaces ////////////////////////

	 public void tryApi(){
					TopParameters params=new TopParameters();
					params.setMethod("taobao.itemprops.get");
					params.addFields("pid,name,must,multi,prop_values");
					params.addParam("cid", "44343");
					client.api(params, userId, new TopApiListener(){

						@Override
						public void onComplete(JSONObject json) {
							Log.e(TAG, json.toString());
							Toast t=Toast.makeText(MainActivityTaoBao.this,json.toString(), Toast.LENGTH_SHORT);
							t.show();
							setTopResultText(json.toString());
						}

						@Override
						public void onError(ApiError error) {
							Log.e(TAG, error.toString());
							setTopResultText(error.toString());
						}

						@Override
						public void onException(Exception e) {
							setTopResultText(e.getMessage());
							
						}}, true);//这里必须填写true;否则就成了同步执行，会出问题；
	    }
	 
	 /**
	  * 保存userId到本地sharePreference
	  * @param context
	  * @param userId
	  */
	 public void saveUserIdToSharePreference(Context context,Long userId){
		 AppSharedPreference.saveString(context, Constants.USERID,userId+"");
	 }
	 
	 /**
	  * 获取本地保存的userId
	  * @param context
	  * @return
	  */
	 public Long getUserIdFromSharePrerence(Context context){
		 return Long.parseLong(AppSharedPreference.getString(context, Constants.USERID));
	 }
	 
}
