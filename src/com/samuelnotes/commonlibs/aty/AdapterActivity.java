package com.samuelnotes.commonlibs.aty;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

import com.samuelnotes.commonlibs.utils.EmptyViewUtils;

public abstract class AdapterActivity<T> extends BaseActivity {
    public static final int TYPE_LISTVIEW = 0;
    public static final int TYPE_GRIDVIEW = 0;
    public Adapter adapter;

    //for empty view
    private View mEmptyLoadingView = null;
    private View mEmptyFailView = null;

    protected AbsListView listView;
    public AbsListView getListView() {
        return listView;
    }
    public void setListView(int resId) {
        this.listView = (AbsListView) findViewById(resId);
    }

    protected List<T> listData = new ArrayList<T>();

    /**
     * setContentView(int resId)
     * setListView(int resId)
     */
    protected abstract void setLayoutView();

    /**
     * the adapter's getView() method
     * @param position
     * @param convertView
     * @return
     */
    protected abstract View getView(int position, View convertView);
    protected boolean isItemEnabled(int position) {
        return true;
    }

    /**
     * the listView's item click event
     * @param adapterView
     * @param view
     * @param position
     * @param id
     */
    protected abstract void onItemClick(AdapterView<?> adapterView, View view, int position, long id);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutView();
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                AdapterActivity.this.onItemClick(adapterView, view, position, id);
            }
        });
    }

    public class Adapter extends SimpleAdapter{

        public Adapter(Context context) {
            super(context, null, 0, null, null);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public boolean isEnabled(int position) {
            return AdapterActivity.this.isItemEnabled(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return AdapterActivity.this.getView(position, convertView);
        }
    }

    //show loading view when init and loading data from network
    protected void showLoadingEmptyView() {
        if (mEmptyFailView == null) {
            mEmptyLoadingView = EmptyViewUtils.createLoadingView(this);
        }
        ((ViewGroup)listView.getParent()).removeView(mEmptyFailView);
        ((ViewGroup)listView.getParent()).addView(mEmptyLoadingView);
        listView.setEmptyView(mEmptyLoadingView);
    }

    //show fail view when loading data fail
    protected void showFailEmptyView() {
        if (mEmptyFailView == null) {
            mEmptyFailView = EmptyViewUtils.createFailView(this);
        }
        ((ViewGroup)listView.getParent()).removeView(mEmptyLoadingView);
        ((ViewGroup)listView.getParent()).addView(mEmptyFailView);
        listView.setEmptyView(mEmptyFailView);
    }
}
