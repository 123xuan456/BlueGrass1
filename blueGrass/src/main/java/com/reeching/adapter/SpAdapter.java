package com.reeching.adapter;

import java.util.List;

import android.content.Context;
import android.widget.ArrayAdapter;

public class SpAdapter<T> extends ArrayAdapter {
	public SpAdapter(Context context, int resource, List<String> objects) {
		super(context, resource, objects);
	}

	@Override
	public int getCount() {
		// don't display last item. It is used as hint.
		int count = super.getCount();
		return count > 0 ? count - 1 : count;
	}
}
