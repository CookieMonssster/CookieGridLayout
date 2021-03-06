package com.binarapps.cookiegridlayout.layout.utils;

import android.graphics.Point;

/**
 * Created by Cookie on 03.01.2017.
 */

public class CookieGridLayoutDimensions {

    private static final int MAX_VERTICAL_POSITIONS = 303;
    private static final int START_POSITION = 0;

    private int workspaceTop, workspaceLeft, workspaceRight, workspaceBottom;
    private int paddingLeft, paddingRight, paddingTop, paddingBottom;
    private float gapPercent;
    private int gapInPixels;
    private int childSize;
    private int count;

    private int columns = 1;


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

    public int getGapInPixels() { return gapInPixels; }

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
        this.gapInPixels = Math.round(gapPercent * width);

        int horizontalGapCount = columns - 1;
        childSize = (width - (horizontalGapCount * gapInPixels)) / columns;
    }

    public int calculateLeftPoint(Point drawPoint) {
        if(drawPoint.x >= columns || drawPoint.x < 0) {
            return workspaceLeft;
        }
        return calculatePosition(workspaceLeft, drawPoint.x);
    }

    public int calculateTopPoint(Point drawPoint) {
        if(drawPoint.y >= MAX_VERTICAL_POSITIONS || drawPoint.y < 0) {
            return workspaceTop;
        }
        return calculatePosition(workspaceTop, drawPoint.y);
    }

    private int calculatePosition(int workspaceCoordinate, int pointCoordinate) {
        if(pointCoordinate >= START_POSITION) {
            return workspaceCoordinate + (pointCoordinate * childSize) + (pointCoordinate * gapInPixels);
        }
        return workspaceCoordinate;
    }

    public int calculateRightPoint(int startLeft, int spanColumns) {
        int endRight = startLeft + (childSize * spanColumns) + gapInPixels * (spanColumns - 1);
        if (workspaceRight - endRight < this.childSize) {
            return workspaceRight;
        }
        return endRight;
    }

    public int calculateBottomPoint(int startTop, int spanRows) {
        int endBottom = startTop + childSize * spanRows + gapInPixels * (spanRows - 1);
        return endBottom;
    }

    public int getRealChildSize(int span) {
        int realChildSize = childSize * span + gapInPixels * (span - 1);
        return realChildSize;
    }

    public static class Builder {
        private static final float MINIMUM_GAP_PERCENT = 0f;
        private static final float MAXIMUM_GAP_SURFACE = 0.5f;
        private static final float PERCENT_BREAKER = 100;

        private int columns = 1;
        private float gapPercent = 5f;

        public Builder(int columns) {
            this.columns = columns;
        }

        public Builder withGapPercent(float percent) {
            percent = percent / PERCENT_BREAKER;
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
