package com.neuedu.my12306.book;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.widget.TextView;

import com.neuedu.my12306.R;

public class Book5Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book5);

        ActionBar bar = getActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        TextView textViewBook = (TextView) findViewById(R.id.textViewBook5);
        textViewBook.setText("您的订单" + getIntent().getStringExtra("orderNo") +
        "支付成功,可以凭此二维码办理取票业务,也可以在订单中查看相关信息及二维码。");
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
            default:
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
