package com.neuedu.my12306.sub;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

import android.app.AlertDialog.*;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

import com.neuedu.my12306.R;

public class EditContactActivity extends Activity {
	List<Map<String,Object>> data =null;
	ListView listview = null;
	Button btnSave = null;
	int num = 0;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		num = intent.getIntExtra("num", 0);
		Map<String,Object> contact =  (Map<String, Object>) intent.getSerializableExtra("row");
		listview = (ListView) findViewById(R.id.lvEdit);
		data = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> row0 = new HashMap<String, Object>();
		String name = contact.get("name").toString();
		row0.put("arg0", "姓名");
		row0.put("arg1", name.split("（")[0]);
		row0.put("arg2", R.drawable.forward_25);
		data.add(row0);
		Map<String,Object> row1 = new HashMap<String, Object>();
		String ID = contact.get("ID").toString();
		row1.put("arg0", "证件类型");
		row1.put("arg1", ID.split("：")[0]);
		row1.put("arg2", null);
		data.add(row1);
		Map<String,Object> row2 = new HashMap<String, Object>();
		row2.put("arg0", "证件号码");
		row2.put("arg1", ID.split("：")[1]);
		row2.put("arg2", null);
		data.add(row2);
		Map<String,Object> row3 = new HashMap<String, Object>();
		row3.put("arg0", "乘客类型");
		row3.put("arg1", name.split("（")[1].split("）")[0]);
		row3.put("arg2", R.drawable.forward_25);
		data.add(row3);
		Map<String,Object> row4 = new HashMap<String, Object>();
		String tel = contact.get("tel").toString();
		row4.put("arg0", "电话");
		row4.put("arg1", tel.split("：")[1]);
		row4.put("arg2", R.drawable.forward_25);
		data.add(row4);

		final SimpleAdapter adapter = new SimpleAdapter(this,
				data,R.layout.item_edit,
				new String[]{"arg0","arg1","arg2"},
				new int[]{R.id.editKey,R.id.editValue,R.id.imgArrow});
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case 0:
					final EditText editName = new EditText(EditContactActivity.this);
					editName.setText(data.get(arg2).get("arg1").toString());
					editName.selectAll();
					Builder dialogName = new AlertDialog.Builder(EditContactActivity.this);
					dialogName.setIcon(android.R.drawable.ic_dialog_info);
					dialogName.setTitle("请输入姓名");
					dialogName.setView(editName);
					dialogName.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(editName.getText().toString().isEmpty())
							{
								setClosable(dialog, false);
								editName.setError("联系人名不能为空！！");
								editName.requestFocus();
							} else{
								setClosable(dialog, true);
								data.get(0).put("arg1", editName.getText().toString());
								adapter.notifyDataSetChanged();
							}
						}
					});
					dialogName.setNegativeButton("取消", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							setClosable(dialog, true);
						}
					});
					dialogName.show();
					break;
				case 3:
					String type = data.get(arg2).get("arg1").toString();
					final ArrayList<String> types =new ArrayList<String>();
					types.add("成人");
					types.add("儿童");
					types.add("学生");
					types.add("其它");
					new AlertDialog.Builder(EditContactActivity.this)
					.setTitle("请选择乘客类型")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(new String[]{"成人","儿童","学生","其它"}, types.indexOf(type), new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							data.get(3).put("arg1", types.get(which));
							adapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("取消", null)
					.show();
					break;
				case 4:
					final EditText editTel = new EditText(EditContactActivity.this);
					editTel.setText(data.get(arg2).get("arg1").toString());
					editTel.selectAll();
					new AlertDialog.Builder(EditContactActivity.this)
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
								data.get(4).put("arg1", editTel.getText().toString());
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
					default:
				}
			}
			
		});
		
		btnSave = (Button) findViewById(R.id.btn_save);
		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("item", (Serializable)data);
				intent.putExtra("num", num);
				setResult(67, intent);
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
		getMenuInflater().inflate(R.menu.edit_contact, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("num", num);
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		case R.id.mn_connect_del:
			setResult(52, intent);
			finish();
			break;
		default:
		}
		
		return super.onMenuItemSelected(featureId, item);
	}
}
