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

import com.reeching.bean.ZhanlanBean.Infos;
import com.reeching.bluegrass.BeginToCheck;
import com.reeching.bluegrass.GoHere;
import com.reeching.bluegrass.R;

public class CheckAdapter extends BaseAdapter {
	private List<Infos> infos;
	private Context context;

	public CheckAdapter(List<Infos> infos, Context context) {
		this.context = context;
		this.infos = infos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null== infos ? 0 : infos.size();
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
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.waitforcheck_item, null);
			vh.tvname = (TextView) convertView
					.findViewById(R.id.waitforcheck_tv1);
			vh.tvlocation = (TextView) convertView
					.findViewById(R.id.waitforcheck_tv2);
			vh.ivlever = (ImageView) convertView
					.findViewById(R.id.waitforcheck_iv);
			vh.tvcheck = (TextView) convertView
					.findViewById(R.id.waitforcheck_tv4);
			vh.tvgothere = (TextView) convertView
					.findViewById(R.id.waitforcheck_tv5);
			vh.tvgallery= (TextView) convertView.findViewById(R.id.waitforcheck_tv6);
			convertView.setTag(vh);

		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		vh.tvname.setText(info.getTheme());
		vh.tvlocation.setText(info.getAddress());
		vh.tvgallery.setText(info.getGalleryName());
		if (info.getCareLevel().equals("0")) {
			vh.ivlever.setImageResource(R.drawable.green);

		} else if (info.getCareLevel().equals("1")) {
			vh.ivlever.setImageResource(R.drawable.yellow);
		} else {
			vh.ivlever.setImageResource(R.drawable.red);
		}
		vh.tvcheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, BeginToCheck.class);
				intent.putExtra("info", info);
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
		TextView tvname, tvlocation, tvcheck, tvgothere,tvgallery;
		ImageView ivlever;
	}
}
