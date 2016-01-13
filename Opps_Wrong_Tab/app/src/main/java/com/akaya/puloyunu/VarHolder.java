package com.akaya.puloyunu;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;


public class VarHolder {
	
	public static int LOADING_BITMAP_COUNT;
	public static boolean EXIT_APPLICATION;
	public static boolean customeSurfaceBitmapLoaded;
	public static int SCREEN_WIDTH;
	public static int SCREEN_HEIGHT;
	public static int TIME_THRUSHHOLD=50;
	public static Bitmap TEMP;
	public static boolean IS_APPLICATION_PAUSE=false;
	public static boolean IS_GAME_PAUSE=true;
	public static boolean IS_TOUCHED=false;
	public static boolean FIRST_TOUCH=false;
	public static SharedPreferences SHARED_PREFERENCES;
	public static Editor EDITOR;
	
	public static long SCORE = 0;
	public static long LAST_SCORE=0;
	public static boolean FIRST;

	public static long GAME_TYPE = 1;
	public static String PREFERENCES="com.initedit.pianotiles.1";
	public static String SUFFIX_PREFERENCES="value";
	public static class Acceleration {
		public static float currX=0;
		public static float currY=0;
		public static float currZ=0;
		public static float lastX=0;
		public static float lastY=0;
		public static float lastZ=0;
		public static float deltaX=0;
		public static float deltaY=0;
		public static float deltaZ=0;
		
		public static float xFactor = 10;
		public static float yFactor = 2;
		public static float zFactor = 2;
	}
	public static class Touch {
		public static boolean IS_TOUCHING=false; 
		public static float X=0;
		public static float Y=0;
	}	
}
