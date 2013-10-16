package com.puzzlebobble.stateSystem;

import java.util.List;

import com.game.R;
import com.puzzlebobble.PBSurfaceView;
import com.puzzlebobble.gameactor.Bullet;
import com.puzzlebobble.gameactor.GameActor;
import com.puzzlebobble.gameactor.Shooter;
import com.puzzlebobble.gameactor.Storehouse;

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

public class PlayState implements IGameObject {
	
	private StateSystem stateSystem;
	
	private Storehouse storehouse;
	private Shooter shooter;
	
	Rect src;
	private Bitmap backGround, isWin, isLose;
	
	Paint paint;
	
	
	
	public PlayState(final Context context, final StateSystem stateSystem) {
		this.stateSystem = stateSystem;
		
		backGround = BitmapFactory.decodeResource(context.getResources(), R.drawable.background);
		backGround = Bitmap.createBitmap(backGround, backGround.getWidth()/4, 0, backGround.getWidth()/2, backGround.getHeight());
		src = new Rect(0, 0, backGround.getWidth(), backGround.getHeight());
		isWin = BitmapFactory.decodeResource(context.getResources(), R.drawable.win_panel);
		isLose = BitmapFactory.decodeResource(context.getResources(), R.drawable.lose_panel);
		
		new Bullet(context);		//给Bullet的所有静态变量赋初值
		storehouse = new Storehouse("Storehouse");
		shooter = new Shooter(storehouse, context);
		
		paint = new Paint();
	}
	
	public void logic(long elapsedTime) {
		shooter.logic(elapsedTime);
		storehouse.logic(elapsedTime);
		
	}
	
	public void myDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);		//万一露边 黑色补充
		canvas.drawBitmap(backGround, src, PBSurfaceView.dst, paint);		//画背景
		
		shooter.myDraw(canvas);
		storehouse.myDraw(canvas);
			
		if(isLose())
			canvas.drawBitmap(isLose, (PBSurfaceView.screenW - isLose.getWidth()) / 2, PBSurfaceView.screenH / 2, paint);
		
		if(isWin())
			canvas.drawBitmap(isWin, (PBSurfaceView.screenW - isWin.getWidth()) / 2, PBSurfaceView.screenH / 2, paint);
	}
	
	public boolean isWin() {
		if(storehouse.getChildren().size() == 0)
			return true;
		return false;
	}
	
	public boolean isLose() {
		List<GameActor> children = storehouse.getChildren();
		for(GameActor actor : children)
			if(((Bullet) actor).getPosition().getY() == storehouse.positions.get(storehouse.positions.size() - 1).getY())
				return true;
		
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.i("PlayState", "onKeyDown ——> back");
			storehouse.reSet();
			stateSystem.changeState("MenuState");
		}
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(!isWin() && !isLose()) {
			shooter.onTouchEvent(event);
		}
		else{
			if(event.getAction() == MotionEvent.ACTION_UP) {
				storehouse.reSet();
			}
		}
		return true;
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
	}
	
}
