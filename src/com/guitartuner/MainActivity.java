package com.guitartuner;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

	private AudioRecord mRecorder = null;
	private int N;
	private Thread mRecordThread = null;
	private Buffer buf = null;
	private FrequencyDetector fd = new FrequencyDetector();
	private volatile boolean mIsRecording = false;
	private Info info = null;
	private int count;
	private Pointer ptr = null;
	private ViewPropertyAnimator ptrAnimator = null;
	private Dial dial = null;
	private int micLevel;
	private final static int MIN_AMP = 2000;
	
	public static int getMIN_AMP() {
		return MIN_AMP;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout fl = new FrameLayout(this);
		info = new Info(this);
		fl.addView(info);
		ptr = new Pointer(this);
		fl.addView(ptr);
		ptrAnimator = ptr.animate();
		dial = new Dial(this);
		fl.addView(dial);
		setContentView(fl);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch(item.getItemId()) {
		case R.id.action_settings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.about:
			intent = new Intent(this, AboutActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mRecordThread = new Thread(new AudioRecordTask());
		try {
			mRecordThread.start();
		} catch(IllegalThreadStateException e) {
			e.printStackTrace();
		}
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
	}
	
	
	@Override
	protected void onStop() {
		super.onStop();
		mIsRecording = false;
		try {
			mRecorder.stop();
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
		mRecorder.release();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		info.invalidate();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		mIsRecording = false;
		try {
			mRecorder.stop();
		} catch(IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		info.invalidate();
	}

	@Override
	protected void onDestroy() {
		mIsRecording = false;
		super.onDestroy();
	}
	
	class AudioRecordTask implements Runnable {

		@Override
		public void run() {
			startRecording();
			return;

		}
		private void startRecording() {
			N = AudioRecord.getMinBufferSize(8000,AudioFormat.CHANNEL_IN_MONO, 
					AudioFormat.ENCODING_PCM_16BIT);
			if(N == AudioRecord.ERROR_BAD_VALUE)
				System.err.println("Recording parameters are not supported by the hardware");
			if(N == AudioRecord.ERROR)
				System.err.println("AudioRecord.getMinBufferSize() returned ERROR value");

			int i = 0;
			while(N/2 > 1){
				N /= 2;
				i++;
			}
			N<<=i+2;
			buf = new Buffer(N,4);
			try {
				mRecorder = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,
						AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, N);
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}

			if(mRecorder.getState() != AudioRecord.STATE_INITIALIZED)
				System.err.println("mRecorder state is uninitialized");

			try {
				mRecorder.startRecording();
				mIsRecording = true;
				new Thread(new Runnable() {
					@Override
					public void run() {
						getData();
					}	
				}).start();

			} catch(IllegalStateException e) {
				e.printStackTrace();
				Toast.makeText(getApplicationContext(), "Can't connect to microphone. Please reload app.", Toast.LENGTH_SHORT).show();
				System.exit(1);
			}
		}  	
	}
	
	
	private void getData() {
		
		short[] data = new short[N];
		while(true) {
			if(mIsRecording) {
				count = mRecorder.read(data, 0, N);
				if (count == AudioRecord.ERROR_BAD_VALUE)
					System.err.println("mRecorder.read() returned ERROR_BAD_VALUE");
				if (count == AudioRecord.ERROR_INVALID_OPERATION)
					System.err.println("mRecorder.read() returned ERROR_INVALID_OPERATION");
				if(count*2 == N) {
					if(isNoise(data))
						continue;
					else {
						buf.collect(data);
						count = 0;
						if (buf.getCount() > 0) {

							runOnUiThread(new Runnable() {
								double f = getFrequency();
								char note = fd.getNote(f);
								@Override
								public void run() {
									info.changeValues(f, note);
									info.invalidate();
									ptrAnimator.rotation((float)fd.getDeviation(f, note));
								}
							});
						}
					}
				}
			}
			else return;
		}
	}

	
	private double getFrequency() {
		return fd.detect(buf.data);
	}
	
	
	private boolean isNoise(short[] data) {
		int imax = 0;
		for(int i=1; i<data.length; i++) {
			if(Math.abs(data[i])>Math.abs(data[imax])) {
				imax = i;
			}
		}
		micLevel = Math.abs(data[imax]);
		info.setMicLevel(micLevel);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				info.invalidate();
				
			}
			
		});
		if(micLevel<MIN_AMP) 
			return true;
		else return false;
		
	}
		
}