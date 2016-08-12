package com.neuedu.my12306.book;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neuedu.my12306.CommonServer;
import com.neuedu.my12306.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Book2Activity extends Activity {
    String datetime;
    String interzone;
    TextView textViewDateTime;
    TextView textViewInterzone;
    ListView listViewBook;
    TextView textViewTrainNo;
    TextView textViewTime;
    Map<String, Object> map;
    List<Map<String, Object>> list;
    List<Map<String, Object>> list_temp;

    public class SeatAdapter extends BaseAdapter{
        private LayoutInflater m_Inflater;
        private List<Map<String, Object>> data;

        public SeatAdapter(Context context, List<Map<String, Object>> data) {
            m_Inflater = LayoutInflater.from(context);
            this.data = data;
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
            final Holder holder;
            if (convertView == null) {
                holder = new Holder();
                convertView = m_Inflater.inflate(R.layout.item_seatlist,null);
                holder.seatType = (TextView) convertView.findViewById(R.id.textViewSeatType);
                holder.seatNum = (TextView) convertView.findViewById(R.id.textViewSeatNum);
                holder.seatPrice = (TextView) convertView.findViewById(R.id.textViewSeatPrice);
                holder.seatButton = (Button) convertView.findViewById(R.id.buttonSeat);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.seatType.setText(data.get(position).get("seatType").toString());
            holder.seatNum.setText(data.get(position).get("seatNum").toString());
            holder.seatPrice.setText(data.get(position).get("seatPrice").toString());
            if(Integer.parseInt(data.get(position).get("seatNum").toString().split("张")[0]) > 0) {
                holder.seatButton.setBackgroundColor(Color.BLUE);
                holder.seatButton.setTextColor(Color.WHITE);
                holder.seatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(Book2Activity.this, Book3Activity.class)
                                .putExtra("data", (Serializable) map)
                                .putExtra("datetime", datetime)
                                .putExtra("interzone", interzone)
                                .putExtra("position", position)
                                .putExtra("price", data.get(position).get("seatPrice").toString()), 3);
                    }
                });
            } else {
                holder.seatButton.setBackgroundColor(Color.LTGRAY);
                holder.seatButton.setTextColor(Color.BLACK);
            }

            return convertView;
        }

        private class Holder{
            private TextView seatType;
            private TextView seatNum;
            private TextView seatPrice;
            private Button seatButton;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 3 && resultCode ==RESULT_OK){
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book2);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        map = (Map<String, Object>) intent.getSerializableExtra("data");
        datetime = intent.getStringExtra("datetime");
        interzone = intent.getStringExtra("interzone");

        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime_book2);
        textViewInterzone = (TextView) findViewById(R.id.textViewInterzone2);
        textViewTrainNo = (TextView) findViewById(R.id.textViewTrainNo2);
        textViewTime = (TextView) findViewById(R.id.textViewTime2);

        textViewDateTime.setText(datetime);
        textViewInterzone.setText(interzone.split("-")[0] + "->" + interzone.split("-")[1]);
        textViewTrainNo.setText(map.get("trainNo").toString());
        textViewTime.setText(map.get("startTime").toString() + "-" +map.get("stopTime").toString().split("\\(")[0]
            + ",历时" +calculateTime(map.get("startTime").toString(),
                map.get("stopTime").toString().split("\\(")[0],
                map.get("stopTime").toString().split("\\(")[1].split("日")[0]));

        TextView textViewLast = (TextView) findViewById(R.id.textViewLast2);
        TextView textViewNext = (TextView) findViewById(R.id.textViewNext2);

        //计算一天的毫秒数
        final Calendar calendar = Calendar.getInstance();
        calendar.set(1970,1,2);
        long end = calendar.getTimeInMillis();
        calendar.set(1970,1,1);
        long start = calendar.getTimeInMillis();
        final long oneDay = end - start;

        textViewLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //               Toast.makeText(BookActivity.this,oneDay+"", Toast.LENGTH_SHORT).show();
                calendar.set(Integer.parseInt(datetime.split("-")[0]),
                        Integer.parseInt(datetime.split("-")[1]) - 1, Integer.parseInt(datetime.split("-")[2].split(" ")[0]));
                calendar.setTimeInMillis(calendar.getTimeInMillis() - oneDay);
                datetime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                        + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                        DateUtils.formatDateTime(Book2Activity.this, calendar.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_WEEKDAY).replace("星期", "周");
                textViewDateTime.setText(datetime);
                refresh();
            }
        });
        textViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Integer.parseInt(datetime.split("-")[0]),
                        Integer.parseInt(datetime.split("-")[1]) - 1, Integer.parseInt(datetime.split("-")[2].split(" ")[0]));
                calendar.setTimeInMillis(calendar.getTimeInMillis() + oneDay);
                datetime = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1)
                        + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
                        DateUtils.formatDateTime(Book2Activity.this, calendar.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_WEEKDAY).replace("星期", "周");
                textViewDateTime.setText(datetime);
                refresh();
            }
        });

        listViewBook = (ListView) findViewById(R.id.listViewBook2);
        list = new ArrayList<Map<String, Object>>();

        list_temp = new ArrayList<Map<String, Object>>();
        String trainNo = map.get("trainNo").toString();
        for (int i = 0; i < CommonServer.trainInformation.size(); i++){
            if (CommonServer.trainInformation.get(i).get("trainNo").toString().equals(trainNo)){
                list_temp.add(CommonServer.trainInformation.get(i));
            }
        }

        int price = 630;//测试数据,临时价格
        for (int i = 1; i <= 4; i++) {
            if (map.get("seat"+i) != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("seatType", map.get("seat" + i).toString().split(":")[0]);
                data.put("seatNum", map.get("seat" + i).toString().split(":")[1] + "张");
                data.put("seatPrice", "￥" + price);
                list.add(data);
                price /= 2;
            }
        }

        SeatAdapter adapter = new SeatAdapter(this, list);
        listViewBook.setAdapter(adapter);
    }

    private void refresh(){
        list.clear();
        map = new HashMap<String, Object>();

        for (int i = 0; i < list_temp.size(); i++){
            if (datetime.split(" ")[0].equals(list_temp.get(i).get("datetime").toString()))
                map = list_temp.get(i);
        }

        int price = 630;//测试数据,临时价格
        for (int i = 1; i <= 4; i++) {
            if (map.get("seat"+i) != null) {
                Map<String, Object> data = new HashMap<String, Object>();
                data.put("seatType", map.get("seat" + i).toString().split(":")[0]);
                data.put("seatNum", map.get("seat" + i).toString().split(":")[1] + "张");
                data.put("seatPrice", "￥" + price);
                list.add(data);
                price /= 2;
            }
        }

        ((SeatAdapter)listViewBook.getAdapter()).notifyDataSetChanged();
    }

    private String calculateTime(String startTime, String stopTime, String days){
        String time;
        int interval = (Integer.parseInt(stopTime.split(":")[0]) * 60 + Integer.parseInt(days) * 24 * 60
                +Integer.parseInt(stopTime.split(":")[1])) - (Integer.parseInt(startTime.split(":")[0]) * 60
                +Integer.parseInt(startTime.split(":")[1]));

        time = interval / 60 + "小时"+ interval % 60 + "分";

        return time;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
