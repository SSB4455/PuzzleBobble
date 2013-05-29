package com.puzzle;

import android.app.Activity;
import android.os.Bundle;

public class PuzzleBobbleActivity extends Activity {
	
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(new MyGameSurfaceView(this));
        
        /* 完全没用
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        int nowWidth = dm.widthPixels;//获得控件的原始宽度
        int nowheight = dm.heightPixels;//获得控件的原始高度
        int density = (int) dm.density;//获得真机的像素密度
        //乘以手机的密度是因为每一个手机存在差异，
        screenW = (int) (nowWidth * density);//获得当前手机的宽度
        screenH = (int) (nowheight * density);//获得当前手机的高度
        Log.i("PuzzleBobbleActivity", "screenW = " + screenW + ", screenH = " + screenH);
        */
    }
    
    public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.exit(0);
	}
    
}