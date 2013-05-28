package ldp.games.dragon;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class DragonActivity extends Activity {
    /** Called when the activity is first created. */
	
	 GameView gameView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
        gameView.StartGame();
      //  gameView
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			float X = event.getX();
			float Y = event.getY();
			
			if(GameView.ismoved_flag == false){
				gameView.ballsManager.SetBallTan(X, Y);
				GameView.ismoved_flag = true;
			}
		}
		return super.onTouchEvent(event);
	}
}