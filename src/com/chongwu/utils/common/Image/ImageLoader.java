package com.chongwu.utils.common.Image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import com.chongwu.utils.common.MD5_AES.MD5Util;
import com.chongwu.utils.common.file.FileUtil;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;

public class ImageLoader {
	
	private static final String TAG = "ImageLoader";
	private static final long DELAY_BEFORE_PURGE = 10 * 1000;// 定时清理缓存


	private HashMap< String, SoftReference< Bitmap > > cache = new LinkedHashMap< String, SoftReference< Bitmap > >() ;

	private FileUtil fileUtils = new FileUtil();
	
	private String fileCachePath = FileUtil._ROOT_PATH + FileUtil._IMAGE_CACHE ;

	// 定时清理缓存
	private Runnable mClearCache = new Runnable() {
		
		@Override
		public void run() {
			clear();
		}
	};
	private Handler mPurgeHandler = new Handler();

	// 重置缓存清理的timer
	private void resetPurgeTimer() {
		mPurgeHandler.removeCallbacks(mClearCache);
		mPurgeHandler.postDelayed(mClearCache, DELAY_BEFORE_PURGE);
	}

	/**
	 * 清理缓存
	 */
	private void clear() {
		cache.clear();
	}

	/**
	 * 返回缓存，如果没有则返回null
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmapFromCache(String url) {
		Bitmap bitmap = null;
		bitmap = getBitmap2Cache(url);// 从缓存中拿
		if (bitmap != null) {
			return bitmap;
		}
		bitmap = getBitmap2Sdcard(url);// 从内存卡中拿
		return bitmap;
	}


	/**
	 * 从缓存中拿
	 * 
	 * @param url
	 * @return
	 */
	private Bitmap getBitmap2Cache(String url) {
		Bitmap bitmap = null;
		try {
			synchronized (cache) {
				bitmap = cache.get( url ).get();
			}
		} catch (Exception e) {
			
		}
		return bitmap;
	}
	
	/**
	 * 从内存卡中拿
	 * @param url
	 * @return
	 */
	private Bitmap getBitmap2Sdcard( String url ){
		
		Bitmap bitmap = null;
		
		ByteArrayInputStream in = null;
		
		try {
			
			byte[] b = fileUtils.readFile( fileCachePath , MD5Util.getMD5String( url ) );
			
			in = new ByteArrayInputStream(b);
			
			bitmap = BitmapFactory.decodeStream(in);
		
		} catch ( Exception e ) {
			
		}finally{
			if( null != in ){
				try {
					in.close();
				} catch (IOException e) {
					
				}
			}
		}
		
		return bitmap;
	}

	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, BaseAdapter adapter, ImageView image,int defaultIcon) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			image.setImageResource(defaultIcon);//缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, image);
		} else {
			image.setImageBitmap(bitmap);//设为缓存图片
		}

	}
	
	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, PagerAdapter adapter, ImageView image,int defaultIcon) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			image.setImageResource(defaultIcon);//缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, adapter, image , 0);
		} else {
			image.setImageBitmap(bitmap);//设为缓存图片
		}

	}
	
	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, ImageView image,int defaultIcon) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
			image.setImageResource(defaultIcon);//缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, image);
		} else {
			image.setImageBitmap(bitmap);//设为缓存图片
		}

	}
	
	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, Button btn) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
//			btn.setImageResource(R.drawable.head_icon);//缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, btn , 0 , 0);
		} else {
			btn.setBackgroundDrawable( new BitmapDrawable( bitmap ));//设为缓存图片
		}

	}
	
	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage(String url, View view) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
//			image.setImageResource(R.drawable.head_icon);//缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url, view , 0 , 0);
		} else {
			view.setBackgroundDrawable( new BitmapDrawable( bitmap ));//设为缓存图片
		}

	}
	
	/**
	 * 加载图片，如果缓存中有就直接从缓存中拿，缓存中没有就下载
	 * @param url
	 * @param adapter
	 * @param holder
	 */
	public void loadImage( String url ) {
		resetPurgeTimer();
		Bitmap bitmap = getBitmapFromCache(url);// 从缓存中读取
		if (bitmap == null) {
//			image.setImageResource(R.drawable.ic_launcher);//缓存没有设为默认图片
			ImageLoadTask imageLoadTask = new ImageLoadTask();
			imageLoadTask.execute(url );
		} 

	}

	/**
	 * 放入缓存
	 * 
	 * @param url
	 * @param value
	 */
	private void addImage2Cache(String url, Bitmap value) {
		if (value == null || url == null) {
			return;
		}
		synchronized ( cache ) {
			cache.put(url, new SoftReference<Bitmap>( value ) );
		}
	}
	
	/**
	 * 放入内存卡
	 * 
	 * @param url
	 * @param value
	 */
	private void addImage2Sdcard( String url , Bitmap bitmap ){
		if ( null == bitmap || null == url ) {
			return;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	    bitmap.compress( Bitmap.CompressFormat.PNG, 100, baos);
	    		
	    fileUtils.witreFile( fileCachePath , MD5Util.getMD5String(url),  baos.toByteArray() );
	    
	    if( null != baos ){
	    	try {
				baos.close();
			} catch (IOException e) {
//				e.printStackTrace();
			}
	    }
		
	}

	class ImageLoadTask extends AsyncTask<Object, Void, Bitmap> {
		String url;
		BaseAdapter adapter;
		PagerAdapter a;
		ImageView image;
		View view;

		
		@Override
		protected Bitmap doInBackground(Object... params) {
			if( params.length == 1 ){
				
				url = (String) params[0];
				
			} else if( params.length == 2 ){
				
				url = (String) params[0];
				image = (ImageView) params[1];
				
			}else if( params.length == 3 ) {
				
				url = (String) params[0];
				adapter = (BaseAdapter) params[1];
				image = (ImageView) params[2];
				
			}else if( params.length == 4 ) {
				
				url = (String) params[0];
				view = (View) params[1];
				
			}else{
				
				url = (String) params[0];
				a = (PagerAdapter) params[1];
				image = (ImageView) params[2];
				
			}
			Bitmap drawable = loadImageFromInternet(url);
			return drawable;
		}

		
		@Override
		protected void onPostExecute(Bitmap result) {
			if (result == null) {
				return;
			}
			addImage2Cache(url, result);
			addImage2Sdcard(url, result);
			if( null != adapter ){
				adapter.notifyDataSetChanged();
			}
			if( null != a ){
				a.notifyDataSetChanged();
			}
			if( null != image ){
				image.setImageBitmap(result);
			}
			if( null != view ){
				view.setBackgroundDrawable( new BitmapDrawable( result ));
			}
		}
	}

	@SuppressLint("NewApi")
	private Bitmap loadImageFromInternet(String url) {
		System.out.println( "加载的图片路径为：" + url );
		Bitmap bitmap = null;
		HttpClient client = AndroidHttpClient.newInstance("Android");
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSocketBufferSize(params, 3000);
		HttpResponse response = null;
		InputStream inputStream = null;
		HttpGet httpGet = null;
		try {
			httpGet = new HttpGet(url);
			response = client.execute(httpGet);
			int stateCode = response.getStatusLine().getStatusCode();
			if (stateCode != HttpStatus.SC_OK) {
				Log.d(TAG, "func [loadImage] stateCode=" + stateCode);
				return bitmap;
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					inputStream = entity.getContent();
					return bitmap = BitmapFactory.decodeStream(inputStream);
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (ClientProtocolException e) {
			if( null != httpGet ){
				httpGet.abort();
			}
			e.printStackTrace();
		} catch (IOException e) {
			if( null != httpGet ){
				httpGet.abort();
			}
			e.printStackTrace();
		} catch ( NullPointerException e ){
			if( null != httpGet ){
				httpGet.abort();
			}
			e.printStackTrace();
		} catch ( Exception e ){
			if( null != httpGet ){
				httpGet.abort();
			}
			e.printStackTrace();
		}
		finally {
			((AndroidHttpClient) client).close();
		}
		return bitmap;
	}

}
