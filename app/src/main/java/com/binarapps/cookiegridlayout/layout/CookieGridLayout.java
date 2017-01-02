package com.binarapps.cookiegridlayout.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.binarapps.cookiegridlayout.R;
import com.binarapps.cookiegridlayout.layout.utils.TwoDimMatrix;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * Created by Cookie on 28.12.2016.
 */

public class CookieGridLayout extends ViewGroup {

    private int columns;
    private int rows;
    private float gapPercent;
    private float outsideGapPercent;
    private float heightMultiple;
    private TwoDimMatrix availableSpace;


    public CookieGridLayout(Context context) {
        super(context);
    }

    public CookieGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs);
    }

    public CookieGridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs);
    }

    private void initialize(Context context, AttributeSet attrs) {
        readAttributes(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(availableSpace==null) {
            availableSpace = new TwoDimMatrix(columns);
            for (int i = 0; i < getChildCount(); i++) {
                final View child = getChildAt(i);
                CookieGridLayout.LayoutParams lp = (CookieGridLayout.LayoutParams) child.getLayoutParams();

                int childSpanColumns = lp.spanColumns;
                int childSpanRows = lp.spanRows;

                availableSpace.addNewElement(childSpanColumns, childSpanRows);
            }
        }
        int heightSize;
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int size = widthSize / columns;
        heightSize = size * availableSpace.getRowsCount();
        Log.d("TAG", "rows:" + availableSpace.getRowsCount());
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
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

        int gap = Math.round(gapPercent * width);

        int childSize = (width - (horizontalGapCount * gap)) / columns;


        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            CookieGridLayout.LayoutParams lp = (CookieGridLayout.LayoutParams) child.getLayoutParams();

            int childSpanColumns = lp.spanColumns;
            int childSpanRows = lp.spanRows;
            if(availableSpace!=null) {
                Point drawPoint = availableSpace.getPosition(i);


                int startLeft = workspaceLeft + (drawPoint.x * childSize) + (drawPoint.x * gap);
                int startTop = workspaceTop + (drawPoint.y * childSize) + (drawPoint.y * gap);

                child.measure(makeMeasureSpec(childSize * childSpanColumns, EXACTLY), makeMeasureSpec(childSize * childSpanRows, EXACTLY));

                //            if(isNewRow(i, columns)) {
                //                child.layout(startLeft, startTop, startLeft + (workspaceRight - startLeft), startTop + childHeight);
                //            } else {
                child.layout(startLeft, startTop, startLeft + childSize * childSpanColumns + gap * (childSpanColumns - 1),
                  startTop + childSize * childSpanRows + gap * (childSpanRows - 1));
                //            }

                if (isNewRow(i, columns)) {
                    currentRow = currentRow + 1;
                    currentColumn = 0;
                } else {
                    currentColumn = currentColumn + 1;
                }
            }
        }
    }


    private boolean isNewRow(int i, int columns) {
        return (i + 1) % columns == 0;
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CookieGridLayout, 0, 0);

        try {
            gapPercent = a.getFloat(R.styleable.CookieGridLayout_gap, 0.01f);
            outsideGapPercent = a.getFloat(R.styleable.CookieGridLayout_outsideGap, 0.01f);
            columns = a.getInteger(R.styleable.CookieGridLayout_columns, 3);
            if (columns == 0) {
                columns = 1;
            }
            heightMultiple = a.getFloat(R.styleable.CookieGridLayout_heightMultiple, 1);
        } finally {
            a.recycle();
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CookieGridLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof CookieGridLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
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
