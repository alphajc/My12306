package com.neuedu.my12306;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.neuedu.my12306.order.OrderFrom;
import com.neuedu.my12306.order.OrderManageActivity;
import com.neuedu.my12306.order.OrderUtils;
import com.neuedu.my12306.order.Seat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment {
    Button buttonNoPayOrder;
    Button buttonAllOrder;
    ListView listViewOrder;
    List<Map<String, Object>> list;

    class OrderAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<Map<String, Object>> data;

        public OrderAdapter(Context context, List<Map<String, Object>> data) {
            this.inflater = LayoutInflater.from(context);
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

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Holder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_order, null);
                holder = new Holder();
                holder.oderTime = (TextView) convertView.findViewById(R.id.textViewOderTime);
                holder.orderImage = (ImageView) convertView.findViewById(R.id.imageViewOrder);
                holder.orderInterZone = (TextView) convertView.findViewById(R.id.textViewOrderInterZone);
                holder.orderNo = (TextView) convertView.findViewById(R.id.textViewOrderNo);
                holder.orderTrainNo = (TextView) convertView.findViewById(R.id.textViewOrderTrainNo);
                holder.payStatus = (TextView) convertView.findViewById(R.id.textViewPayStatus);
                holder.peopleAmount = (TextView) convertView.findViewById(R.id.textViewPeopleAmount);
                holder.totalPrice = (TextView) convertView.findViewById(R.id.textViewTotalPrice);

                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.totalPrice.setText(data.get(position).get("totalPrice").toString());
            holder.peopleAmount.setText(data.get(position).get("peopleAmount").toString());
            holder.orderTrainNo.setText(data.get(position).get("orderTrainNo").toString());
            holder.oderTime.setText(data.get(position).get("oderTime").toString());
            holder.orderInterZone.setText(data.get(position).get("orderInterZone").toString());
            holder.orderNo.setText(data.get(position).get("orderNo").toString());
            holder.payStatus.setText(data.get(position).get("payStatus").toString());
            if (holder.payStatus.getText().toString().equals("已支付"))
                holder.payStatus.setTextColor(Color.parseColor("#00bfff"));
            else if (holder.payStatus.getText().toString().equals("已取消"))
                holder.payStatus.setTextColor(Color.parseColor("#696969"));
            else if (holder.payStatus.getText().toString().equals("未支付"))
                holder.payStatus.setTextColor(Color.parseColor("#fbb622"));
            holder.orderImage.setImageResource((Integer) data.get(position).get("orderImage"));
            holder.orderImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String button;
                    if (buttonAllOrder.getCurrentTextColor() == Color.BLACK)
                        button = "noPay";
                    else button = "all";
                    if (holder.payStatus.getText().toString().equals("已支付")) {
                        startActivityForResult(new Intent(getActivity(), OrderManageActivity.class)
                                .putExtra("orderNo", data.get(position).get("orderNo").toString())
                                .putExtra("button", button), 0);
                    } else if (holder.payStatus.getText().toString().equals("未支付")) {
                        startActivityForResult(new Intent(getActivity(), OrderManageActivity.class)
                                .putExtra("orderNo", data.get(position).get("orderNo").toString())
                                .putExtra("button", button), 0);
                    }
                }
            });

            return convertView;
        }

        class Holder {
            private TextView orderNo;
            private TextView orderTrainNo;
            private TextView oderTime;
            private TextView orderInterZone;
            private TextView peopleAmount;
            private TextView payStatus;
            private TextView totalPrice;
            private ImageView orderImage;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        buttonAllOrder = (Button) getActivity().findViewById(R.id.buttonAllOrder);
        buttonNoPayOrder = (Button) getActivity().findViewById(R.id.buttonNoPayOrder);
        listViewOrder = (ListView) getActivity().findViewById(R.id.listViewOrder);

        //初始状态
        buttonNoPayOrder.setBackgroundColor(Color.parseColor("#73b6f5"));
        buttonNoPayOrder.setTextColor(Color.WHITE);

        list = new ArrayList<Map<String, Object>>();
        readOrder(false);//false代表只有待支付订单
/*        for (int i = 0; i < CommonServer.no_pay_order.size(); i++) {
            List<Map<String, Object>> temp_list = (List<Map<String, Object>>) CommonServer.no_pay_order.get(i).get("contact");
            Map<String, Object> order_map = new ArrayMap<String, Object>();
            order_map.put("orderNo", CommonServer.no_pay_order.get(i).get("orderNo"));
            order_map.put("orderTrainNo", temp_list.get(0).get("trainNo"));
            order_map.put("oderTime", temp_list.get(0).get("datetime"));
            order_map.put("orderInterZone", ((String) CommonServer.no_pay_order.get(i).get("interZone")).replace("-", "->"));
            order_map.put("peopleAmount", temp_list.size() + "人");
            order_map.put("payStatus", "未支付");
            order_map.put("totalPrice", CommonServer.no_pay_order.get(i).get("total"));
            order_map.put("orderImage", R.drawable.forward_25);

            list.add(order_map);
        }
*/
//		final SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, R.layout.item_order, new String[]{
//				"orderNo", "orderTrainNo", "oderTime", "orderInterZone", "peopleAmount", "payStatus",
//				"totalPrice", "orderImage"},new int[]{R.id.textViewOrderNo, R.id.textViewOrderTrainNo,
//				R.id.textViewOderTime, R.id.textViewOrderInterZone, R.id.textViewPeopleAmount,
//				R.id.textViewPayStatus, R.id.textViewTotalPrice, R.id.imageViewOrder});

        final OrderAdapter adapter = new OrderAdapter(getActivity(), list);
        listViewOrder.setAdapter(adapter);

        buttonNoPayOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_no_pay();
            }
        });

        buttonAllOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh_all();
            }
        });
    }

    private void readOrder(boolean flag){
        List<Map<String, Object>> data = OrderUtils.getAlldata(getActivity());
        int count = 0;
        for (int i = 0; i < data.size(); i++) {
            if (((OrderFrom)data.get(i).get("order")).getPay_status().equals("未支付")) {
                Map<String, Object> order_map = new HashMap<String, Object>();
                order_map.put("orderNo", ((OrderFrom)data.get(i).get("order")).getOrder_number());
                order_map.put("orderTrainNo", ((OrderFrom)data.get(i).get("order")).getTrain_number());
                order_map.put("oderTime", ((OrderFrom)data.get(i).get("order")).getDatetime());
                order_map.put("orderInterZone", (((OrderFrom)data.get(i).get("order")).getInter_zone()).replace("-", "->"));
                order_map.put("peopleAmount", ((ArrayList<Seat>)data.get(i).get("seat")).size() + "人");
                order_map.put("payStatus", "未支付");
                order_map.put("totalPrice", ((OrderFrom)data.get(i).get("order")).getTotal_price());
                order_map.put("orderImage", R.drawable.forward_25);
                list.add(0,order_map);
                count++;
            } else if (flag && ((OrderFrom)data.get(i).get("order")).getPay_status().equals("已支付")){
                Map<String, Object> order_map = new HashMap<String, Object>();
                order_map.put("orderNo", ((OrderFrom)data.get(i).get("order")).getOrder_number());
                order_map.put("orderTrainNo", ((OrderFrom)data.get(i).get("order")).getTrain_number());
                order_map.put("oderTime", ((OrderFrom)data.get(i).get("order")).getDatetime());
                order_map.put("orderInterZone", (((OrderFrom)data.get(i).get("order")).getInter_zone()).replace("-", "->"));
                order_map.put("peopleAmount", ((ArrayList<Seat>)data.get(i).get("seat")).size() + "人");
                order_map.put("payStatus", "已支付");
                order_map.put("totalPrice", ((OrderFrom)data.get(i).get("order")).getTotal_price());
                order_map.put("orderImage", R.drawable.forward_25);
                list.add(count,order_map);
            } else if (flag && ((OrderFrom)data.get(i).get("order")).getPay_status().equals("已取消")){
                Map<String, Object> order_map = new HashMap<String, Object>();
                order_map.put("orderNo", ((OrderFrom)data.get(i).get("order")).getOrder_number());
                order_map.put("orderTrainNo", ((OrderFrom)data.get(i).get("order")).getTrain_number());
                order_map.put("oderTime", ((OrderFrom)data.get(i).get("order")).getDatetime());
                order_map.put("orderInterZone", (((OrderFrom)data.get(i).get("order")).getInter_zone()).replace("-", "->"));
                order_map.put("peopleAmount", ((ArrayList<Seat>)data.get(i).get("seat")).size() + "人");
                order_map.put("payStatus", "已取消");
                order_map.put("totalPrice", ((OrderFrom)data.get(i).get("order")).getTotal_price());
                order_map.put("orderImage", R.drawable.flg_null);
                list.add(list.size(),order_map);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void refresh_all() {
        buttonNoPayOrder.setBackgroundColor(Color.parseColor("#c1d7e1"));
        buttonNoPayOrder.setTextColor(Color.BLACK);
        buttonAllOrder.setBackgroundColor(Color.parseColor("#73b6f5"));
        buttonAllOrder.setTextColor(Color.WHITE);
        list.clear();

        readOrder(true);
        /*for (int i = 0; i < CommonServer.no_pay_order.size(); i++) {
            List<Map<String, Object>> temp_list = (List<Map<String, Object>>) CommonServer.no_pay_order.get(i).get("contact");
            Map<String, Object> order_map = new ArrayMap<String, Object>();
            order_map.put("orderNo", "订单:" + CommonServer.no_pay_order.get(i).get("orderNo"));
            order_map.put("orderTrainNo", temp_list.get(0).get("trainNo"));
            order_map.put("oderTime", temp_list.get(0).get("datetime"));
            order_map.put("orderInterZone", ((String) CommonServer.no_pay_order.get(i).get("interZone")).replace("-", "->"));
            order_map.put("peopleAmount", temp_list.size() + "人");
            order_map.put("payStatus", "未支付");
            order_map.put("totalPrice", CommonServer.no_pay_order.get(i).get("total"));
            order_map.put("orderImage", R.drawable.forward_25);

            list.add(order_map);
        }
        for (int i = 0; i < CommonServer.pay_order.size(); i++) {
            List<Map<String, Object>> temp_list = (List<Map<String, Object>>) CommonServer.pay_order.get(i).get("contact");
            Map<String, Object> order_map = new ArrayMap<String, Object>();
            order_map.put("orderNo", "订单:" + CommonServer.pay_order.get(i).get("orderNo"));
            order_map.put("orderTrainNo", temp_list.get(0).get("trainNo"));
            order_map.put("oderTime", temp_list.get(0).get("datetime"));
            order_map.put("orderInterZone", ((String) CommonServer.pay_order.get(i).get("interZone")).replace("-", "->"));
            order_map.put("peopleAmount", temp_list.size() + "人");
            order_map.put("payStatus", "已支付");
            order_map.put("totalPrice", CommonServer.pay_order.get(i).get("total"));
            order_map.put("orderImage", R.drawable.forward_25);

            list.add(order_map);
        }
        for (int i = 0; i < CommonServer.cancel_order.size(); i++) {
            List<Map<String, Object>> temp_list = (List<Map<String, Object>>) CommonServer.cancel_order.get(i).get("contact");
            Map<String, Object> order_map = new ArrayMap<String, Object>();
            order_map.put("orderNo", "订单:" + CommonServer.cancel_order.get(i).get("orderNo"));
            order_map.put("orderTrainNo", temp_list.get(0).get("trainNo"));
            order_map.put("oderTime", temp_list.get(0).get("datetime"));
            order_map.put("orderInterZone", ((String) CommonServer.cancel_order.get(i).get("interZone")).replace("-", "->"));
            order_map.put("peopleAmount", temp_list.size() + "人");
            order_map.put("payStatus", "已取消");
            order_map.put("totalPrice", CommonServer.cancel_order.get(i).get("total"));
            order_map.put("orderImage", R.drawable.flg_null);

            list.add(order_map);
        }
*/

        ((OrderAdapter) listViewOrder.getAdapter()).notifyDataSetChanged();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void refresh_no_pay(){
        //字体及背景变化
        buttonAllOrder.setBackgroundColor(Color.parseColor("#c1d7e1"));
        buttonAllOrder.setTextColor(Color.BLACK);
        buttonNoPayOrder.setBackgroundColor(Color.parseColor("#73b6f5"));
        buttonNoPayOrder.setTextColor(Color.WHITE);
        list.clear();

        readOrder(false);
/*
        for (int i = 0; i < CommonServer.no_pay_order.size(); i++) {
            List<Map<String, Object>> temp_list = (List<Map<String, Object>>) CommonServer.no_pay_order.get(i).get("contact");
            Map<String, Object> order_map = new ArrayMap<String, Object>();
            order_map.put("orderNo", CommonServer.no_pay_order.get(i).get("orderNo"));
            order_map.put("orderTrainNo", temp_list.get(0).get("trainNo"));
            order_map.put("oderTime", temp_list.get(0).get("datetime"));
            order_map.put("orderInterZone", ((String) CommonServer.no_pay_order.get(i).get("interZone")).replace("-", "->"));
            order_map.put("peopleAmount", temp_list.size() + "人");
            order_map.put("payStatus", "未支付");
            order_map.put("totalPrice", CommonServer.no_pay_order.get(i).get("total"));
            order_map.put("orderImage", R.drawable.forward_25);

            list.add(order_map);
        }
*/

        ((OrderAdapter) listViewOrder.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("tag",requestCode + "" + resultCode);
        if (requestCode == 0) {
            if (resultCode == 2)
                refresh_all();
            else if (resultCode == 5){
                refresh_no_pay();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
