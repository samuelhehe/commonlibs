package com.samuelnotes.commonlibs;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * Ӧ�ó�������࣬�������е�Activity��Ӧ�ó�����˳�
 *
 */
public class AppManager {
	
	private Stack<Activity> activityStack;
	
	/**
	 * ����ģʽ
	 * @return
	 */
	private static AppManager appManagerInstance;
	
	private AppManager(){}
	
	public static AppManager getAppManager(){
		if(appManagerInstance == null){
			return  new AppManager();
		}
		return appManagerInstance;
	}
	
	/**
	 * ��ӵ�ǰActivity ����ջ
	 * @param activity
	 */
	public void addActivity(Activity activity){
		if(activityStack == null){
				activityStack = new Stack<Activity>();
		}
		activityStack.push(activity);
	}

	/**
	 * ��ȡ��ǰ��Activity ����ջ�е����һ��ѹ��ģ�
	 * @return
	 */
	public Activity getCurrentActivity(){
		return activityStack.lastElement();
	}
	
	/**
	 * ���ٵ�ǰ��Activity
	 * @param activity
	 */
	public void finishActivity(){
		finishActivity(activityStack.lastElement());
	}
	
	/**
	 * ����ָ����Activity
	 */
	public void finishActivity(Activity activity) {
		if(activity !=null ){
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * ����ָ����Activity
	 * @param clazz
	 */
	public void finishActivity(Class<?> clazz){
		for (Activity activity : activityStack){
			if(activity.getClass().equals(clazz)){
				finishActivity(activity);
			}
		}
	}
	
	public void finishAllActivity(){
		for(Activity activity : activityStack){
			finishActivity(activity);
		}
		activityStack.clear();
	}

	/**
	 * �˳�Ӧ�ó���
	 */
	@SuppressWarnings("deprecation")
	public void ExitApp(Context context){
		finishAllActivity();
		ActivityManager activityMgr = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		activityMgr.restartPackage(context.getPackageName());
		System.exit(0);
	}
}
