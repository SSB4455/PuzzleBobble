package com.puzzle.stateSystem;

import java.util.List;

import com.game.R;
import com.puzzle.actor.Bullet;
import com.puzzle.actor.GameActor;
import com.puzzle.actor.GameActor.ActorStatus;
import com.puzzle.actor.GameMap;
import com.puzzle.actor.Shooter;
import com.puzzle.MyGameSurfaceView;

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
	
	private GameMap gameMap;
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
		gameMap = new GameMap("GameMap");
		shooter = new Shooter(gameMap, context);
		
		paint = new Paint();
	}
	
	public void logic(long elapsedTime) {
		shooter.logic(elapsedTime);
		gameMap.logic(elapsedTime);
		
	}
	
	public void myDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);		//万一露边 黑色补充
		canvas.drawBitmap(backGround, src, MyGameSurfaceView.dst, paint);		//画背景
		
		shooter.myDraw(canvas);
		gameMap.myDraw(canvas);
			
		if(isLose()) {
			Log.i("PlayState", "is Lose");
			canvas.drawBitmap(isLose, (MyGameSurfaceView.screenW - isLose.getWidth()) / 2, MyGameSurfaceView.screenH / 2, paint);
		}
		
		if(isWin()) {
			Log.i("PlayState", "is Win");
			canvas.drawBitmap(isWin, (MyGameSurfaceView.screenW - isWin.getWidth()) / 2, MyGameSurfaceView.screenH / 2, paint);
		}
	}
	
	public boolean isWin() {
		if(gameMap.getChildren().size() == 0)
			return true;
		return false;
	}
	
	public boolean isLose() {
		List<GameActor> children = gameMap.getChildren();
		for(GameActor actor : children)
			if(((Bullet) actor).getPosition().getY() == gameMap.positions.get(gameMap.positions.size() - 1).getY())
				return true;
		
		return false;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Log.i("PlayState", "onKeyDown ——> back");
			gameMap.reSet();
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
				gameMap.reSet();
			}
		}
		return true;
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
	}
	
}
