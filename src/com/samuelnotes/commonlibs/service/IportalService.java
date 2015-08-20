package com.samuelnotes.commonlibs.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.samuelnotes.commonlibs.receiver.SamuelnotesBroadCastReceiver;
import com.samuelnotes.commonlibs.receiver.SamuelnotesBroadCastReceiver.EventHandler;
import com.samuelnotes.commonlibs.tools.NetWorkTools;
import com.samuelnotes.commonlibs.utils.L;
import com.samuelnotes.commonlibs.utils.MThreadFactory;

public class IportalService extends Service implements EventHandler {

	private ExecutorService processTaskService;

	private TaskSubmitter taskSubmitter;

	private TaskTracker taskTracker;

	private AlarmManager mAlarmManager;

	private PendingIntent mPendingIntent;

	private boolean currentNetworkstate;

	private IportalProcessManager iportalprocessmanager;

	@Override
	public void onCreate() {
		super.onCreate();
		taskSubmitter = new TaskSubmitter(this);
		taskTracker = new TaskTracker(this);
		iportalprocessmanager = new IportalProcessManager(this);
		processTaskService = (ExecutorService) MThreadFactory
				.getExecutorService();
		SamuelnotesBroadCastReceiver.mListeners.add(this);
		currentNetworkstate = NetWorkTools.isNetworkAvailable(this);
		protectServiceProcess();

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		iportalprocessmanager.startCheckProcess();
		return Service.START_STICKY;
	}

	@Override
	public void onNetworkChangeEventHandler(Intent intent, boolean networkstate) {
		currentNetworkstate = networkstate;
		if (!networkstate) {// 如果是网络断开，不作处理
			L.w(getClass(), "network is error ");

			return;
		} else {
			iportalprocessmanager.startCheckProcess();
		}
	}

	@Override
	public void onMessageReceiverEventHandler(Intent intent) {
		// TODO Auto-generated method stub

	}

	private void protectServiceProcess() {
		Intent intent = new Intent(getApplicationContext(),
				IportalService.class);
		mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		mPendingIntent = PendingIntent.getService(this, 0, intent,
				Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 60 * 1000,
				mPendingIntent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (getProcessTaskService() != null
				&& getProcessTaskService().isTerminated()) {
			getProcessTaskService().shutdown();
		}
	}

	/**
	 * @return the processTaskService
	 */
	public ExecutorService getProcessTaskService() {
		return processTaskService;
	}

	/**
	 * @param processTaskService
	 *            the processTaskService to set
	 */
	public void setProcessTaskService(ExecutorService processTaskService) {
		this.processTaskService = processTaskService;
	}

	/**
	 * @return the taskSubmitter
	 */
	public TaskSubmitter getTaskSubmitter() {
		return taskSubmitter;
	}

	/**
	 * @param taskSubmitter
	 *            the taskSubmitter to set
	 */
	public void setTaskSubmitter(TaskSubmitter taskSubmitter) {
		this.taskSubmitter = taskSubmitter;
	}

	/**
	 * @return the taskTracker
	 */
	public TaskTracker getTaskTracker() {
		return taskTracker;
	}

	/**
	 * @param taskTracker
	 *            the taskTracker to set
	 */
	public void setTaskTracker(TaskTracker taskTracker) {
		this.taskTracker = taskTracker;
	}

	/**
	 * @return the currentNetworkstate
	 */
	public boolean isCurrentNetworkOK() {
		return currentNetworkstate;
	}

	/**
	 * @param currentNetworkstate
	 *            the currentNetworkstate to set
	 */
	public void setCurrentNetworkstate(boolean currentNetworkstate) {
		this.currentNetworkstate = currentNetworkstate;
	}

	/**
	 * Class for summiting a new runnable task.
	 */
	public class TaskSubmitter {

		final IportalService iportalService;

		public TaskSubmitter(IportalService notificationService) {
			this.iportalService = notificationService;
		}

		public Future submit(Runnable task) {
			Future result = null;
			if (!iportalService.getProcessTaskService().isTerminated()
					&& !iportalService.getProcessTaskService().isShutdown()
					&& task != null) {
				result = iportalService.getProcessTaskService().submit(task);
			}
			return result;
		}

	}

	/**
	 * Class for monitoring the running task count.
	 */
	public class TaskTracker {

		final IportalService iportalService;

		public int count;

		public TaskTracker(IportalService notificationService) {
			this.iportalService = notificationService;
			this.count = 0;
		}

		public void increase() {
			synchronized (iportalService.getTaskTracker()) {
				iportalService.getTaskTracker().count++;
				L.d(this.getClass(), "Incremented task count to " + count);
			}
		}

		public void decrease() {
			synchronized (iportalService.getTaskTracker()) {
				iportalService.getTaskTracker().count--;
				L.d(this.getClass(), "Decremented task count to " + count);
			}
		}

	}
}
