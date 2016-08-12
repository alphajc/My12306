package com.neuedu.my12306;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.neuedu.my12306.book.BookActivity;
import com.neuedu.my12306.stationlist.StationActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TicketFragment extends Fragment {
	ListView listViewHistory = null;
	Button buttonQuery = null;
	TextView textViewSource;
	TextView textViewDestination;
	HistoryAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		return inflater.inflate(R.layout.fragment_ticket, container, false);
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		int count = 0;
		textViewSource = (TextView) getActivity().findViewById(R.id.textViewSource);
		textViewDestination = (TextView) getActivity().findViewById(R.id.textViewDestination);
		ImageView imageViewSource = (ImageView) getActivity().findViewById(R.id.imageViewSource);
		ImageView imageViewDestination = (ImageView) getActivity().findViewById(R.id.imageViewDestination);
		ImageView imageViewSwitch = (ImageView) getActivity().findViewById(R.id.imageViewSwitch);
		buttonQuery = (Button) getActivity().findViewById(R.id.buttonQuery);
		listViewHistory = (ListView) getActivity().findViewById(R.id.listViewHistory);
		final TextView textViewDateTime = (TextView) getActivity().findViewById(R.id.textViewDateTime);

		final List<String> list = new ArrayList<String>();
		SharedPreferences settings = getActivity().getSharedPreferences("history", 0);
		try {
			count = Integer.parseInt(settings.getString("count",""));
		} catch (NumberFormatException e) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("count","0");
			editor.commit();
		}

		for (int i = count; i > 0; i--){
			list.add(settings.getString(i + "", ""));
		}

		adapter = new HistoryAdapter(getActivity(), list);
		//if(!adapter.isEmpty())
			listViewHistory.setAdapter(adapter);
		listViewHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String tmp = list.get(position);
				textViewDestination.setText(tmp.split("-")[1]);
				textViewSource.setText(tmp.split("-")[0]);
			}
		});

		textViewDateTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(year, monthOfYear, dayOfMonth);
						textViewDateTime.setText(year + "-" + (monthOfYear + 1)
								+ "-" + dayOfMonth + " " + DateUtils.formatDateTime(getActivity(), calendar.getTimeInMillis(),DateUtils.FORMAT_SHOW_WEEKDAY).replace("星期","周"));
					}
				},Integer.parseInt(textViewDateTime.getText().toString().split("-")[0]),
						Integer.parseInt(textViewDateTime.getText().toString().split("-")[1]) - 1,
						Integer.parseInt(textViewDateTime.getText().toString().split("-")[2].split(" ")[0]));
				datePickerDialog.show();
			}
		});
		textViewSource.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sourceFunction();
			}
		});
		imageViewSource.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sourceFunction();
			}
		});
		textViewDestination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				destinationFunction();
			}
		});
		imageViewDestination.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				destinationFunction();
			}
		});
		imageViewSwitch.setOnClickListener(new View.OnClickListener() {
			String source;
			String destination;
			@Override
			public void onClick(View v) {
				source = textViewSource.getText().toString();
				destination = textViewDestination.getText().toString();
				float x_distance = textViewDestination.getX() - textViewSource.getX();
				TranslateAnimation fromAnima = new TranslateAnimation(0, x_distance, 0, 0);
				fromAnima.setInterpolator(new LinearInterpolator());
				fromAnima.setDuration(500);
				fromAnima.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						textViewSource.clearAnimation();
						textViewSource.setText(destination);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}
				});
				TranslateAnimation toAnima = new TranslateAnimation(0, -x_distance, 0, 0);
				toAnima.setInterpolator(new LinearInterpolator());
				toAnima.setDuration(500);
				toAnima.setAnimationListener(new Animation.AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						textViewDestination.clearAnimation();
						textViewDestination.setText(source);
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}
				});
				textViewSource.startAnimation(fromAnima);
				textViewDestination.startAnimation(toAnima);
			}
		});
		buttonQuery.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String source = textViewSource.getText().toString();
				String destination = textViewDestination.getText().toString();
				String datetime = textViewDateTime.getText().toString();
				startActivityForResult(new Intent(getActivity(), BookActivity.class)
						.putExtra("datetime",datetime)
						.putExtra("interzone",source + "-" + destination),3);
				SharedPreferences settings = getActivity().getSharedPreferences("history", 0);
				int count = Integer.parseInt(settings.getString("count", ""));
				//为凑数据暂不作处理,后期将对数目进行限制
				SharedPreferences.Editor editor = settings.edit();
				count++;
				editor.putString(count + "", source + "-" + destination);
				editor.putString("count", count + "");
				editor.commit();
				//刷新listViewHistory
				list.add(0, source + "-" + destination);
			}
		});
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		adapter.notifyDataSetChanged();
		super.onResume();
	}

	private void sourceFunction(){
		startActivityForResult(new Intent(getActivity(), StationActivity.class),20);
	}

	private void destinationFunction(){
		startActivityForResult(new Intent(getActivity(), StationActivity.class),21);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			if (requestCode == 20) {
				textViewSource.setText(data.getExtras().get("station_name").toString());
			} else if (requestCode == 21) {
				textViewDestination.setText(data.getExtras().get("station_name").toString());
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public class HistoryAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<String> list;

		public HistoryAdapter(Context ctx, List<String> list){
			this.inflater = LayoutInflater.from(ctx);
			this.list = list;
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
			convertView = inflater.inflate(R.layout.item_history, null);
			TextView textViewHistory = (TextView) convertView.findViewById(R.id.textViewHistory);
			textViewHistory.setText(list.get(position));
			convertView.setTag(textViewHistory);

			return convertView;
		}
	}
}
