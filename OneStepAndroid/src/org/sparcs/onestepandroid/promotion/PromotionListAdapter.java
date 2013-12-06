package org.sparcs.onestepandroid.promotion;

import java.util.ArrayList;

import org.sparcs.onestepandroid.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PromotionListAdapter extends ArrayAdapter<PromotionInfo> {
	private LayoutInflater inflater;
	private ArrayList<PromotionInfo> list;
	
	public PromotionListAdapter(Context context, ArrayList<PromotionInfo> list) {
		super(context, 0, list);
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public PromotionInfo getItem(int position) {
		return list.get(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		//return super.getView(position, convertView, parent);
		if ( convertView == null )
		{
			convertView = inflater.inflate(R.layout.promotion_list_item, null);
		}
		PromotionInfo item = getItem(position);
		TextView title = (TextView) convertView.findViewById(R.id.promotion_list_item_subject);
		ImageView img = (ImageView) convertView.findViewById(R.id.promotion_list_item_image);
		img.setImageBitmap(item.getPicture());
		title.setText(item.getTitle());
		return convertView;
	}
	
}
