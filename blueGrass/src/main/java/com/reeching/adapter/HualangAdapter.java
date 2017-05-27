package com.reeching.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.reeching.bean.AllHualangInfo.Infos;
import com.reeching.bluegrass.CheckHualangActivity;
import com.reeching.bluegrass.GoHere;
import com.reeching.bluegrass.R;

public class HualangAdapter extends BaseAdapter {
	private List<Infos> infos;
	private Context context;

	public HualangAdapter(List<Infos> infos, Context context) {
		this.context = context;
		this.infos = infos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Infos info = infos.get(position);
		ViewHolder vh = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.hualanginfo_item, null);
			vh.tvname = (TextView) convertView
					.findViewById(R.id.hualanginfo_tv1);
			vh.tvlocation = (TextView) convertView
					.findViewById(R.id.hualanginfo_tv2);
			vh.ivlever = (ImageView) convertView
					.findViewById(R.id.hualanginfo_iv);
			vh.tvtime = (TextView) convertView
					.findViewById(R.id.hualanginfo_tv7);
			vh.tvgothere = (TextView) convertView
					.findViewById(R.id.hualanginfo_tv5);
			vh.tvcheck = (TextView) convertView
					.findViewById(R.id.hualanginfo_tv4);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tvname.setText(info.getName());
		vh.tvlocation.setText(info.getAddress());
		if (info.getCareLevel().equals("0")) {
			vh.ivlever.setImageResource(R.drawable.green);

		} else if (info.getCareLevel().equals("1")) {
			vh.ivlever.setImageResource(R.drawable.yellow);
		} else {
			vh.ivlever.setImageResource(R.drawable.red);
		}
		vh.tvtime.setText(info.getCreateDate());
		vh.tvcheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,
						CheckHualangActivity.class);
				intent.putExtra("lat", info.getMapLat());
				intent.putExtra("lng", info.getMapLng());
				context.startActivity(intent);
			}
		});

		vh.tvgothere.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, GoHere.class);
				intent.putExtra("lat", info.getMapLat());
				intent.putExtra("lng", info.getMapLng());
				context.startActivity(intent);
			}
		});
		return convertView;

	}

	static class ViewHolder {
		TextView tvname, tvlocation, tvcheck, tvgothere, tvtime;
		ImageView ivlever;
	}

}
