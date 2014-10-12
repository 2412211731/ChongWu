package com.chongwu.utils.common.message;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class MessageManageUtil {
	private static MessageManageUtil  messageManageUtil= null;
	
	public static MessageManageUtil getInstance(){
		if(messageManageUtil == null){
			messageManageUtil = new MessageManageUtil();
		}
		return messageManageUtil;
	}
	
	/**
	 * 增加新短信到数据库
	 * @param context
	 * @param uri 短信的资源标示，取值： content://sms/ 所有短信，content://sms/inbox  收件箱  ；  content://sms/sent   已发送  ；  content://sms/draft      草稿  ；
	 * content://sms/outbox     发件中；   content://sms/failed     失败  ；   content://sms/queued     待发送
	 * 
	 * @param msg 
	 */
	public void addMessage(Context context,Uri uri,Message msg) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("_id",msg._id);
		values.put("thread_id",msg.thread_id );
		values.put("address", msg.address );
		values.put("person", msg.person );
		values.put("date", msg.date );
		values.put("date_sent", msg.date_sent );
		values.put("protocol", msg.protocol );
		values.put("read",msg.read  );
		values.put("status",msg.status  );
		values.put("type",msg.type  );
		values.put("reply_path_present",msg.reply_path_present  );
		values.put("subject",msg.subject  );
		values.put("body", msg.body );
		values.put("service_center",msg.service_center  );
		values.put("locked", msg.locked );
		values.put("error_code", msg.error_code );
		values.put( "seen", msg.seen );
		values.put("ref_number", msg.ref_number );
		values.put("date_to_send" , msg.date_to_send );
		resolver.insert(uri, values);
	}

	/**
	 * 删除短信
	 * @param context
	 * @param uri 短信库路径
	 * @param where 查询的where条件，如：address=？
	 * @param selectionArgs 对应?的值
	 */
	public void  deleteMessage(Context context,String uri,String where,String[] selectionArgs) {
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(Uri.parse(uri), where, selectionArgs);
	}


	/**
	 * 查询消息
	 * @param context
	 * @param uri 短信的资源标示，取值： content://sms/ 所有短信，content://sms/inbox  收件箱  ；  content://sms/sent   已发送  ；  content://sms/draft      草稿  ；
	 * content://sms/outbox     发件中；   content://sms/failed     失败  ；   content://sms/queued     待发送
	 * 
	 * @param projection  返回哪些列，为null时候返回所有列
	 * @param selection  ：SQL WHERE语句，里面用?问号来表示要查询的值，如 id=?
	 * @param selectionArgs  对应selection中?问号的值
	 * @param sortOrder 排序，可以为null
	 */
	public Cursor selectMessages(Context context,String uri,String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		try{
			ContentResolver resolver = context.getContentResolver();
			Cursor cursor = resolver.query(Uri.parse(uri), projection, selection, selectionArgs, sortOrder);
			cursor.moveToFirst();
			return cursor;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取条数
	 * @param c 
	 * @return
	 */
	public int getMessageCount(Cursor c){
		return c.getCount();
	}



	
//	自己通过getColumnNames()方法获取到的0：表示：_id;1：表示：thread_id;2：表示：address;3：表示：person;4：表示：date;5：表示：date_sent;6：表示：protocol;7：表示：read;8：表示：status;9：表示：type;10：表示：reply_path_present;11：表示：subject;12：表示：body;13：表示：service_center;14：表示：locked;15：表示：error_code;16：表示：seen;17：表示：ref_number;18：表示：date_to_send;
	/**
	 * 获取消息列表
	 * @param c
	 * @return
	 */
	public List<Message> getMessageList(Cursor c){
		ArrayList<Message> list = new ArrayList<MessageManageUtil.Message>();
		int count = c.getCount();
		c.moveToFirst();
		for(int i=0;i<count;i++){
			Message msg = new Message();
			msg._id = c.getString(0);
			msg.thread_id = c.getString(1);
			msg.address = c.getString(2);
			msg.person = c.getString(3);
			msg.date = c.getString(4);
			msg.date_sent = c.getString(5);
			msg.protocol = c.getString(6);
			msg.read = c.getString(7);
			msg.status = c.getString(8);
			msg.type = c.getString(9);
			msg.reply_path_present = c.getString(10);
			msg.subject = c.getString(11);
			msg.body = c.getString(12);
			msg.service_center = c.getString(13);
			msg.locked = c.getString(14);
			msg.error_code = c.getString(15);
			msg.seen = c.getString(16);
			msg.ref_number = c.getString(17);
			msg.date_to_send = c.getString(18);
			
			list.add(msg);
			c.moveToNext();
		}
		return list;
	}

	public void updateMessage() {
	}
	
	
	/**
	 * 获取表的所有列号和对应列名
	 * @param c
	 * @return
	 */
	public String getColumnNames(Cursor c){
		StringBuilder str = new StringBuilder();
		int lengh = c.getColumnCount();
		for(int i=0;i<lengh;i++){
			str.append(i+"：表示："+c.getColumnName(i)+";");
		}
		return str.toString();
	}
	
      
	public static class Message{
		public String _id;//primary key     integer                  与words表内的source_id关联
		public String thread_id;//会话id，一个联系人的会话一个id，与threads表内的_id关联      integer
		public String address; //对方号码          text
		public String person;//联系人id           integer  
		public String date;
		public String date_sent;//  发件日期           integer
		public String protocol;//通信协议，判断是短信还是彩信     integer       0：SMS_RPOTO, 1：MMS_PROTO
		public String read;// 是否阅读           integer       default 0             0：未读， 1：已读  
		public String status;// 状态          integer  default -1  -1：接收，   0：complete,  64： pending,  128： failed  
		public String type;//短信类型           integer  1：inbox收件箱 2：sent发件箱  3：draft草稿  4：outbox 5：failed发送失败  6：queued排队放送中的 
		public String reply_path_present;
		public String subject; //主题 
		public String body;//内容 
		public String service_center;//服务中心号码
		public String locked;
		public String error_code;
		public String seen;
		public String ref_number;
		public String date_to_send;
		
		
		public Message(){
			
		}
				
		/**  
		 * _id;//primary key     integer                  与words表内的source_id关联
		 * thread_id;//会话id，一个联系人的会话一个id，与threads表内的_id关联      integer
		 * address; //对方号码          text
		 * person;//联系人id           integer  
		 * date;
		 * date_sent;//  发件日期           integer
		 * protocol;//通信协议，判断是短信还是彩信     integer       0：SMS_RPOTO, 1：MMS_PROTO
		 * read;// 是否阅读           integer       default 0             0：未读， 1：已读  
		 * status;// 状态          integer  default -1  -1：接收，   0：complete,  64： pending,  128： failed  
		 * type;//短信类型           integer  1：inbox收件箱 2：sent发件箱  3：draft草稿  4：outbox 5：failed发送失败  6：queued排队放送中的 
		 * reply_path_present;
		 * subject; //主题 
		 * body;//内容 
		 * service_center;//服务中心号码
		 * locked;
		 * error_code;
		 * seen;
		 * ref_number;
		 * date_to_send;
		 */
		public Message(String _id, String thread_id, String address, String person, String date, String date_sent, String protocol, String read, String status, String type, String reply_path_present, String subject, String body, String service_center, String locked, String error_code, String seen, String ref_number, String date_to_send) {
			super();
			this._id = _id;
			this.thread_id = thread_id;
			this.address = address;
			this.person = person;
			this.date = date;
			this.date_sent = date_sent;
			this.protocol = protocol;
			this.read = read;
			this.status = status;
			this.type = type;
			this.reply_path_present = reply_path_present;
			this.subject = subject;
			this.body = body;
			this.service_center = service_center;
			this.locked = locked;
			this.error_code = error_code;
			this.seen = seen;
			this.ref_number = ref_number;
			this.date_to_send = date_to_send;
		}
		
		
	}
}
