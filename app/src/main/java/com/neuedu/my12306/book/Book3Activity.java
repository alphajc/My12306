package com.neuedu.my12306.book;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.neuedu.my12306.CommonServer;
import com.neuedu.my12306.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Book3Activity extends Activity {
    TextView textViewSource;
    TextView textViewStartTime;
    TextView textViewTrainNum;
    TextView textViewDateTime;
    TextView textViewDestination;
    TextView textViewStopTime;
    ListView listViewMyContact;
    TextView textViewSeat;
    TextView textViewSeatPrice;
    TextView textViewAdd;
    TextView textViewTotal;
    Button buttonSubmit;
    List<Map<String, Object>> list;
    DelAdapter adapter;
    float total;
    float price;
    String status;
    float multiple;
    Map<String, Object> map;
    int position;

    public class DelAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<Map<String, Object>> data;

        public DelAdapter(Context context, List<Map<String, Object>> list) {
            this.inflater = LayoutInflater.from(context);
            this.data = list;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder holder;
            if (convertView == null){
                convertView = inflater.inflate(R.layout.item_book3,null);
                holder = new Holder();

                holder.textViewID = (TextView) convertView.findViewById(R.id.textViewID3);
                holder.textViewName = (TextView) convertView.findViewById(R.id.textViewName3);
                holder.textViewTel = (TextView) convertView.findViewById(R.id.textViewTel3);
                holder.imageViewDel = (ImageView) convertView.findViewById(R.id.imageViewDel);

                convertView.setTag(holder);

            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.textViewID.setText(data.get(position).get("ID").toString());
            holder.textViewName.setText(data.get(position).get("name").toString());
            holder.textViewTel.setText(data.get(position).get("tel").toString());
            holder.imageViewDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    multiple = 1;
                    status = data.get(position).get("name").toString().split("（")[1].split("）")[0];
                    if (status.equals("学生") || status.equals("儿童") || status.equals("残疾军人"))
                        multiple = (float) 0.5; //优惠计算方式,此处暂定半价
                    textViewTotal.setText("订单总额:￥" + (total -= multiple * price) + "元");
                    list.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class Holder {
            private TextView textViewName;
            private TextView textViewID;
            private TextView textViewTel;
            private ImageView imageViewDel;
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book3);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        textViewSource = (TextView) findViewById(R.id.textViewSource3);
        textViewStartTime = (TextView) findViewById(R.id.textViewStartTime3);
        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime3);
        textViewTrainNum = (TextView) findViewById(R.id.textViewTrainNum3);
        textViewDestination = (TextView) findViewById(R.id.textViewDestination3);
        textViewStopTime = (TextView) findViewById(R.id.textViewStopTime3);
        listViewMyContact = (ListView) findViewById(R.id.listViewMyContact);
        textViewSeat = (TextView) findViewById(R.id.textViewSeat_3);
        textViewSeatPrice = (TextView) findViewById(R.id.textViewSeatPrice_3);
        textViewAdd = (TextView) findViewById(R.id.textViewAdd);
        textViewTotal = (TextView) findViewById(R.id.textViewTotal);
        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        list = new ArrayList<Map<String, Object>>();
        adapter = new DelAdapter(this, list);
        listViewMyContact.setAdapter(adapter);

        final Intent intent = getIntent();
        map = (Map<String, Object>) intent.getSerializableExtra("data");
        position = intent.getIntExtra("position", 0);
        price = Float.parseFloat(intent.getStringExtra("price").replace("￥", ""));

        textViewSource.setText(intent.getStringExtra("interzone").split("-")[0]);
        textViewStartTime.setText(map.get("startTime").toString());
        textViewDateTime.setText(intent.getStringExtra("datetime").split(" ")[0]
                + "(" + map.get("stopTime").toString().split("\\(")[1]);
        textViewTrainNum.setText(map.get("trainNo").toString());
        textViewDestination.setText(intent.getStringExtra("interzone").split("-")[1]);
        textViewStopTime.setText(map.get("stopTime").toString().split("\\(")[0]);
        textViewSeat.setText(map.get("seat"+(position + 1))
                .toString().replace(":","(") + "张)");
        textViewSeatPrice.setText("￥" + 630/Math.pow(2, position));
        textViewTotal.setText("订单总额:￥0.0元");

        textViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Book3Activity.this, AddTravellerActivity.class)
                        .putExtra("data", (Serializable) list), 1);
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(list.size() != 0 && list != null) {
                    startActivityForResult(new Intent(Book3Activity.this, Book4Activity.class)
                            .putExtra("contact", (Serializable) list)
                            .putExtra("train", (Serializable) map)
                            .putExtra("datetime", intent.getStringExtra("datetime"))
                            .putExtra("total",total+"")
                            .putExtra("interzone",getIntent().getStringExtra("interzone")), 2);
                } else {
                    Toast.makeText(Book3Activity.this,"请添加联系人,再提交!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && data != null) {
            List<Map<String, Object>> da = (List<Map<String, Object>>) data.getSerializableExtra("data");
            if (Integer.parseInt(map.get("seat"+(position + 1)).toString().split(":")[1]) < da.size())
                Toast.makeText(this, "余票不足,请重新加入联系人!", Toast.LENGTH_LONG).show();
            else {
                for (int i = 0; i < da.size(); i++) {
                    multiple = 1;
                    status = da.get(i).get("name").toString().split("（")[1].split("）")[0];
                    list.add(da.get(i));
                    if (status.equals("学生") || status.equals("儿童") || status.equals("残疾军人"))
                        multiple = (float) 0.5; //优惠计算方式,此处暂定半价
                    textViewTotal.setText("订单总额:￥" + (total += multiple * price) + "元");
                }
                adapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 2 && resultCode == RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
