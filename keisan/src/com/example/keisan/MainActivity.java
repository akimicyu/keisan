package com.example.keisan;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private TextView textView1;
	private TextView textViewAnswer;

	private Random rand = new Random();
	private int result = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 = (TextView)findViewById(R.id.textView1);
		textViewAnswer = (TextView)findViewById(R.id.textViewAnswer);
		newQuestion();
	}

	public void inputNumber(View v) {
		String ansStr = textViewAnswer.getText().toString();
		CharSequence input  = ((Button)v).getText();
		textViewAnswer.setText(ansStr + input);
	}

	public void inputClear(View v) {
		textViewAnswer.setText("");
	}

	public void inputEnter(View v) {
		try {
			String text = textViewAnswer.getText().toString();
			int answer = Integer.parseInt(text);
			String message = "○：正解です";
			if (answer != result) {
				message = "×：正解は" + result + "です";
			}
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
			newQuestion();
		} catch (NumberFormatException nfe) {
			Toast.makeText(this, "数字を入力してください", Toast.LENGTH_SHORT).show();
		}
	}

	private void newQuestion() {
		int a = rand.nextInt(9) + 1;
		int b = rand.nextInt(9) + 1;
		result = a * b;
		textView1.setText(String.format("%d * %d = ?", a, b));
		textViewAnswer.setText("");
	}
	
}
