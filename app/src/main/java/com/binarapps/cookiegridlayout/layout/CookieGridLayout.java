package com.binarapps.cookiegridlayout.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.binarapps.cookiegridlayout.R;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * Created by Cookie on 28.12.2016.
 */

public class CookieGridLayout extends ViewGroup {

    private boolean square;
    private float gapPercent;
    private float outsideGapPercent;
    private int columns;


    public CookieGridLayout(Context context) {
        super(context);
    }

    public CookieGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(context, attrs);
    }

    public CookieGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttributes(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        int horizontalGapCount = columns - 1;
        int currentRow = 0;
        int currentColumn = 0;

        int workspaceLeft = getPaddingLeft();
        int workspaceRight = right - left - getPaddingRight();
        int workspaceTop = getPaddingTop();
        int workspaceBottom = bottom - top - getPaddingBottom();

        int width = workspaceRight - workspaceLeft;
        int height = workspaceBottom - workspaceTop;

        int gap = Math.round(gapPercent * width);

        int childWidth = (width - (horizontalGapCount * gap)) / columns;
        int childHeight;
        if(square) {
            childHeight = childWidth;
        } else {
            int rows = count / columns;
            if(count % columns > 0) {
                rows = rows + 1;
            }
            int verticalGapCount = rows - 1;
            childHeight = (height - (verticalGapCount * gap)) / rows;
        }



        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            int startLeft = workspaceLeft + (currentColumn * childWidth) + (currentColumn * gap);
            int startTop = workspaceTop + (currentRow * childHeight) + (currentRow * gap);


            child.measure(makeMeasureSpec(childWidth, EXACTLY), makeMeasureSpec(childHeight, EXACTLY));

            if(isNewRow(i, columns)) {
                child.layout(startLeft, startTop, startLeft + (workspaceRight - startLeft), startTop + childHeight);
            } else {
                child.layout(startLeft, startTop, startLeft + childWidth, startTop + childHeight);
            }

            if(isNewRow(i, columns)) {
                currentRow = currentRow + 1;
                currentColumn = 0;
            } else {
                currentColumn = currentColumn + 1;
            }
        }
    }

    private boolean isNewRow(int i, int columns) {
        return (i + 1) % columns == 0;
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CookieGridLayout, 0, 0);

        try {
            square = a.getBoolean(R.styleable.CookieGridLayout_square, true);
            gapPercent = a.getFloat(R.styleable.CookieGridLayout_gap, 0.01f);
            outsideGapPercent = a.getFloat(R.styleable.CookieGridLayout_outsideGap, 0.01f);
            columns = a.getInteger(R.styleable.CookieGridLayout_columns, 3);
        } finally {
            a.recycle();
        }
    }
}
