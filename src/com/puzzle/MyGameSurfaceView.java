package com.puzzle;

import com.puzzle.stateSystem.MenuState;
import com.puzzle.stateSystem.PlayState;
import com.puzzle.stateSystem.StateSystem;
import com.puzzle.stateSystem.TestState;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class MyGameSurfaceView extends SurfaceView implements Callback, Runnable {
	
	public static int screenH, screenW;
	public static Rect dst;
	public static long ELAPSED_TIME = 20;
	
	Context context;
	private SurfaceHolder sfh;
	
	long [] time;
	long elapsedTime;
	
	private boolean flag;
	Thread th;
	
	private StateSystem stateSystem;
	
	private Paint paint;
	
	
	
	public MyGameSurfaceView(Context context) {
		super(context);
		this.context = context;
		
		time = new long[3];
		
		stateSystem = new StateSystem();
		
		paint = new Paint();
		paint.setColor(Color.BLACK);
		
		sfh = this.getHolder();
		sfh.addCallback(this);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		screenW = this.getWidth();
		screenH = this.getHeight();
		
		dst = new Rect(0, 0, MyGameSurfaceView.screenW, MyGameSurfaceView.screenH);
		
		stateSystem.addState("MenuState", new MenuState(context, stateSystem));
		stateSystem.addState("PlayState", new PlayState(context, stateSystem));
		stateSystem.addState("TestState", new TestState(context, stateSystem));
		stateSystem.changeState("MenuState");
		
		//Æô¶¯Ïß³Ì
		flag = true;
		th = new Thread(this);
		th.start();
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
	
	@Override
	public void run() {
		while(flag) {
			time[0] = System.currentTimeMillis();
			Canvas canvas = sfh.lockCanvas();
			time[1] = System.currentTimeMillis();
			stateSystem.logic(elapsedTime);
			time[2] = System.currentTimeMillis();
			stateSystem.myDraw(canvas);
			canvas.drawText("logicTime      = " + (time[2] - time[1]), 0, 10, paint);
			canvas.drawText("myDrawTime = " + (System.currentTimeMillis() - time[2]), 0, 21, paint);
			canvas.drawText("elapsedTime  = " + elapsedTime, 0, 32, paint);
			sfh.unlockCanvasAndPost(canvas);
			
			elapsedTime = (System.currentTimeMillis() - time[0]);
			if(elapsedTime < ELAPSED_TIME){
				try {
					Thread.sleep(ELAPSED_TIME - elapsedTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			elapsedTime = (System.currentTimeMillis() - time[0]);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Log.i("MyGameSurfaceView", "onKeyDown ¡ª¡ª> back");
			//Toast.makeText(context, "MyGameSurfaceView --> back", Toast.LENGTH_SHORT).show();
		}
		
		return stateSystem.onKeyDown(keyCode, event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return stateSystem.onTouchEvent(event);
	}
}
