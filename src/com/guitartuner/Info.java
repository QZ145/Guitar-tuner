package com.guitartuner;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.View;

public class Info extends View {
	
	private int heigth, width, micLevel;
	private boolean valuesChanged = false;
	private boolean micLevelChanged = false;
	private String frequencyStr;
	private int f;
	private String note;
	private Typeface tf;
	private SharedPreferences prefs;
	private int noiseLevel;
	private boolean drawFreqbar;
	private boolean drawFreq;
	private boolean drawMicLevel;
	
	public Info(Context context) {
		super(context);
		tf = Typeface.createFromAsset(context.getAssets(), "fonts/DIGITALDREAMFAT.ttf");
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		noiseLevel = MainActivity.getMIN_AMP();
		heigth = canvas.getHeight();
		width = canvas.getWidth();
		drawFreqbar = prefs.getBoolean(SettingsActivity.PREFS_KEY_SHOW_FREQBAR, true);
		drawFreq = prefs.getBoolean(SettingsActivity.PREFS_KEY_SHOW_FREQ, true);
		drawMicLevel = prefs.getBoolean(SettingsActivity.PREFS_KEY_SHOW_MIC_LEVEL, false);
		if(!valuesChanged) {
			frequencyStr = "  0.0";
			note = "X";
			f = 1;
		}
		
		if(!micLevelChanged) {
			micLevel = 0;
		}
		Paint paint = new Paint();
		canvas.drawColor(Color.rgb(150, 150, 100));
		
		if(drawFreq) {
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLACK);
			paint.setTypeface(tf);
			paint.setTextSize(20);
			canvas.drawText(frequencyStr + " Hz", (int) width/2 - 50, heigth/2 + 30, paint);
		}
		
		paint.setStyle(Paint.Style.FILL);
		paint.setTypeface(tf);
		paint.setColor(Color.RED);
		paint.setTextSize(50);
		canvas.drawText(note, width/2-10, 80, paint);
		
		if(drawFreqbar) {
			paint.setTypeface(null);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.BLACK);
			canvas.drawRect(10, heigth-80, 10+f*(width-20)/350, heigth-50, paint);
			paint.setColor(Color.GREEN);
			paint.setTextSize(20);
			canvas.drawText("Frequency", 13, heigth-58, paint);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			canvas.drawRect(9, heigth-81, width-11, heigth-51, paint);
			paint.setTextSize(12);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawText("0 Hz", 10, heigth-82, paint);
			canvas.drawText("175 Hz", width/2-25, heigth-82, paint);
			canvas.drawText("350 Hz", width-42, heigth-82, paint);
		}
		
		if(drawMicLevel) {
			paint.setTypeface(null);
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.RED);
			canvas.drawRect(90, heigth-30, (int)(90+noiseLevel*(width-100)/32767.0), heigth-10, paint);
			paint.setColor(Color.BLUE);
			canvas.drawRect(90, heigth-28, (int)(90+micLevel*(width-100)/32767.0), heigth-12, paint);
			paint.setColor(Color.BLACK);
			paint.setTextSize(20);
			canvas.drawText("Mic level:", 5, heigth-12, paint);
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawRect(89, heigth-31, width-9, heigth-9, paint);
		}
		
	}
	
	public void changeValues(double f, char note) {
		frequencyStr = Double.toString(f);
		this.f = (int) f;
		if (f<100) 
			frequencyStr = " " + frequencyStr.substring(0, 4);
		else 
			frequencyStr = frequencyStr.substring(0, 5);
		this.note = String.valueOf(note);
		valuesChanged = true;
	}
	
	public void setMicLevel(int level) {
		micLevel = level;
		micLevelChanged = true;
	}
}
