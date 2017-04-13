package com.ej.ejdian.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by xubo on 2016/8/5.
 */
public class BaseLayout extends ViewGroup {

    public BaseLayout(Context context, int layoutTitleId, int layoutMainId, LayoutInflater layoutInflater) {
        super(context);
        if (layoutTitleId != -1) {
            View titleView = layoutInflater.inflate(layoutTitleId, this, false);
            addTitleView(titleView);
        }
        View mainView = layoutInflater.inflate(layoutMainId, null);
        addMainView(mainView);
    }

    public BaseLayout(Context context, int layoutTitleId, View mainView, LayoutInflater layoutInflater) {
        super(context);
        if (layoutTitleId != -1) {
            View titleView = layoutInflater.inflate(layoutTitleId, this, false);
            addTitleView(titleView);
        }
        addMainView(mainView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        final int count = getChildCount();
        int totalLength = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams lp = child.getLayoutParams();
            int childWidthMeasureSpec;
            int childHeightMeasureSpec;
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY);
            if (lp.height == LayoutParams.MATCH_PARENT) {
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(measuredHeight - totalLength - getPaddingTop(), MeasureSpec.EXACTLY);
            } else {
                childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop(), lp.height);
            }
            totalLength += getDefaultSize(0, childHeightMeasureSpec);
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int totalLength = getPaddingTop();
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child == null) continue;
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            child.layout(0, totalLength, width, totalLength + height);
            totalLength += height;
        }
    }

    private void addTitleView(View titleView) {
        addView(titleView);
    }

    private void addMainView(View mainView) {
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mainView, params);
    }
}
