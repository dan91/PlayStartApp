package models;

import play.Logger;

public class Helper {
	public static float getBarWidth(int first, int sec) {
		if(sec == 0)
			return (float) 0;
		float res = (float) ((first*100) / sec);
		Logger.info("first: "+first+", sec:"+sec+", res:"+res);
		return res;
	}
}
