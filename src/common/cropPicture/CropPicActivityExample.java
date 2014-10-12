package common.cropPicture;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import com.chongwu.R;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CropPicActivityExample extends Activity {
	private Context context;
	Button btn;
	ImageView img;
	private static final String IMAGE_UNSPECIFIED = "image/*";
	private static final int BASEINTEGER = 0;
	private static final int REQEUST_CODE_SYSTEM_ALBUM = BASEINTEGER + 1;
	private static final int REQEUST_CODE_CROP_IMAGE = BASEINTEGER + 2;
	private static final String TAG = "test";

	private final int FAIL = 4;
	private final int SUCCESS = 3;
	private final int UPLOAD_HEAD = 2;

	private Bitmap returnBitmap = null;
	// 待裁剪图片的宽度
	private int mCropImageOriginalWidth = 0;
	// 待裁剪图片的高度
	private int mCropImageOriginalHeight = 0;
	// 裁剪完成后图片的大小
	private static final int CROP_IMAGE_SIZE = 100;
	private final int TAKE_PICTURE = 0;// 从相册获取图片
	private final int CHOOSE_PICTURE = 1;// 从图库获取图片
	private final String SAVE_HEAD_ICON_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	private final String TEMP_ICON_NAME = "temp_icon_name.png";
	private final String HEAD_ICON_NAME = "head_icon_name.png";

	// 裁剪完成后图片的大小
	private static final int CROP_IMAGE_WIDTH = 100;
	private static final int CROP_IMAGE_HEIGHT = 100;

	private final int SAVE_HEAD_ICON_TO_FILE = 0;
	private ProgressDialog progressDialog;
	/*private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SAVE_HEAD_ICON_TO_FILE:// 保存头像到文件
//				showProgressDialog(context);
				new Thread(new Runnable() {

					@Override
					public void run() {
						boolean saveSuccess = saveHeadIconToFile(returnBitmap);
						if (saveSuccess) {// 保存成功
							// 上传头像
							handler.sendEmptyMessage(UPLOAD_HEAD);
							Log.d("******", "保存头像到本地成功");
						} else {
							handler.sendEmptyMessage(FAIL);
							Log.d("******", "保存头像到本地失败");
						}
					}
				}).start();
				break;
			// case UPLOAD_HEAD:// 上传头像
			// uploadHeadIcon();
			// break;
			// case SUCCESS:// 上传头像成功
			// dismissProgressDialog();
			// showToast("上传头像成功");
			// break;
			// case FAIL:// 上传头像失败
			// dismissProgressDialog();
			// showToast("上传头像失败");
			// break;
			default:
				break;
			}
		}
	};*/

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_main);
		btn = (Button) findViewById(R.id.test);
		img = (ImageView) findViewById(R.id.showCropImage);
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showPicturePicker(context);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CHOOSE_PICTURE) {
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			Uri uri = null;
			String path = null;
			if (data != null) {
				uri = data.getData();
				if (uri != null) {
					path = processCropFilePath(this, uri.toString());
					Log.d("******", "从系统相册返回的路进: uri=" + uri);
					Log.d("******", "从系统相册返回的路进: path=" + path);
					if (path != null) {
						BitmapFactory.Options option = new BitmapFactory.Options();
						option.inJustDecodeBounds = true;
						BitmapFactory.decodeFile(path, option);
						mCropImageOriginalWidth = option.outWidth;
						mCropImageOriginalHeight = option.outHeight;
						Log.d("******", "图片的宽度:" + mCropImageOriginalWidth);
						Log.d("******", "图片的高度:" + mCropImageOriginalHeight);
						Log.d("******", "图片的类型:" + option.outMimeType);
						Intent cropIntent = new Intent(this, CropImage.class);
						cropIntent.setData(uri);
						cropIntent.putExtra("return-data", true);
						// 裁剪成圆形图片
						cropIntent.putExtra("aspectX", 1);
						cropIntent.putExtra("aspectY", 1);
						cropIntent.putExtra("outputX", CROP_IMAGE_WIDTH);
						cropIntent.putExtra("outputY", CROP_IMAGE_HEIGHT);
						startActivityForResult(cropIntent, REQEUST_CODE_CROP_IMAGE);
					}
				} else {
					Log.d("******", "从系统获取到的裁剪图片信息为uri=null");
				}
			} else {
				Log.d("******", "从系统获取到的裁剪图片信息为data=null");
			}
			return;
		} else if (requestCode == TAKE_PICTURE) {
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			Uri uri = Uri.parse("file://" + SAVE_HEAD_ICON_PATH + "/" + TEMP_ICON_NAME);
			String path = null;
			if (uri != null) {
				path = processCropFilePath(this, uri.toString());
				Log.d("******", "从系统相册返回的路进: uri=" + uri);
				Log.d("******", "从系统相册返回的路进: path=" + path);
				if (path != null) {
					BitmapFactory.Options option = new BitmapFactory.Options();
					option.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(path, option);
					mCropImageOriginalWidth = option.outWidth;
					mCropImageOriginalHeight = option.outHeight;
					Log.d("******", "图片的宽度:" + mCropImageOriginalWidth);
					Log.d("******", "图片的高度:" + mCropImageOriginalHeight);
					Log.d("******", "图片的类型:" + option.outMimeType);
					Intent cropIntent = new Intent(this, CropImage.class);
					cropIntent.setData(uri);
					cropIntent.putExtra("return-data", true);
					// 裁剪
					cropIntent.putExtra("aspectX", 1);
					cropIntent.putExtra("aspectY", 1);
					cropIntent.putExtra("outputX", CROP_IMAGE_WIDTH);
					cropIntent.putExtra("outputY", CROP_IMAGE_HEIGHT);
					startActivityForResult(cropIntent, REQEUST_CODE_CROP_IMAGE);
				}
			} else {
				Log.d("******", "从系统获取到的裁剪图片信息为uri=null");
			}
			return;
		} else if (requestCode == REQEUST_CODE_CROP_IMAGE) {
			if (resultCode == RESULT_CANCELED) {
				return;
			}
			Log.d("******", "返回了裁剪后的数据 data=" + data);
			if (data != null) {
				Bundle bundle = data.getExtras();
				if (bundle != null) {
					Set<String> keys = bundle.keySet();
					Iterator<String> iterator = keys.iterator();
					Rect rect = null;

					while (iterator.hasNext()) {
						String key = iterator.next();
						Log.d("******", "key=" + key);
						if (key.equals("cropped-rect")) {
							rect = bundle.getParcelable(key);
							Log.d("******", "l=" + rect.left + ",t=" + rect.top + ",r=" + rect.right + ",b=" + rect.bottom);
						} else if (key.equals("data")) {
							returnBitmap = bundle.getParcelable(key);
						}
					}
					if (returnBitmap != null) {
						Log.d("******", "returnBitmap width=" + returnBitmap.getWidth());
						Log.d("******", "returnBitmap height=" + returnBitmap.getHeight());
						img.setImageBitmap(returnBitmap);

//						handler.sendEmptyMessage(SAVE_HEAD_ICON_TO_FILE);

					}
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	public static String processCropFilePath(Activity activity, String fileUri) {
		if (fileUri.startsWith("file://")) {
			String temp = fileUri.substring(7);
			return temp;
		} else if (fileUri.startsWith("content://")) {
			return getRealPathFromURI(activity, Uri.parse(fileUri));
		}
		return null;
	}

	private static String getRealPathFromURI(Activity activity, Uri contentUri) {
		try {
			String[] proj = { Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = activity.managedQuery(contentUri, proj, // Which
																	// columns
																	// to return
					null, // WHERE clause; which rows to return (all rows)
					null, // WHERE clause selection arguments (none)
					null); // Order-by clause (ascending by name)
			int column_index = cursor.getColumnIndexOrThrow(Images.Media.DATA);
			cursor.moveToFirst();

			return cursor.getString(column_index);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void showPicturePicker(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("图片来源");
		builder.setNegativeButton("取消", null);
		builder.setItems(new String[] { "拍照", "相册" }, new DialogInterface.OnClickListener() {
			// 类型码
			int REQUEST_CODE;

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case TAKE_PICTURE:// 照相
					REQUEST_CODE = TAKE_PICTURE;
					Uri imageUri = null;
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					File file = new File(SAVE_HEAD_ICON_PATH, TEMP_ICON_NAME);
					try {
						if (file.exists()) {
							file.delete();
						}
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}

					imageUri = Uri.fromFile(file);
					// 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, REQUEST_CODE);
					break;

				case CHOOSE_PICTURE:// 选则图片
					REQUEST_CODE = CHOOSE_PICTURE;
					Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);
					innerIntent.setType(IMAGE_UNSPECIFIED);
					Intent wrapperIntent = Intent.createChooser(innerIntent, null);
					startActivityForResult(wrapperIntent, REQUEST_CODE);
					break;

				default:
					break;
				}
			}
		});
		builder.create().show();
	}

	public void showProgressDialog(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setMessage("正在上传头像,请稍等...");
		progressDialog.show();
	}

	/**
	 * 保存bitmap数据到文件
	 * 
	 * @param b
	 */
//	public boolean saveHeadIconToFile(Bitmap b) {
//		boolean saveSuccess = false;
//		File file = new File(SAVE_HEAD_ICON_PATH + "/" + HEAD_ICON_NAME);
//		try {
//			if (file.exists()) {
//				file.delete();
//			}
//			file.createNewFile();
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			b.compress(Bitmap.CompressFormat.PNG, 1, baos);
//			
//			Log.i("!!!!!!", baos.size()+"");;
//			new FileUtils().witreFile(SAVE_HEAD_ICON_PATH, "/" + HEAD_ICON_NAME, baos.toByteArray());
//			
//			saveSuccess = true;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return saveSuccess;
//	}
}
