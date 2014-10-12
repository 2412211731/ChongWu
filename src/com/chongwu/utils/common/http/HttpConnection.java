package com.chongwu.utils.common.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;  
import java.util.List;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.params.BasicHttpParams;  
import org.apache.http.params.HttpConnectionParams;  
import org.apache.http.params.HttpParams;  
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;  
import android.os.Bundle;  
import android.os.Handler;  
import android.os.Message;  
  
/** 
 * Asynchronous HTTP connections 
 *  
 *  
 *for example: 
   try {
			JSONObject params = new JSONObject();
			params.put("op", "getWelcome");
			params.put("deviceType", "1");
			new HttpConnection().post(Config.SERVER_URL, params.toString(), new CallbackListener() {
				@Override
				public void callBack(String result) {
					if (!"fail".equals(result)) {

					}
					System.out.println("result : " + result);
				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}
 * @author Greg Zavitz & Joseph Roth 
 */  
public class HttpConnection implements Runnable {  
  
    public static final int DID_START = 0;  
    public static final int DID_ERROR = 1;  
    public static final int DID_SUCCEED = 2;  
  
    private static final int GET = 0;  
    private static final int POST = 1;  
    private static final int PUT = 2;  
    private static final int DELETE = 3;  
    private static final int BITMAP = 4;  
    private static final int IPOST = 5 ;
   
    private String url;  
    private int method;  
    private String data;  
    private CallbackListener listener;  
  
    private HttpClient httpClient;  
  
    // public HttpConnection() {  
    // this(new Handler());  
    // }  
  
    public void create(int method, String url, String data, CallbackListener listener) {  
        this.method = method;  
        this.url = url;  
        this.data = data;  
        this.listener = listener;  
        ConnectionManager.getInstance().push(this);  
    }  
  
    public void get(String url, CallbackListener listener) {  
        create(GET, url, null, listener);  
    }  
  
    public void post(String url, String data, CallbackListener listener) {  
        create(POST, url, data, listener);  
    }  
    
    public void post(String url, String data, CallbackListener listener , int i) {  
        create(IPOST, url, data, listener);  
    }  
  
    public void put(String url, String data) {  
        create(PUT, url, data, listener);  
    }  
  
    public void delete(String url) {  
        create(DELETE, url, null, listener);  
    }  
  
    public void bitmap(String url) {  
        create(BITMAP, url, null, listener);  
    }  
  
    public interface CallbackListener {  
        public void callBack(String result);
    }  
  
    private static final Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message message) {  
            switch (message.what) {  
                case HttpConnection.DID_START: {  
                    break;  
                }  
                case HttpConnection.DID_SUCCEED: {  
                    CallbackListener listener = (CallbackListener) message.obj;  
                    Object data = message.getData();  
                    if (listener != null) {  
                        if(data != null) {  
                            Bundle bundle = (Bundle)data;  
                            String result = bundle.getString("callbackkey");  
                            listener.callBack(result);  
                        }  
                    }  
                    break;  
                }  
                case HttpConnection.DID_ERROR: {  
                    break;  
                }  
            }  
        }  
    };  
  
    @Override
	public void run() {  
        httpClient = getHttpClient();  
        try {  
            HttpResponse httpResponse = null;  
            switch (method) {  
            case GET:  
            	HttpGet request = new HttpGet( url );
    	        
    	        httpResponse = httpClient.execute( request );  
    	        
    	        if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK){  
                	String result = EntityUtils.toString(httpResponse.getEntity());  
                	this.sendMessage( result );  
                }else{
                	this.sendMessage("fail");  
                }
    	        
                break;  
            case POST:  
                HttpPost httpPost = new HttpPost( url );  
                List<NameValuePair> params = new ArrayList<NameValuePair>();  
                BasicNameValuePair valuesPair = new BasicNameValuePair( "params", data );  
                params.add(valuesPair);
                
                
                httpPost.setEntity(new UrlEncodedFormEntity( params, "UTF-8" ) );  
                
                httpResponse = httpClient.execute( httpPost );  
                
                if (isHttpSuccessExecuted(httpResponse)) {  
                    String result = EntityUtils.toString( httpResponse.getEntity() );  
                    this.sendMessage( result );  
                } else {  
                    this.sendMessage("fail");  
                }  
                break;
                
            case IPOST: 
            	
            	 URL mUrl=new URL( url );  
    		     HttpURLConnection httpConn = (HttpURLConnection)mUrl.openConnection();  
    		     
    		     httpConn.setConnectTimeout(3000);
    		     httpConn.setReadTimeout(300000);
    		     httpConn.setDoOutput(true);
    		     httpConn.setDoInput(true);  
    		     httpConn.setUseCaches(false);
    		     httpConn.setRequestMethod("POST");
    		     
    	         byte[] requestStringBytes = ("params="+data).getBytes( HTTP.UTF_8 );  
    	         httpConn.setRequestProperty("Content-length", "" + requestStringBytes.length);   
    	         httpConn.setRequestProperty("Connection", "Keep-Alive");// 
    	         httpConn.setRequestProperty("Charset", "UTF-8");  
    	         
    	         OutputStream outputStream = httpConn.getOutputStream();  
	   	         outputStream.write(requestStringBytes);  
	   	         outputStream.close();  
	   	         
	   	         int responseCode = httpConn.getResponseCode(); 
	   	         
	   	         if(HttpURLConnection.HTTP_OK == responseCode){//成功
		             
		           StringBuffer sb = new StringBuffer();  
		              String readLine;  
		              BufferedReader responseReader;  
		       
		              responseReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream(), HTTP.UTF_8 ));  
		              while ((readLine = responseReader.readLine()) != null) {  
		               sb.append(readLine).append("\n");  
		              }  
		              responseReader.close();  

		              this.sendMessage( sb.toString() );
		              
		          }  
            	
            	break;
            }  
        } catch (Exception e) {  
        	this.sendMessage("fail"); 
        	e.printStackTrace();
        }  
        ConnectionManager.getInstance().didComplete(this);  
    }  
  
    private void sendMessage(String result) {  
        Message message = Message.obtain(handler, DID_SUCCEED,  
                listener);  
        Bundle data = new Bundle();  
        data.putString("callbackkey", result);  
        message.setData(data);  
        handler.sendMessage(message);  
          
    }  
  
    public static DefaultHttpClient getHttpClient() {  
        HttpParams httpParams = new BasicHttpParams();  
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);  
        HttpConnectionParams.setSoTimeout(httpParams, 20000);  
  
        DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);  
        return httpClient;  
    }  
  
    public static boolean isHttpSuccessExecuted(HttpResponse response) {  
        int statusCode = response.getStatusLine().getStatusCode();  
        return (statusCode > 199) && (statusCode < 400);  
    }  
  
}  