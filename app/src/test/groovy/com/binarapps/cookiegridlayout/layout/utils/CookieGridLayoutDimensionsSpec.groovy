package com.binarapps.cookiegridlayout.layout.utils

import android.graphics.Point
import com.binarapps.cookiegridlayout.BuildConfig
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import spock.lang.Unroll

/**
 * Created by martacabaj on 29/12/16.
 */

@Config(manifest = "./src/main/AndroidManifest.xml", constants = BuildConfig, sdk = 18)
public class CookieGridLayoutDimensionsSpec extends RoboSpecification {

    @Unroll
    def "Checking updating dimensions system"() {
        given:
        def cookieDim = new CookieGridLayoutDimensions.Builder(4)
                .withGapPercent(gapPercent)
                .build();
        when:
        cookieDim.updateDimensions(count, left, top, right, bottom)
        then:
        cookieDim.gap == result_gap
        cookieDim.childSize == result_childSize
        cookieDim.workspaceLeft == result_workspaceLeft
        cookieDim.workspaceBottom == result_workspaceBottom
        cookieDim.workspaceTop == result_workspaceTop
        cookieDim.workspaceRight == result_workspaceRight
        where:
        gapPercent | count | left | top | right | bottom | result_gap | result_childSize | result_workspaceLeft | result_workspaceBottom | result_workspaceRight | result_workspaceTop
        0.05       | 5     | 0    | 0   | 500   | 500    | 25         | 106              | 0                    | 500                    | 500                   | 0
        0.5        | 3     | 20   | 20  | 100   | 100    | 13         | 10               | 0                    | 80                     | 80                    | 0
    }

    @Unroll
    def "Checking updating dimensions with padding"() {
        given:
        def cookieDim = new CookieGridLayoutDimensions.Builder(4)
                .withGapPercent(gapPercent)
                .build();
        when:
        cookieDim.updatePadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        cookieDim.updateDimensions(count, 0, 0, 500, 500)
        then:
        cookieDim.gap == result_gap
        cookieDim.childSize == result_childSize
        cookieDim.workspaceLeft == result_workspaceLeft
        cookieDim.workspaceBottom == result_workspaceBottom
        cookieDim.workspaceTop == result_workspaceTop
        cookieDim.workspaceRight == result_workspaceRight
        where:
        gapPercent | count | paddingLeft | paddingTop | paddingRight | paddingBottom | result_gap | result_childSize | result_workspaceLeft | result_workspaceBottom | result_workspaceRight | result_workspaceTop
        0.05       | 5     | 16          | 16         | 16           | 16            | 23         | 99               | 16                   | 484                    | 484                   | 16
        0.5        | 3     | 20          | 20         | 100          | 100           | 63         | 47               | 20                   | 400                    | 400                   | 20
    }

    @Unroll
    def "Checking left and top point calculations"() {
        given:
        def cookieDim = new CookieGridLayoutDimensions.Builder(4)
                .withGapPercent(0.01)
                .build();
        cookieDim.updateDimensions(1, 0, 0, 800, 1200)
        def point = new Point(x, y)
        when:
        def left = cookieDim.calculateLeftPoint(point)
        def top = cookieDim.calculateTopPoint(point)
        then:
        left == left_result
        top == top_result
        where:
        x                 | y                 | left_result | top_result
        0                 | 0                 | 0           | 0
        -1                | -3                | 0           | 0
        2                 | 0                 | 404         | 0
        0                 | 2                 | 0           | 404
        50                | 50                | 0           | 10100
        Integer.MAX_VALUE | 2                 | 0           | 404
        2                 | 303               | 404         | 0
        2                 | Integer.MAX_VALUE | 404         | 0
        Integer.MIN_VALUE | Integer.MIN_VALUE | 0           | 0
    }



}