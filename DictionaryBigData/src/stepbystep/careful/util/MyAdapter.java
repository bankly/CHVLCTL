package stepbystep.careful.util;

import java.lang.ref.WeakReference;

import stepbystep.careful.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	private WeakReference<Context> mContext;
	private Engine mEngine;

	public MyAdapter(Context context, Engine engine) {
		super();
		mContext = new WeakReference<Context>(context);
		this.mEngine = engine;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// create a ViewHolder reference
		ViewHolder holder;
		LayoutInflater layoutInflater = (LayoutInflater) mContext.get().getSystemService(
		        Context.LAYOUT_INFLATER_SERVICE);

		// check to see if the reused view is null or not, if is not null then reuse it
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = layoutInflater.inflate(R.layout.layout_list_view_custom, null);
			holder.itemName = (TextView) convertView.findViewById(R.id.text_item);

			// Set font for TextView
			Typeface tf = Typeface.createFromAsset(convertView.getContext().getAssets(),
			        "fonts/DejaVuSans.ttf");
			holder.itemName.setTypeface(tf);

			// the setTag() is used to store the data within this view
			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		// get the string item from the position "position" from array list to put it on TextView
		String stringItem = (String) getItem(position);
		if (stringItem != null) {
			if (holder.itemName != null) {
				holder.itemName.setText(stringItem);
			}
		}

		// this method must return the view corresponding to the data a the specified position
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mEngine.getWordCount();
	}

	@Override
	public Object getItem(int position) {
		return mEngine.getEntryById(position + 1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		protected TextView itemName;
	}

}
