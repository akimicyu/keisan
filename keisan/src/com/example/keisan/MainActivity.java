package com.example.keisan;

import java.util.Random;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private TextView textViewQuestion;
	private TextView textViewAnswer;
	private TextView textViewStatus;
	private SoundPool soundPool;
	private int soundOk;
	private int soundNg;
	
	private Random rand = new Random();
	private int result = 0;

	private int countAnswer  = 0;
	private int countCorrect = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
		textViewAnswer = (TextView) findViewById(R.id.textViewAnswer);
		textViewStatus = (TextView) findViewById(R.id.textViewStatus);
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		soundOk = soundPool.load(this, R.raw.crrect_answer2, 1);
		soundNg = soundPool.load(this, R.raw.blip1, 1);
		newQuestion();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		soundPool.release();
	}

	public void inputNumber(View v) {
		String ansStr = textViewAnswer.getText().toString();
		CharSequence input = ((Button) v).getText();
		textViewAnswer.setText(ansStr + input);
	}

	public void inputClear(View v) {
		textViewAnswer.setText("");
	}

	public void inputEnter(View v) {
		try {
			String text = textViewAnswer.getText().toString();
			int answer = Integer.parseInt(text);
			boolean correct = (answer == result);
			updateStatus(correct);
			String message = correct ? "○：正解です" : "×：正解は" + result + "です";
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			int playSound = correct ? soundOk : soundNg;
			soundPool.play(playSound, 1.0F, 1.0F, 0, 0, 1.0F);
			newQuestion();
		} catch (NumberFormatException nfe) {
			Toast.makeText(this, "数字を入力してください", Toast.LENGTH_SHORT).show();
		}
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

}
