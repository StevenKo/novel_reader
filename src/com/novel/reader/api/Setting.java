package com.novel.reader.api;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;

public class Setting {
//	public static int textSize;
//	public static int textLanguage; // 0 for 繁體, 1 for 簡體
//	public static int readingDirection; // 0 for 直向, 1 for 橫向
//	public static int clickToNextPage; // 0 for yes, 1 for no
//	public static int stopSleeping;  // 0 for yes, 1 for no
	
	public final static String keyRemindLeaving = "Leaving";
	public final static Boolean initialReindLeaving = true;
	
	public final static String keyPref = "pref";
	public final static String keyTextSize = "TextSize";
	public final static String keyTextLanguage = "TextLanguage";
	public final static String keyReadingDirection = "ReadingDirection";
	public final static String keyClickToNextPage = "ClickToNextPage";
	public final static String keyStopSleeping = "StopSleeping";
	public final static String keyOpenDownloadPage = "OpenDownloadPage";
	public final static String keyTextColor= "TextColor";
	public final static String keyTextBackground = "TextBackground";
	public final static String keyAppTheme = "AppTheme";
	
	public final static int initialTextSize = 20; // textsize in pixel
	public final static int initialTextLanguage = 0;
	public final static int initialReadingDirection = 0;
	public final static int initialClickToNextPage = 1;
	public final static int initialStopSleeping = 1;
	public final static int initialOpenDownloadPage = 0;
	public final static int initialTextColor = -16777216;
	public final static int initialTextBackground = -1;
	public final static int initialAppTheme = 0; // 0 for 亮白, 1 for 灰黑
	
	private static final HashMap<String,Integer> initMap = new HashMap<String,Integer>(){
		{
			put(keyTextSize,initialTextSize);
			put(keyTextLanguage,initialTextLanguage);
			put(keyReadingDirection,initialReadingDirection);
			put(keyClickToNextPage,initialClickToNextPage);
			put(keyStopSleeping,initialStopSleeping);
			put(keyOpenDownloadPage,initialOpenDownloadPage);
			put(keyTextColor, initialTextColor);
			put(keyTextBackground, initialTextBackground);
			put(keyAppTheme, initialAppTheme);
		}
	};
	
	
	public static int getSetting(String settingKey,Context context){
		SharedPreferences sharePreference = context.getSharedPreferences(keyPref, 0);
		int settingValue = sharePreference.getInt(settingKey, initMap.get(settingKey));
		return settingValue;
	}
	
	public static void saveSetting(String settingKey,int settingValue, Context context){
		SharedPreferences sharePreference = context.getSharedPreferences(keyPref, 0);
		sharePreference.edit().putInt(settingKey, settingValue).commit();
	}
	
	public static Boolean getSettingRemind(Context context){
		SharedPreferences sharePreference = context.getSharedPreferences(keyPref, 0);
		Boolean settingValue = sharePreference.getBoolean(keyRemindLeaving, initialReindLeaving);
		return settingValue;
	}
	
	public static void saveSettingRemind(Boolean value,Context context){
		SharedPreferences sharePreference = context.getSharedPreferences(keyPref, 0);
		sharePreference.edit().putBoolean(keyRemindLeaving, value).commit();
	}
	

}
