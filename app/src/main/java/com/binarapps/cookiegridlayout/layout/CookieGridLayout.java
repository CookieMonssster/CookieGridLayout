package com.binarapps.cookiegridlayout.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

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

        int count = getChildCount();

        Log.d("klop", "klop");

        for(int i = 0; i < count; i++) {
            View child= getChildAt(i);

            child.getLayoutParams().width = 500;
            child.getLayoutParams().height = 500;
        }




    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int gapCount = count - 1;

        final boolean square = true;
        final float gapPercent = 0.05f;

        int workspaceLeft = getPaddingLeft();
        int workspaceRight = right - left - getPaddingRight();
        int workspaceTop = getPaddingTop();
        int workspaceBottom = bottom - top - getPaddingBottom();

        int width = workspaceRight - workspaceLeft;
        int height = workspaceBottom - workspaceTop;

        int gap = Math.round(gapPercent * width);

        int childWidth = (width - (gapCount * gap)) / count;

        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            int childHeight = child.getHeight();
            if(square) {
                childHeight = childWidth;
            }
            int startLeft = workspaceLeft + (i * childWidth) + (i * gap);

            child.measure(makeMeasureSpec(childWidth, EXACTLY), makeMeasureSpec(childHeight, EXACTLY));
            child.layout(startLeft, workspaceTop, startLeft + childWidth, workspaceTop + childHeight);

            child.invalidate();
        }
    }
}
