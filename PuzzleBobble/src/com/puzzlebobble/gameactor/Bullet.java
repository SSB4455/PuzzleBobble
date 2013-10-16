package com.puzzlebobble.gameactor;

import com.game.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

public class Bullet extends GameActor {
	
	public static Bitmap [] normal_bullet = new Bitmap[8];
	public static Bitmap [] frozen_bullet = new Bitmap[8];
	public static Bitmap [] blind_bullet = new Bitmap[8];
	public static int radius;
	//发射泡泡的方向
	public static final int DIRECTION_LEFT = -1;
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_DOWN = 2;
	public static final int DIRECTION_STOP = 3;
	public static int speed;
	
	int type, direction;
	double shootAngle;
	
	Position position;
	ArcNode nextArc;
	
	
	
	public Bullet(Context context) {
		for(int i = 0; i < 8; i++) {
			normal_bullet[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.bubble_1 + i);
			frozen_bullet[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.frozen_1 + i);
			blind_bullet[i] = BitmapFactory.decodeResource(context.getResources(), R.drawable.blind_1 + i);
		}
		
		radius = normal_bullet[0].getHeight() / 2;
		speed = radius * 3;
	}
	
	public Bullet(Position position, int type, String name) {
		super(name);
		
		setType(type);
		this.position = position;
		direction = DIRECTION_STOP;
		
		paint = new Paint();
	}
	
	public void logic(long elapsedTime) {
		
		if(position.y > Storehouse.GAME_AREA_TOP) {
			position.y -= speed * Math.cos(shootAngle * Math.PI / 180) * elapsedTime / 100;
			if(direction == DIRECTION_RIGHT)
				position.x += speed * Math.sin(shootAngle * Math.PI / 180) * elapsedTime / 100;
			else if(direction == DIRECTION_LEFT)
				position.x -= speed * Math.sin(shootAngle*Math.PI / 180) * elapsedTime / 100;
		}else 
			position.y = Storehouse.GAME_AREA_TOP;
		
		if(direction == DIRECTION_DOWN) {
			position.y += 2 * speed * elapsedTime / 100;
		}
		
		if(position.x > Storehouse.GAME_AREA_RIGHT) {
			direction = DIRECTION_LEFT;
			position.x = Storehouse.GAME_AREA_RIGHT;
		}else if(position.x < Storehouse.GAME_AREA_LEFT) {
			direction = DIRECTION_RIGHT;
			position.x = Storehouse.GAME_AREA_LEFT;
		}
	}
	
	public void myDraw(Canvas canvas) {
		canvas.drawBitmap(actorBitmap, position.x - radius, position.y - radius, paint);
	}
	
	void startShoot(double angle) {
		shootAngle = Math.abs(angle);
		
		if(angle >= 0)
			direction = DIRECTION_RIGHT;
		else if(angle < 0)
			direction = DIRECTION_LEFT;
		else 
			direction = DIRECTION_UP;
	}
	
	public boolean addArc(ArcNode arcNode) {
		if(nextArc == null)
			nextArc = arcNode;
		else{
			ArcNode a = nextArc;
			while(a.hasNext())
				a = a.nextArc();
			a.nextArc = arcNode;
		}
		return true;
	}

	boolean deleteArc(ArcNode arcNode) {
		if(nextArc == null)
			return false;
		else{
			if(nextArc.equals(arcNode)) {
				nextArc = nextArc.nextArc;
				return true;
			}
			
			ArcNode tempArcNode = nextArc;
			while(tempArcNode.hasNext()) {
				if(tempArcNode.nextArc.equals(arcNode)) 
					tempArcNode.nextArc = tempArcNode.nextArc.nextArc;
				tempArcNode = tempArcNode.nextArc;
				return true;
			}
		}
		return false;
	}
	
	boolean deleteArcTo(Bullet toBullet) {
		if(nextArc == null)
			return false;
		else{
			if(nextArc.bullet == toBullet) {
				nextArc = nextArc.nextArc;
				return true;
			}
			
			ArcNode tempArcNode = nextArc;
			while(tempArcNode.hasNext()) {
				if(tempArcNode.nextArc.bullet == toBullet) 
					tempArcNode.nextArc = tempArcNode.nextArc.nextArc;
				tempArcNode = tempArcNode.nextArc;
				return true;
			}
		}
		return false;
	}
	
	String showArcLink() {
		String str = new String("[" + name + "] arc link：");
		ArcNode arcNode = nextArc;
		while(arcNode != null) {
			str += "[" + arcNode.bullet.name + "]->";
			arcNode = arcNode.nextArc();
		}
		str += "null";
		//System.out.println(str);
		return str;
	}
	
	public void setBitmap_bullet(Bitmap bitmap_bullet) {
		this.actorBitmap = bitmap_bullet;
	}
	
	public Bitmap getBitmap_bullet() {
		return actorBitmap;
	}
	
	public void setType(int type) {
		if(type < 0 || type > 7)
			this.type = 0;
		else
			this.type = type;
		//Log.i("Bullet-steType", "this.type = " + this.type);
		actorBitmap = normal_bullet[this.type];
	}
	
	public int getType() {
		return type;
	}
	
	Position setPosition(Position p) {
		position = p;
		return position;
	}
	
	public Position getPosition() {
		return position;
	}
	
}
