package com.puzzle.actor;

public class ArcNode {
	Bullet bullet;
	ArcNode nextArc;
	
	
	
	public ArcNode(Bullet bullet) {
		nextArc = null;
		this.bullet = bullet;
	}
	
	boolean hasNext() {
		if(nextArc != null)
			return true;
		return false;
	}
	
	ArcNode nextArc() {
		return nextArc;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof ArcNode)
			if(((ArcNode) o).bullet == bullet)
				return true;
		
		return super.equals(o);
	}
	
}
