package com.akaya.puloyunu;

public class PermanentScoreHolder {
	
	public static boolean storeScore(String prefName,float score)
	{
		float temp = VarHolder.SHARED_PREFERENCES.getFloat(VarHolder.SUFFIX_PREFERENCES+prefName, 0f);
		if(temp<score)
		{
			VarHolder.EDITOR.putFloat(VarHolder.SUFFIX_PREFERENCES+prefName, score);
			VarHolder.EDITOR.commit();
			return true;
		}
		return false;
	}
	public static float getScore(String prefName)
	{
		return VarHolder.SHARED_PREFERENCES.getFloat(VarHolder.SUFFIX_PREFERENCES+prefName, 0f);
	}	
}
