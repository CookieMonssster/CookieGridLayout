package com.binarapps.cookiegridlayout.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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
    private int columns;
    private int rows;
    private float gapPercent;
    private float outsideGapPercent;
    private float heigthMultiple;


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
        rows = getRowsCount(getChildCount());
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        if(square) {
            int size = widthSize / columns;
            heightSize = size * rows;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.round(heightSize * heigthMultiple), MeasureSpec.AT_MOST);
        }
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
            int verticalGapCount = rows - 1;
            childHeight = (height - (verticalGapCount * gap)) / rows;
        }



        for(int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            CookieGridLayout.LayoutParams lp = (CookieGridLayout.LayoutParams) child.getLayoutParams();

            int childSpanColumns = lp.spanColumns;
            int childSpanRows = lp.spanRows;

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

    private int getRowsCount(int count) {
        int rows = count / columns;
        if(count % columns > 0) {
            rows = rows + 1;
        }
        return rows;
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CookieGridLayout, 0, 0);

        try {
            square = a.getBoolean(R.styleable.CookieGridLayout_square, true);
            gapPercent = a.getFloat(R.styleable.CookieGridLayout_gap, 0.01f);
            outsideGapPercent = a.getFloat(R.styleable.CookieGridLayout_outsideGap, 0.01f);
            columns = a.getInteger(R.styleable.CookieGridLayout_columns, 3);
            if(columns == 0) {
                columns = 1;
            }
            heigthMultiple = a.getFloat(R.styleable.CookieGridLayout_heightMultiple, 1);
        } finally {
            a.recycle();
        }
    }

    @Override public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new CookieGridLayout.LayoutParams(getContext(), attrs);
    }

    @Override protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
    {
        return p instanceof CookieGridLayout.LayoutParams;
    }

    @Override protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new LayoutParams(p);
    }

    private static class LayoutParams extends ViewGroup.LayoutParams {

        public int spanColumns;
        public int spanRows;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.CookieGridLayout_Layout);
            spanColumns = a.getInt(R.styleable.CookieGridLayout_Layout_CookieLayout_spanColumns, 1);
            spanRows = a.getInt(R.styleable.CookieGridLayout_Layout_CookieLayout_spanRows, 1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }


    }
}
