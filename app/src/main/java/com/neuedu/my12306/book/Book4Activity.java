package com.neuedu.my12306.book;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.neuedu.my12306.CommonServer;
import com.neuedu.my12306.R;
import com.neuedu.my12306.order.OrderFrom;
import com.neuedu.my12306.order.OrderUtils;
import com.neuedu.my12306.order.Seat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Book4Activity extends Activity {
    ListView listViewBook;
    Button buttonPay;
    Button buttonNoPay;
    TextView textViewBook;
    Random random;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book4);

        listViewBook = (ListView) findViewById(R.id.listViewBook4);
        buttonPay = (Button) findViewById(R.id.buttonPay);
        buttonNoPay = (Button) findViewById(R.id.buttonNoPay);
        textViewBook = (TextView) findViewById(R.id.textViewBook4);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        List<Map<String, Object>> contact_list = (List<Map<String, Object>>) getIntent().getSerializableExtra("contact");
        Map<String, Object> train_map = (Map<String, Object>) getIntent().getSerializableExtra("train");
        /*得座位*/
        random = new Random();
        int railway = Math.abs(random.nextInt() % 10) + 1;
        int seatNoDecade = Math.abs(random.nextInt() % 10);
        int seatNoUnit = Math.abs(random.nextInt() % 10) + 1;
        /*得订单号*/
        final String orderNumber;
        orderNumber = "E"+ Math.abs(random.nextLong() % 1000000000);

        textViewBook.setText("订单提交成功,您的订单编号为: " + orderNumber);
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < contact_list.size(); i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("name", contact_list.get(i).get("name").toString().split("（")[0]);
            map.put("trainNo", train_map.get("trainNo").toString());
            map.put("datetime", getIntent().getStringExtra("datetime").split(" ")[0]);
            map.put("seat", railway + "车" + ( seatNoDecade * 10 +seatNoUnit + i ) + "号");
            list.add(map);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_book4,
                new String[]{"name", "trainNo", "datetime", "seat"},
                new int[]{R.id.textViewName4, R.id.textViewTrainNo4, R.id.textViewDateTime4, R.id.textViewSeat_4});
        listViewBook.setAdapter(adapter);

        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFrom orderFrom = new OrderFrom();
                orderFrom.setOrder_number(orderNumber);
                orderFrom.setDatetime(list.get(0).get("datetime").toString());
                orderFrom.setInter_zone(getIntent().getStringExtra("interzone"));
                orderFrom.setPay_status("已支付");
                orderFrom.setTotal_price(getIntent().getStringExtra("total"));
                orderFrom.setTrain_number(list.get(0).get("trainNo").toString());
                ArrayList<Seat> seats = new ArrayList<Seat>();
                Seat seat;
                long rand = Math.abs(random.nextLong() % 1000000000);
                for (int i = 0; i < list.size(); i++){
                    seat = new Seat();
                    seat.setSerial_number("S"+ rand + i);
                    seat.setName(list.get(i).get("name").toString());
                    seat.setSeat_number(list.get(i).get("seat").toString());
                    seat.setOrder_number(orderNumber);
                    seats.add(seat);
                }
                OrderUtils.insertOrder(Book4Activity.this, orderFrom, seats);
                startActivityForResult(new Intent(Book4Activity.this, Book5Activity.class)
                        .putExtra("orderNo", orderNumber), 9);
            }
        });

        buttonNoPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFrom orderFrom = new OrderFrom();
                orderFrom.setOrder_number(orderNumber);
                orderFrom.setDatetime(list.get(0).get("datetime").toString());
                orderFrom.setInter_zone(getIntent().getStringExtra("interzone"));
                orderFrom.setPay_status("未支付");
                orderFrom.setTotal_price(getIntent().getStringExtra("total"));
                orderFrom.setTrain_number(list.get(0).get("trainNo").toString());
                ArrayList<Seat> seats = new ArrayList<Seat>();
                Seat seat;
                long rand = Math.abs(random.nextLong() % 1000000000);
                for (int i = 0; i < list.size(); i++){
                    seat = new Seat();
                    seat.setSerial_number("S"+ rand + i);
                    seat.setName(list.get(i).get("name").toString());
                    seat.setSeat_number(list.get(i).get("seat").toString());
                    seat.setOrder_number(orderNumber);
                    seats.add(seat);
                }
                OrderUtils.insertOrder(Book4Activity.this, orderFrom, seats);
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
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
