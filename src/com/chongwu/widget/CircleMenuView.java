package com.chongwu.widget;

import com.chongwu.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

public class CircleMenuView extends View implements Runnable {
	private boolean isInit = true;
	private Paint paint;
	private int screenWidth;
	private int screenHeight;

	private int choiceBtnIndex = 0;
	private float arc = 0;// 圆盘转动的角度
	// 表盘宽和高
	private int cankaoWidth = 356;
	private int cankaoHeight = 356;
	private int panWidth;
	private int panheight;

	private int marginLeft;// 背景离屏幕左边距
	private int marginTop;// 背景离屏幕上边距

	// private int radius;//按钮中心点离圆心的距离：半径；所有按钮的中心在一个圆上

	private int divideWidth;// 每两个btn之间的x轴距离
	private int divideHeight;// 每两个btn之间的y轴距离

//	private View view;

	private CircleMenuViewBtnArea[] btnList = new CircleMenuViewBtnArea[12];

	private int[] btnOn = new int[] { R.drawable.service_main_all_click, R.drawable.service_main_baihuo_click,
			R.drawable.service_main_binzang_click, R.drawable.service_main_hotel_click,
			R.drawable.service_main_jiyang_click, R.drawable.service_main_meirong_click,
			R.drawable.service_main_sheying_click, R.drawable.service_main_tuoyun_click,
			R.drawable.service_main_xiangqin_click, R.drawable.service_main_xunlian_click,
			R.drawable.service_main_yiyuan_click, R.drawable.service_main_yule_click };
	private int[] btnOff = new int[] { R.drawable.service_main_all_unclick, R.drawable.service_main_baihuo_unclick,
			R.drawable.service_main_binzang_unclick, R.drawable.service_main_hotel_unclick,
			R.drawable.service_main_jiyang_unclick, R.drawable.service_main_meirong_unclick,
			R.drawable.service_main_sheying_unclick, R.drawable.service_main_tuoyun_unclick,
			R.drawable.service_main_xiangqin_unclick, R.drawable.service_main_xunlian_unclick,
			R.drawable.service_main_yiyuan_unclick, R.drawable.service_main_yule_unclick };

	public CircleMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 初始化画笔
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(0Xff020202);
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(25f);

//		view = inflate(context, R.layout.imagebtn, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isInit) {
			initView(canvas, arc);
			isInit = false;
		} else {
			// 转动圆盘的度数
			arc =  ((360f / btnList.length * choiceBtnIndex));
			initView(canvas, arc);
		}

	}

	private void initView(Canvas canvas, float arc) {
		screenWidth = getWidth();
		screenHeight = getHeight();
		// 画圆盘
		drawPan(canvas, arc);
		// 画按钮
		drawAllBtn(canvas);
	}

	/**
	 * 画盘
	 * 
	 * @param canvas
	 */
	private void drawPan(Canvas canvas, float arc) {
		Bitmap bitmap;
		Bitmap btn = BitmapFactory.decodeResource(getResources(), btnOff[0]);
		// 画圆盘盘
		// 大背景
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.service_main_zhuanpan);
		panWidth = bitmap.getWidth();
		panheight = bitmap.getHeight();
		marginLeft = (screenWidth - bitmap.getWidth()) / 2;
		marginTop = (screenHeight - bitmap.getHeight()) / 2;
		divideWidth = (bitmap.getWidth() - btn.getWidth()) / (btnOn.length / 2);
		divideHeight = (bitmap.getHeight() - btn.getHeight()) / (btnOn.length / 2);
		canvas.drawBitmap(bitmap, (screenWidth - bitmap.getWidth()) / 2, (screenHeight - bitmap.getHeight()) / 2, paint);

		// 小圆盘
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.service_main_zhuanpan_out);
		canvas.drawBitmap(bitmap, (screenWidth - bitmap.getWidth()) / 2, (screenHeight - bitmap.getHeight()) / 2, paint);
		// 小圆盘
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.service_main_zhuanpan_zhi);
		Matrix m = new Matrix();
		m.setRotate(arc);
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
		canvas.drawBitmap(bitmap, (screenWidth - bitmap.getWidth()) / 2, (screenHeight - bitmap.getHeight()) / 2, paint);
		// 小圆盘
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.service_main_zhuanpan_in);
		canvas.drawBitmap(bitmap, (screenWidth - bitmap.getWidth()) / 2, (screenHeight - bitmap.getHeight()) / 2, paint);
		// 小圆盘
		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.service_main_zhuanpan_in);
		canvas.drawBitmap(bitmap, (screenWidth - bitmap.getWidth()) / 2, (screenHeight - bitmap.getHeight()) / 2, paint);
	}

	private void drawAllBtn(Canvas canvas) {
		drawBtn(canvas, 3, 0, 0, 0, 5);
		drawBtn(canvas, 4, 1, 1, (int) ((25f / cankaoWidth) * panWidth), (int) ((-30f / cankaoHeight) * panheight));
		drawBtn(canvas, 5, 2, 2, (int) ((20f / cankaoWidth) * panWidth), (int) ((-20f / cankaoHeight) * panheight));
		drawBtn(canvas, 6, 3, 3, (int) ((-5f / cankaoWidth) * panWidth), 0);
		drawBtn(canvas, 5, 4, 4, (int) ((20f / cankaoWidth) * panWidth), (int) ((20f / cankaoHeight) * panheight));
		drawBtn(canvas, 4, 5, 5, (int) ((25f / cankaoWidth) * panWidth), (int) ((30f / cankaoHeight) * panheight));
		drawBtn(canvas, 3, 6, 6, 0, (int) ((-5f / cankaoHeight) * panheight));
		drawBtn(canvas, 2, 5, 7, (int) ((-25f / cankaoWidth) * panWidth), (int) ((30f / cankaoHeight) * panheight));
		drawBtn(canvas, 1, 4, 8, (int) ((-20f / cankaoWidth) * panWidth), (int) ((20f / cankaoHeight) * panheight));
		drawBtn(canvas, 0, 3, 9, (int) ((5f / cankaoWidth) * panWidth), 0);
		drawBtn(canvas, 1, 2, 10, (int) ((-20f / cankaoWidth) * panWidth), (int) ((-20f / cankaoHeight) * panheight));
		drawBtn(canvas, 2, 1, 11, (int) ((-25f / cankaoWidth) * panWidth), (int) ((-30f / cankaoHeight) * panheight));
	}

	/**
	 * 画按钮
	 * 
	 * @param canvas
	 * @param dividerXNum
	 *            x轴的间隔数
	 * @param dividerYNum
	 *            y轴的间隔数
	 * @param btnIndex
	 *            从上按照顺时针转动，从0开始计数
	 * @param pianyiX
	 *            X轴的需要校正的便宜位置
	 * @param pianyiY
	 *            Y轴的需要校正的便宜位置
	 */
	private void drawBtn(Canvas canvas, int dividerXNum, int dividerYNum, int btnIndex, int pianyiX, int pianyiY) {
		Bitmap bitmap;
		int startX;
		int startY;
		if (btnIndex == choiceBtnIndex) {
			bitmap = BitmapFactory.decodeResource(getResources(), btnOn[btnIndex]);
		} else {
			bitmap = BitmapFactory.decodeResource(getResources(), btnOff[btnIndex]);
		}
		startX = marginLeft + dividerXNum * divideWidth + pianyiX;
		startY = marginTop + dividerYNum * divideHeight + pianyiY;
		btnList[btnIndex] = new CircleMenuViewBtnArea(startX, startY, startX + bitmap.getWidth(), startY
				+ bitmap.getHeight());
		canvas.drawBitmap(bitmap, startX, startY, paint);
	}

	@Override
	public void run() {

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			int choiceIndex = getChoiceBtnIndex(x, y);
			if (choiceIndex != -1) {
				choiceBtnIndex = choiceIndex;
				postInvalidate();// 刷新页面
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 点击事件的位置
	 * 
	 * @param x
	 * @param y
	 * @return -1表示没找到点
	 */
	public int getChoiceBtnIndex(int x, int y) {
		for (int i = 0; i < btnList.length; i++) {
			if (x > btnList[i].startX && x < btnList[i].endX && y > btnList[i].startY && y < btnList[i].endY) {
				return i;
			}
		}
		return -1;
	}
}

