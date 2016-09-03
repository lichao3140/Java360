package com.lichao.java360.engine;

import java.lang.reflect.Field;

import com.lichao.java360.view.FloatCircleView;
import com.lichao.java360.view.FloatMenuView;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;

/**
 * 浮窗管理者的创建（单例）
 * @author LiChao
 *
 */
public class FloatViewManager {
	private static final String TAG = FloatViewManager.class.getSimpleName();
	
	private Context context;
	private FloatCircleView circleView;
	private WindowManager wm;//通过WindowManager来操控浮体窗口的移动和隐藏
	private static FloatViewManager instance;
	private LayoutParams params;
	
	private float startx;//小球移动前的坐标
	private float starty;//小球移动前的坐标
	private float x0;
	private float y0;
	private OnTouchListener circleTouchListener = new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//三个事件的监听
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				startx = event.getRawX();
				starty = event.getRawY();
				x0 = event.getRawX();
				y0 = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				float x = event.getRawX();
                float y = event.getRawY();
                float dx = x - startx;
                float dy = y - starty;
                params.x+= dx;//偏移坐标x
                params.y+= dy;//偏移坐标y
                circleView.setDragStatus(true);
                wm.updateViewLayout(circleView, params);//刷新浮窗位置
                startx = x;
                starty = y;
                break;
            case MotionEvent.ACTION_UP:
            	float x1 = event.getRawX();
            	if(x1 > getScreenWidth()/2){//停在右边
            		params.x = getScreenWidth() - circleView.width;
            	}else{//停在左边
            		params.x = 0;
            	}
            	circleView.setDragStatus(false);
            	wm.updateViewLayout(circleView, params);
            	if(Math.abs(x1 - x0)>6){
            		//如果浮窗离开像素超过6，则判断为移动，即只相应移动事件，否则为点击事件
            		return true;
            	}else {
					return false;
				}
			default:
				break;
			}
			return false;
		}
	};

	private FloatMenuView floatMenuView;
	
	public int getScreenWidth(){
		return wm.getDefaultDisplay().getWidth();
	}
	
	public int getScreenHeight(){
		return wm.getDefaultDisplay().getHeight();
	}
	//获取状态栏的高
	public int getStatusHeight(){
		try {//运用反射机制
			Class<?> c =Class.forName("com.android.internal.R$dimen");
			Object o =c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = (Integer)field.get(o);
			return context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			return 0;
		} 
	}
	
	//私有化构造函数
	private FloatViewManager(final Context context){
		this.context = context;
		//获取窗口管理的服务
		wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
		circleView = new FloatCircleView(context);
		circleView.setOnTouchListener(circleTouchListener);
		circleView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "点击小球", Toast.LENGTH_SHORT).show();
				//隐藏cricleView,显示菜单栏,开启动画
				wm.removeView(circleView);
				showFloatMenuView();
				floatMenuView.startAnimation();
			}
		});
		floatMenuView = new FloatMenuView(context);
	}
	
	protected void showFloatMenuView() {
		LayoutParams params = new LayoutParams();
		params.width = getScreenWidth();
		params.height = getScreenHeight() - getStatusHeight();
		params.gravity = Gravity.BOTTOM|Gravity.LEFT;
		params.x = 0;
		params.y = 0;
		params.type = LayoutParams.TYPE_TOAST;
		params.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCH_MODAL;
		params.format = PixelFormat.RGBA_8888;
		wm.addView(floatMenuView, params);
	}

	//获取FloatViewManager实例
	public static FloatViewManager getInstance(Context context){
		if(instance == null){
			synchronized (FloatViewManager.class) {
				if (instance == null) {
					instance = new FloatViewManager(context);
				}
			}
		}
		return instance;
	}
	
	/**
	 * 展示浮体小球到窗体
	 */
	public void showFloatCircleView(){
		if(params == null){
			params = new LayoutParams();
			params.width = circleView.width;
			params.height = circleView.height;
			Log.e(TAG, "width:"+params.width+"height:"+params.height);
			params.gravity = Gravity.TOP|Gravity.LEFT;
			params.x = 0;
			params.y = 0;
			
			String packname = context.getPackageName();
			PackageManager pm = context.getPackageManager();
			boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname)); 
			if(permission){
				params.type = LayoutParams.TYPE_PHONE;
			}else{
				params.type = LayoutParams.TYPE_TOAST;
			}
			
			//设置手机电话类型
			//华为，小米等手机是默认禁止显示悬浮窗的,把TYPE_PHONE改成TYPE_TOAST，即可。使用TYPE_TOAST不需要申请权限
			//params.type = LayoutParams.TYPE_TOAST;
			//设置不与手机应用抢焦点
			params.flags = LayoutParams.FLAG_NOT_FOCUSABLE|LayoutParams.FLAG_NOT_TOUCH_MODAL;
			//设置背景透明
			params.format = PixelFormat.RGBA_8888;
		}
		wm.addView(circleView, params);
	}

	//隐藏菜单栏
	public void hideFloatMenuView() {
		wm.removeView(floatMenuView);
	}
}
