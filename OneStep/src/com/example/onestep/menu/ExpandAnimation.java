package com.example.onestep.menu;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ExpandAnimation extends Animation {
	private ArrayList<View> childs;
	private ArrayList<LinearLayout.LayoutParams> layoutParamsList;
	private ArrayList<Integer> deltaMargins;
	private ArrayList<Integer> endMargins;
	private int totalMargin;
	private boolean mWasEndedAlready = false;
	private boolean isExpand;
	private float previousTime = 0;
	private  int postIndex = -1;
	private View emptyView;
	private Context context;
	private ListView listview;

	/**
	 * Initialize the animation
	 * @param view The layout we want to animate
	 * @param duration The duration of the animation, in ms
	 */
	public ExpandAnimation(Context context, ArrayList<View> chlids, int duration, ListView listview) {
		setDuration(duration);
		this.childs = chlids;
		this.emptyView = new View(context);
		initialize();
		this.listview = listview;
	}

	public void initialize() {
		this.layoutParamsList = new ArrayList<LinearLayout.LayoutParams>();
		this.deltaMargins = new ArrayList<Integer>();
		this.endMargins = new ArrayList<Integer>();
		postIndex = -1;
		for (View v :childs) {
			LinearLayout.LayoutParams lp = ((LinearLayout.LayoutParams)(v.getLayoutParams()));
			int startMargin = lp.topMargin;
			int endMargin = (startMargin == 0 ? (0- v.getHeight()) : 0);
			isExpand = (totalMargin < 0 ? false : true);
			layoutParamsList.add(lp);
			deltaMargins.add(endMargin - startMargin);
			endMargins.add(endMargin);
			totalMargin += endMargin - startMargin;
		}
	}

	public void initialize(ArrayList<View> chlids, int duration) {
		setDuration(duration);
		this.childs = chlids;
		initialize();
	}


	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		if (interpolatedTime < 1.0f) {
			int toReduce = (int)((interpolatedTime - previousTime) * totalMargin);
			previousTime = interpolatedTime;
			int index;
			if (isExpand) {
				if (postIndex < 0) {
					postIndex = 0;
				}
				index = postIndex;

				while(toReduce > 0 && index >= 0) {
					childs.get(index).setVisibility(View.VISIBLE);
					if (toReduce > deltaMargins.get(index)) {
						toReduce -= deltaMargins.get(index);
						deltaMargins.set(index, 0);
						layoutParamsList.get(index).topMargin = 0;
						postIndex += 1;
					}
					else {
						deltaMargins.set(index, deltaMargins.get(index) - toReduce);
						toReduce = 0;
						layoutParamsList.get(index).topMargin = - deltaMargins.get(index);
					}
					childs.get(index).requestLayout();
					index += 1;
				}
			}
			else {
				if (postIndex < 0) {
					postIndex = deltaMargins.size() - 1;
				}
				index = postIndex;
				while(toReduce < 0 && index < deltaMargins.size()) {
					if (toReduce < deltaMargins.get(index)) {
						toReduce -= deltaMargins.get(index);
						deltaMargins.set(index, 0);
						layoutParamsList.get(index).topMargin = endMargins.get(index);
						postIndex -= 1;
					}
					else {
						deltaMargins.set(index, deltaMargins.get(index) - toReduce);
						toReduce = 0;
						layoutParamsList.get(index).topMargin = endMargins.get(index) - deltaMargins.get(index);
					}
					listview.requestLayout();
					index -= 1;
				}
			}

			// Making sure we didn't run the ending before (it happens!)
		} else if (!mWasEndedAlready) {
			for (int index=0;index < childs.size();index++) {
				layoutParamsList.get(index).topMargin = endMargins.get(index);
				childs.get(index).requestLayout();
				mWasEndedAlready = true;
			}
		}
		//Log.i("",String.valueOf(layoutParamsList.get(0).topMargin) + ", " + String.valueOf(layoutParamsList.get(1).topMargin) + ", " + String.valueOf(layoutParamsList.get(2).topMargin) + ", " + String.valueOf(layoutParamsList.get(3).topMargin) + ", ");
		//Log.i("",String.valueOf(((TextView)childs.get(0).findViewById(R.id.textView1)).getText() + "," + ((TextView)childs.get(1).findViewById(R.id.textView1)).getText() + "," + ((TextView)childs.get(2).findViewById(R.id.textView1)).getText() + "," + ((TextView)childs.get(3).findViewById(R.id.textView1)).getText()));
	}
}