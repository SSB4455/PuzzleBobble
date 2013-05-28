package ldp.games.balls;

import android.graphics.Canvas;

public abstract class AbstractManager {
	
	public static final int[][] REVISE_COORX = {{20, 60, 100, 140, 180, 220, 260, 300}, {40, 80, 120, 160, 200, 240, 280}};
	//public static final int[] REVISE_ODD_COORX = {40, 80, 120, 160, 200, 240, 280};
	
	public abstract void DrawBalls(Canvas canvas);
    public abstract void SetBallTan(float X, float Y);
}
