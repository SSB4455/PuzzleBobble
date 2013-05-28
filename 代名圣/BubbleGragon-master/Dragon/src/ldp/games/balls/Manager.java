package ldp.games.balls;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import ldp.games.dragon.GameView;

import android.R;
import android.R.color;
import android.R.integer;
import android.R.string;
import android.content.Context;
import android.graphics.Canvas;
import android.inputmethodservice.Keyboard.Key;
import android.util.Log;

public class Manager extends AbstractManager {
	
	Context context;
	AbstractBall current_ball;
	HashMap<String, AbstractBall> ballMap;
	Map<String, String> stayBallMap;  //保存可以留下的ball的在ballMap中的索引
	Map<String, String> dropingMap;  //保存要掉落的ball的在ballMap中的索引
	Map<String, AbstractBall> dropedballMap;
	
	int current_color;
	
	int[][] gameMap;
	int stop_row_index;
	int stop_col_index;
	
//	int times = 0;
	
	private Manager(/*Context context*/) {
    	current_ball = Balls.BallFactory.GetBall(GetNewBallColor());
	    ballMap = new HashMap<String, AbstractBall>();
	    dropingMap = new HashMap<String, String>();
	    stayBallMap = new HashMap<String, String>();
	    dropedballMap = new HashMap<String, AbstractBall>();
	    initgameMap();
	}

	private void initgameMap() {
		gameMap = new int[11][15];
		for(int i=0; i<11; i++)
			for(int j=0; j<15; j++)
				gameMap[i][j] = Balls.COLOR_NULL;
	}

	@Override
	public void DrawBalls(Canvas canvas) {
		
		if(IsCurrBallStop() == false){
			current_ball.DrawSelf(canvas);
		}
		else{
			//产生一个新的ball, 并且将就的ball放入HashMap
			ReviseBallCoor();
			ballMap.put(""+stop_row_index+":"+""+stop_col_index, current_ball);
			gameMap[stop_row_index][stop_col_index] = current_ball.ball_color;
			current_color = current_ball.ball_color;
		   
		    dropingMap.put(""+stop_row_index+":"+""+stop_col_index, ""+stop_row_index+":"+""+stop_col_index);
			GetDropingBalls(stop_row_index, stop_col_index);
		   // String string;
		   // string.equalsIgnoreCase(string)
			if(dropingMap.size() >= 3)
			    RemoveDropingBall();
			stayBallMap.clear();
			dropingMap.clear();
			
			current_ball = Balls.BallFactory.GetBall(GetNewBallColor());
			GameView.ismoved_flag = false;
			
		}
		
		for(String key : ballMap.keySet()){
			ballMap.get(key).DrawSelf(canvas);
		}
		DrawDroping(canvas);
	}
	
    private void DrawDroping(Canvas canvas) {
		Map<String, String> tempMap = new HashMap<String, String>();
		for(String key : dropedballMap.keySet()){
		    dropedballMap.get(key).Vertical_Speed = 7.0;
			dropedballMap.get(key).DrawSelf(canvas);
			dropedballMap.get(key).Move();
			if(dropedballMap.get(key).CoorY>480)
				tempMap.put(key, key);
		}
		for(String key : tempMap.keySet()){
			dropedballMap.remove(key);
		}
		
		
		
	}

	//返回新的求的颜色，由随机数产生器产生
    private int GetNewBallColor() {
		int color = new Random().nextInt(6)+1; 
		return color;
	}

	//recursion
    private void GetDropingBalls(int ROW, int COL){
		int temprow = ROW-1;
		int tempcol = COL-1;
		String temp;
		int times = 0;
		
		while(times < 3){
			tempcol = temprow == ROW ? COL-2 : COL-1;
			int Maxcol = temprow == ROW ? COL+2 : COL+1;
			for(; tempcol<=Maxcol; tempcol+=2){
				if(temprow >= 0 && temprow <= 10 && tempcol >=0 && tempcol<=14){
					temp = ""+temprow+":"+tempcol+"";
					if(gameMap[temprow][tempcol] != Balls.COLOR_NULL && gameMap[temprow][tempcol] == current_color){
						if(!dropingMap.containsKey(temp)){
							dropingMap.put(temp, temp);
							GetDropingBalls(temprow, tempcol);
						}
					}
				}
			}
	    	
			
			temprow++;
		    times++;
		}
		
		
	  
	  
	}

	private void RemoveDropingBall() {
		
	 
		Removedropball();
		dropingMap.clear();
		for(int j=0; j<=14; j+=2){
			if(gameMap[0][j] != Balls.COLOR_NULL)
				RemoveLeftBall(0, j);
		}
	    Log.e("stayballmap",""+stayBallMap.size());
	    for(String key : ballMap.keySet()){
			if(!stayBallMap.containsKey(key)){
				dropingMap.put(key, key);
			}
		}
	    Removedropball();
	    dropingMap.clear();
	}
	
	private void Removedropball(){
		 for(String key : dropingMap.keySet()){
				dropedballMap.put(key, ballMap.remove(key));
			    String[] temp = key.split(":");
			    int row = Integer.parseInt(temp[0]);
			    int col = Integer.parseInt(temp[1]);
			    gameMap[row][col] = Balls.COLOR_NULL;
			}
	}

	private void RemoveLeftBall(int ROW, int COL) {
		int temprow = ROW-1;
		int tempcol = COL-1;
		String temp;
		int times = 0;
	
			while(times < 3){
				tempcol = temprow == ROW ? COL-2 : COL-1;
				int Maxcol = temprow == ROW ? COL+2 : COL+1;
				for(; tempcol<=Maxcol; tempcol+=2){
					if(temprow >= 0 && temprow <= 10 && tempcol >=0 && tempcol<=14){
						temp = ""+temprow+":"+tempcol+"";
						if(gameMap[temprow][tempcol] != Balls.COLOR_NULL){
							if(!stayBallMap.containsKey(temp)){
								stayBallMap.put(temp, temp);
						        Log.e(temp,temp);
								RemoveLeftBall(temprow, tempcol);
							}
						}
					}
				}
		    	
				
				temprow++;
				times++;
			}
		
	}
	
		
	
  
	//detect whether the current moving ball is need to stop 
	private boolean IsCurrBallStop() {
		AbstractBall ball;
		double xdis, ydis, distance;
		current_ball.Move();
		if(current_ball.CoorY <= 22.0)
			return true;
		for(String key : ballMap.keySet()){
			ball = ballMap.get(key);
			xdis = Math.abs(ball.CoorX - current_ball.CoorX);
			ydis = Math.abs(ball.CoorY - current_ball.CoorY);
			distance = Math.sqrt(xdis*xdis + ydis*ydis);
			if(distance <= 40)
				return true;
		}
		return false;
	}
	
	
	//before the current_ball stops moving and be added to the hashmap, revise its CoorX and CoorY
	private void ReviseBallCoor(){
		ReviseCoorY();
	    int temp = (int)current_ball.CoorY / 35;
	    stop_row_index = temp;
	    int index = temp%2 == 0 ? 0 : 1;
	    int during = index == 0 ? 8 : 7;
	    
	    double minlength=320;
	    int minindex=0;
	    for(int j=0; j<during; j++){
	    	double templingth = Math.abs(AbstractManager.REVISE_COORX[index][j] - current_ball.CoorX);
	    	if(templingth < minlength){
	    		minlength = templingth;
	    		minindex = j;
	    	}
	    }
	    current_ball.CoorX = AbstractManager.REVISE_COORX[index][minindex];
	    stop_col_index = index == 0 ? (minindex*2) : (minindex*2+1);
	    //Set_Stop_col(index);
	  
	}

	private void ReviseCoorY() {
		if(current_ball.CoorY < 40)
			current_ball.CoorY = 20;
		else if(current_ball.CoorY < 70){
			current_ball.CoorY = 55;
			
		}
		else if(current_ball.CoorY < 105){
			current_ball.CoorY = 90;
		}
		else if(current_ball.CoorY < 140){
			current_ball.CoorY = 125;
		}
		else if(current_ball.CoorY < 175){
			current_ball.CoorY = 160;
		}
		else if(current_ball.CoorY < 210){
			current_ball.CoorY = 195;
		}
		else if(current_ball.CoorY < 245){
			current_ball.CoorY = 230;
		}
		else if(current_ball.CoorY < 280){
			current_ball.CoorY = 265;
		}
		else if(current_ball.CoorY < 315){
			current_ball.CoorY = 300;
		}
		else if(current_ball.CoorY < 350){
			current_ball.CoorY = 335;
		}
		else if(current_ball.CoorY < 385){
			current_ball.CoorY = 370;
		}
		
		
			
		
	}

	@Override
	public void SetBallTan(float X, float Y) {
		double Xpinfang = (current_ball.CoorX - X) * (current_ball.CoorX - X);
		double Ypinfang = (Y - current_ball.CoorY) * (Y - current_ball.CoorY);
		double xiebian = Xpinfang + Ypinfang;
		double sqrt = Math.sqrt(xiebian);
		
		current_ball.Speed = AbstractBall.DEFAULT_SPEED;
		if(Y - current_ball.CoorY > Math.abs(X - current_ball.CoorX)){
			current_ball.Vertical_Speed = current_ball.Speed * (Y - current_ball.CoorY) / sqrt;
			current_ball.Horizonal_Speed = Math.sqrt(current_ball.Speed * current_ball.Speed  - current_ball.Vertical_Speed * current_ball.Vertical_Speed);
		}
		else{
			current_ball.Horizonal_Speed = current_ball.Speed * Math.abs(X - current_ball.CoorX) / sqrt;
			current_ball.Vertical_Speed = Math.sqrt(current_ball.Speed * current_ball.Speed  - current_ball.Horizonal_Speed * current_ball.Horizonal_Speed);
		}
		
		current_ball.Vertical_Speed *= -1;
		if(X < current_ball.CoorX){
			current_ball.Horizonal_Speed *= -1;
		}
		
	
		
	}
	
	
	


	public static class ManagerFactory{
		
		private static AbstractManager abstractManager;
		
		public static AbstractManager GetManager(/*Context context*/){
			if(abstractManager == null)
				abstractManager = new Manager(/*context*/);
			return abstractManager;
			
		}
	}


}
