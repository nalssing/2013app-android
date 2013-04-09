package com.example.onestep.menu;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Log.i("", "this is rec " + canvas.getClipBounds());
	}
	

}
