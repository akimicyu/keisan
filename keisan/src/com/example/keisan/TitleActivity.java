package com.example.keisan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

	private void gameStart(String mode, int requestcode) {
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
		i.putExtra("mode", mode);
		startActivityForResult(i,requestcode);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == MainActivity.NORMAL_END) {
			Intent i = new Intent(this, ResultActivity.class);
			i.putExtra("countAnswer",  data.getIntExtra("countAnswer", 0));
			i.putExtra("countCorrect", data.getIntExtra("countCorrect", 0));
			startActivity(i);
		} else if (requestCode == MainActivity.NORMAL_RETRY) {
			gameStart("Normal", MainActivity.NORMAL_END);
		}
	}
	
}
