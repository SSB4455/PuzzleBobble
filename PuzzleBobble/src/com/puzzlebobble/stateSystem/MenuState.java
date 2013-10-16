package com.puzzlebobble.stateSystem;

import com.game.R;
import com.puzzlebobble.PBSurfaceView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;
import android.util.Log;

public class MenuState implements IGameObject {
	
	private Context context;
	private StateSystem stateSystem;
	
	private int screenW, screenH;
	private long exitTime;
	private String bubbleBobble;
	private float [][] menuLocation;
	private final int X = 0, Y = 1;
	
	Rect src;
	private Bitmap menuBackground, play, morePlayer, showTime, setting;
	private Bitmap [] menuButton;
	Paint paint;
	
	
	
	public MenuState(Context context, StateSystem stateSystem) {
		this.context = context;
		this.stateSystem = stateSystem;
		
		screenW = PBSurfaceView.screenW;
		screenH = PBSurfaceView.screenH;
		
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
		paint.setTextSize(50);
		
		menuButton = new Bitmap[5];
		menuLocation = new float[5][];
		for(int i = 0; i < 5; i++)
			menuLocation[i] = new float[2];
		
		menuBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.menu_background);
		play = BitmapFactory.decodeResource(context.getResources(), R.drawable.one_player);
		morePlayer = BitmapFactory.decodeResource(context.getResources(), R.drawable.more_player);
		showTime = BitmapFactory.decodeResource(context.getResources(), R.drawable.showtime);
		setting = BitmapFactory.decodeResource(context.getResources(), R.drawable.setting);
		
		src = new Rect(0, 0, menuBackground.getWidth(), menuBackground.getHeight());
		menuButton[1] = play;
		menuButton[2] = showTime;
		menuButton[3] = morePlayer;
		menuButton[4] = setting;
		
		bubbleBobble = "Bubble Bobble";
		menuLocation[0][X] = (screenW - paint.measureText(bubbleBobble)) / 2;
		menuLocation[0][Y] = screenH * 5 / 20;
		
		for(int i = 1; i < 5; i++){
			menuLocation[i][X] = screenW - menuButton[i].getWidth();
			menuLocation[i][Y] = i > 1 ? menuLocation[i - 1][Y] + menuButton[i - 1].getHeight() + 5 : screenH / 2;
		}
		
	}
	
	public void logic(long elapsedTime) {
		// TODO Auto-generated method stub
		
	}
	
	public void myDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);		//补充黑色露边
		canvas.drawBitmap(menuBackground, src, PBSurfaceView.dst, paint);		//画菜单背景
		
		canvas.drawText(bubbleBobble, menuLocation[0][X], menuLocation[0][Y], paint);
		
		for(int i = 1; i < 5; i++)
			canvas.drawBitmap(menuButton[i], menuLocation[i][X], menuLocation[i][Y], paint);
		
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {		//防止到另一个State中产生Fling的崩溃
			for(int i = 1; i < 5; i++) {
				if(menuLocation[i][X] < event.getX() 
						&& event.getX() <  menuLocation[i][X] + menuButton[i].getWidth() 
						&& menuLocation[i][Y] < event.getY() 
						&& event.getY() < menuLocation[i][Y] + menuButton[i].getHeight()) {
					if(i == 1) {
						stateSystem.changeState("PlayState");
						Toast.makeText(context, "Touch the screen ^_^!", Toast.LENGTH_SHORT).show();
					}
					if(i == 2) {
						Toast.makeText(context, "Just look this ^_^!", Toast.LENGTH_SHORT).show();
					}
					/*
					if(i == 3)
						stateSystem.changeState("TestState");
					if(i == 4)
						stateSystem.changeState("SettingState");
					*/
				}
			}
		}
		
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if((System.currentTimeMillis() - exitTime) > 2000) {
		    	Toast.makeText(context, "再按一次退出程序", Toast.LENGTH_SHORT).show();
		        exitTime = System.currentTimeMillis();
		    }
		    else
		        System.exit(0);
			return true; //返回true表示执行结束不需继续执行父类按键响应  
		}
		return false;		//能退出
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	
}
