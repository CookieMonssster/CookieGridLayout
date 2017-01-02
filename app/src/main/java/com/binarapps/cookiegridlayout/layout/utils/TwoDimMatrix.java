package com.binarapps.cookiegridlayout.layout.utils;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cookie on 02.01.2017.
 */

public class TwoDimMatrix {

  private static final String TAG = TwoDimMatrix.class.getSimpleName();

  private int columns;
  private List<boolean[]> list;
  private List<Point> coordinates;

  public TwoDimMatrix(int columns) {
    this.columns = columns;
    list = new ArrayList<>();
    coordinates = new ArrayList<>();
    addNewRow();
  }

  private void addNewRow() {
    list.add(new boolean[columns]);
  }

  public int getRowsCount() {
    return list.size();
  }

  public Point getPosition(int i) {
    return coordinates.get(i);
  }


  public Point addNewElement(int spanColumns, int spanRows) {
    if (spanColumns > columns)
      throw new IllegalStateException("Too many columns to span!");
    int k = 0;

    while (true) {
      boolean[] row = list.get(k);
      for (int i = 0; i < row.length; i++) {
        if (i + spanColumns <= row.length) {
          for (int j = 0; j < spanColumns; j++) {
            if (row[i + j]) {
              continue;
            } else if (j == spanColumns - 1 && placesBelowAreAvailable(k, i, spanColumns, spanRows)) {
              Log.d("klop", "Instert new element: " + k + ":" + i);
              bookPlaces(k, i, spanColumns, spanRows);
              coordinates.add(new Point(i, k));
              return new Point(i, k);
            }
          }
        }
      }
      k++;
      if (k >= list.size()) {
        Log.d("klopp", "Add new row, i:" + k);
        addNewRow();
      }
    }
  }

  private void bookPlaces(int rowPosition, int columnPosition, int spanColumns, int spanRows) {
    for (int i = rowPosition; i < rowPosition + spanRows; i++) {
      boolean[] row = list.get(i);
      for (int j = columnPosition; j < spanColumns + columnPosition; j++) {
        row[j] = true;
      }
    }
  }

  private boolean placesBelowAreAvailable(int rowPosition, int columnPosition, int spanColumns, int spanRows) {

    boolean[] row;
    int count = 0;
    for (int i = rowPosition; i < rowPosition + spanRows; i++) {
      if (i >= list.size()) {

        row = new boolean[columns];
        count++;
      } else {
        row = list.get(i);
      }
      for (int j = columnPosition; j < spanColumns + columnPosition; j++) {
        if (row[j]) {
          return false;
        }
      }
    }
    for (int i = 0; i < count; i++) {
      addNewRow();

    }
    return true;
  }
}
