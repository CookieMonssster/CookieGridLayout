package com.binarapps.cookiegridlayout.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Cookie on 28.12.2016.
 */

public class CookieGridLayout extends ViewGroup {
    public CookieGridLayout(Context context) {
        super(context);
    }

    public CookieGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CookieGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        final int count = getChildCount();


        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            child.layout(10, 10, 100, 100);
        }
    }
}
