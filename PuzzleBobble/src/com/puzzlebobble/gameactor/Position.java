package com.puzzlebobble.gameactor;

public class Position {
	float x, y;

	
	
	public Position(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	float distance2(Position p) {
		return (x - p.x) * (x - p.x) + (y - p.y) * (y - p.y);
	}
	
	double distance(Position p) {
		return Math.sqrt(distance2(p));
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		
		if(o instanceof Position)
			if(((Position) o).x == x && ((Position) o).y == y)
				return true;
		
		return super.equals(o);
	}
	
}
