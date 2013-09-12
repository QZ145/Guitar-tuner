package com.guitartuner;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextView mText = new TextView(this);
		setContentView(mText);
		mText.setTextSize(20);
		mText.setText("This tuner has next features:\n" +
				"- Simple noise filter\n- Standard guitar tuning(6 strings)\n- User-friendly interface\n- Some appearance settings" +
				"\n\n\n\n\nAuthor: Artem Moskovko");
	}

	
}
