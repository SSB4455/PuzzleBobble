package com.puzzle.actor;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GameActor {
	
	public String name;
	protected float actorX, actorY;
	protected Bitmap actorBitmap;
	protected Paint paint;
	
	protected long level;
	protected List<GameActor> children;
	
	public enum ActorStatus{
		Action,
		Dead,
		IsFace,
		IsFlying,
		IsShooting,		//此状态多余
		IsBreak,
	}
	public ActorStatus status;
	
	
	
	public GameActor() {
		this("no name");
	}
	
	public GameActor(String name) {
		this.name = name;
		this.status = ActorStatus.Action;
		children = new ArrayList<GameActor>();
	}
	
	public void logic(long elapsedTime) {
		for(GameActor actorChild : children){		//增强的for循环
			if(actorChild.status != ActorStatus.Dead)
				actorChild.logic(elapsedTime);
		}
		
	}
	
	public void myDraw(Canvas canvas) {
		for(GameActor actorChild : children)
			if(actorChild.status != ActorStatus.Dead)
				actorChild.myDraw(canvas);
	}
	
	public void addChild(GameActor actor) {
		children.add(actor);
		actor.level = level + 1;
	}
	
	public GameActor search(String name) {
		if(this.name == name)
			return this;
		
		for(GameActor actorChild : children)
			if(actorChild.name == name)
				return actorChild;
		
		return null;
	}
	
	boolean changeName(String name) {
		this.name = name;
		return true;
	}
	
	public void changeStatus(ActorStatus status) {
		this.status = status;
	}
	
	ActorStatus checkStatus(){
		return this.status;
	}
	
	ActorStatus checkStatus(GameActor actor) {
		return actor.status;
	}
	
	float getX() {
		return actorX;
	}
	
	float getY() {
		return actorY;
	}
	
	public void cleanUpDead(){
		if(this.status == ActorStatus.Dead){
			for(GameActor actorChild : children)
				actorChild.status = ActorStatus.Dead;
			children.clear();
		}
		for(GameActor actorChild : children)
			if(actorChild.status == ActorStatus.Dead)
				actorChild.children.clear();
	}
	
}
