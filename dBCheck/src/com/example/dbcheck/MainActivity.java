package com.example.dbcheck;

import java.io.IOException;

import com.example.dbcheck.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private MediaRecorder mRecorder;
	private int maxAmplitude, deciBel, dBTotal, count, status = 1;
	private TextView dBValue, averageValue;
	private ImageView chart;
	private boolean run;
	private Thread thread;
	private ProBar proBar;
	private Graph graph;

	private Handler handler = new Handler();

	private Runnable updater = new Runnable() {
		@Override
		public void run() {
			getSoundLevel();
			averageCalculation();
			graph.addValue(deciBel);
			setProBarValue();
			setDBText();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ImageButton resetButton = (ImageButton) findViewById(R.id.resetButton);
		ImageButton changeButton = (ImageButton) findViewById(R.id.changeButton);
		proBar = (ProBar) findViewById(R.id.proBar1);
		chart = (ImageView) findViewById(R.id.chart);
		dBValue = (TextView) findViewById(R.id.dBValue);
		averageValue = (TextView) findViewById(R.id.average_Value);

		graph = new Graph(this, "dB/s");
		LinearLayout layout = (LinearLayout) findViewById(R.id.subLayout);
		graph.setVisibility(View.GONE);
		layout.addView(graph);

		resetButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dBTotal = 0;
				count = 0;
			}
		});

		changeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				setView();
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_info) {
			showInfoActivity();

			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	public void showInfoActivity() {
		Intent intent = new Intent(this, InfoActivity.class);
		startActivity(intent);

	}


	@Override
	protected void onResume() {
		super.onResume();
		startRecording();

		thread = new Thread() {
			@Override
			public void run() {
				run = true;
				while (run) {
					try {
						Thread.sleep(1000);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					handler.post(updater);
				}
			}
		};
		thread.start();

	}

	@Override
	protected void onStop() {
		super.onStop();
		stopRecording();
		run = false;

	}

	@Override
	protected void onPause() {
		super.onPause();
		run = false;
		stopRecording();
	}

	public void startRecording() {

		mRecorder = new MediaRecorder();
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile("/dev/null");
		try {
			mRecorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		mRecorder.start();
		mRecorder.getMaxAmplitude();
	}

	public void stopRecording() {
		if (mRecorder != null) {

			mRecorder.stop();
			mRecorder.release();
			mRecorder = null;
		}
	}

	public int getSoundLevel() {
		if (mRecorder != null) {
			maxAmplitude = mRecorder.getMaxAmplitude();
		}
		if (maxAmplitude > 0) {
			deciBel = (int) (20.0 * Math.log10(maxAmplitude / 3));
		}
		return deciBel;
	}

	public void setDBText() {
		dBValue.setText(Integer.toString(deciBel) + " dB");
	}

	public void setProBarValue() {
		proBar.setProgress(deciBel * 2);
	}

	public void averageCalculation() {
		int average;
		System.out.println(deciBel);
		dBTotal += deciBel;
		count++;
		average = dBTotal / count;
		averageValue.setText(Integer.toString(average) + " dB");

	}

	public void setView() {
		if (status == 2) {
			graph.setVisibility(View.GONE);
			proBar.setVisibility(View.VISIBLE);
			chart.setVisibility(View.VISIBLE);
			status = 1;

		} else {
			proBar.setVisibility(View.GONE);
			chart.setVisibility(View.GONE);
			graph.setVisibility(View.VISIBLE);
			status = 2;
		}

	}
}