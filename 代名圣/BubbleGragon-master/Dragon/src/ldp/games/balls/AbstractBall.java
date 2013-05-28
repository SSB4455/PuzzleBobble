package ldp.games.balls;

import android.graphics.Canvas;

public abstract class AbstractBall {
	public static final int COLOR_NULL = 0;
	public static final int COLOR_ONE = 1;
	public static final int COLOR_TWO = 2;
	public static final int COLOR_THREE = 3;
	public static final int COLOR_FOUR = 4;
	public static final int COLOR_FIVE = 5;
	public static final int COLOR_SIX = 6;
	
	public static final double DEFAULT_SPEED = 8.0;
	public static final int LEFT_RANGE = 20;
	public static final int RIGHT_RANGE = 300;
	public static final int TOP_RANGE = 20;
	
	public int ball_color;
	public double CoorX;
	public double CoorY;
	
	
	public double Speed;
	public double Horizonal_Speed;  //向左为负的，向右为正的
	public double Vertical_Speed;   //永远向上，而且为负的
	
	public abstract void DrawSelf(Canvas vCanvas);
	public abstract void Move();
	//public abstract void DropDown();
 
}
