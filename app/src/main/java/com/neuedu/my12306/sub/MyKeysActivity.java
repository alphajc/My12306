package com.neuedu.my12306.sub;

import com.neuedu.my12306.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MyKeysActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_keys);
		
		Button btnSave = (Button) findViewById(R.id.btn_save_key);
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				EditText etKey = (EditText) findViewById(R.id.etKey);
				EditText etKey2 = (EditText) findViewById(R.id.etKey2);
				if(etKey.getText().toString().equals(etKey2.getText().toString())){
					//存入自动登录文件
					SharedPreferences settings = getSharedPreferences("account-key", 0);
					SharedPreferences.Editor editor = settings.edit();
					editor.putString("password", etKey.getText().toString());
					editor.commit();
					Toast.makeText(MyKeysActivity.this, "密码修改成功!", Toast.LENGTH_LONG).show();
					finish();
					
				}else{
					Toast.makeText(MyKeysActivity.this, "两次密码输入不一致!", Toast.LENGTH_LONG).show();
					etKey.setText(null);
					etKey2.setText(null);
					etKey.requestFocus();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_keys, menu);
		return true;
	}

}
