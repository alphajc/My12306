package com.neuedu.my12306;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Toast;

import java.lang.reflect.Field;

public class MainActivity extends FragmentActivity {
    ViewPager view = null;
    long startTime = 0;
    OrderFragment orderFragment;
    TicketFragment ticketFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment myFragment = new MyFragment();
        orderFragment = new OrderFragment();
        ticketFragment = new TicketFragment();

        view = (ViewPager) findViewById(R.id.pager);

        class MyTabListener implements TabListener {
            FragmentManager fm;
            private Fragment fragment;

            public MyTabListener(FragmentManager fm, Fragment f) {
                this.fm = fm;
                this.fragment = f;
            }

            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                //fm.beginTransaction().add(R.id.layoutMain,fragment).commit();
                view.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
                //fm.beginTransaction().remove(fragment).commit();
            }

            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {

            }
        }

        class TabFragmentPagerAdapter extends FragmentPagerAdapter {
            public TabFragmentPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return super.getPageTitle(position);
            }

            @Override
            public Fragment getItem(int position) {
                Fragment ft = null;
                switch (position) {
                    case 2:
//                        position = 3;
                        ft = new MyFragment();
                        break;
                    case 0:
                        ft = new TicketFragment();
                        break;
                    case 1:
                        ft = new OrderFragment();
                        break;
//                    case 3:
//                        ft = new MyFragment();
//                        break;
//                    case 4:
//                        position = 0;
//                        ft = new TicketFragment();
//                        break;
                    default:
                        ;
                }

                return ft;
            }

            @Override
            public int getCount() {
                return 3;
            }
        }

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        bar.addTab(bar.newTab().setText("车票").setTabListener(new MyTabListener(fragmentManager, ticketFragment)));
        bar.addTab(bar.newTab().setText("订单").setTabListener(new MyTabListener(fragmentManager, orderFragment)));
        bar.addTab(bar.newTab().setText("@我的").setTabListener(new MyTabListener(fragmentManager, myFragment)));

        view.setAdapter(new TabFragmentPagerAdapter(fragmentManager));
        view.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - startTime > 2000) {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                startTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
