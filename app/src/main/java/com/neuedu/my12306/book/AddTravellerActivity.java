package com.neuedu.my12306.book;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neuedu.my12306.CommonServer;
import com.neuedu.my12306.R;
import com.neuedu.my12306.contact.Contact;
import com.neuedu.my12306.contact.ContactUtils;
import com.neuedu.my12306.sub.AddContactActivity;
import com.neuedu.my12306.sub.EditContactActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTravellerActivity extends Activity {
    ListView listViewAddTraveller;
    List<Map<String, Object>> data;
    List<Map<String, Object>> temp;
    TravellerAdapter adapter;
    boolean[] flags;
    List<Map<String, Object>> list;

    public class TravellerAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<Map<String, Object>> list;

        public TravellerAdapter(Context context, List<Map<String, Object>> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder = new Holder();
            if (convertView == null){
                convertView = inflater.inflate(R.layout.item_add_traveller,null);
                holder.imageViewArrow = (ImageView) convertView.findViewById(R.id.imageViewArrow);
                holder.textViewID = (TextView) convertView.findViewById(R.id.textViewID);
                holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName);
                holder.textViewTel = (TextView) convertView.findViewById(R.id.textViewTel);
                holder.checkBoxAddTraveller = (CheckBox) convertView.findViewById(R.id.checkBoxAddTraveller);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.textViewName.setText(list.get(position).get("name").toString());
            holder.textViewID.setText(list.get(position).get("ID").toString());
            holder.textViewTel.setText(list.get(position).get("tel").toString());
            holder.checkBoxAddTraveller.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        flags[position] = true;
                    } else {
                        flags[position] = false;
                    }
                }
            });
            holder.imageViewArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(AddTravellerActivity.this, EditContactActivity.class);
                    intent.putExtra("row", (Serializable) data.get(position));
                    intent.putExtra("num", position);
                    startActivityForResult(intent, 67);
                }
            });

            return convertView;
        }

        public class Holder{
            private CheckBox checkBoxAddTraveller;
            private TextView textViewName;
            private TextView textViewID;
            private TextView textViewTel;
            private ImageView imageViewArrow;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_traveller);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        temp = (List<Map<String, Object>>) getIntent().getSerializableExtra("data");
        data = new ArrayList<Map<String, Object>>();

        listViewAddTraveller = (ListView) findViewById(R.id.listViewAddTraveller);

        int count = 0;
        readContacts();
        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < temp.size(); j++) {//一般来讲ID具有唯一性,如非测试此处用ID
                if (list.get(i).get("ID").equals(temp.get(j).get("ID")))
                    break;
                count++;
            }
            if (count == temp.size())
                data.add(list.get(i));
            count = 0;
        }
        temp.clear();

        flags = new boolean[10];
        adapter = new TravellerAdapter(this, data);
        listViewAddTraveller.setAdapter(adapter);

        Button buttonAddTraveller = (Button) findViewById(R.id.buttonAddTraveller);
        buttonAddTraveller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < data.size(); i++) {
                    if (flags[i]) {
                        temp.add(data.get(i));
                    }
                }

                setResult(2, new Intent().putExtra("data", (Serializable) temp));
                finish();
            }
        });
    }

    private void readContacts(){
        ArrayList<Contact> contacts = ContactUtils.getAllContacts(this);
        list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (int i = 0; i < contacts.size(); i++){
            map = new HashMap<String, Object>();
            map.put("name", contacts.get(i).getName() + "（"+ contacts.get(i).getTraveller_type() +"）");
            map.put("ID", contacts.get(i).getCard_type() + "：" + contacts.get(i).getCard_number());
            map.put("tel", "电话：" + contacts.get(i).getPhone());
            list.add(map);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            if (resultCode == 67) {
                int num = data.getIntExtra("num", 0);
                List<Map<String, Object>> da = (List<Map<String, Object>>) data.getSerializableExtra("item");
                this.data.get(num).put("name", da.get(0).get("arg1").toString() + "（" + da.get(3).get("arg1") + "）");
//                CommonServer.data.get(num).put("name", da.get(0).get("arg1").toString() + "（" + da.get(3).get("arg1") + "）");
                this.data.get(num).put("tel", "电话：" + da.get(4).get("arg1").toString());
//                CommonServer.data.get(num).put("tel", "电话：" + da.get(4).get("arg1").toString());
                ContactUtils.updateContact(this, this.data.get(num).get("ID").toString().split("：")[1],
                        da.get(0).get("arg1").toString(), da.get(3).get("arg1").toString(), da.get(4).get("arg1").toString());
            } else if (resultCode == 52) {
                int num = data.getIntExtra("num", 0);
                this.data.remove(num);
//                CommonServer.data.remove(num);
                ContactUtils.removeContact(this.data.get(num).get("ID").toString().split("：")[1]);
            } else if (resultCode == 20) {
                Contact contact = new Contact();
                List<Map<String, Object>> da = (List<Map<String, Object>>) data.getSerializableExtra("item");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", da.get(0).get("arg1").toString() + "（" + da.get(3).get("arg1") + "）");
                map.put("ID", da.get(1).get("arg1").toString() + "：" + da.get(2).get("arg1"));
                map.put("tel", "电话：" + da.get(4).get("arg1").toString());
                this.data.add(map);
//                CommonServer.data.add(map);
                contact.setCard_number(da.get(2).get("arg1").toString());
                contact.setCard_type(da.get(1).get("arg1").toString());
                contact.setName(da.get(0).get("arg1").toString());
                contact.setPhone(da.get(4).get("arg1").toString());
                contact.setTraveller_type(da.get(3).get("arg1").toString());
                ContactUtils.insertContact(this, contact);
            }

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_connect, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.mn_connect_add:
                startActivityForResult(new Intent(AddTravellerActivity.this,AddContactActivity.class),52);
                break;
            default:;
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
