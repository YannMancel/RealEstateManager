package com.mancel.yann.realestatemanager

import com.mancel.yann.realestatemanager.utils.convertDollarToEuro
import com.mancel.yann.realestatemanager.utils.convertEuroToDollar
import com.mancel.yann.realestatemanager.utils.getTodayDateDDMMYYYY
import com.mancel.yann.realestatemanager.utils.getTodayDateYYYYMMDD
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Yann MANCEL on 21/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager
 *
 * A Test on methods of Utils file
 */
class UtilsTest {

    /*
        Configure Mockito for Final Methods and Classes:
            Add a text file to the project's src/test/resources/mockito-extensions directory
            named org.mockito.plugins.MockMaker and add a single line of text:
                mock-maker-inline
        See: https://www.baeldung.com/mockito-final
     */

    // METHODS -------------------------------------------------------------------------------------

    // -- Basics --

    @Test
    fun convertDollarToEuro_should_be_success() {
        assertEquals(0, convertDollarToEuro(dollars = 0))
        assertEquals(-81, convertDollarToEuro(dollars = -100))
        assertEquals(81, convertDollarToEuro(dollars = 100))
    }

    @Test
    fun convertEuroToDollar_should_be_success() {
        assertEquals(0, convertEuroToDollar(euros = 0))
        assertEquals(-100, convertEuroToDollar(euros = -81))
        assertEquals(100, convertEuroToDollar(euros = 81))
    }

    @Test
    fun getTodayDateYYYYMMDD_should_be_success() {
        // BEFORE: Current date
        val calendar = Calendar.getInstance()

        // THEN: Convert to string format [yyyy/MM/dd]
        val day = if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
                      "0${calendar.get(Calendar.DAY_OF_MONTH)}"
                  else
                      "${calendar.get(Calendar.DAY_OF_MONTH)}"

        val month = if ((calendar.get(Calendar.MONTH) + 1) < 10)
                        "0${calendar.get(Calendar.MONTH) + 1}"
                    else
                        "${calendar.get(Calendar.MONTH) + 1}"

        val expectedString = "${calendar.get(Calendar.YEAR)}/$month/$day"

        // TEST: Good format
        assertEquals(expectedString, getTodayDateYYYYMMDD())
    }

    @Test
    fun getTodayDateDDMMYYYY_should_be_success() {
        // BEFORE: Current date
        val calendar = Calendar.getInstance()

        // THEN: Convert to string format [yyyy/MM/dd]
        val day = if (calendar.get(Calendar.DAY_OF_MONTH) < 10)
                      "0${calendar.get(Calendar.DAY_OF_MONTH)}"
                  else
                      "${calendar.get(Calendar.DAY_OF_MONTH)}"

        val month = if ((calendar.get(Calendar.MONTH) + 1) < 10)
                        "0${calendar.get(Calendar.MONTH) + 1}"
                    else
                        "${calendar.get(Calendar.MONTH) + 1}"

        val expectedString = "$day/$month/${calendar.get(Calendar.YEAR)}"

        // TEST: Good format
        assertEquals(expectedString, getTodayDateDDMMYYYY())
    }

    // -- Mockito --

    @Test
    fun getTodayDateYYYYMMDD_mockito_should_be_success() {
        // BEFORE: Mock of SimpleDateFormat
        mock<SimpleDateFormat> {
            on { format(Date()) } doReturn "2020/03/04"
        }

        // TEST: Good format
        assertEquals("2020/03/04", getTodayDateYYYYMMDD())
    }

    @Test
    fun getTodayDateDDMMYYYY_mockito_should_be_success() {
        // BEFORE: Mock of SimpleDateFormat
        mock<SimpleDateFormat> {
            on { format(Date()) } doReturn "04/03/2020"
        }

        // TEST: Good format
        assertEquals("04/03/2020", getTodayDateDDMMYYYY())
    }
}