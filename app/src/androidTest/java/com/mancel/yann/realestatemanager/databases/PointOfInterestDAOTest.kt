package com.mancel.yann.realestatemanager.databases

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mancel.yann.realestatemanager.dao.PointOfInterestDAO
import com.mancel.yann.realestatemanager.models.Address
import com.mancel.yann.realestatemanager.models.PointOfInterest
import com.mancel.yann.realestatemanager.utils.LiveDataTestUtil
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Yann MANCEL on 27/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.databases
 *
 * An android test on [PointOfInterestDAO].
 */
@RunWith(AndroidJUnit4::class)
class PointOfInterestDAOTest {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mDatabase: AppDatabase
    private lateinit var mPointOfInterestDAO: PointOfInterestDAO

    // The fields that correspond to an unique index or an unique indices couple must not be null.
    private val mAddress = Address("1", "", "", 0)
    private val mPointOfInterest1 = PointOfInterest(mName = "school", mAddress = this.mAddress)
    private val mPointOfInterest2 = PointOfInterest(mName = "business", mAddress = this.mAddress)

    // RULES (Synchronized Tests) ------------------------------------------------------------------

    @get:Rule
    val rule = InstantTaskExecutorRule()

    // METHODS -------------------------------------------------------------------------------------

    @Before
    @Throws(Exception::class)
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        this.mDatabase = Room.inMemoryDatabaseBuilder(context,
                                                      AppDatabase::class.java)
                             .allowMainThreadQueries()
                             .build()

        this.mPointOfInterestDAO = this.mDatabase.pointOfInterestDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- Create --

    @Test
    fun insertPointOfInterest_shouldBeSuccess() = runBlocking {
        val id = mPointOfInterestDAO.insertPointOfInterest(mPointOfInterest1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertPointOfInterest_shouldBeFail() = runBlocking {
        // BEFORE: Add point of interest
        mPointOfInterestDAO.insertPointOfInterest(mPointOfInterest1)

        // THEN: Add a new point of interest with the same indices (Error)
        val id = mPointOfInterestDAO.insertPointOfInterest(mPointOfInterest1)

        // TEST: No insert because the indices must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertPointsOfInterest_shouldBeSuccess() = runBlocking {
        val ids = mPointOfInterestDAO.insertPointsOfInterest(mPointOfInterest1, mPointOfInterest2)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertPointsOfInterest_shouldBeFail() = runBlocking {
        // THEN: Add 2 points of interest with the same indices (Error)
        val ids = mPointOfInterestDAO.insertPointsOfInterest(mPointOfInterest1, mPointOfInterest1)

        // TEST: No insert because the indices must be unique
        assertEquals(0, ids.size)
    }

    // -- Read --

    @Test
    @Throws(InterruptedException::class)
    fun getAllPointsOfInterest_shouldBeEmpty() {
        val pointsOfInterest = LiveDataTestUtil.getValue(this.mPointOfInterestDAO.getAllPointsOfInterest())

        // TEST: Empty list
        assertEquals(0, pointsOfInterest.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllPointsOfInterest_shouldBeSuccess() = runBlocking {
        // BEFORE: Add points of interest
        mPointOfInterestDAO.insertPointsOfInterest(mPointOfInterest1, mPointOfInterest2)

        // THEN: Retrieve points of interest
        val pointOfInterests = LiveDataTestUtil.getValue(mPointOfInterestDAO.getAllPointsOfInterest())

        // TEST: Same points of interest
        assertEquals(mPointOfInterest1.mName, pointOfInterests[0].mName)
        assertEquals(mPointOfInterest2.mName, pointOfInterests[1].mName)
    }
}