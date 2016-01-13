package com.akaya.puloyunu;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.util.Log;

public class BackgroundThread extends Thread {
	public  CustomeSurface games;
	Canvas c;
	long time1=0,time2=0;
	int sleep=VarHolder.TIME_THRUSHHOLD;
	public BackgroundThread(CustomeSurface games) {
		this.games=games;
	}
	@SuppressLint("WrongCall")
	@Override
	public void run() {
		super.run();
		
		while(!VarHolder.IS_APPLICATION_PAUSE){
			
			if(!VarHolder.IS_GAME_PAUSE && games!=null){
				c = games.getHolder().lockCanvas();
				
				if(time2-time1>VarHolder.TIME_THRUSHHOLD){
					sleep=(int) (time2-time1);
				}else{
					sleep = VarHolder.TIME_THRUSHHOLD;
				}
				Log.e("BackgroundThreah", sleep+"");
				if(c!=null){
					
					try {
						Thread.sleep(sleep);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					synchronized (games.getHolder()) {
						time1=System.currentTimeMillis();
						games.onDraw(c);
						time2=System.currentTimeMillis();
					}	
					games.getHolder().unlockCanvasAndPost(c);
				}
			}
		}
		//Start Game Over Activity
		/*
		Intent i = new Intent(games.getContext(),EndGameActivity.class);
		games.getContext().startActivity(i);
		*/
		
	}

}
