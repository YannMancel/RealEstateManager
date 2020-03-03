package com.mancel.yann.realestatemanager.databases

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mancel.yann.realestatemanager.dao.RealEstatePointOfInterestCrossRefDAO
import com.mancel.yann.realestatemanager.dao.UserDAO
import com.mancel.yann.realestatemanager.models.*
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
 * An android test on [UserDAO].
 */
@RunWith(AndroidJUnit4::class)
class RealEstatePointOfInterestCrossRefDAOTest {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mDatabase: AppDatabase
    private lateinit var mRealEstatePointOfInterestCrossRefDAO: RealEstatePointOfInterestCrossRefDAO

    private val mCrossRef1 = RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 1L)
    private val mCrossRef2 = RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 2L)

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

        // Add user, real estates and points of interest
        // to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        this.mDatabase.userDAO().insertUser(User(mUsername = "Yann"))
        this.mDatabase.realEstateDAO().insertRealEstates(RealEstate(mType = "Flat", mEstateAgentId = 1L),
                                                         RealEstate(mType = "House", mEstateAgentId = 1L))
        this.mDatabase.pointOfInterestDAO().insertPointsOfInterest(PointOfInterest(mName = "school"),
                                                                   PointOfInterest(mName = "business"))

        this.mRealEstatePointOfInterestCrossRefDAO = this.mDatabase.realEstatePointOfInterestCrossRefDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- Create --

    @Test
    fun insertCrossRef_shouldBeSuccess() {
        val id = this.mRealEstatePointOfInterestCrossRefDAO.insertCrossRef(this.mCrossRef1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test
    fun insertCrossRef_shouldBeFail() {
        // BEFORE: Add cross-ref
        this.mRealEstatePointOfInterestCrossRefDAO.insertCrossRef(this.mCrossRef1)

        var id = 0L

        // THEN: Add a new cross-ref with the same primary keys (Error)
        try {
            id = this.mRealEstatePointOfInterestCrossRefDAO.insertCrossRef(this.mCrossRef1)
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

        // TEST: No insert because the primary keys must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertSeveralCrossRef_shouldBeSuccess() {
        val ids = this.mRealEstatePointOfInterestCrossRefDAO.insertSeveralCrossRef(this.mCrossRef1,
                                                                                   this.mCrossRef2)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
    }

    @Test
    fun insertSeveralCrossRef_shouldBeFail() {
        var ids = emptyList<Long>()

        // THEN: Add 2 cross-ref with the same primary keys (Error)
        try {
            ids = this.mRealEstatePointOfInterestCrossRefDAO.insertSeveralCrossRef(this.mCrossRef1,
                                                                                   this.mCrossRef1)
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

        // TEST: No insert because the primary keys must be unique
        assertEquals(0, ids.size)
    }
}