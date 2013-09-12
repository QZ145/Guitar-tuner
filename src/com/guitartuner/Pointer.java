package com.guitartuner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;


public class Pointer extends View {
	
	public Pointer(Context context) {
		super(context);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		int heigth = canvas.getHeight();
		int width = canvas.getWidth();
		int cx = width/2;
		int cy = heigth/2;
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		canvas.drawLine(width/2-6, heigth/2, width/2, heigth/2 - width/2 + 25, paint);
		canvas.drawLine(width/2+6, heigth/2, width/2, heigth/2 - width/2 + 25, paint);
		paint.setColor(Color.BLACK);
		canvas.drawCircle(cx, cy, 6, paint);
	}

}
