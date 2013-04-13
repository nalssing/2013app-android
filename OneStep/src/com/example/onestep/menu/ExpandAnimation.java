package com.example.onestep.menu;

import java.util.ArrayList;

import android.content.Context;
import android.sax.StartElementListener;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ExpandAnimation extends Animation {
	private ListView listview;
	private Context context;
	private AbsListView.LayoutParams layoutParams;
	private int startHeight;
	private int endHeight;
	private boolean wasEndedAlready;
	/**
	 * Initialize the animation
	 * @param view The layout we want to animate
	 * @param duration The duration of the animation, in ms
	 */
	public ExpandAnimation(Context context, ListView listview, int duration, int endHeight) {
		setDuration(duration);
		this.listview = listview;
		this.context = context;
		this.layoutParams = (LayoutParams) listview.getLayoutParams();
		this.startHeight = layoutParams.height;
		this.endHeight = endHeight;
	}


	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if (interpolatedTime < 1.0f) {
			layoutParams.height = (int)(startHeight + interpolatedTime * (endHeight - startHeight));
			listview.requestLayout();
			// Making sure we didn't run the ending before (it happens!)
		} else if (!wasEndedAlready) {
				layoutParams.height = endHeight;
				listview.requestLayout();
				wasEndedAlready = true;
		}
		//Log.i("",String.valueOf(layoutParamsList.get(0).topMargin) + ", " + String.valueOf(layoutParamsList.get(1).topMargin) + ", " + String.valueOf(layoutParamsList.get(2).topMargin) + ", " + String.valueOf(layoutParamsList.get(3).topMargin) + ", ");
		//Log.i("",String.valueOf(((TextView)childs.get(0).findViewById(R.id.textView1)).getText() + "," + ((TextView)childs.get(1).findViewById(R.id.textView1)).getText() + "," + ((TextView)childs.get(2).findViewById(R.id.textView1)).getText() + "," + ((TextView)childs.get(3).findViewById(R.id.textView1)).getText()));
	}
}