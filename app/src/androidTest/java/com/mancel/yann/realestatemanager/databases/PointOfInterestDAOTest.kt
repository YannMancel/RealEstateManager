package com.mancel.yann.realestatemanager.databases

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mancel.yann.realestatemanager.dao.PointOfInterestDAO
import com.mancel.yann.realestatemanager.models.PointOfInterest
import com.mancel.yann.realestatemanager.utils.LiveDataTestUtil
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

    private val mPointOfInterest1 = PointOfInterest(mName = "school")
    private val mPointOfInterest2 = PointOfInterest(mName = "business")

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
    fun insertPointOfInterest_shouldBeSuccess() {
        val id = this.mPointOfInterestDAO.insertPointOfInterest(this.mPointOfInterest1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test
    fun insertPointOfInterest_shouldBeFail() {
        // BEFORE: Add point of interest
        this.mPointOfInterestDAO.insertPointOfInterest(this.mPointOfInterest1)

        var id = 0L

        // THEN: Add a new point of interest with the same indices (Error)
        try {
            id = this.mPointOfInterestDAO.insertPointOfInterest(this.mPointOfInterest1.copy(mId = 1L))
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

        // TEST: No insert because the indices must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertPointsOfInterest_shouldBeSuccess() {
        val ids = this.mPointOfInterestDAO.insertPointsOfInterest(this.mPointOfInterest1,
                                                                  this.mPointOfInterest2)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
    }

    @Test
    fun insertPointsOfInterest_shouldBeFail() {
        var ids = emptyList<Long>()

        // THEN: Add 2 points of interest with the same indices (Error)
        try {
            ids = this.mPointOfInterestDAO.insertPointsOfInterest(this.mPointOfInterest1,
                                                                  this.mPointOfInterest1.copy(mId = 1L))
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

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
    fun getAllPointsOfInterest_shouldBeSuccess() {
        // BEFORE: Add points of interest
        this.mPointOfInterestDAO.insertPointsOfInterest(this.mPointOfInterest1,
                                                        this.mPointOfInterest2)

        // THEN: Retrieve points of interest
        val pointOfInterests = LiveDataTestUtil.getValue(this.mPointOfInterestDAO.getAllPointsOfInterest())

        // TEST: Same users
        assertEquals(this.mPointOfInterest1.mName, pointOfInterests[0].mName)
        assertEquals(this.mPointOfInterest2.mName, pointOfInterests[1].mName)
    }
}