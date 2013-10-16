package com.puzzlebobble.gameactor;

import java.util.Random;

import com.game.R;
import com.puzzlebobble.PBSurfaceView;
import com.puzzlebobble.gameactor.Bullet;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

public class Shooter extends GameActor {
	Context context;
	public int shooterH, shooterW;
	private double currentAngle, shootAngle;
	
	private Random random;
	
	private Bullet bullet;
	private Bullet bulletA;
	private Bullet bulletB;
	private Storehouse storehouse;
	
	
	
	public Shooter(Storehouse storehouse, Context context) {
		super("shooter");
		this.context = context;
		
		this.storehouse = storehouse;
		actorBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shooter);
		shooterW = actorBitmap.getWidth();
		shooterH = actorBitmap.getHeight();
		actorX = PBSurfaceView.screenW / 2;
		actorY = PBSurfaceView.screenH - shooterH / 3 * 2;
		currentAngle = 0;
		
		random = new Random();
		
		int x = PBSurfaceView.screenW / 2;
		int y = (int) actorY;
		bulletA = new Bullet(new Position(x, y), random.nextInt(8), "A");
		bulletB = new Bullet(new Position(x, y + shooterH / 2 - 10), random.nextInt(8), "B");
		
		paint = new Paint();
	}
	
	public void logic(long elapsedTime) {
		
	}
	
	public void myDraw(Canvas canvas) {
		//对当前射击角度的显示
		canvas.save();
		canvas.rotate((float)currentAngle, actorX, actorY);
		canvas.drawBitmap(actorBitmap, actorX - shooterW / 2, actorY - shooterH / 2, paint);
		canvas.restore();
		
		bulletA.myDraw(canvas);
		bulletB.myDraw(canvas);
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
			
			if(!storehouse.shooting) {
				shootAngle = currentAngle;
				
				Position p = new Position(bulletA.position.getX(), bulletA.position.getY());
				//此处一定要新建一个Position，不然bulletA会被拉走
				bullet = new Bullet(p, bulletA.getType(), "bullet");
				bullet.startShoot(shootAngle);
				
				bulletA.setType(bulletB.getType());
				bulletB.setType(random.nextInt(8));
				storehouse.push(bullet);
			}
			//输出射击角度
			Log.i("Shooter", "shootAngle = " + shootAngle + "\t touchX = " + touchX + ", touchY = " + touchY);
		}
		return true;
	}

}
