package com.puzzlebobble.gameactor;

import java.util.ArrayList;
import java.util.List;

import com.puzzlebobble.PBSurfaceView;
import com.puzzlebobble.gameactor.Bullet;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Storehouse extends GameActor {
	//屏幕边界
	public static int GAME_AREA_TOP;
	public static int GAME_AREA_LEFT;
	public static int GAME_AREA_RIGHT;
	public static int GAME_AREA_BOTTOM;
	
	int s, score;
	boolean shooting;
	
	Bullet bullet;
	public List<Position> positions;
	GameActor bing;
	
	
	
	public Storehouse(String name) {
		super(name);
		GAME_AREA_TOP = PBSurfaceView.screenH / 16 + Bullet.radius;
		GAME_AREA_BOTTOM = PBSurfaceView.screenH * 19 / 24 - Bullet.radius;
		GAME_AREA_LEFT = PBSurfaceView.screenW * 1 / 16 + Bullet.radius;
		GAME_AREA_RIGHT = PBSurfaceView.screenW * 29 / 32 - Bullet.radius;
		
		bing = new GameActor("bing");
		positions = new ArrayList<Position>();
		for(int i = 0; GAME_AREA_TOP + Bullet.radius * 1.732 * i < GAME_AREA_BOTTOM + Bullet.radius * 2; i++) {
			for(int j = 0; ; j++) {
				Position p = new Position(GAME_AREA_LEFT + Bullet.radius * 2 * j, (float) (GAME_AREA_TOP + Bullet.radius * 1.732 * i));
				p.x += (i % 2 == 0) ? 0 : Bullet.radius;
				if(p.getX() < GAME_AREA_RIGHT)
					positions.add(p);
				else
					break;
			}
		}
		
		reSet();
		
		paint = new Paint();
		paint.setTextSize(15);
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);
	}
	
	public void logic(long elapsedTime) {
		if(shooting) {
			bullet.logic(elapsedTime);
			isCollsionWith(bullet);
		}
		
		bing.logic(elapsedTime);
	}
	
	public void myDraw(Canvas canvas) {
		super.myDraw(canvas);
		bing.myDraw(canvas);
		
		//显示网格小点点
		for(Position p : positions)
			canvas.drawCircle(p.getX(), p.getY(), 1, paint);
		if(shooting) {
			bullet.myDraw(canvas);
		}
		
		canvas.drawText("Score：" + score, 0, PBSurfaceView.screenH * 89 / 96, paint);
	}
	
	boolean push(Bullet bullet) {
		shooting = true;
		this.bullet = bullet;
		return true;
	}
	
	public boolean isCollsionWith(Bullet bullet) {
		if(bullet.position.y <= Storehouse.GAME_AREA_TOP) {
			bullet.direction = Bullet.DIRECTION_STOP;
			setNearlyPosition(bullet);
			addBulletToGameMap(bullet);
			shooting = false;
			return true;
		}
		
		boolean isCollsionWith = false;
		//取最近的泡泡
		for(GameActor actor : this.children) {
			if(bullet.position.distance(((Bullet) actor).position) < Bullet.radius * 2) {
				bullet.direction = Bullet.DIRECTION_STOP;
				setNearlyPosition(bullet, (Bullet) actor);
				addBulletToGameMap(bullet);
				Log.i("Bullet-" + name, "collsion with " + actor.name);
				isCollsionWith = true;
				shooting = false;
				break;
			}
		}
		
		return isCollsionWith;
	}
	
	public void addBulletToGameMap(Bullet bullet) {
		bullet.name = s++ + "-" + bullet.name + "-" + bullet.type;
		super.addChild(bullet);
		
		//为bullet以及与它相邻的泡泡添加临接表
		for(GameActor actor : this.children) {
			if(bullet.position.distance(((Bullet) actor).position) < Bullet.radius * 3) {
				if(actor != bullet) {
					bullet.addArc(new ArcNode((Bullet) actor));
					((Bullet) actor).addArc(new ArcNode(bullet));
					Log.i("Storehouse.addChile", ((Bullet) actor).showArcLink());
				}
			}
		}
		Log.i("Storehouse.addChile", bullet.showArcLink());
		
		bing.children.clear();
		findSameColorBulletToBing(bullet);	//将与actor的同色的泡泡放到bing里面
		checkBing();		//从gameMap里面把bing内的泡泡删除 检查孤立的泡泡 使之落下
	}
	
	void findSameColorBulletToBing(Bullet bullet) {
		bing.addChild(bullet);
		
		for(GameActor actor : this.children) {
			if(bullet.position.distance(((Bullet) actor).position) < Bullet.radius * 3 && bullet.getType() == ((Bullet) actor).getType()) {
				if(!bing.children.contains(actor))
					findSameColorBulletToBing((Bullet) actor);
			}
		}
	}
	
	void checkBing() {
		int countBing = bing.children.size();
		
		if(countBing > 2) {
			score += countBing;		//增加积分
			for(GameActor bingActor : bing.children) {
				ArcNode arcNode = ((Bullet) bingActor).nextArc;
				while(arcNode != null) {
					//把通向消去的那一串泡泡的弧都删除
					arcNode.bullet.deleteArc(new ArcNode((Bullet) bingActor));
					arcNode = arcNode.nextArc;
				}
				((Bullet) bingActor).direction = Bullet.DIRECTION_DOWN;
				this.children.remove(bingActor);
			}
		}else
			bing.children.clear();		//如果没有凑够三个就直接清空bing不做下面
		
		//之所以要再次循环 是因为现在才在gameMap中把所有连到已消去泡泡的弧删除； 不能够边删除边检查孤立点 那样会有问题
		for(int i = 0; i < bing.children.size(); i++) {
			GameActor bingActor = bing.children.get(i);
			ArcNode arcNode = ((Bullet) bingActor).nextArc;
			Log.i("Bing a actor", ((Bullet) bingActor).showArcLink());
			while(arcNode != null) {
				Log.i("Storehouse.checkBing", arcNode.bullet.showArcLink());
				if(!hasRoot(arcNode.bullet, new ArrayList<Bullet>())) {
					Log.i("Storehouse.chechBing", "[" + arcNode.bullet.name + "]" + " no root");
					arcNode.bullet.direction = Bullet.DIRECTION_DOWN;
					if(!bing.children.contains(arcNode.bullet))		//如果这个泡泡不在bing里面再加进来 不然会永远循环
						bing.addChild(arcNode.bullet);
					this.children.remove(arcNode.bullet);
					//这个arcNode.bullet泡泡已经是孤立的泡泡了 要把它周围的泡泡的通向它的弧删掉
					
				}
				arcNode = arcNode.nextArc;
			}
		}
		
		for(GameActor actor : this.children) 
			Log.i("checkBing final", ((Bullet) actor).showArcLink());
		
	}
	
	boolean hasRoot(Bullet bullet, List<Bullet> visted) {		//判断这个泡泡有没有根 不是标准递归
		if(bullet.position.y == GAME_AREA_TOP)
			return true;
		
		ArcNode arcNode = bullet.nextArc;
		while(arcNode != null) {
			if(!visted.contains(arcNode.bullet)) {
				visted.add(arcNode.bullet);
				return hasRoot(arcNode.bullet, visted);
			}
			arcNode = arcNode.nextArc;
		}
		
		return false;
	}
	
	public void reSet() {
		children.clear();
		s = 0;
		score = 0;
		shooting = false;
		addBulletToGameMap(new Bullet(new Position(positions.get(0).x, positions.get(0).y), 0, "0"));
		addBulletToGameMap(new Bullet(new Position(positions.get(1).x, positions.get(1).y), 0, "0"));
	}

	void setNearlyPosition(Bullet bullet) {		//针对于置顶的情况
		Position nearly = positions.get(positions.size() - 1);
		//找到在positions里面和当前bullet的psoition最近的position
		for(Position position : positions) {
			if(findBulletByPosition(position) == null 
					&& bullet.position.distance(position) < bullet.position.distance(nearly)) {
				nearly = position;
			}
		}
		
		bullet.setPosition(new Position(nearly.getX(), nearly.getY()));		//此处要新建一个以防止把地图上面的position带走
	}
	
	void setNearlyPosition(Bullet bullet, Bullet toBullet) {		//针对于粘住别的泡泡的情况
		Position nearly = positions.get(positions.size() - 1);
		//找到在positions里面和当前bullet的psoition最近的position
		for(Position position : positions) {
			if(toBullet.position.distance(position) < Bullet.radius * 3 
					&& findBulletByPosition(position) == null 
					&& bullet.position.distance(position) < bullet.position.distance(nearly) 
					&& position.getY() >= toBullet.position.getY())
				nearly = position;
		}
		
		bullet.setPosition(new Position(nearly.getX(), nearly.getY()));		//此处要新建一个以防止把地图上面的position带走
	}
	
	Bullet findBulletByPosition(Position position) {
		for(GameActor actor : children) {
			if(((Bullet) actor).position.distance(position) < Bullet.radius / 2) {
				Log.i("Storehouse.addChild", "good, i find a can't set position and continue it.");
				return (Bullet) actor;
			}
		}
		return null;
	}
	
	public List<GameActor> getChildren() {
		return children;
	}
	
}
