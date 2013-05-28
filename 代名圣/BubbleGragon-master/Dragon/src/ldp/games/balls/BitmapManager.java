package ldp.games.balls;

import ldp.games.dragon.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class BitmapManager {
     public Bitmap color1;
     public Bitmap color2;
     public Bitmap color3;
     public Bitmap color4;
     public Bitmap color5;
     public Bitmap color6;
     private static BitmapManager bitmapManager = null;
     
     private BitmapManager(Context context){
    	 color1 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.color1)).getBitmap();
    	 color2 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.color2)).getBitmap();
    	 color3 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.color3)).getBitmap();
    	 color4 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.color4)).getBitmap();
    	 color5 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.color5)).getBitmap();
    	 color6 = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.color6)).getBitmap();
    	   
    }
     
     public static synchronized BitmapManager getBitmapManager(Context context){
    	 if(bitmapManager == null)
   		     bitmapManager = new BitmapManager(context);
   	     return bitmapManager;
     }
    
}
