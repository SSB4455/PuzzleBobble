package com.puzzlebobble.stateSystem;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract interface IGameObject{
	
	abstract void logic(long elapsedTime);
	
	abstract void render();
	
	abstract void myDraw(Canvas canvas);
	
	abstract boolean onKeyDown(int keyCode, KeyEvent event);
	
	abstract boolean onTouchEvent(MotionEvent event);
	
}
