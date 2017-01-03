package com.binarapps.cookiegridlayout.layout.utils;

import android.graphics.Point;

/**
 * Created by Cookie on 03.01.2017.
 */

public class CookieGridLayoutDimensions {

    public int left, top, right, bottom;
    public int workspaceTop, workspaceLeft, workspaceRight, workspaceBottom;
    public int width;
    public float gapPercent;
    public int gap;
    public int childSize;
    public int count;

    public int columns = 1;
    public int horizontalGapCount;
    public int currentRow = 0;
    public int currentColumn = 0;


    public CookieGridLayoutDimensions(Builder builder) {
        this.left = builder.left;
        this.top = builder.top;
        this.right = builder.right;
        this.bottom = builder.bottom;
        this.count = builder.count;

        this.workspaceLeft = builder.paddingLeft;
        this.workspaceTop = builder.paddingTop;
        this.workspaceRight = right - left - builder.paddingRight;
        this.workspaceBottom = bottom - top - builder.paddingBottom;

        this.width = workspaceRight - workspaceLeft;
        this.gapPercent = builder.gapPercent;
        this.gap = Math.round(gapPercent * width);
        this.columns = builder.columns;

        horizontalGapCount = columns - 1;
        childSize = (width - (horizontalGapCount * gap)) / columns;
    }

    public int calculateStartLeft(Point drawPoint) {
        return workspaceLeft + (drawPoint.x * childSize) + (drawPoint.x * gap);
    }

    public int calculateStartTop(Point drawPoint) {
        return workspaceTop + (drawPoint.y * childSize) + (drawPoint.y * gap);
    }

    public boolean isNewRow(int i) {
        return (i + 1) % columns == 0;
    }

    public int calculateWidth(int startLeft, int spanColumns) {
        return startLeft + childSize * spanColumns + gap * (spanColumns - 1);
    }

    public int calculateHeight(int startTop, int spanRows) {
        return startTop + childSize * spanRows + gap * (spanRows - 1);
    }

    public static class Builder {

        private int left, top, right, bottom;
        private int count;
        private int paddingLeft, paddingRight, paddingTop, paddingBottom = 0;
        private int columns = 1;
        private float gapPercent = 0.05f;

        public Builder(int count, int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
            this.count = count;
        }

        public Builder withColumns(int columns) {
            this.columns = columns;
            return this;
        }

        public Builder withPadding(int paddingTop, int paddingLeft, int paddingRight, int paddingBottom) {
            this.paddingTop = paddingTop;
            this.paddingLeft = paddingLeft;
            this.paddingRight = paddingRight;
            this.paddingBottom = paddingBottom;
            return this;
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
