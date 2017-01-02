package com.binarapps.cookiegridlayout.layout.utils

import com.binarapps.cookiegridlayout.BuildConfig
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import spock.lang.Shared

/**
 * Created by martacabaj on 29/12/16.
 */
@Config(manifest = "./src/main/AndroidManifest.xml", constants = BuildConfig, sdk = 18)
public class TwoDimMatrixSpec extends RoboSpecification {

    @Shared
    TwoDimMatrix twoDimMatrix

    def setupSpec() {
        twoDimMatrix = new TwoDimMatrix(4);
    }


    def "Adding new element to the layout"() {
        when:
        def point = twoDimMatrix.addNewElement(spanColumns, spanRows)
        then:
        x == point.x
        y == point.y
        where:
        spanColumns | spanRows || x | y
        1           | 1        || 0 | 0
        3           | 3        || 1 | 0
        1           | 2        || 0 | 1
        3           | 2        || 0 | 3
        2           | 1        || 0 | 5
    }

    def "check if places Below Are Available"() {
        when:
        def value = twoDimMatrix.placesBelowAreAvailable(rowPosition, columnPosition, spanColumns, spanRows)
        then:
        expectedResult == value
        where:
        rowPosition | columnPosition | spanColumns | spanRows || expectedResult
        0           | 1              | 3           | 3        || false
        0           | 3              | 3           | 2        || false
        3           | 3              | 1           | 1        || true
        4           | 3              | 1           | 1        || true
        3           | 3              | 1           | 2        || true

    }

    def "check rows count"() {
        when:
        def rows = twoDimMatrix.getRowsCount()
        then:
        rows == 6
    }

}
