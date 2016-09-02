package com.lichao.java360.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义浮体小球view
 * @author dell
 *
 */
public class FloatCircleView extends View{

	private int width = 150;
	private int height = 150;
	private Paint circlePaint;
	private Paint textPaint;
	private String text = "50%";
	public FloatCircleView(Context context) {
		super(context);
		
	}

	public FloatCircleView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		
	}
	
	public FloatCircleView(Context context, AttributeSet attributeSet, int defstyle) {
		super(context, attributeSet, defstyle);
		initPaints();//初始化画笔
	}
	
	private void initPaints() {
		circlePaint = new Paint();
		circlePaint.setColor(Color.CYAN);//画笔的颜色
		circlePaint.setAntiAlias(true);//抗锯齿效果
		
		textPaint = new Paint();
		textPaint.setTextSize(25);
		textPaint.setColor(Color.WHITE);
		textPaint.setAntiAlias(true);
		textPaint.setFakeBoldText(true);//文字加粗
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(width, height);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(width/2, height/2, width/2, circlePaint);//绘制圆
		float textWidth = textPaint.measureText(text);//测量文本的宽度
		float x = width/2 - textWidth/2;
		//文本的偏移量在-(descent+Ascent)/2的位置  Ascent为正数  descent为负数
		//文字规格
		FontMetrics metrics = textPaint.getFontMetrics();
		float dy = -(metrics.descent - metrics.ascent)/2;
		float y = height/2 + dy;
		canvas.drawText(text, x, y, textPaint);//绘制文本
		
	}
}
