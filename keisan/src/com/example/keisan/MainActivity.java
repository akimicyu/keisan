package com.example.keisan;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int NORMAL_END   = 100;
	public static final int ENDLESS_END  = 101;
	
	private TextView textViewQuestion;
	private TextView textViewAnswer;
	private TextView textViewStatus;
	private TextView textViewTime;
	private Toast myToast;
	private SoundPool soundPool;
	private int soundOk;
	private int soundNg;
	
	NormalModeTask nmt = null;
	private Random rand = new Random();
	private int result = 0;

	private int countAnswer  = 0;
	private int countCorrect = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 画面周り
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
		textViewAnswer = (TextView) findViewById(R.id.textViewAnswer);
		textViewStatus = (TextView) findViewById(R.id.textViewStatus);
		textViewTime   = (TextView) findViewById(R.id.textViewTime);
		// サウンド
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		soundOk = soundPool.load(this, R.raw.crrect_answer2, 1);
		soundNg = soundPool.load(this, R.raw.blip1, 1);
		// モード
		Intent i = getIntent();
		String mode = i.getStringExtra("mode");
		if ("Normal".equals(mode)) {
			textViewTime.setVisibility(View.VISIBLE);
			nmt = new NormalModeTask(this);
			nmt.execute();
		}
		newQuestion();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		soundPool.release();
		if (nmt != null) {
			nmt.cancel(true);
		}
	}

	public void inputNumber(View v) {
		String ansStr = textViewAnswer.getText().toString();
		if (ansStr.length() >= 6) return; // 桁数の上限
		CharSequence input = ((Button) v).getText();
		textViewAnswer.setText(ansStr + input);
	}

	public void inputClear(View v) {
		textViewAnswer.setText("");
	}

	public void inputEnter(View v) {
		boolean correct;
		String message;
		try {
			String text = textViewAnswer.getText().toString();
			int answer = Integer.parseInt(text);
			correct = (answer == result);
			message = correct ? "○：正解です" : "×：正解は" + result + "です";
		} catch (NumberFormatException nfe) {
			correct = false;
			message = "数字を入力してください";
		}
		if (myToast != null) { myToast.cancel(); }
		myToast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		myToast.show();
		int playSound = correct ? soundOk : soundNg;
		soundPool.play(playSound, 1.0F, 1.0F, 0, 0, 1.0F);
		updateStatus(correct);
		newQuestion();
	}

	private void newQuestion() {
		Operation op;
		int a, b;
		do {
			op = Operation.values()[rand.nextInt(Operation.values().length)];
			a = rand.nextInt(10);
			b = rand.nextInt(10);
			if (op == Operation.MINUS) { // マイナスを入力させたくない
				if (a < b) continue;
			}
			if (op == Operation.DIVIDE) { // ゼロ除算と余りは無しにしたい
				if (b == 0) continue;
				if (a % b > 0) continue;
			}
			break;
		} while(true);
		result = op.apply(a, b);
		textViewQuestion.setText(String.format("%d %s %d = ?", a, op.character(), b));
		textViewAnswer.setText("");
	}
	
	private void updateStatus(boolean correct) {
		countAnswer++;
		if (correct) countCorrect++;
		String status = getResources().getString(R.string.status, countCorrect, countAnswer);
		textViewStatus.setText(status);
	}

	public void timeUpdate(String newTime) {
		textViewTime.setText(newTime);
		int remainMin = Integer.parseInt(newTime.substring(3,5));
		if (remainMin < 10) {
			textViewTime.setTextColor(getResources().getColor(R.color.red));
		} else if (remainMin < 20) {
			textViewTime.setTextColor(getResources().getColor(R.color.yellow));
		}
	}

	public void gameEnd() {
		Intent i = new Intent();
		i.putExtra("countAnswer", countAnswer);
		i.putExtra("countCorrect", countCorrect);
		setResult(NORMAL_END, i);
		finish();
	}

}
