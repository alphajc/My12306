package com.neuedu.my12306.order;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.neuedu.my12306.CommonServer;
import com.neuedu.my12306.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderManageActivity extends Activity {
    boolean flag;
    ListView listViewOrderManager;
    TextView textViewOrderManager;
    List<Map<String, Object>> list;
    List<Map<String, Object>> data;
    Map<String, Object> map;
    ArrayList<Seat> contact_list;
    OrderFrom orderFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String orderNo = getIntent().getStringExtra("orderNo");
        data = OrderUtils.getAlldata(this);
        map = new HashMap<String, Object>();;
        for (int i = 0; i < data.size(); i++){
            if (((OrderFrom)data.get(i).get("order")).getOrder_number().equals(orderNo))
                map = data.get(i);
        }
        if (((OrderFrom)map.get("order")).getPay_status().equals("未支付"))
            flag = false;
        else
            flag = true;
        if (flag)
            setContentView(R.layout.activity_order_manage2);
        else
            setContentView(R.layout.activity_order_manage);

        listViewOrderManager = (ListView) findViewById(R.id.listViewOrderManager);
        textViewOrderManager = (TextView) findViewById(R.id.textViewOrderManager);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        textViewOrderManager.setText("订单提交成功,您的订单编号为: " + orderNo);
        list = new ArrayList<Map<String, Object>>();
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.item_book4,
                new String[]{"name", "trainNo", "datetime", "seat", "image"},
                new int[]{R.id.textViewName4, R.id.textViewTrainNo4, R.id.textViewDateTime4, R.id.textViewSeat_4, R.id.imageViewChange});
        listViewOrderManager.setAdapter(adapter);
        readOrder();

        if (flag){
            Button buttonLook = (Button) findViewById(R.id.buttonLook);
            listViewOrderManager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                    new AlertDialog.Builder(OrderManageActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setTitle("请选择操作")
                            .setItems(new String[]{"退票", "改签"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.e("OMA","click"+which);
                                    switch (which){
                                        case 0://退票
                                            if (list.size() == 1) {
                                                OrderUtils.removeOrder(orderNo);
                                                if (getIntent().getStringExtra("button").equals("all"))
                                                    setResult(2);
                                                else if (getIntent().getStringExtra("button").equals("noPay"))
                                                    setResult(5);
                                                finish();
                                            } else
                                                OrderUtils.removeContact(contact_list.get(position).getSerial_number());
                                            list.remove(position);
                                            ((SimpleAdapter)listViewOrderManager.getAdapter()).notifyDataSetChanged();
                                            break;
                                        case 1://改签
                                            break;
                                        default:
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .show();
                }
            });
            buttonLook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = new ImageView(OrderManageActivity.this);
                    imageView.setImageResource(R.drawable.qr);
                    new AlertDialog.Builder(OrderManageActivity.this)
                            .setTitle("我的二维码")
                            .setView(imageView)
                            .setPositiveButton("确定", null).show();
                }
            });
        }
        else {
            Button buttonCancel = (Button) findViewById(R.id.buttonCancel);
            Button buttonCertain = (Button) findViewById(R.id.buttonCertain);
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CommonServer.cancel_order.add(CommonServer.no_pay_order.get(getIntent().getIntExtra("position", 0)));
//                    CommonServer.no_pay_order.remove(getIntent().getIntExtra("position", 0));
                    OrderUtils.updatePayStatus(OrderManageActivity.this, orderNo, "已取消");
                    if (getIntent().getStringExtra("button").equals("all"))
                        setResult(2);
                    else if (getIntent().getStringExtra("button").equals("noPay"))
                        setResult(5);
                    finish();
                }
            });
            buttonCertain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CommonServer.pay_order.add(CommonServer.no_pay_order.get(getIntent().getIntExtra("position", 0)));
//                    CommonServer.no_pay_order.remove(getIntent().getIntExtra("position", 0));
                    OrderUtils.updatePayStatus(OrderManageActivity.this, orderNo, "已支付");
                    if (getIntent().getStringExtra("button").equals("all"))
                        setResult(2);
                    else if (getIntent().getStringExtra("button").equals("noPay"))
                        setResult(5);
                    finish();
                }
            });
        }
    }

    private void readOrder(){
        contact_list = (ArrayList<Seat>)map.get("seat");
        orderFrom = (OrderFrom)map.get("order");
        Map<String, Object> map_temp;
        for (int i = 0; i < contact_list.size(); i++) {
            map_temp = new HashMap<String, Object>();
            map_temp.put("name", contact_list.get(i).getName());
            map_temp.put("trainNo", orderFrom.getTrain_number());
            map_temp.put("datetime", orderFrom.getDatetime());
            map_temp.put("seat", contact_list.get(i).getSeat_number());
            if (flag)
                map_temp.put("image", R.drawable.forward_25);
            else
                map_temp.put("image", null);
            list.add(map_temp);
        }
        ((SimpleAdapter)listViewOrderManager.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        // TODO Auto-generated method stub
        switch(item.getItemId()){
            case android.R.id.home:
                if (getIntent().getStringExtra("button").equals("all"))
                    setResult(2);
                else if (getIntent().getStringExtra("button").equals("noPay"))
                    setResult(5);
                finish();
                break;
            default:
        }

        return super.onMenuItemSelected(featureId, item);
    }
}
