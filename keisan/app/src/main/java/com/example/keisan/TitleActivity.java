package com.example.keisan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.widget.Button;
import android.widget.TextView;

public class TitleActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_title);
		getActionBar().hide();
		Button bn = (Button) findViewById(R.id.buttonNormal);
		bn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameStart("Normal", MainActivity.NORMAL_END);
			}
		});
		Button be = (Button) findViewById(R.id.buttonEndless);
		be.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gameStart("Endless", MainActivity.ENDLESS_END);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ハイスコア表示
		SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		int highScore = sp.getInt("highScore", 0);
		TextView highScoreText = ((TextView) findViewById(R.id.textViewHighScore));
		highScoreText.setText(getResources().getString(R.string.highScore, highScore));
		Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
		highScoreText.setAnimation(alpha);
		alpha.start();
	}

	private void gameStart(String mode, int requestcode) {
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.putExtra("mode", mode);
		startActivityForResult(i,requestcode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MainActivity.NORMAL_END && data != null) {
			Intent i = new Intent(this, ResultActivity.class);
			i.putExtra("countAnswer",  data.getIntExtra("countAnswer", 0));
			i.putExtra("countCorrect", data.getIntExtra("countCorrect", 0));
			startActivityForResult(i, ResultActivity.RESULT_END);
		} else if (requestCode == ResultActivity.RESULT_END && data != null) {
			if (data.getBooleanExtra("retry", false)) {
				gameStart("Normal", MainActivity.NORMAL_END);
			}
		}
	}
	
}
