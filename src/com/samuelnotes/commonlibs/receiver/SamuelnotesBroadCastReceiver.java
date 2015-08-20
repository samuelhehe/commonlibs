package com.samuelnotes.commonlibs.receiver;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.baidu.navisdk.ui.routeguide.subview.L;
import com.samuelnotes.commonlibs.tools.NetWorkTools;

public class SamuelnotesBroadCastReceiver extends BroadcastReceiver {
	public static ArrayList<EventHandler> mListeners = new ArrayList<EventHandler>();

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (TextUtils.equals(action, ConnectivityManager.CONNECTIVITY_ACTION)) {
			boolean flag = NetWorkTools.isNetworkAvailable(context);
			// AppSharedPreference.setCurrentNetworkState(context, flag);
			if (mListeners.size() > 0)
				for (EventHandler handler : mListeners) {
					handler.onNetworkChangeEventHandler(intent, flag);
				}
		} else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
			
		} 
	}

	public static interface EventHandler {

		public abstract void onNetworkChangeEventHandler(Intent intent,
				boolean networkstate);

		public abstract void onMessageReceiverEventHandler(Intent intent);

	}
}
