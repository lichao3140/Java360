package com.lichao.java360.engine;

import android.content.Context;
import android.view.WindowManager;

/**
 * 浮窗管理者的创建（单例）
 * @author LiChao
 *
 */
public class FloatViewManager {
	private Context context;
	private WindowManager wm;//通过WindowManager来操控浮体窗口的移动和隐藏
	private static FloatViewManager instance;
	//私有化构造函数
	private FloatViewManager(Context context){
		this.context = context;
		//获取窗口管理的服务
		wm = (WindowManager) context.getSystemService(context.WINDOW_SERVICE);
	}
	
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
}
