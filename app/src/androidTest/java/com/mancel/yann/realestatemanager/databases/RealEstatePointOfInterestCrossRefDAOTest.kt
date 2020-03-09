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
    fun setUp() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()

        mDatabase = Room.inMemoryDatabaseBuilder(context,
                                                 AppDatabase::class.java)
                        .allowMainThreadQueries()
                        .build()

        // Add user, real estates and points of interest
        // to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        mDatabase.userDAO().insertUser(User(mUsername = "Yann"))
        mDatabase.realEstateDAO().insertRealEstates(RealEstate(mType = "Flat", mEstateAgentId = 1L),
                                                    RealEstate(mType = "House", mEstateAgentId = 1L))
        mDatabase.pointOfInterestDAO().insertPointsOfInterest(PointOfInterest(mName = "school"),
                                                              PointOfInterest(mName = "business"))

        mRealEstatePointOfInterestCrossRefDAO = mDatabase.realEstatePointOfInterestCrossRefDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- Create --

    @Test
    fun insertCrossRef_shouldBeSuccess() = runBlocking {
        val id = mRealEstatePointOfInterestCrossRefDAO.insertCrossRef(mCrossRef1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertCrossRef_shouldBeFail() = runBlocking {
        // BEFORE: Add cross-ref
        mRealEstatePointOfInterestCrossRefDAO.insertCrossRef(mCrossRef1)

        // THEN: Add a new cross-ref with the same primary keys (Error)
        val id = mRealEstatePointOfInterestCrossRefDAO.insertCrossRef(mCrossRef1)

        // TEST: No insert because the primary keys must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertSeveralCrossRef_shouldBeSuccess() = runBlocking {
        val ids = mRealEstatePointOfInterestCrossRefDAO.insertSeveralCrossRef(mCrossRef1,
                                                                              mCrossRef2)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertSeveralCrossRef_shouldBeFail() = runBlocking {
        // THEN: Add 2 cross-ref with the same primary keys (Error)
        val ids = mRealEstatePointOfInterestCrossRefDAO.insertSeveralCrossRef(mCrossRef1,
                                                                              mCrossRef1)

        // TEST: No insert because the primary keys must be unique
        assertEquals(0, ids.size)
    }
}