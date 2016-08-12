package com.neuedu.my12306;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class LauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_launcher);
		CommonServer.getInstance();
		new Thread() {
			public void run() {
				if (autoLogin()) {
					try {
						sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startActivity(new Intent(LauncherActivity.this,
							MainActivity.class));
					Log.i(null, "main_next");
				} else {
					startActivity(new Intent(LauncherActivity.this,
							LoginActivity.class));
					Log.i(null, "login_next");
				}
				finish();
			}
		}.start();
	}

	protected boolean autoLogin() {
		SharedPreferences setting = getSharedPreferences("account-key", 0);
		String username = setting.getString("username", "");
		String password = setting.getString("password", "");
		if (username.isEmpty() || password.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.launcher, menu);
		return true;
	}

}
