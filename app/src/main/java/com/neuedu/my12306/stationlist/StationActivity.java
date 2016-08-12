package com.neuedu.my12306.stationlist;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.neuedu.my12306.R;

public class StationActivity extends Activity {
    public static final int RESULT_STATION = 0;

    private ListView lvStationList = null;
    private StationListAdapter adapter = null;
    private List<Station> stations = null;
    private LetterIndexView letterIndexView = null;
    private HashMap<String, Integer> letterMap = new HashMap<String, Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);

        lvStationList = (ListView) findViewById(R.id.listViewStationList);
        letterIndexView = (LetterIndexView) findViewById(R.id.letterIndexView);

        stations = StationUtils.getAllStations(this);
        adapter = new StationListAdapter(this, stations);
        lvStationList.setAdapter(adapter);

        lvStationList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = getIntent();
                intent.putExtra("station_name", stations.get(arg2).getStation_name());
                setResult(RESULT_STATION, intent);
                finish();
            }
        });

        letterIndexView.setOnTouchingLetterChangedListener(new LetterIndexView.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String choose) {
            //    Toast.makeText(StationActivity.this,choose,Toast.LENGTH_LONG);
                if(letterMap.get(choose) != null)
                    lvStationList.setSelection(letterMap.get(choose));
            }
        });
    }

    public class StationListAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private List<Station> list;

        public StationListAdapter(Context ctx, List<Station> list){
            this.inflater = LayoutInflater.from(ctx);
            this.list = list;
            for (int i=0; i < list.size(); i++){
                String current = list.get(i).getSort_order();
                String preview = (i - 1) >= 0 ? list.get(i-1).getSort_order() : "";

                if(!preview.equals(current)){
                    if (current == "常用")
                        current = "#";
                    letterMap.put(current, i);
                }
            }
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder;

            if(convertView == null){
                convertView = inflater.inflate(R.layout.item_station_list, null);
                holder = new ViewHolder();
                holder.letter = (TextView) convertView.findViewById(R.id.letter);
                holder.station_name = (TextView) convertView.findViewById(R.id.station_name);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.station_name.setText(list.get(position).getStation_name());

            String current = list.get(position).getSort_order();
            String preview = (position - 1) >= 0 ? list.get(position-1).getSort_order() : "";

            if(!preview.equals(current)){
                holder.letter.setVisibility(View.VISIBLE);
                holder.letter.setText(current);
             //   letterMap.put(current,position);
            }else{
                holder.letter.setVisibility(View.GONE);
            }

            return convertView;
        }

        private class ViewHolder{
            TextView letter;
            TextView station_name;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.station, menu);
        return true;
    }

}
