package com.neuedu.my12306.stationlist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Gavin on 2015/12/28.
 */
public class StationUtils {
    public static final String DB_NAME = "my12306.db";
    public static final String DB_PATH = "/data/data/com.neuedu.my12306/databases";
    public static ArrayList<Station> stations;

    public static void init(Context context) throws IOException {
        File file = new File(DB_PATH);
        if (!file.exists()){
            file.mkdirs();
        }

        File file1 = new File(DB_PATH +"/"+ DB_NAME);
        if(!file1.exists()){
            //读文件
            InputStream inputStream = context.getAssets().open(DB_NAME);
            //BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            //写文件
            FileOutputStream fileOutputStream = new FileOutputStream(DB_PATH +"/"+ DB_NAME);
            //BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            //读写操作
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1){
                fileOutputStream.write(buffer,0,length);
            }

            inputStream.close();
            //bufferedInputStream.close();
            fileOutputStream.close();
            //bufferedOutputStream.close();
        }
    }

    public static ArrayList<Station> getAllStations(Context context){
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);

        stations = new ArrayList<Station>();
        Cursor cursor = database.rawQuery("select * from station order by sort_order",null);
        while (cursor.moveToNext()) {
            Station station = new Station();
            station.setStation_name(cursor.getString(cursor.getColumnIndex("station_name")));

            String tmp = cursor.getString(cursor.getColumnIndex("sort_order"));
            if(tmp.isEmpty()){
                station.setSort_order("常用");
            }else{
                station.setSort_order(cursor.getString(cursor.getColumnIndex("sort_order")));
            }

            station.setCity(cursor.getString(cursor.getColumnIndex("city")));
            stations.add(station);
        }
        database.close();

        return stations;
    }
}
