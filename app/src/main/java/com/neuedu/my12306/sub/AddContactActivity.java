package com.neuedu.my12306.sub;

import java.io.Serializable;
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
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddContactActivity extends Activity {
	List<Map<String,Object>> data =null;
	ListView listview = null;
	Button btnAdd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		listview = (ListView) findViewById(R.id.lvAdd);
		data = new ArrayList<Map<String,Object>>();
		
		Map<String,Object> row0 = new HashMap<String, Object>();
		row0.put("arg0", "姓名");
		row0.put("arg1", "");
		data.add(row0);
		Map<String,Object> row1 = new HashMap<String, Object>();
		row1.put("arg0", "证件类型");
		row1.put("arg1", "");
		data.add(row1);
		Map<String,Object> row2 = new HashMap<String, Object>();
		row2.put("arg0", "证件号码");
		row2.put("arg1", "");
		data.add(row2);
		Map<String,Object> row3 = new HashMap<String, Object>();
		row3.put("arg0", "乘客类型");
		row3.put("arg1", "");
		data.add(row3);
		Map<String,Object> row4 = new HashMap<String, Object>();
		row4.put("arg0", "电话");
		row4.put("arg1", "");
		data.add(row4);

		final SimpleAdapter adapter = new SimpleAdapter(this,
				data,R.layout.item_add,
				new String[]{"arg0","arg1"},
				new int[]{R.id.add_key,R.id.add_value});
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				switch(arg2){
				case 0:
					final EditText editName = new EditText(AddContactActivity.this);
					Builder dialogName = new AlertDialog.Builder(AddContactActivity.this);
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
				case 1:
					final ArrayList<String> ID_types =new ArrayList<String>();
					ID_types.add("身份证");
					ID_types.add("学生证");
					ID_types.add("港澳同胞证");
					ID_types.add("军人证");
					new AlertDialog.Builder(AddContactActivity.this)
					.setTitle("请选择证件类型")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(new String[]{"身份证","学生证","港澳同胞证","军人证"}, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							data.get(1).put("arg1", ID_types.get(which));
							adapter.notifyDataSetChanged();
						}
					})
					.setNegativeButton("取消", null)
					.show();
					break;
				case 2:
					final EditText editID = new EditText(AddContactActivity.this);
					new AlertDialog.Builder(AddContactActivity.this)
					.setTitle("证件号码")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(editID)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(editID.getText().toString().isEmpty()||editID.getText().toString().length() != 18){
								editID.setError("证件号码有误");
								editID.requestFocus();
								setClosable(dialog, false);
							}
							else{
								data.get(2).put("arg1", editID.getText().toString());
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
				case 3:
					final ArrayList<String> types =new ArrayList<String>();
					types.add("成人");
					types.add("儿童");
					types.add("学生");
					types.add("其它");
					new AlertDialog.Builder(AddContactActivity.this)
					.setTitle("请选择乘客类型")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setSingleChoiceItems(new String[]{"成人","儿童","学生","其它"}, 0, new DialogInterface.OnClickListener() {
						
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
					final EditText editTel = new EditText(AddContactActivity.this);
					new AlertDialog.Builder(AddContactActivity.this)
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
		btnAdd = (Button) findViewById(R.id.btn_add);
		btnAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("item", (Serializable)data);
				setResult(20, intent);
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
		getMenuInflater().inflate(R.menu.add_contact, menu);
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
