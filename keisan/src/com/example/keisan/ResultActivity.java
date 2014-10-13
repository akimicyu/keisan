package com.example.keisan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends Activity {

	public static final int RESULT_END   = 300;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		getActionBar().hide();
		Intent result = getIntent();
		if (result.hasExtra("countAnswer") && result.hasExtra("countCorrect")) {
			int countAnswer  = result.getIntExtra("countAnswer", 0);
			int countCorrect = result.getIntExtra("countCorrect", 0);
			TextView status = ((TextView) findViewById(R.id.textViewStatus));
			status.setText(getResources().getString(R.string.status, countCorrect, countAnswer));
			TextView percent = ((TextView) findViewById(R.id.textViewPercent));
			float per = 100.0f * countCorrect / countAnswer;
			percent.setText(getResources().getString(R.string.percent, per));
			// 以降、ハイスコア周り
			SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
			int highScore = sp.getInt("highScore", 0);
			if (highScore < countCorrect) {
				sp.edit().putInt("highScore", countCorrect).commit();
				TextView hvhs = ((TextView) findViewById(R.id.textViewHighScoreUpdate));
				hvhs.setText(getResources().getString(R.string.highScoreUpdate, highScore, countCorrect));
				Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
				hvhs.setAnimation(alpha);
				alpha.start();
				hvhs.setVisibility(View.VISIBLE);
			}
		}
		Button bn = (Button) findViewById(R.id.buttonRetry);
		bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.putExtra("retry", true);
				setResult(ResultActivity.RESULT_END, i);
				finish();
			}
		});
	}

}
