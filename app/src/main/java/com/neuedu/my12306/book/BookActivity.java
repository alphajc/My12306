package com.neuedu.my12306.book;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class BookActivity extends Activity {
    ListView listViewBook;
    String datetime;
    String interzone;
    TextView textViewDateTime;
    TextView textViewInterzone;
    List<Map<String,Object>> data;
    Map<String,Object> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        datetime = intent.getStringExtra("datetime");
        interzone = intent.getStringExtra("interzone");

        textViewDateTime = (TextView) findViewById(R.id.textViewDateTime_book);
        textViewInterzone = (TextView) findViewById(R.id.textViewInterzone);
        textViewDateTime.setText(datetime);
        textViewInterzone.setText(interzone.split("-")[0] + "->" + interzone.split("-")[1]);

        TextView textViewLast = (TextView) findViewById(R.id.textViewLast);
        TextView textViewNext = (TextView) findViewById(R.id.textViewNext);

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
                        DateUtils.formatDateTime(BookActivity.this, calendar.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_WEEKDAY).replace("星期", "周");
                textViewDateTime.setText(datetime);
                ticketAmount();
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
                        DateUtils.formatDateTime(BookActivity.this, calendar.getTimeInMillis(),
                                DateUtils.FORMAT_SHOW_WEEKDAY).replace("星期", "周");
                textViewDateTime.setText(datetime);
                ticketAmount();
            }
        });

        listViewBook = (ListView) findViewById(R.id.listViewBook);
        data= new ArrayList<Map<String,Object>>();

        for (int i = 0; i <CommonServer.trainInformation.size(); i++) {
            String trainDatetime = CommonServer.trainInformation.get(i).get("datetime").toString();
            if (datetime.split(" ")[0].equals(trainDatetime)) {
                map = CommonServer.trainInformation.get(i);
                data.add(map);
            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.item_book,
                new String[]{"trainNo","source","destination","startTime","stopTime",
                        "seat1", "seat2", "seat3", "seat4"}, new int[]{R.id.textViewTrainNo,
         R.id.ivSource, R.id.ivDestination, R.id.textViewStartTime, R.id.textViewStopTime,
         R.id.textViewSeat1, R.id.textViewSeat2, R.id.textViewSeat3, R.id.textViewSeat4});

        listViewBook.setAdapter(adapter);
        listViewBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivityForResult(new Intent(BookActivity.this,Book2Activity.class)
                .putExtra("data", (Serializable)data.get(position))
                .putExtra("datetime",datetime)
                .putExtra("interzone",interzone),4);
            }
        });
    }

    private void ticketAmount(){
        data.clear();

        for (int i = 0; i <CommonServer.trainInformation.size(); i++) {
            String trainDatetime = CommonServer.trainInformation.get(i).get("datetime").toString();
            if (datetime.split(" ")[0].equals(trainDatetime)) {
                map = CommonServer.trainInformation.get(i);
                data.add(map);
            }
        }

        ((SimpleAdapter)listViewBook.getAdapter()).notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 4 && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(1000);
                finish();
                break;
            default:
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
