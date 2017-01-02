package com.binarapps.cookiegridlayout.layout.utils

import com.binarapps.cookiegridlayout.BuildConfig
import org.robolectric.annotation.Config
import pl.polidea.robospock.RoboSpecification
import spock.lang.Shared
import spock.lang.Unroll

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

    @Unroll
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


}
