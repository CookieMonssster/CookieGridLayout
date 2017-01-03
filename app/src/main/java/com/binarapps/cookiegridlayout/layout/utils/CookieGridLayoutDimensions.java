package com.binarapps.cookiegridlayout.layout.utils;

import android.graphics.Point;

/**
 * Created by Cookie on 03.01.2017.
 */

public class CookieGridLayoutDimensions {


    private int workspaceTop, workspaceLeft, workspaceRight, workspaceBottom;
    private int paddingLeft, paddingRight, paddingTop, paddingBottom;
    private float gapPercent;
    private int gap;
    private int childSize;
    private int count;

    private int columns = 1;
    public int currentRow = 0;
    public int currentColumn = 0;


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
        return workspaceLeft + (drawPoint.x * childSize) + (drawPoint.x * gap);
    }

    public int calculateTopPoint(Point drawPoint) {
        return workspaceTop + (drawPoint.y * childSize) + (drawPoint.y * gap);
    }

    public boolean isNewRow(int i) {
        return (i + 1) % columns == 0;
    }

    public int calculateRightPoint(int startLeft, int spanColumns) {
        int endRight = startLeft + childSize * spanColumns + gap * (spanColumns - 1);
        if (workspaceRight - endRight < this.childSize) {
            endRight = workspaceRight;
        }
        return endRight;
    }

    public int calculateBottomPoint(int startTop, int spanRows) {
        return startTop + childSize * spanRows + gap * (spanRows - 1);
    }

    public void checkThatIsNewRow(int i) {
        if (isNewRow(i)) {
            currentRow = currentRow + 1;
            currentColumn = 0;
        } else {
            currentColumn = currentColumn + 1;
        }
    }

    public static class Builder {
        private int columns = 1;
        private float gapPercent = 0.05f;

        public Builder(int columns) {
            this.columns = columns;
        }

        public Builder withGapPercent(float percent) {
            this.gapPercent = percent;
            return this;
        }

        public CookieGridLayoutDimensions build() {
            return new CookieGridLayoutDimensions(this);
        }
    }

}
