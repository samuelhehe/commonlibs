package com.samuelnotes.commonlibs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppConfig {
	
	public final static boolean DEBUG = true;
	private static AppConfig appConfig;
	private Context mContext;
	public final static String CONF_APP_UNIQUEID = "APP_UNIQUEID";
	private final static String APP_CONFIG = "config";
	public final static String CONF_VOICE = "perf_voice";
	public final static String CONF_LOAD_IMAGE = "perf_loadimage";
	public final static String CONF_CHECKUP = "perf_checkup";
	
	
	public static AppConfig getAppConfig(Context context)
	{
		if(appConfig == null){
			appConfig = new AppConfig();
			appConfig.mContext = context;
		}
		return appConfig;
	}
	

	public static SharedPreferences getSharedPreferences(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static boolean isLoadImage(Context context)
	{
		return getSharedPreferences(context)
				.getBoolean(CONF_LOAD_IMAGE, true);
	}	
	
}
