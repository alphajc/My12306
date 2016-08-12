package com.neuedu.my12306.order;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gavin on 2016/1/8.
 */
public class OrderUtils {
    public static final String DB_NAME = "my12306.db";
    public static final String DB_PATH = "/data/data/com.neuedu.my12306/databases";
    public static ArrayList<Map<String, Object>> data;

    public static void init(Context context) throws IOException {
        File file = new File(DB_PATH);
        if (!file.exists()){
            file.mkdirs();
        }

        File file1 = new File(DB_PATH +"/"+ DB_NAME);
        if(!file1.exists()){
            //读文件
            InputStream inputStream = context.getAssets().open(DB_NAME);

            //写文件
            FileOutputStream fileOutputStream = new FileOutputStream(DB_PATH +"/"+ DB_NAME);

            //读写操作
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,length);
            }

            inputStream.close();
            fileOutputStream.close();
        }
    }

    public static ArrayList<Map<String, Object>> getAlldata(Context context){
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH + "/" + DB_NAME, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS ORDER_TABLE (" +
                "ORDER_NUMBER CHAR(10) PRIMARY KEY UNIQUE NOT NULL," +
                "TRAIN_NUMBER CHAR(5) NOT NULL," +
                "DATE_TIME CHAR(10) NOT NULL," +
                "INTER_ZONE VARCHAR(30) NOT NULL," +
                "PAY_STATUS VARCHAR(10) NOT NULL," +
                "TOTAL_PRICE VARCHAR(10) NOT NULL" +
                ")");
        database.execSQL("CREATE TABLE IF NOT EXISTS SEAT_TABLE (" +
                "[SERIAL_NUMBER] CHAR(10) PRIMARY KEY UNIQUE NOT NULL," +
                "[NAME] VARCHAR(16) NOT NULL," +
                "[SEAT_NUMBER] VARCHAR(10) NOT NULL," +
                "[ORDER_NUMBER] CHAR(10) NOT NULL REFERENCES ORDER_TABLE (ORDER_NUMBER) On Delete CASCADE On Update CASCADE" +
                ")");

        data = new ArrayList<Map<String, Object>>();
        Cursor orderFormCursor = database.rawQuery("SELECT * FROM ORDER_TABLE", null);
        Map<String, Object> map;
        Cursor seatCursor = database.rawQuery("SELECT * FROM SEAT_TABLE", null);
        ArrayList<Seat> seats = new ArrayList<Seat>();
        while (orderFormCursor.moveToNext()) {
            map = new HashMap<String, Object>();
            OrderFrom orderFrom = new OrderFrom();
            orderFrom.setOrder_number(orderFormCursor.getString(orderFormCursor.getColumnIndex("ORDER_NUMBER")));
            orderFrom.setDatetime(orderFormCursor.getString(orderFormCursor.getColumnIndex("DATE_TIME")));
            orderFrom.setInter_zone(orderFormCursor.getString(orderFormCursor.getColumnIndex("INTER_ZONE")));
            orderFrom.setPay_status(orderFormCursor.getString(orderFormCursor.getColumnIndex("PAY_STATUS")));
            orderFrom.setTotal_price(orderFormCursor.getString(orderFormCursor.getColumnIndex("TOTAL_PRICE")));
            orderFrom.setTrain_number(orderFormCursor.getString(orderFormCursor.getColumnIndex("TRAIN_NUMBER")));
            map.put("order", orderFrom);
            map.put("seat", new ArrayList<Seat>());
            data.add(map);
        }
        while (seatCursor.moveToNext()){
            Seat seat = new Seat();
            seat.setName(seatCursor.getString(seatCursor.getColumnIndex("NAME")));
            seat.setOrder_number(seatCursor.getString(seatCursor.getColumnIndex("ORDER_NUMBER")));
            seat.setSeat_number(seatCursor.getString(seatCursor.getColumnIndex("SEAT_NUMBER")));
            seat.setSerial_number(seatCursor.getString(seatCursor.getColumnIndex("SERIAL_NUMBER")));
            for (int i = 0; i < data.size(); i++){
                if (((OrderFrom)data.get(i).get("order")).getOrder_number().equals(seat.getOrder_number()))
                    ((ArrayList<Seat>)data.get(i).get("seat")).add(seat);
            }
        }
        database.close();

        return data;
    }

    public static void updatePayStatus(Context context, String order_number, String pay_status){
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);

        database.execSQL("UPDATE ORDER_TABLE SET PAY_STATUS = '" +
                pay_status +"' WHERE ORDER_NUMBER = '" + order_number + "'");
        database.close();

    }

    public static void insertOrder(Context context, OrderFrom orderFrom, ArrayList<Seat> seats){
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS ORDER_TABLE (" +
                "ORDER_NUMBER CHAR(10) PRIMARY KEY UNIQUE NOT NULL," +
                "TRAIN_NUMBER CHAR(5) NOT NULL," +
                "DATE_TIME CHAR(10) NOT NULL," +
                "INTER_ZONE VARCHAR(30) NOT NULL," +
                "PAY_STATUS VARCHAR(10) NOT NULL," +
                "TOTAL_PRICE VARCHAR(10) NOT NULL" +
                ")");
        database.execSQL("CREATE TABLE IF NOT EXISTS SEAT_TABLE (" +
                "[SERIAL_NUMBER] CHAR(10) PRIMARY KEY UNIQUE NOT NULL," +
                "[NAME] VARCHAR(16) NOT NULL," +
                "[SEAT_NUMBER] VARCHAR(10) NOT NULL," +
                "[ORDER_NUMBER] CHAR(10) NOT NULL REFERENCES ORDER_TABLE (ORDER_NUMBER) On Delete CASCADE On Update CASCADE" +
                ")");
        database.execSQL("INSERT INTO ORDER_TABLE VALUES('" + orderFrom.getOrder_number() +
                "','" + orderFrom.getTrain_number() +
                "','" + orderFrom.getDatetime() +
                "','" + orderFrom.getInter_zone() +
                "','" + orderFrom.getPay_status() +
                "','" + orderFrom.getTotal_price() +
                "')");

        for (int i = 0; i < seats.size(); i++){
            database.execSQL("INSERT INTO SEAT_TABLE VALUES('" + seats.get(i).getSerial_number() +
                    "','" + seats.get(i).getName() +
                    "','" + seats.get(i).getSeat_number() +
                    "','" + seats.get(i).getOrder_number() +
                    "')");
        }
        database.close();
    }

    public static void removeContact(String serial_number){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);

        database.execSQL("DELETE FROM SEAT_TABLE WHERE SERIAL_NUMBER = '" + serial_number + "'");
    }

    public static void removeOrder(String order_number){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);

        database.execSQL("DELETE FROM ORDER_TABLE WHERE ORDER_NUMBER = '" + order_number + "'");
        database.close();
    }
}
