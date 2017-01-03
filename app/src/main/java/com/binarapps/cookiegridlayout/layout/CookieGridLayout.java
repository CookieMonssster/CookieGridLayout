package com.binarapps.cookiegridlayout.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.binarapps.cookiegridlayout.R;
import com.binarapps.cookiegridlayout.layout.utils.CookieGridLayoutDimensions;
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
    private TwoDimMatrix availableSpace;

    private CookieGridLayoutDimensions cookieDim;


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
        cookieDim = new CookieGridLayoutDimensions.Builder(columns)
                .withGapPercent(gapPercent)
                .build();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(availableSpace == null) {
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
        cookieDim.updatePadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        cookieDim.updateDimensions(getChildCount(), left, top, right, bottom);

        for (int i = 0; i < cookieDim.getCount(); i++) {
            final View child = getChildAt(i);
            CookieGridLayout.LayoutParams lp = (CookieGridLayout.LayoutParams) child.getLayoutParams();

            if(availableSpace != null) {
                Point drawPoint = availableSpace.getPosition(i);

                int startLeft = cookieDim.calculateStartLeft(drawPoint);
                int startTop = cookieDim.calculateStartTop(drawPoint);
                int childWidth = cookieDim.calculateWidth(startLeft, lp.spanColumns);
                int childHeight = cookieDim.calculateHeight(startTop, lp.spanRows);

                child.measure(makeMeasureSpec(cookieDim.getChildSize() * lp.spanColumns, EXACTLY),
                        makeMeasureSpec(cookieDim.getChildSize() * lp.spanRows, EXACTLY));

                //            if(isNewRow(i, columns)) {
                //                child.layout(startLeft, startTop, startLeft + (workspaceRight - startLeft), startTop + childHeight);
                //            } else {
                child.layout(startLeft, startTop, childWidth, childHeight);
                //            }
                cookieDim.checkThatIsNewRow(i);
            }
        }
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
        } finally {
            a.recycle();
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CookieGridLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams params) {
        return params instanceof CookieGridLayout.LayoutParams;
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
