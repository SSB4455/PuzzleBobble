package com.puzzle.stateSystem;

import java.util.Hashtable;


import android.graphics.Canvas;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class StateSystem {

	Hashtable<String, IGameObject> stateStore;
	IGameObject currentState;
	
	SurfaceHolder sfh;
	Canvas canvas;
	
	
	
	public StateSystem() {
		stateStore = new Hashtable<String, IGameObject>();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return currentState.onKeyDown(keyCode, event);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		return currentState.onTouchEvent(event);
	} 
	
	public void logic(long elapsedTime) {
		//Log.i("StateSystem", "start update");
		currentState.logic(elapsedTime);
	}
	
	public void myDraw(Canvas canvas) {
		//Log.i("StateSystem", "start render with canvas");
		currentState.myDraw(canvas);
	}
	
	public boolean addState(String statId, IGameObject state) {
		stateStore.put(statId, state);
		Log.i("StateSystem", "addState " + statId + " is succeed");
		return true;
	}
	
	public boolean changeState(String stateId) {
		currentState = stateStore.get(stateId);
		Log.i("StateSystem", "changeState to " + stateId);
		return true;
	}
	
	public boolean exits(String stateId) {
		return true;
	}
	
	public void testArgument(SurfaceHolder sfh,Canvas canvas) {
		this.sfh = sfh;
		this.canvas = canvas;
	}
	
}
