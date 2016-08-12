package com.neuedu.my12306;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {
	Button btn_login = null;
	TextView tvPassWd = null;
	EditText etUsername = null;
	EditText etPassword = null;
	CheckBox cbAutoLogin = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        SharedPreferences setting = getSharedPreferences("account-key", 0);
		String username = setting.getString("username", "");
		String password = setting.getString("password", "");
		
        setContentView(R.layout.activity_login);
        
        tvPassWd = (TextView) findViewById(R.id.tvpasswd);
        etUsername = (EditText) findViewById(R.id.username);
        etPassword = (EditText) findViewById(R.id.passwd);
        cbAutoLogin = (CheckBox) findViewById(R.id.auto_login);
        
        etUsername.setText(username);
        etPassword.setText(password);
        
        tvPassWd.setText(Html.fromHtml("<a href=\"http://www.12306.cn\">忘记密码？</a>"));
        tvPassWd.setMovementMethod(LinkMovementMethod.getInstance());
        
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			/*	Toast.makeText(LoginActivity.this,"你说啥？",Toast.LENGTH_LONG).show();*/
				String strUsername = etUsername.getText().toString();
				String strPassword = etPassword.getText().toString();
				if(strUsername.isEmpty()){
					etUsername.setError("账号不能为空");
					etUsername.requestFocus();
				}
				else if(strPassword.isEmpty()){
					etPassword.setError("密码不能为空");
					etPassword.requestFocus();
				}
				else{
					if(cbAutoLogin.isChecked()){
						SharedPreferences setting = getSharedPreferences("account-key", 0);
						SharedPreferences.Editor editor = setting.edit();
						editor.putString("username", strUsername);
						editor.putString("password", strPassword);
						editor.commit();
					}
					else{
						SharedPreferences setting = getSharedPreferences("account-key", 0);
						SharedPreferences.Editor editor = setting.edit();
						editor.remove("username");
						editor.remove("password");
						editor.commit();
					}
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				}
			}
		});
    }

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
