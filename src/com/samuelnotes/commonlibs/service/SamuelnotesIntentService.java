package com.samuelnotes.commonlibs.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.samuelnotes.commonlibs.utils.L;

public class SamuelnotesIntentService extends IntentService {

	private Context mContext;
	
	public static final String SAMUELNOTES_INTENTSERVICE_CLASSNAME = "com.samuelntoes.commonlibs.service.SamuelnotesIntentService";
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.mContext = this.getApplicationContext();
		L.d(getClass(), "IportalIntentService is onCreate");
	}
	public SamuelnotesIntentService(){
		this(SamuelnotesIntentService.class.getSimpleName());
	}

	public SamuelnotesIntentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		L.d(getClass(), "IportalIntentService is onHandleIntent");
//		JsonAccount  httpRequestTools = new JsonAccount();
//		sendBroadCastReceiver(httpRequestTools.getAppNews());
	}

//	private void sendBroadCastReceiver(GetAPPNewsResult appNewsResult) {
//		Intent intent = new Intent();
//		intent.setAction(AppContants.SYS_ACTION.REQUEST_NEWS_ACTION);
//		intent.putExtra(AppContants.SYS_ACTION.NEWS_BEAN, appNewsResult);
//		mContext.sendBroadcast(intent);
//	}

	public static void runIportalIntentService(Context context, Intent intent ,String className){
		intent.setClassName(context, className);
		context.startService(intent);
	}
	
}
