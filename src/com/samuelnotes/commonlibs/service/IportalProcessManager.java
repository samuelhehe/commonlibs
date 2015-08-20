package com.samuelnotes.commonlibs.service;

import java.util.ArrayList;
import java.util.concurrent.Future;

import com.samuelnotes.commonlibs.utils.L;

public class IportalProcessManager {

	private IportalService iportalService;

	private ArrayList<Runnable> taskList;
	private boolean running = false;
	private Future<?> futureTask;
	private IportalService.TaskSubmitter taskSubmitter;

	private IportalService.TaskTracker taskTracker;


	public IportalProcessManager(IportalService iportalService) {
		this.iportalService = iportalService;
		taskList = new ArrayList<Runnable>();
		taskTracker = iportalService.getTaskTracker();
		taskSubmitter = iportalService.getTaskSubmitter();
	}

	public void startCheckProcess() {
	}

	String lastUpdateTime;
	String lastCheckTime;


	private class CheckUpdateTask implements Runnable {
		final IportalProcessManager iportalProcessManager;

		private CheckUpdateTask() {
			this.iportalProcessManager = IportalProcessManager.this;
		}

		public void run() {
			L.i(this.getClass(), "CheckUpdateTask.run()...");
			try {
			} catch (Exception e) {
				// L.d(this.getClass(), e.printStackTrace());
				e.printStackTrace();
				iportalProcessManager.removeTask(1);
			} finally {
				L.d("runTask (", "runTask");
				iportalProcessManager.runTask();
			}
		}
	}

	public void runTask() {
		L.d(this.getClass(), "runTask()...");
		synchronized (taskList) {
			running = false;
			futureTask = null;
			if (!taskList.isEmpty()) {
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		L.d(this.getClass(), "runTask()...done");
	}

	public boolean isNeedCheckUpdate() {
		return true;
	}

	public void addTask(Runnable runnable) {
		L.d(this.getClass(), "addTask(runnable)...");
		taskTracker.increase();
		synchronized (taskList) {
			if (taskList.isEmpty() && !running) {
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null) {
					taskTracker.decrease();
				}
			} else {
				taskList.add(runnable);
			}
		}
		L.d(this.getClass(), "addTask(runnable)... done");
	}

	public void removeTask(int dropCount) {
		synchronized (taskList) {
			if (taskList.size() >= dropCount) {
				for (int i = 0; i < dropCount; i++) {
					taskList.remove(0);
					taskTracker.decrease();
				}
			}
		}
	}

}
