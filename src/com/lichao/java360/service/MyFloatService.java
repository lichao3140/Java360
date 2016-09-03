package com.lichao.java360.service;

import com.lichao.java360.engine.FloatViewManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyFloatService extends Service{
	private static final String TAG = MyFloatService.class.getSimpleName();

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		FloatViewManager floatViewManager = FloatViewManager.getInstance(this);
		floatViewManager.showFloatCircleView();
		Log.e(TAG, "MyFloatService onCreate");
		super.onCreate();
	}
	
}
