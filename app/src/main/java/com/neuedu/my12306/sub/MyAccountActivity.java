package com.neuedu.my12306.sub;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.neuedu.my12306.R;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyAccountActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_account);
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		ListView listview = null;
		listview = (ListView) findViewById(R.id.lvMyAccount);
		final List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("key", "用户名");
		map.put("value", "moximoxi");
		map.put("img", null);
		list.add(map);
		Map<String,Object> map1 = new HashMap<String, Object>();
		map1.put("key", "姓名");
		map1.put("value", "安倍晋三");
		map1.put("img", null);
		list.add(map1);
		Map<String,Object> map2 = new HashMap<String, Object>();
		map2.put("key", "证件类型");
		map2.put("value", "身份证");
		map2.put("img", null);
		list.add(map2);
		Map<String,Object> map3 = new HashMap<String, Object>();
		map3.put("key", "证件号码");
		map3.put("value", "123324565215796328");
		map3.put("img", null);
		list.add(map3);
		Map<String,Object> map4 = new HashMap<String, Object>();
		map4.put("key", "乘客类型");
		map4.put("value", "成人");
		map4.put("img", R.drawable.forward_25);
		list.add(map4);
		Map<String,Object> map5 = new HashMap<String, Object>();
		map5.put("key", "电话");
		map5.put("value", "13569742681");
		map5.put("img", R.drawable.forward_25);
		list.add(map5);
		
		final SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_account, 
				new String[]{"key","value","img"}, 
				new int[]{R.id.accountKey,R.id.accountValue,R.id.imgAccount});
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case 4:
					final ArrayList<String> types =new ArrayList<String>();
					types.add("成人");
					types.add("儿童");
					types.add("学生");
					types.add("其它");
					new AlertDialog.Builder(MyAccountActivity.this)
					.setTitle("请选择乘客类型")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(new String[]{"成人","儿童","学生","其它"}, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							list.get(4).put("value", types.get(which));
							adapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("取消", null)
					.show();
					break;
				case 5:
					final EditText editTel = new EditText(MyAccountActivity.this);
					editTel.setText(list.get(5).get("value").toString());
					editTel.selectAll();
					new AlertDialog.Builder(MyAccountActivity.this)
					.setTitle("联系电话")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(editTel)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(editTel.getText().toString().isEmpty()||editTel.getText().toString().length() != 11){
								editTel.setError("电话号码有误");
								editTel.requestFocus();
								setClosable(dialog, false);
							}
							else{
								list.get(5).put("value", editTel.getText().toString());
								adapter.notifyDataSetChanged();
								setClosable(dialog, true);
							}
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							setClosable(dialog, true);
						}
					})
					.show();
					break;
					default:;
				}
			}
		});
		Button btnAccount = (Button) findViewById(R.id.btn_account);
		btnAccount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void setClosable(DialogInterface dialog, boolean b){
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_account, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		default:;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

}
