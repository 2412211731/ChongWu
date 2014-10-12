package com.chongwu.utils.common.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import android.os.Environment;

public class FileUtil {
	public static String _ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator + "flow_control" + File.separator;
	public final static String _IMAGE_CACHE = "imageCache" + File.separator;
	public final static String _SETTING_CACHE = "settingCache" + File.separator;
	public final static String _UPDATA = "update" + File.separator;
	
	/**
	 * 实例引用：单例模式
	 */
	private static FileUtil fileUtilInstance = null;
	
	
	/**
	 * 获取实例引用
	 * @return
	 */
	public static FileUtil getFileUtilInstance(){
		if(null == fileUtilInstance){
			fileUtilInstance = new FileUtil();
		}
		return fileUtilInstance;
	}
	
	
	/**
	 * 判断文件是否存在
	 * @param file 文件路径，包含文件名
	 */
   public boolean isFileExit(String file){
	   try{
		   File f = new File(file); 
		   if(f.exists()){
			  return true;
		   }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
	   return false;
   }
   
   
   /**
    * 如果文件不存在，则创建文件
    * @param file 文件路径，包含文件名
    */
   public void createFile(String file){
	   try{
		   File f = new File(file); 
		   if(!f.exists()){
			   f.createNewFile(); 
		   }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   /**
    * 如果文件或文件夹不存在，则创建它
    * @param file 文件
    */
   public void createFileOrDir(File file){
	   try{
		   if(!file.exists()){
			   if(file.isDirectory()){
				   file.mkdir();
			   }else if(file.isFile()){
				   file.createNewFile(); 
			   }
		   }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   
   /**
    * 删除文件
    * @param file 文件路径，包含文件名
    */
   public void deleteFile(String file){
	   try{
		   File f = new File(file); 
		   if(f.exists()){
			   f.delete(); 
		   }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   /**
    * 删除文件
    * @param file 文件
    */
   public void deleteFile(File f){
	   try{
		   if(f.exists()){
			   f.delete(); 
		   }
	   }catch(Exception e){
		   e.printStackTrace();
	   }
   }
   
   
   /**
    * 获取/data/data目录下应用目录中的目录
    * 我们可以在该目录中创建文件   例如：File actionLogDir = fileUtil.getAppDataDir(bookParent.getBaseContext(),"actionLog");
    * @param context 上下文
    * @param fileName 文件名
    * @return
    */
   public File getAppDataDir(Context context,String fileName){
	   return context.getDir(fileName, Context.MODE_WORLD_WRITEABLE);
   }
   
   
   /**
    * 打开文件输出流
    * ：此方法在打开/data/data目录下应用目录中的文件时候会被用到，因为context会给于打开该文件为输出流的权限
    * @param context
    * @param fileName 文件名
    * @return
    * @throws FileNotFoundException
    */
   public FileOutputStream saveInMemory(Context context, String fileName) throws FileNotFoundException {
		// important: note the permission of the file in memory
		return context.openFileOutput(fileName, Context.MODE_WORLD_READABLE);
	}
   
   
   /**
    * 读取文件内容
    * @param file 文件
    * @return
    * @throws IOException
    */
   public byte[] readFileData(File file) throws IOException {
		InputStream is = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			is = new FileInputStream(file);// pathStr 文件路径
			byte[] b = new byte[1024];
			int n;
			while ((n = is.read(b)) != -1) {
				out.write(b, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return out.toByteArray();
	}
   
   
   /**
    * 把字符串写入文件
    * @param str 字串
    * @param file 文件
    * @return
    */
   public boolean writeFile(String str,File file){
		boolean bRet = false;
		try {
			byte[] strBts = str.getBytes();
			
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(strBts, 0, strBts.length);
			fos.close();
			
			bRet = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bRet;
   }
   
   
   /**
    * 把字节数据写入文件
    * @param str 字串
    * @param file 文件
    * @return
    */
   public boolean writeFile(byte[] strBts,File file){
		boolean bRet = false;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(strBts, 0, strBts.length);
			fos.close();
			
			bRet = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bRet;
   }
   
   /**
	 * 把文件复制到其他文件
	 * @param context
	 */
	public void copyDataBase(File inputFile,File outputFile) {
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(outputFile);// 得到数据库文件的写入流
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = new FileInputStream(inputFile);// 得到数据库文件的数据流
			byte[] buffer = new byte[8192];
			int count = 0;

			while ((count = is.read(buffer)) > 0) {
				os.write(buffer, 0, count);
				os.flush();
			}
		} catch (IOException e) {

		}
		try {
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
   
public boolean witreFile( String path , String name , byte[] by ){
		
		File file = new File( path );
		if( !file.exists() ){
			file.mkdirs();
		}
		
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream( new ByteArrayInputStream(by) );
			out = new BufferedOutputStream( new FileOutputStream(  path  + name ) );
			
			byte[] b = new byte[5000];
			int length = 0;
			while( ( length = in.read(b) ) != -1 ){
				out.write(b , 0, length);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if( null != out ){
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if( null != in ){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return true;
		}
		
	}
	
	public byte[] readFile( String path , String name ){
		
		FileInputStream in = null;
		BufferedInputStream bin = null;
		byte[] data = null;  
		try {
			in = new FileInputStream( path + name );
			bin = new BufferedInputStream(in);
			
			data = new byte[bin.available()];
			
			bin.read(data);
			
			return data;
			
			
		} catch (Exception e) {
		}finally{
			if( null != bin){
				try {
					bin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if( null != in ){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
public byte[] readFile( String path ){
		
		FileInputStream in = null;
		BufferedInputStream bin = null;
		byte[] data = null;  
		try {
			in = new FileInputStream( path );
			bin = new BufferedInputStream(in);
			
			data = new byte[bin.available()];
			
			bin.read(data);
			
			return data;
			
			
		} catch (Exception e) {
		}finally{
			if( null != bin){
				try {
					bin.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if( null != in ){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
}
	   
