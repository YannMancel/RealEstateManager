package com.mancel.yann.realestatemanager.databases

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mancel.yann.realestatemanager.dao.RealEstateDAO
import com.mancel.yann.realestatemanager.models.*
import com.mancel.yann.realestatemanager.utils.LiveDataTestUtil
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Yann MANCEL on 27/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.databases
 *
 * An android test on [RealEstateDAO].
 */
@RunWith(AndroidJUnit4::class)
class RealEstateDAOTest {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mDatabase: AppDatabase
    private lateinit var mRealEstateDAO: RealEstateDAO

    // The fields that correspond to an unique index or an unique indices couple must not be null.
    private val mAddress = Address("1", "", "", 0)
    private val mRealEstate1 = RealEstate(mType = "Flat", mSurface = 0.0, mNumberOfRoom = 2, mEstateAgentId = 1L, mAddress = this.mAddress)
    private val mRealEstate2 = RealEstate(mType = "House", mSurface = 0.0, mNumberOfRoom = 2, mEstateAgentId = 1L, mAddress = this.mAddress)

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

        // Add user to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        this.mDatabase.userDAO().insertUser(User(mUsername = "Yann"))

        this.mRealEstateDAO = this.mDatabase.realEstateDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- Create --

    @Test
    fun insertRealEstate_shouldBeSuccess() {
        val id = this.mRealEstateDAO.insertRealEstate(this.mRealEstate1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test
    fun insertRealEstate_shouldBeFail() {
        // BEFORE: Add real estate
        this.mRealEstateDAO.insertRealEstate(this.mRealEstate1)

        var id = 0L

        // THEN: Add a new real estate with the same indices (Error)
        try {
            id = this.mRealEstateDAO.insertRealEstate(this.mRealEstate1)
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

        // TEST: No insert because the indices must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertRealEstates_shouldBeSuccess() {
        val ids = this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                                        this.mRealEstate2)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
    }

    @Test
    fun insertRealEstates_shouldBeFail() {
        var ids = emptyList<Long>()

        // THEN: Add 2 real estates with the same indices (Error)
        try {
            ids = this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                                        this.mRealEstate1)
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
    fun getRealEstateById_shouldBeNull() {
        val realEstate = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstateById(1L))

        // TEST: No user
        Assert.assertNull(realEstate)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getRealEstateById_shouldBeSuccess() {
        // BEFORE: Add real estates
        this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                              this.mRealEstate2)

        // THEN: Retrieve real estate by Id
        val realEstate = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstateById(1L))

        // TEST: Same real estate
        assertEquals(this.mRealEstate1.mType, realEstate.mType)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllRealEstates_shouldBeEmpty() {
        val realEstates = LiveDataTestUtil.getValue(this.mRealEstateDAO.getAllRealEstates())

        // TEST: Empty list
        assertEquals(0, realEstates.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllRealEstates_shouldBeSuccess() {
        // BEFORE: Add real estates
        this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                              this.mRealEstate2)

        // THEN: Retrieve real estates
        val realEstates = LiveDataTestUtil.getValue(this.mRealEstateDAO.getAllRealEstates())

        // TEST: All real estates
        assertEquals(2, realEstates.size)
        assertEquals(this.mRealEstate1.mType, realEstates[0].mType)
        assertEquals(this.mRealEstate2.mType, realEstates[1].mType)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getIdTypeAddressPriceTupleOfRealEstate_shouldBeSuccess() {
        // BEFORE: Add real estates
        this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                              this.mRealEstate2)

        // THEN: Retrieve tuples of real estates
        val realEstatesTuples = LiveDataTestUtil.getValue(this.mRealEstateDAO.getIdTypeAddressPriceTupleOfRealEstate())

        // TEST: All real estates
        assertEquals(2, realEstatesTuples.size)
        assertEquals(this.mRealEstate1.mType, realEstatesTuples[0].mType)
        assertEquals(this.mRealEstate2.mType, realEstatesTuples[1].mType)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getRealEstatesWithPhotos_shouldBeSuccess() {
        // BEFORE: Add real estates
        this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                              this.mRealEstate2)

        // THEN: Add photos
        this.mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL1", mRealEstateId = 1L))
        this.mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL2", mRealEstateId = 2L))
        this.mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL3", mRealEstateId = 2L))

        // THEN: Retrieve real estates with their photos
        val realEstatesWithPhotos = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstatesWithPhotos())

        // TEST: All real estates with their photos
        assertEquals(2, realEstatesWithPhotos.size)
        assertEquals(this.mRealEstate1.mType, realEstatesWithPhotos[0].mRealEstate?.mType)
        assertEquals(this.mRealEstate2.mType, realEstatesWithPhotos[1].mRealEstate?.mType)
        assertEquals(1, realEstatesWithPhotos[0].mPhotos?.size)
        assertEquals(2, realEstatesWithPhotos[1].mPhotos?.size)
        assertEquals("URL1", realEstatesWithPhotos[0].mPhotos?.get(0)?.mUrlPicture)
        assertEquals("URL2", realEstatesWithPhotos[1].mPhotos?.get(0)?.mUrlPicture)
        assertEquals("URL3", realEstatesWithPhotos[1].mPhotos?.get(1)?.mUrlPicture)
    }

    @Test
    fun getRealEstatesWithPointsOfInterest_shouldBeSuccess() {
        // BEFORE: Add real estates
        this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                              this.mRealEstate2)

        // THEN: Add points of interest to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        this.mDatabase.pointOfInterestDAO().insertPointsOfInterest(PointOfInterest(mName = "school"),
                                                                   PointOfInterest(mName = "business"))

        // THEN: Add cross-reference between real estates and points of interest
        this.mDatabase.realEstatePointOfInterestCrossRefDAO().insertSeveralCrossRef(
            RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 1L),
            RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 2L),
            RealEstatePointOfInterestCrossRef(mRealEstateId = 2L, mPointOfInterestId = 1L)
        )

        // THEN: Retrieve real estates with their points of interest
        val realEstatesWithPointsOfInterest = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstatesWithPointsOfInterest())

        // TEST: All real estates with their points of interest
        assertEquals(2, realEstatesWithPointsOfInterest.size)
        assertEquals(this.mRealEstate1.mType, realEstatesWithPointsOfInterest[0].mRealEstate?.mType)
        assertEquals(this.mRealEstate2.mType, realEstatesWithPointsOfInterest[1].mRealEstate?.mType)
        assertEquals(2, realEstatesWithPointsOfInterest[0].mPointsOfInterest?.size)
        assertEquals(1, realEstatesWithPointsOfInterest[1].mPointsOfInterest?.size)
        assertEquals("school", realEstatesWithPointsOfInterest[0].mPointsOfInterest?.get(0)?.mName)
        assertEquals("business", realEstatesWithPointsOfInterest[0].mPointsOfInterest?.get(1)?.mName)
        assertEquals("school", realEstatesWithPointsOfInterest[1].mPointsOfInterest?.get(0)?.mName)
    }

    // -- Update --

    @Test
    @Throws(InterruptedException::class)
    fun updateRealEstate_shouldBeSuccess() {
        // BEFORE: Add real estates
        this.mRealEstateDAO.insertRealEstate(this.mRealEstate1)

        // THEN: Retrieve the real estate
        val realEstateBeforeUpdate = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstateById(1L))

        // THEN: Update the real estate
        val realEstateUpdated = realEstateBeforeUpdate.copy(mType = "Random")
        val numberOfUpdatedRow = this.mRealEstateDAO.updateRealEstate(realEstateUpdated)

        // AFTER: Retrieve the real estate
        val realEstateAfterUpdate = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstateById(1L))

        // TEST: Number of updated row
        assertEquals(1, numberOfUpdatedRow)

        // TEST: Same real estate
        assertEquals(realEstateUpdated.mType, realEstateAfterUpdate.mType)
    }

    @Test
    fun updateRealEstate_shouldBeFail() {
        // BEFORE: Add real estates
        this.mRealEstateDAO.insertRealEstates(this.mRealEstate1,
                                              this.mRealEstate2)

        // THEN: Retrieve the real estate
        val realEstateBeforeUpdate = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstateById(1L))

        // THEN: Update the real estate 1 with the several fields of the real estate 2 (Error)
        val realEstateUpdated = realEstateBeforeUpdate.copy(mType = this.mRealEstate2.mType,
                                                            mSurface = this.mRealEstate2.mSurface,
                                                            mNumberOfRoom = this.mRealEstate2.mNumberOfRoom,
                                                            mAddress = this.mRealEstate2.mAddress)

        var numberOfUpdatedRow = 0

        try {
            numberOfUpdatedRow = this.mRealEstateDAO.updateRealEstate(realEstateUpdated)
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

        // TEST: No update because the indices must be unique
        assertEquals(0, numberOfUpdatedRow)
    }

    // -- Delete --

    @Test
    @Throws(InterruptedException::class)
    fun deleteRealEstate_shouldBeSuccess() {
        // BEFORE: Add real estate
        this.mRealEstateDAO.insertRealEstate(this.mRealEstate1)

        // THEN: Retrieve the real estate
        val realEstate = LiveDataTestUtil.getValue(this.mRealEstateDAO.getRealEstateById(1L))

        // THEN: Delete real estate
        val numberOfDeletedRow = this.mRealEstateDAO.deleteRealEstate(realEstate)

        // TEST: Number of deleted row
        assertEquals(1, numberOfDeletedRow)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteRealEstate_shouldBeFail() {
        // THEN: Delete real estate (Error)
        val numberOfDeletedRow = this.mRealEstateDAO.deleteRealEstate(this.mRealEstate1)

        // TEST: No delete
        assertEquals(0, numberOfDeletedRow)
    }
}