package com.mancel.yann.realestatemanager

import com.mancel.yann.realestatemanager.utils.convertDollarToEuro
import com.mancel.yann.realestatemanager.utils.convertEuroToDollar
import org.junit.Assert
import org.junit.Test

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager
 *
 * A Test on methods of Utils file
 */
class UtilsTest {

    // METHODS -------------------------------------------------------------------------------------

    @Test
    fun convertDollarToEuro_should_be_success() {
        Assert.assertEquals(0, convertDollarToEuro(dollars = 0))
        Assert.assertEquals(-81, convertDollarToEuro(dollars = -100))
        Assert.assertEquals(81, convertDollarToEuro(dollars = 100))
    }

    @Test
    fun convertEuroToDollar_should_be_success() {
        Assert.assertEquals(0, convertEuroToDollar(euros = 0))
        Assert.assertEquals(-100, convertEuroToDollar(euros = -81))
        Assert.assertEquals(100, convertEuroToDollar(euros = 81))
    }

}