package com.example.keisan;

import java.util.Locale;

import android.os.AsyncTask;

public class NormalModeTask extends AsyncTask<Integer, String, Void> {

	private final int SECONDS = 1000;
	private final int MINUTES = 60 * SECONDS;
	MainActivity activity;
	
	public NormalModeTask(MainActivity activity) {
		this.activity = activity;
	}

	@Override
	protected Void doInBackground(Integer... params) {
		try {
			long startTime = System.currentTimeMillis();
			long currentTime = System.currentTimeMillis();
			String remainText = "01:00:00";
			while (currentTime - startTime < 1 * MINUTES) {
				Thread.sleep(13); // タイマーのまわりをそれっぽくしたい
				long remain = 1*MINUTES - (currentTime - startTime);
				remainText = String.format(
						Locale.JAPAN,
						"00:%1$02d:%2$04d",
						remain / SECONDS,
						remain % SECONDS
					);
				publishProgress(remainText);
				currentTime = System.currentTimeMillis();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		activity.timeUpdate(values[0]);
	}

	@Override
	protected void onPostExecute(Void result) {
		activity.gameEnd();
	}

}
