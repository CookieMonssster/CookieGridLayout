package com.binarapps.cookiegridlayout.layout.utils;

import android.graphics.Point;

/**
 * Created by Cookie on 03.01.2017.
 */

public class CookieGridLayoutDimensions {

    private static final int MAX_VERTICAL_POSITIONS = 303;

    private int workspaceTop, workspaceLeft, workspaceRight, workspaceBottom;
    private int paddingLeft, paddingRight, paddingTop, paddingBottom;
    private float gapPercent;
    private int gap;
    private int childSize;
    private int count;

    private int columns = 1;
    private int currentRow = 0;
    private int currentColumn = 0;


    public CookieGridLayoutDimensions(Builder builder) {
        this.gapPercent = builder.gapPercent;
        this.columns = builder.columns;
    }

    public void setGapPercent(int gapPercent) {
        this.gapPercent = gapPercent;
    }

    public int getCount() {
        return count;
    }

    public int getChildSize() {
        return childSize;
    }

    public int getGap() { return gap; }

    public int getWorkspaceTop() {
        return workspaceTop;
    }

    public int getWorkspaceLeft() {
        return workspaceLeft;
    }

    public int getWorkspaceRight() {
        return workspaceRight;
    }

    public int getWorkspaceBottom() {
        return workspaceBottom;
    }

    public void updatePadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
    }

    public void updateDimensions(int count, int left, int top, int right, int bottom) {
        this.count = count;

        this.workspaceLeft = paddingLeft;
        this.workspaceTop = paddingTop;
        this.workspaceRight = right - left - paddingRight;
        this.workspaceBottom = bottom - top - paddingBottom;

        int width = workspaceRight - workspaceLeft;
        this.gap = Math.round(gapPercent * width);

        int horizontalGapCount = columns - 1;
        childSize = (width - (horizontalGapCount * gap)) / columns;
    }

    public int calculateLeftPoint(Point drawPoint) {
        if(drawPoint.x >= columns) {
            return 0;
        }
        return calculatePosition(workspaceLeft, drawPoint.x);
    }

    public int calculateTopPoint(Point drawPoint) {
        if(drawPoint.y >= MAX_VERTICAL_POSITIONS) {
            return 0;
        }
        return calculatePosition(workspaceTop, drawPoint.y);
    }

    private int calculatePosition(int workspaceCoordinate, int pointCoordinate) {
        if(pointCoordinate > 0) {
            return workspaceCoordinate + (pointCoordinate * childSize) + (pointCoordinate * gap);
        }
        return 0;
    }

    public int calculateRightPoint(int startLeft, int spanColumns, int position) {
        int endRight = startLeft + (childSize * spanColumns) + gap * (spanColumns - 1);
        if (workspaceRight - endRight < this.childSize) {
            return workspaceRight;
        }
        if(position == 0) {
            return endRight + paddingLeft;
        }
        return endRight;
    }

    public int calculateBottomPoint(int startTop, int spanRows, int position) {
        int endBottom = startTop + childSize * spanRows + gap * (spanRows - 1);
        if(position == 0) {
            return endBottom + paddingTop;
        }
        return endBottom;
    }

    public boolean isNewRow(int i) {
        return (i + 1) % columns == 0;
    }

    public void checkThatIsNewRow(int i) {
        if (isNewRow(i)) {
            currentRow = currentRow + 1;
            currentColumn = 0;
        } else {
            currentColumn = currentColumn + 1;
        }
    }

    public int getRealChildSize(int span, int pos, int padding) {
        int realChildSize = childSize * span + gap * (span - 1);
        if(pos == 0) {
            return realChildSize + 2 * padding;
        }
        return realChildSize;
    }

    public static class Builder {
        private static final float MINIMUM_GAP_PERCENT = 0f;
        private static final float MAXIMUM_GAP_SURFACE = 0.5f;

        private int columns = 1;
        private float gapPercent = 0.05f;

        public Builder(int columns) {
            this.columns = columns;
        }

        public Builder withGapPercent(float percent) {
            if(percent < MINIMUM_GAP_PERCENT) {
                gapPercent = MINIMUM_GAP_PERCENT;
                return this;
            }
            float maxGap = getMaximumGap();

            if(percent > maxGap) {
                this.gapPercent = maxGap;
                return this;
            }
            this.gapPercent = percent;
            return this;
        }

        public CookieGridLayoutDimensions build() {
            return new CookieGridLayoutDimensions(this);
        }

        private float getMaximumGap() {
            if (columns > 1)
                return MAXIMUM_GAP_SURFACE / (columns - 1);
            else {
                return MINIMUM_GAP_PERCENT;
            }
        }
    }

}
