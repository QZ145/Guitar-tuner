package com.guitartuner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class Dial extends View{

	public Dial(Context context) {
		super(context);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas) {
		int y = canvas.getHeight();
		int x = canvas.getWidth();
		
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
		
		for(int i=0; i<19; i++) {
			if(i%3 == 0)
				canvas.drawLine(5, y/2, 23, y/2, paint);
			else
				canvas.drawLine(5, y/2, 15, y/2, paint);
			canvas.rotate(10, x/2, y/2);
		}
		canvas.restore();
	}
	
}
