package com.puzzle.actor;

import java.util.Random;

import com.game.R;
import com.puzzle.MyGameSurfaceView;
import com.puzzle.actor.Bullet;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Shooter extends GameActor{
	Context context;
	public int shooterH, shooterW;
	private double currentAngle, shootAngle;
	
	private boolean shooting;
	
	private Random random;
	
	private Bullet bullet;
	private GameActor preare;
	private GameMap gameMap;
	
	
	
	public Shooter(GameMap gameMap, Context context) {
		super("shooter");
		this.context = context;
		
		this.gameMap = gameMap;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shooter);
		shooterW = actorBitmap.getWidth();
		shooterH = actorBitmap.getHeight();
		actorX = MyGameSurfaceView.screenW / 2;
		actorY = MyGameSurfaceView.screenH - shooterH / 3 * 2;
		currentAngle = 0;
		shooting = false;
		
		random = new Random();
		
		int x = MyGameSurfaceView.screenW / 2;
		int y = (int) actorY;
		preare = new GameActor("preare");
		preare.addChild(new Bullet(new Position(x, y), random.nextInt(8), "a"));
		preare.addChild(new Bullet(new Position(x, y + shooterH / 2 - 10), random.nextInt(8), "b"));
		
		paint = new Paint();
	}
	
	public void logic(long elapsedTime) {
		if(shooting) {
			bullet.logic(elapsedTime);
			
			if(bullet.isCollsionWith(gameMap)) {
				shooting = false;
				gameMap.addChild(bullet);
			}
		}
	}
	
	public void myDraw(Canvas canvas) {
		
		//对当前射击角度的显示
		canvas.save();
		canvas.rotate((float)currentAngle, actorX, actorY);
		canvas.drawBitmap(actorBitmap, actorX - shooterW / 2, actorY - shooterH / 2, paint);
		canvas.restore();
		
		preare.myDraw(canvas);
		
		if(shooting)
			bullet.myDraw(canvas);
	}
	
	public boolean onTouchEvent(MotionEvent event) {
		
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
			currentAngle = Math.atan2((touchX - actorX), (actorY - touchY)) * 180 / Math.PI;
		}
		
		if(currentAngle > 75)
			currentAngle = 75;
		if(currentAngle < -75)
			currentAngle = -75;
				
		if(event.getAction() == MotionEvent.ACTION_UP) {
			
			if(!shooting) {
				shootAngle = currentAngle;
				
				Position p = new Position(((Bullet) preare.search("a")).position.getX(), ((Bullet) preare.search("a")).position.getY());
				//此处一定要新建一个Position
				bullet = new Bullet(p, ((Bullet) preare.search("a")).getType(), "bullet");
				bullet.startShoot(shootAngle);
				
				shooting = true;
				
				((Bullet) preare.search("a")).setType(((Bullet) preare.search("b")).getType());
				((Bullet) preare.search("b")).setType(random.nextInt(8));
			}
			//输出射击角度
			Log.i("Shooter", "shootAngle = " + shootAngle + "\t touchX = " + touchX + ", touchY = " + touchY);
		}
		return true;
	}

}
