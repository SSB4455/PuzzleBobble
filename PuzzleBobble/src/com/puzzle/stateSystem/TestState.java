package com.puzzle.stateSystem;

import com.puzzle.MyGameSurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class TestState implements IGameObject, OnGestureListener{
	
	public GestureDetector mGestureDetector;
	
	StateSystem stateSystem; 
	Context context;
	
	int screenW, screenH;
	float cloudX, cloudY;
	
	Paint paint;
	Bitmap cloud, gameBackground;
	
	
	
	public TestState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		screenW = MyGameSurfaceView.screenW;
		screenH = MyGameSurfaceView.screenH;
		
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		
		//兜兵晒GestureDetector
		mGestureDetector = new GestureDetector(context, this);
		mGestureDetector.setIsLongpressEnabled(true);
		
		
	}
	
	public void logic(long elapsedTime) {
		
	}

	public void myDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
	}
	
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
			Log.i("TestState", "onKeyDown ！！> back");
			stateSystem.changeState("MenuState");
		}
		return true;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		return true;
	}
	
	public boolean onDown(MotionEvent arg0) {		//OnGestureListener
		return true;
	}
	
	public boolean onSingleTapUp(MotionEvent e) {		//OnGestureListener
		Log.i("TestState", "onSingleTapup");
		Toast.makeText(context, "onSingleTapup", Toast.LENGTH_SHORT).show();
		return false;
	}
	
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {		//OnGestureListener
		/*
		int distanceX = (int)(e2.getX() - e1.getX());
		int distanceY = (int)(e2.getY() - e1.getY());
		Log.i("TestState", "onFling ！！！！> distanceX = " + distanceX);
		Log.i("TestState", "onFling ！！！！> distanceY = " + distanceY);
		//Toast.makeText(context, "onFling", Toast.LENGTH_SHORT).show();
		
		int minL = 99;
		int minOfHalfW = 35;
		
		if(distanceX > minL && distanceY < minOfHalfW && distanceY > -minOfHalfW){
			Log.i("TestState", "Fling to right.");
			//Toast.makeText(context, "Fling to right.", Toast.LENGTH_SHORT).show();
		}
		if(distanceX < -minL && distanceY < minOfHalfW && distanceY > -minOfHalfW){
			Log.i("TestState", "Fling to left.");
			//Toast.makeText(context, "Fling to left.", Toast.LENGTH_SHORT).show();
		}
		*/
		return false;
	}
	
	public void onLongPress(MotionEvent e) {		//OnGestureListener
		//Toast.makeText(context, "onLongPress", Toast.LENGTH_SHORT).show();
		
	}
	
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {		//OnGestureListener
		
		return false;
	}
	
	public void onShowPress(MotionEvent e) {		//OnGestureListener
		// TODO Auto-generated method stub
		
	}
	
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}