package com.chongwu.utils;

import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;

public class ChartUtil {

	private static void drawBackground(Canvas canvas, int cnum, int w, int h, int cw, int ch, JSONArray rows, boolean isLine) throws Exception {

		canvas.drawColor(0xFFeef2f6);

		{ // 绘制图形区Grid-X坐标线
			Paint paint = new Paint();
			paint.setColor(0xffc8d0d9);
			for (int i = 0; i <= cnum; i++) {
				canvas.drawLine(0, i * ch + 30, w, i * ch + 30, paint);
			}
		}

		{ // 绘制图形区Grid-Y坐标线
			Paint paint = new Paint();
			paint.setColor(0xffc8d0d9);
			for (int i = 0; i <= rows.length(); i++) {
				int x = i * cw;
				if (isLine)
					x += +cw / 2;
				canvas.drawLine(x, 30, x, h + 30, paint);
			}
		}

		{ // 绘制X坐标LABLE
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(0Xff020202);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(25f);
			for (int i = 0; i < rows.length(); i++) {
				JSONObject row = rows.getJSONObject(i);
				int x = i * cw + cw / 2;
				canvas.drawText(row.getString("label"), x, h + 30 + 30, paint);
			}
		}
	}

	public static Bitmap drawBar(int cw, int bh, JSONArray rows) throws Exception {
		int cnum = 4;// 纵向格子数+1
		int w = cw * rows.length();
		int bw = w;
		int h = bh - 70;
		int ch = h / cnum;

		Bitmap bitmap = Bitmap.createBitmap(bw, bh, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap,100是文字区高度

		Canvas canvas = new Canvas(bitmap);
		drawBackground(canvas, cnum, w, h, cw, ch, rows, false);

		for (int i = 0; i < rows.length(); i++) {
			JSONObject row = rows.getJSONObject(i);
			float x = i * cw + cw / 2;
			double y = h + 30 - (h * row.getDouble("scale")) / 100D;
			Paint barPaint = new Paint();
			if (row.isNull("color")) {
				barPaint.setColor(0x66419c24);
			} else {
				barPaint.setColor(row.getInt("color"));
			}
			canvas.drawRect(x - 15, (float) y, x + 15, h + 30, barPaint);

			if (!row.isNull("value")) {
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(0Xff5c5d60);
				paint.setTextAlign(Align.CENTER);
				paint.setTextSize(25f);
				canvas.drawText(row.getString("value"), x, (float) (y - 5), paint);
			}
		}

		return bitmap;
	}

	public static Bitmap drawLine(int cw, int bh, JSONArray rows) throws Exception {
		int cnum = 4;// 纵向格子数+1
		int w = cw * rows.length();
		int bw = w;
		int h = bh - 70;
		int ch = h / cnum;

		Bitmap bitmap = Bitmap.createBitmap(bw, bh, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmap);
		drawBackground(canvas, cnum, w, h, cw, ch, rows, false);

		float preX = -1;
		float preY = -1;

		for (int i = 0; i < rows.length(); i++) {
			JSONObject row = rows.getJSONObject(i);
			float x = i * cw + cw / 2;
			double y = h + 30 - (h * row.getDouble("scale")) / 100D;

			Paint linePaint = new Paint();
			linePaint.setColor(0xFF419c24);
			linePaint.setStrokeWidth(2);
			linePaint.setAntiAlias(true);
			if (preX >= 0) {
				canvas.drawLine(preX, preY, x, (float) y, linePaint);
			}

			Paint barPaint = new Paint();
			barPaint.setAntiAlias(true);
			if (row.isNull("color")) {
				barPaint.setColor(0xFF419c24);
			} else {
				barPaint.setColor(row.getInt("color"));
			}
			canvas.drawCircle(x, (float) y, 10, barPaint);

			preX = x;
			preY = (float) y;

			if (!row.isNull("value")) {
				Paint paint = new Paint();
				paint.setAntiAlias(true);
				paint.setColor(0Xff5c5d60);
				paint.setTextAlign(Align.CENTER);
				paint.setTextSize(25f);
				canvas.drawText(row.getString("value"), x, (float) (y - 15), paint);
			}
		}

		return bitmap;
	}

	/**
	 * 
	 * @param bw
	 * @param bh
	 * @param rows 列表包含的JSONObject 包含color 字段int型；  value字段Double型；
	 * @return
	 * @throws Exception
	 */
	public static Bitmap drawPie(int bw,int bh, JSONArray rows) throws Exception {

		Bitmap bitmap = Bitmap.createBitmap(bw, bh, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(0xFFeef2f6);

		int s = bh - 20; // 图形区高度
		int x = s / 2 + 10;// 饼图中心点X坐标
		int y = s / 2 + 10;// 饼图中心点Y坐标
		int r = s / 2;// 饼图半径
		int sx = x - r;
		int sy = y - r;
		int ex = x + r;
		int ey = y + r;

		int tx = bh + 20;

		float sum = 0;
		for (int i = 0; i < rows.length(); i++) {
			JSONObject row = rows.getJSONObject(i);
			sum += row.getDouble("value");
		}

		float preR = 0;
		RectF rectF = new RectF(sx, sy, ex, ey);
		for (int i = 0; i < rows.length(); i++) {
			JSONObject row = rows.getJSONObject(i);

			Paint piePaint = new Paint();
			piePaint.setColor(row.getInt("color"));
			piePaint.setAntiAlias(true);
			float v = (float) row.getDouble("value");
			float p = v / sum;//所占百分比
			float d = p * 360f;
			canvas.drawArc(rectF, preR, d, true, piePaint);
			preR = preR + d;

			// 绘制label
			int px = tx;
			int py = 20 + i * 40;
			canvas.drawRect(tx, py + 10, tx + 20, py + 30, piePaint);

			//绘制百分比文字
			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(0Xff5c5d60);
			paint.setTextAlign(Align.LEFT);
			paint.setTextSize(25f);
			canvas.drawText(row.getString("label") + f.format(p), px + 30, py + 30, paint);
		}
		// canvas.drawCircle(x, y, r, piePaint);

		return bitmap;
	}

	private static DecimalFormat f = new DecimalFormat("(#.##%)");

}
