package com.aiapp.aiapp.widgetCustom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

public class CircleAnimation extends RelativeLayout {
	private final static String TAG = "CicleAnimation";
	private RectF mRect = new RectF(10,30, 310, 330);
	private int mBeginAngle = 0;
	private int mEndAngle = 180;
	private int mFrontColor = 0xffff0000;
	private float mFrontLine = 20;
	private Style mFrontStyle = Style.STROKE;
	private int mShadeColor = 0xffeeeeee;
	private float mShadeLine = 10;
	private Style mShadeStyle = Style.STROKE;
	private ShadeView mShadeView;
	private FrontView mFrontView;
	private int mRate = 2;
	private int mDrawTimes;
	private int mInterval = 70;
	private int mFactor;
	private String mStrText = "相似度:0%";
	private Context mContext;
	private int mSeq = 0;
	private int mDrawingAngle = 0;
	private int mTextColor = 0xFA323200;
	public CircleAnimation(Context context) {
		super(context);
		mContext = context;
	}
	
	public void render() {
		removeAllViews();
		mShadeView = new ShadeView(mContext);
		addView(mShadeView);
		mFrontView = new FrontView(mContext);
		addView(mFrontView);
		play();
	}

	public void play() {
		mSeq = 0;
        mDrawingAngle = 0;
		mDrawTimes = mEndAngle/mRate;
		mFactor = mDrawTimes/mInterval + 1;
		Log.d(TAG, "mDrawTimes="+mDrawTimes+",mInterval="+mInterval+",mFactor="+mFactor);
		mFrontView.invalidateView();
	}
	public void SetText(String strTempText)
	{
		mStrText = strTempText;
	}

	public void setRect(int left, int top, int right, int bottom) {
		mRect = new RectF(left, top, right, bottom);
	}

	public void setAngle(int begin_angle, int end_angle) {
		mBeginAngle = begin_angle;
		mEndAngle = end_angle;
	}
	
	//speed:每次移动几个度数   frames:每秒移动几帧
	public void setmRate(int speed, int frames) {
		mRate = speed;
		mInterval = 1000/frames;
	}

	public void setFront(int color, float line, Style style) {
		mFrontColor = color;
		mFrontLine = line;
		mFrontStyle = style;
	}

	public void setShade(int color, float line, Style style) {
		mShadeColor = color;
		mShadeLine = line;
		mShadeStyle = style;
	}
	
	private class ShadeView extends View {
		private Paint paint;

		public ShadeView(Context context) {
			super(context);
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setDither(true);
			paint.setColor(mShadeColor);
			paint.setStrokeWidth(mShadeLine);
			paint.setStyle(mShadeStyle);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawArc(mRect, mBeginAngle, 360, false, paint);
		}
	}

	private class FrontView extends View {
		private Paint paint;
		private Paint textPaint;
	    private Handler handler = new Handler();

		public FrontView(Context context) {
			super(context);
			paint = new Paint();
			paint.setAntiAlias(true);         //设置画笔为无锯齿
			paint.setDither(true);            //防抖动
			paint.setColor(mFrontColor);       //设置画笔颜色
			paint.setStrokeWidth(mFrontLine);  //线宽
			paint.setStyle(mFrontStyle);       //画笔类型 STROKE空心 FILL 实心
			//paint.setStrokeJoin(Paint.Join.ROUND); //画笔接洽点类型 如影响矩形直角的外轮廓
			paint.setStrokeCap(Paint.Cap.ROUND);  ////画笔笔刷类型 如影响画笔的始末端
			textPaint = new Paint();
			textPaint.setTextSize(46f);
			textPaint.setColor(Color.BLUE);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.drawArc(mRect, mBeginAngle, (float) (mDrawingAngle), false, paint);
			canvas.drawText(mStrText, getWidth()/2 - getHeight()/2 +40,getHeight()/2 + 20,textPaint);
		}

	    public void invalidateView(){
	        handler.postDelayed(drawRunnable, 0);
	    }

	    private Runnable drawRunnable = new Runnable() {
	        @Override
	        public void run() {
	            if (mDrawingAngle >= mEndAngle) {
	            	mDrawingAngle = mEndAngle;
	                invalidate();
	                handler.removeCallbacks(drawRunnable);
	            } else {
	            	mDrawingAngle = mSeq*mRate;
	            	mSeq++;
	                handler.postDelayed(drawRunnable, (long) (mInterval-mSeq/mFactor));
	                invalidate();
	            }
	        }
	    };
	}

}
