package com.neuedu.my12306.contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Gavin on 2016/1/8.
 */
public class ContactUtils {
    public static final String DB_NAME = "my12306.db";
    public static final String DB_PATH = "/data/data/com.neuedu.my12306/databases";
    public static ArrayList<Contact> contacts;

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

    public static ArrayList<Contact> getAllContacts(Context context){
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS CONTACT (" +
                "CARD_NUMBER CHAR(18) PRIMARY KEY UNIQUE NOT NULL," +
                "NAME VARCHAR(16) NOT NULL," +
                "TRAVELLER_TYPE VARCHAR(9) NOT NULL," +
                "CARD_TYPE VARCHAR(16) NOT NULL," +
                "PHONE CHAR(11) NOT NULL" +
                ")");

        contacts = new ArrayList<Contact>();
        Cursor cursor = database.rawQuery("SELECT * FROM CONTACT",null);
        while (cursor.moveToNext()) {
            Contact contact = new Contact();
            contact.setCard_number(cursor.getString(cursor.getColumnIndex("CARD_NUMBER")));
            contact.setName(cursor.getString(cursor.getColumnIndex("NAME")));
            contact.setTraveller_type(cursor.getString(cursor.getColumnIndex("TRAVELLER_TYPE")));
            contact.setCard_type(cursor.getString(cursor.getColumnIndex("CARD_TYPE")));
            contact.setPhone(cursor.getString(cursor.getColumnIndex("PHONE")));
            contacts.add(contact);
        }
        database.close();

        return contacts;
    }

    public static void updateContact(Context context, String card_number, String name, String traveller_type, String phone){
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);
        database.execSQL("UPDATE CONTACT SET NAME = '" + name +
                "',TRAVELLER_TYPE = '" + traveller_type +
                "',PHONE = '" + phone + "' WHERE CARD_NUMBER = '" + card_number + "'");
        database.close();
    }

    public static void insertContact(Context context, Contact contact){
        try {
            init(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);
        database.execSQL("CREATE TABLE IF NOT EXISTS CONTACT (" +
                "CARD_NUMBER CHAR(18) PRIMARY KEY UNIQUE NOT NULL," +
                "NAME VARCHAR(16) NOT NULL," +
                "TRAVELLER_TYPE VARCHAR(9) NOT NULL," +
                "CARD_TYPE VARCHAR(16) NOT NULL," +
                "PHONE CHAR(11) NOT NULL" +
                ")");
        database.execSQL("INSERT INTO CONTACT VALUES('" + contact.getCard_number() +
                "','" + contact.getName() +
                "','" + contact.getTraveller_type() +
                "','" + contact.getCard_type() +
                "','" + contact.getPhone() +
                "')");
        database.close();
    }

    public static void removeContact(String card_number){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH +"/"+ DB_NAME,null);

        database.execSQL("DELETE FROM CONTACT WHERE CARD_NUMBER = '"+ card_number +"'");
        database.close();
    }

}
