package ldp.games.balls;

import ldp.games.dragon.GameView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

public class Balls extends AbstractBall {
	
//	Bitmap color;
	
	public Balls(int BallsColor){
	//	GetColor(BallsColor);
		this.CoorX = 160;
		this.CoorY = 400;
		this.Speed = 2;
		this.ball_color = BallsColor;
	}
	
	private Bitmap GetColor(int BallsColor){
		switch(BallsColor){
		case COLOR_ONE:
			return GameView.bitmapManager.color1;
			
		case COLOR_TWO:
			return GameView.bitmapManager.color2;
			
		case COLOR_THREE:
			return GameView.bitmapManager.color3;
			
		case COLOR_FOUR:
			return GameView.bitmapManager.color4;
			
		case COLOR_FIVE:
			return GameView.bitmapManager.color5;
			
		case COLOR_SIX:
			return GameView.bitmapManager.color6;
			
		}
		return null;
	}

	@Override
	public void DrawSelf(Canvas canvas) {
		
		canvas.drawBitmap(GetColor(ball_color), (int)(this.CoorX-20), (int)(this.CoorY-20), null);
		
	}
	
	
	@Override
	public void Move() {
		this.CoorX += this.Horizonal_Speed;
		this.CoorY += this.Vertical_Speed;
		if(this.CoorX >= 298 || this.CoorX <= 22){
			this.Horizonal_Speed *= -1;
		}
	}


	
	 public static class BallFactory{
	    	
		 public static AbstractBall GetBall(int BallColor){
				return new Balls(BallColor);
	    		
	     }
	 }




	

	
	
}
