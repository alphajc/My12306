package com.neuedu.my12306;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.neuedu.my12306.sub.MyAccountActivity;
import com.neuedu.my12306.sub.MyConnectActivity;
import com.neuedu.my12306.sub.MyKeysActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MyFragment extends Fragment {
	Button btn_logout = null;
	ListView listView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_my, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		btn_logout = (Button) getActivity().findViewById(R.id.LogOut);

		listView = (ListView) getActivity().findViewById(R.id.listView01);
		String[] listData = getResources().getStringArray(R.array.list_data);
		List<String> list = new ArrayList<String>();
		list.add(listData[0]);
		list.add(listData[1]);
		list.add(listData[2]);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
		listView.setAdapter(adapter);
		AdapterView.OnItemClickListener adapterView = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				switch (arg2) {
					case 0:
						startActivity(new Intent(getActivity(), MyConnectActivity.class));
						break;
					case 1:
						startActivity(new Intent(getActivity(), MyAccountActivity.class));
						break;
					case 2:
						final EditText editKey = new EditText(getActivity());
						new AlertDialog.Builder(getActivity())
								.setTitle("请输入原密码")
								.setIcon(android.R.drawable.ic_dialog_alert)
								.setView(editKey)
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										//获取原密码
										SharedPreferences settings = getActivity().getSharedPreferences("account-key", 0);
										String password = settings.getString("password", "");
										//判定不为空
										if (editKey.getText().toString() == null) {
											setClosable(dialog, false);
											editKey.setError("密码不能为空！");
											editKey.requestFocus();
										} else if (editKey.getText().toString().equals(password)) {//密码正确
											setClosable(dialog, true);
											startActivity(new Intent(getActivity(), MyKeysActivity.class));
										} else {//密码错误
											setClosable(dialog, false);
											editKey.setError("原密码错误，请重新输入");
											editKey.requestFocus();
										}
									}
								})
								.setNegativeButton("取消", new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										setClosable(dialog, true);
									}
								})
								.show();
						break;
					default:
						Toast.makeText(getActivity(), "wrong", Toast.LENGTH_LONG).show();
				}
			}
		};
		listView.setOnItemClickListener(adapterView);

		btn_logout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(getActivity(), LoginActivity.class));
				getActivity().finish();
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	private void setClosable(DialogInterface dialog, boolean b) {
		Field field;
		try {
			field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
			field.setAccessible(true);
			field.set(dialog, b);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
