package com.samuelnotes.commonlibs.service;

import android.content.Context;
import android.content.Intent;

/**
 * Service manager
 * 
 * @author superuser
 *
 */
public class ServiceManager {

	private Context context;

	public ServiceManager(Context context) {
		this.context = context;
	}

	/**
	 * start SamuelnotesIntentService
	 */
	public void startIportalIntentService() {

		Thread startIportalIntentService = new Thread(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent();
				SamuelnotesIntentService.runIportalIntentService(context, intent,SamuelnotesIntentService.SAMUELNOTES_INTENTSERVICE_CLASSNAME);
			}
		});
		startIportalIntentService.start();
	}

}
