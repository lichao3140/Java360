package com.lichao.java360.view;

import com.lichao.java360.R;
import com.lichao.java360.engine.FloatViewManager;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

public class FloatMenuView extends LinearLayout{

	private TranslateAnimation animation;
	private LinearLayout llLayout;

	public FloatMenuView(Context context) {
		super(context);
		View rootView=View.inflate(getContext(), R.layout.float_menu_view, null);
		llLayout = (LinearLayout) rootView.findViewById(R.id.id_main_layout);
		animation = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, 
				Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f,
                Animation.RELATIVE_TO_SELF, 0);
		animation.setDuration(500);//时长
		animation.setFillAfter(true);
		llLayout.setAnimation(animation);
		rootView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				FloatViewManager manager = FloatViewManager.getInstance(getContext());
				manager.showFloatCircleView();
				manager.hideFloatMenuView();
				return false;
			}
		});
		addView(rootView);
	}
	
	public void startAnimation(){
		animation.start();
	}
}
