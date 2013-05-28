package ldp.games.dragon;


import ldp.games.balls.AbstractManager;
import ldp.games.balls.BitmapManager;
import ldp.games.balls.Manager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;

public class GameView extends View {
	public static BitmapManager bitmapManager;
	AbstractManager ballsManager;
	public static boolean ismoved_flag = false;  //indicates whether the ball can move or not
//	GameThread gameThread;
	
	public GameView(Context context) {
		super(context);
		bitmapManager = BitmapManager.getBitmapManager(context);
		ballsManager = Manager.ManagerFactory.GetManager(/*this.getContext()*/);
		
	}

	public void StartGame(){
		new Thread(new GameThread()).start();
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.GRAY);
		ballsManager.DrawBalls(canvas);
		
		super.onDraw(canvas);
	}
	
	
    private class GameThread implements Runnable{

		@Override
		public void run() {
			while(true){
					try{
						
						Thread.sleep(10);
						
					}catch (Exception e) {
						// TODO: handle exception
					}
				
					postInvalidate();
				}
		}
    	
    }
}
