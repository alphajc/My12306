package com.neuedu.my12306.sub;

import java.io.Serializable;
import java.util.*;

import com.neuedu.my12306.CommonServer;
import com.neuedu.my12306.R;
import com.neuedu.my12306.contact.Contact;
import com.neuedu.my12306.contact.ContactUtils;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class MyConnectActivity extends Activity {
	public List<Map<String, Object>> data;
	ListView lvMyConnect;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_connect);
		
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		
		lvMyConnect = (ListView) findViewById(R.id.lvMyConnect);
		data = new ArrayList<Map<String, Object>>();//数据库+
//		data = CommonServer.data;
		SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item_my_connect,
								new String[]{"name","ID","tel"}, 
								new int[]{R.id.tvName,R.id.tvID,R.id.tvTel});
		lvMyConnect.setAdapter(adapter);
		readContacts();//数据库
		lvMyConnect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MyConnectActivity.this, EditContactActivity.class);
				intent.putExtra("row", (Serializable)data.get(arg2));
				intent.putExtra("num", arg2);
				startActivityForResult(intent, 67);
			}
		});
	}

	private void readContacts(){
		Map<String,Object> map;
		ArrayList<Contact> contacts = ContactUtils.getAllContacts(this);
		for (int i = 0; i < contacts.size(); i++) {
			map = new HashMap<String, Object>();
			map.put("name", contacts.get(i).getName() + "（"+ contacts.get(i).getTraveller_type() +"）");
			map.put("ID", contacts.get(i).getCard_type() + "：" + contacts.get(i).getCard_number());
			map.put("tel", "电话：" + contacts.get(i).getPhone());
			data.add(map);
		}
		((SimpleAdapter)lvMyConnect.getAdapter()).notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_connect, menu);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(data != null){
			if(resultCode == 67){
				int num = data.getIntExtra("num", 0);
				List<Map<String,Object>> da = (List<Map<String, Object>>) data.getSerializableExtra("item");
				this.data.get(num).put("name", da.get(0).get("arg1").toString() + "（" + da.get(3).get("arg1").toString() + "）");
				this.data.get(num).put("tel", "电话：" + da.get(4).get("arg1").toString());
				ContactUtils.updateContact(this, this.data.get(num).get("ID").toString().split("：")[1],
						da.get(0).get("arg1").toString(), da.get(3).get("arg1").toString(), da.get(4).get("arg1").toString());
			}
			else if(resultCode == 52){
				int num = data.getIntExtra("num", 0);
				this.data.remove(num);
				ContactUtils.removeContact(this.data.get(num).get("ID").toString().split("：")[1]);
			}
			else if(resultCode == 20){
				Contact contact = new Contact();
				List<Map<String,Object>> da = (List<Map<String, Object>>) data.getSerializableExtra("item");
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("name", da.get(0).get("arg1").toString() + "（" + da.get(3).get("arg1").toString() + "）");
				map.put("ID", da.get(1).get("arg1").toString() + "：" + da.get(2).get("arg1").toString());
				map.put("tel", "电话：" + da.get(4).get("arg1").toString());
				this.data.add(map);
				contact.setCard_number(da.get(2).get("arg1").toString());
				contact.setCard_type(da.get(1).get("arg1").toString());
				contact.setName(da.get(0).get("arg1").toString());
				contact.setPhone(da.get(4).get("arg1").toString());
				contact.setTraveller_type(da.get(3).get("arg1").toString());
				ContactUtils.insertContact(this, contact);
			}

			((SimpleAdapter)lvMyConnect.getAdapter()).notifyDataSetChanged();
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	};
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		case R.id.mn_connect_add:
			startActivityForResult(new Intent(MyConnectActivity.this,AddContactActivity.class),52);
			break;
		default:;
		}
		
		return super.onMenuItemSelected(featureId, item);
	}

}
