package com.lichao.java360.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Bitmap.Config;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.widget.Toast;

/**
 * 自定义加速球
 * @author dell
 *
 */
public class MyProgressView extends View{

	private int width = 200;
	private int height = 200;
	private Paint textPaint;
	private Paint progressPaint;
	private Paint circlePaint;
	private Bitmap bitmap;
	private Canvas bitmapCanvas;
	private Path path = new Path();//画线
	private int progress = 50;//目标进度
	private int curentprogress=0;//初始进度
	private int max = 100;
	private int count = 50;//波浪跳动次数
	private boolean isSingleTop = false;
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		};
	};
	
	public MyProgressView(Context context){
		super(context);
		init();
	}
	
	public MyProgressView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	public MyProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);//抗锯齿
		circlePaint.setColor(Color.argb(0xff, 0x3a, 0x8c,0x6c));
		
		progressPaint = new Paint();
		progressPaint.setAntiAlias(true);
		progressPaint.setColor(Color.argb(0xff, 0x4e, 0xc9, 0x63));
		//绘制重叠的部分
		progressPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
		textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(25);
		
		bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		bitmapCanvas = new Canvas(bitmap);
		
		final GestureDetector gestureDetector = new GestureDetector(new MyGestureDetector());
		setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// 将触摸事件给手势捕捉
				return gestureDetector.onTouchEvent(event);
			}
		});
		//接受点击操作
		setClickable(true);
	}

	//手势捕捉
	class MyGestureDetector extends SimpleOnGestureListener{
		// 双击事件
		@Override
		public boolean onDoubleTap(MotionEvent e) {			
			Toast.makeText(getContext(), "双击小球", Toast.LENGTH_SHORT).show();
			startDoubleTapAnimation();
			return super.onDoubleTap(e);
		}
		
		private void startDoubleTapAnimation() {
			handler.postDelayed(doubleTapRunable, 50);
			
		}
		private DoubleTapRunable doubleTapRunable = new DoubleTapRunable();
		class DoubleTapRunable implements Runnable{
			@Override
			public void run() {
				curentprogress ++;
				if(curentprogress <= progress){
					//刷新UI
					invalidate();
					//还没达到目标进度再重新执行此方法
					handler.postDelayed(doubleTapRunable, 50);
				}else {
					handler.removeCallbacks(doubleTapRunable);
					curentprogress = 0;
				}
			}
			
		}
		
		// 单击事件
		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			Toast.makeText(getContext(), "单击小球", Toast.LENGTH_SHORT).show();
			isSingleTop = true;
			curentprogress = progress;
			startSingleTapAnimation();
			return super.onSingleTapConfirmed(e);
		}
	}
	
	public void startSingleTapAnimation() {
		handler.postDelayed(singleTapRunable, 200);
		
	}
	private SingleTapRunable singleTapRunable = new SingleTapRunable();
	class SingleTapRunable implements Runnable{
		@Override
		public void run() {
			count--;
			if(count>=0){//50次还没绘制玩
				invalidate();
				handler.postDelayed(singleTapRunable, 200);
			}else{
				handler.removeCallbacks(singleTapRunable);
				count=50;
			}
		}
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		//将圆画到画布上
		bitmapCanvas.drawCircle(width/2, height/2, width/2, circlePaint);
		path.reset();
		//线条的高度，由进度动态决定
		float y = (1-(float)curentprogress/max)*height;
		path.moveTo(width, y);
		//将线条连接到右下角
		path.lineTo(width, height);
		//将线条连接到左下角
		path.lineTo(0, height);
		//将线条连接到左边
		path.lineTo(0, y);
		
		if(!isSingleTop){	
			//曲线波浪的高度，最终为水平直线  d振幅
			float d = (1-((float)curentprogress/progress))*10;
			for(int i=0; i<5; i++){
				//绘制贝塞尔曲线
				path.rQuadTo(10, -d, 20, 0);
				path.rQuadTo(10, d, 20, 0);
			}
		}else{
			float d = (float)count/50*10;
			if(count%2==0){
				for(int i=0; i<5; i++){
					//绘制贝塞尔曲线
					path.rQuadTo(20, -d, 40, 0);
					path.rQuadTo(10, d, 40, 0);
				}
			}else{
				for(int i=0; i<5; i++){
					//绘制贝塞尔曲线
					path.rQuadTo(10, d, 40, 0);
					path.rQuadTo(20, -d, 40, 0);
				}
			}
		}
		path.close();
		//将曲线画到画布上
		bitmapCanvas.drawPath(path, progressPaint);
		String text = (int)(((float)curentprogress/max)*100)+"%";
		float textWidth = textPaint.measureText(text);
		//文字规格
		FontMetrics textMetrics = textPaint.getFontMetrics();
		//文本的基线
		float baseLine = height/2 - (textMetrics.ascent+textMetrics.descent)/2;
		bitmapCanvas.drawText(text, width/2 - textWidth/2, baseLine, textPaint);
		canvas.drawBitmap(bitmap, 0, 0, null);	
		super.onDraw(canvas);
	}
	
}
