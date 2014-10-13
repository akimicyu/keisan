package com.example.keisan;

import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	
	private TextView textView1;
	private EditText editText1;
	private Button button1;

	private Random rand = new Random();
	private int result = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1 = (TextView)findViewById(R.id.textView1);
		editText1 = (EditText)findViewById(R.id.editText1);
		button1 = (Button)findViewById(R.id.button1);
		button1.setOnClickListener(this);
		newQuestion();
	}

	@Override
	public void onClick(View v) {
		try {
			String text = editText1.getText().toString();
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
		textView1.setText(String.format("%d %s %d = ?", a, op.character(), b));
		editText1.setText("");
	}
	
}
