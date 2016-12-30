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

    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final boolean square = true;
        final float gapPercent = 0.05f;
        final int columns = 3;

        int gapCount = columns - 1;
        int currentRow = 0;
        int currentColumn = 0;

        int workspaceLeft = getPaddingLeft();
        int workspaceRight = right - left - getPaddingRight();
        int workspaceTop = getPaddingTop();
        int workspaceBottom = bottom - top - getPaddingBottom();

        int width = workspaceRight - workspaceLeft;
        int height = workspaceBottom - workspaceTop;

        int gap = Math.round(gapPercent * width);

        int childWidth = (width - (gapCount * gap)) / columns;
        int childHeight;
        if(square) {
            childHeight = childWidth;
        } else {
            childHeight = 100;
        }



        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int startLeft = workspaceLeft + (currentColumn * childWidth) + (currentColumn * gap);
            int startTop = workspaceTop + (currentRow * childHeight) + (currentRow * gap);
            startTop  = startTop + 100;


            child.measure(makeMeasureSpec(childWidth, EXACTLY), makeMeasureSpec(childHeight, EXACTLY));
            child.layout(startLeft, startTop, startLeft + childWidth, workspaceTop + childHeight);

            if((i + 1)%columns == 0) {
                currentRow = currentRow + 1;
                currentColumn = 0;
            } else {
                currentColumn = currentColumn + 1;
            }
        }
    }
}
