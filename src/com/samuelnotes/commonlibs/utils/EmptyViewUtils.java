package com.samuelnotes.commonlibs.utils;


import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.samuelnotes.commonlibs.R;

public class EmptyViewUtils {

    public static View createLoadingView(Context context) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_empty_loading, null);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setVisibility(View.GONE);
        return linearLayout;
    }

    public static View createFailView(Context context) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.list_empty_fail, null);
        linearLayout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setVisibility(View.GONE);
        return linearLayout;
    }

}
