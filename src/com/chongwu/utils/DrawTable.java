package com.chongwu.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;

public class DrawTable {

	/**
	 * 画统计表
	 * 
	 * @param rows：是一个jsonObject数组，每个jsonObject包含 如下三个值 
	 *            JSONObject row = new JSONObject();
	 *             row.put("label", i);
	 *              row.put("scale", ((int)(Math.random() * 1000) % 100));
	 *               row.put("value", "120K");
	 * @param day  今天是本月第几天
	 * @return
	 * @throws Exception
	 */
	public static Bitmap createBarBitmap(JSONArray rows, int day) throws Exception {
		int h = 250; // 图形区高度
		int cw = 80; // 单元宽度
		int top = 0; // 顶部坐标
		int bottom = h + top; // 图形区底部坐标
		int w = cw * rows.length(); // 图形区宽度

		Bitmap bitmap = Bitmap.createBitmap(w,320, Bitmap.Config.ARGB_8888); // 建立一个空的BItMap

		Canvas canvas = new Canvas(bitmap);// 初始化画布 绘制的图像到icon上
		canvas.drawColor(0xFFeef2f6);

		{ // 绘制图形区Grid-X坐标线
			Paint paint = new Paint();
			paint.setColor(0xffc8d0d9);
			int ch = cw;
			for (int i = 0; i <= 4; i++) {
				canvas.drawLine(0, i * ch+10, w, i * ch+10, paint);
			}
		}

		{ // 绘制图形区Grid-Y坐标线
			Paint paint = new Paint();
			paint.setColor(0xffc8d0d9);
			for (int i = 0; i <= rows.length(); i++) {
				int x = i * cw;
				canvas.drawLine(x, 0, x, bottom, paint);
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
				if (i == (day - 1)) {
					canvas.drawText("今天", x, bottom + 40, paint);
				} else {
					canvas.drawText(row.getString("label"), x, bottom + 40, paint);
				}
			}
		}

		{// 绘制Bar

			Paint barPaint = new Paint();
			barPaint.setColor(0x66419c24);

			Paint barPaint2 = new Paint();
			barPaint2.setColor(0xFF419c24);

			Paint paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(0Xff5c5d60);
			paint.setTextAlign(Align.CENTER);
			paint.setTextSize(25f);

			for (int i = 0; i < rows.length(); i++) {
				JSONObject row = rows.getJSONObject(i);
				float x = i * cw + cw / 2;
				double y = (h + top) - (h * row.getDouble("scale")) / 100D;
				if ((day - 1) == i) {
					canvas.drawRect(x - 20, (float) y, x + 20, bottom, barPaint2);
				} else {
					canvas.drawRect(x - 20, (float) y, x + 20, bottom, barPaint);
				}
				canvas.drawText(row.getString("value"), x, (float) (y - 15d), paint);
			}
		}

		return bitmap;
	}

}
