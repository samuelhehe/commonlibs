package com.samuelnotes.commonlibs.aty;

import java.io.Serializable;

import android.app.Activity;
import android.os.Bundle;

import com.samuelnotes.commonlibs.AppManager;

public abstract class BaseActivity extends Activity {
	private AppManager appManager = AppManager.getAppManager();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appManager.addActivity(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		appManager.finishActivity(this);
	}
	
	/**
     * Get serializable extra from activity's intent
     *
     * @param name
	 * @return 
     * @return extra
     */
    @SuppressWarnings("unchecked")
    protected <T extends Serializable> T getSerializableExtra(final String name) {
            return (T) getIntent().getSerializableExtra(name);
    }

    /**
     * Get string extra from activity's intent
     *
     * @param name
     * @return extra
     */
    protected String getStringExtra(final String name) {
            return getIntent().getStringExtra(name);
    }
	
	protected abstract void initViews();
	protected abstract void initEvents();
		
}
