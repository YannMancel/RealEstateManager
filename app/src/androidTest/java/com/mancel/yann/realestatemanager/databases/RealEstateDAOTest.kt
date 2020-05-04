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
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

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
    private val mAddress = Address(mLatitude = 0.0, mLongitude = 0.0)
    private val mRealEstate1 = RealEstate(mType = "Flat", mPrice = 120_000.0, mSurface = 0.0, mNumberOfRoom = 2, mEstateAgentId = 1L, mAddress = this.mAddress)
    private val mRealEstate2 = RealEstate(mType = "House", mPrice = 300_000.0, mSurface = 100.0, mNumberOfRoom = 10, mEstateAgentId = 1L, mAddress = this.mAddress)

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

        // Add user to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        mDatabase.userDAO().insertUser(User(mUsername = "Yann"))

        mRealEstateDAO = mDatabase.realEstateDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- TypeConverters (Date) --

    @Test
    fun typeConverters_date_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estate with current date
        val realEstate = RealEstate(mEffectiveDate = Date(), mEstateAgentId = 1L)
        mRealEstateDAO.insertRealEstate(realEstate)

        // THEN: Retrieve the real estate
        val realEstateFromRoom = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateById(1L))

        // TEST: Same date
        assertEquals(realEstate.mEffectiveDate, realEstateFromRoom.mEffectiveDate)
    }

    // -- Create --

    @Test
    fun insertRealEstate_shouldBeSuccess() = runBlocking {
        val id = mRealEstateDAO.insertRealEstate(mRealEstate1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertRealEstate_shouldBeFail() = runBlocking {
        // BEFORE: Add real estate
        mRealEstateDAO.insertRealEstate(mRealEstate1)

        // THEN: Add a new real estate with the same indices (Error)
        val id = mRealEstateDAO.insertRealEstate(mRealEstate1)

        // TEST: No insert because the indices must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertRealEstates_shouldBeSuccess() = runBlocking {
        val ids = mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertRealEstates_shouldBeFail() = runBlocking {
        // THEN: Add 2 real estates with the same indices (Error)
        val ids = mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate1)

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
    fun getRealEstateById_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Retrieve real estate by Id
        val realEstate = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateById(1L))

        // TEST: Same real estate
        assertEquals(mRealEstate1.mType, realEstate.mType)
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
    fun getAllRealEstates_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Retrieve real estates
        val realEstates = LiveDataTestUtil.getValue(mRealEstateDAO.getAllRealEstates())

        // TEST: All real estates
        assertEquals(2, realEstates.size)
        assertEquals(mRealEstate1.mType, realEstates[0].mType)
        assertEquals(mRealEstate2.mType, realEstates[1].mType)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getCountOfRealEstatesByUserId_shouldBeZero() {
        // Retrieve the count of row with the same user Id
        val count = LiveDataTestUtil.getValue(mRealEstateDAO.getCountOfRealEstatesByUserId(1L))

        // TEST: Must be zero
        assertEquals(0, count)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getCountOfRealEstatesByUserId_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Retrieve the count of row with the same user Id
        val count = LiveDataTestUtil.getValue(mRealEstateDAO.getCountOfRealEstatesByUserId(1L))

        // TEST: Must not be zero
        assertEquals(2, count)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getIdTypeAddressPriceTupleOfRealEstate_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Retrieve tuples of real estates
        val realEstatesTuples = LiveDataTestUtil.getValue(mRealEstateDAO.getIdTypeAddressPriceTupleOfRealEstateByUserId(1L))

        // TEST: All real estates
        assertEquals(2, realEstatesTuples.size)
        assertEquals(mRealEstate1.mType, realEstatesTuples[0].mType)
        assertEquals(mRealEstate2.mType, realEstatesTuples[1].mType)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getRealEstatesWithPhotos_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Add photos
        mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL1", mRealEstateId = 1L))
        mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL2", mRealEstateId = 2L))
        mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL3", mRealEstateId = 2L))

        // THEN: Retrieve real estates with their photos
        val realEstatesWithPhotos = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstatesWithPhotosByUserId(1L))

        // TEST: All real estates with their photos
        assertEquals(2, realEstatesWithPhotos.size)
        assertEquals(mRealEstate1.mType, realEstatesWithPhotos[0].mRealEstate?.mType)
        assertEquals(mRealEstate2.mType, realEstatesWithPhotos[1].mRealEstate?.mType)
        assertEquals(1, realEstatesWithPhotos[0].mPhotos?.size)
        assertEquals(2, realEstatesWithPhotos[1].mPhotos?.size)
        assertEquals("URL1", realEstatesWithPhotos[0].mPhotos?.get(0)?.mUrlPicture)
        assertEquals("URL2", realEstatesWithPhotos[1].mPhotos?.get(0)?.mUrlPicture)
        assertEquals("URL3", realEstatesWithPhotos[1].mPhotos?.get(1)?.mUrlPicture)
    }

    @Test
    fun getRealEstatesWithPointsOfInterest_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Add points of interest to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        mDatabase.pointOfInterestDAO().insertPointsOfInterest(PointOfInterest(mName = "school"),
                                                              PointOfInterest(mName = "business"))

        // THEN: Add cross-reference between real estates and points of interest
        mDatabase.realEstatePointOfInterestCrossRefDAO().insertSeveralCrossRef(
            RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 1L),
            RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 2L),
            RealEstatePointOfInterestCrossRef(mRealEstateId = 2L, mPointOfInterestId = 1L)
        )

        // THEN: Retrieve real estates with their points of interest
        val realEstatesWithPointsOfInterest = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstatesWithPointsOfInterestByUserId(1L))

        // TEST: All real estates with their points of interest
        assertEquals(2, realEstatesWithPointsOfInterest.size)
        assertEquals(mRealEstate1.mType, realEstatesWithPointsOfInterest[0].mRealEstate?.mType)
        assertEquals(mRealEstate2.mType, realEstatesWithPointsOfInterest[1].mRealEstate?.mType)
        assertEquals(2, realEstatesWithPointsOfInterest[0].mPointsOfInterest?.size)
        assertEquals(1, realEstatesWithPointsOfInterest[1].mPointsOfInterest?.size)
        assertEquals("school", realEstatesWithPointsOfInterest[0].mPointsOfInterest?.get(0)?.mName)
        assertEquals("business", realEstatesWithPointsOfInterest[0].mPointsOfInterest?.get(1)?.mName)
        assertEquals("school", realEstatesWithPointsOfInterest[1].mPointsOfInterest?.get(0)?.mName)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getRealEstateWithPhotosById_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estate
        mRealEstateDAO.insertRealEstate(mRealEstate1)

        // THEN: Add photos
        mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL1", mRealEstateId = 1L))

        // THEN: Retrieve real estate with its photos
        val realEstateWithPhotos = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateWithPhotosById(1L))

        // TEST: The real estate with its photos
        assertEquals(mRealEstate1.mType, realEstateWithPhotos.mRealEstate?.mType)
        assertEquals(1, realEstateWithPhotos.mPhotos?.size)
        assertEquals("URL1", realEstateWithPhotos.mPhotos?.get(0)?.mUrlPicture)
    }

    @Test
    fun getRealEstateWithPointsOfInterestById_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Add points of interest to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        mDatabase.pointOfInterestDAO().insertPointsOfInterest(
            PointOfInterest(mName = "school"),
            PointOfInterest(mName = "business")
        )

        // THEN: Add cross-reference between real estates and points of interest
        mDatabase.realEstatePointOfInterestCrossRefDAO().insertSeveralCrossRef(
            RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 1L),
            RealEstatePointOfInterestCrossRef(mRealEstateId = 1L, mPointOfInterestId = 2L),
            RealEstatePointOfInterestCrossRef(mRealEstateId = 2L, mPointOfInterestId = 1L)
        )

        // THEN: Retrieve real estate with its points of interest
        val realEstateWithPointsOfInterest = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateWithPointsOfInterestById(1L))

        // TEST: real estate with its points of interest
        assertEquals(mRealEstate1.mType, realEstateWithPointsOfInterest.mRealEstate?.mType)
        assertEquals(2, realEstateWithPointsOfInterest.mPointsOfInterest?.size)
        assertEquals("school", realEstateWithPointsOfInterest.mPointsOfInterest?.get(0)?.mName)
        assertEquals("business", realEstateWithPointsOfInterest.mPointsOfInterest?.get(1)?.mName)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getRealEstatesWithPhotosByMultiSearch_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Add photos
        mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL1", mRealEstateId = 1L))
        mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL2", mRealEstateId = 2L))
        mDatabase.photoDAO().insertPhoto(Photo(mUrlPicture = "URL3", mRealEstateId = 2L))

        // THEN: Retrieve real estates with their photos
        val realEstatesWithPhotos = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstatesWithPhotosByMultiSearch(
            minPrice= 0.0,
            maxPrice= 150_000.0
        ))

        // TEST: Just mRealEstate1 with their photos
        assertEquals(1, realEstatesWithPhotos.size)
        assertEquals(mRealEstate1.mType, realEstatesWithPhotos[0].mRealEstate?.mType)
        assertEquals(1, realEstatesWithPhotos[0].mPhotos?.size)
        assertEquals("URL1", realEstatesWithPhotos[0].mPhotos?.get(0)?.mUrlPicture)
    }

    // -- Update --

    @Test
    @Throws(InterruptedException::class)
    fun updateRealEstate_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstate(mRealEstate1)

        // THEN: Retrieve the real estate
        val realEstateBeforeUpdate = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateById(1L))

        // THEN: Update the real estate
        val realEstateUpdated = realEstateBeforeUpdate.copy(mType = "Random")
        val numberOfUpdatedRow = mRealEstateDAO.updateRealEstate(realEstateUpdated)

        // AFTER: Retrieve the real estate
        val realEstateAfterUpdate = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateById(1L))

        // TEST: Number of updated row
        assertEquals(1, numberOfUpdatedRow)

        // TEST: Same real estate
        assertEquals(realEstateUpdated.mType, realEstateAfterUpdate.mType)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun updateRealEstate_shouldBeFail() = runBlocking {
        // BEFORE: Add real estates
        mRealEstateDAO.insertRealEstates(mRealEstate1, mRealEstate2)

        // THEN: Retrieve the real estate
        val realEstateBeforeUpdate = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateById(1L))

        // THEN: Update the real estate 1 with the several fields of the real estate 2 (Error)
        val realEstateUpdated = realEstateBeforeUpdate.copy(mType = mRealEstate2.mType,
                                                            mSurface = mRealEstate2.mSurface,
                                                            mNumberOfRoom = mRealEstate2.mNumberOfRoom,
                                                            mAddress = mRealEstate2.mAddress)

        val numberOfUpdatedRow = mRealEstateDAO.updateRealEstate(realEstateUpdated)

        // TEST: No update because the indices must be unique
        assertEquals(0, numberOfUpdatedRow)
    }

    // -- Delete --

    @Test
    @Throws(InterruptedException::class)
    fun deleteRealEstate_shouldBeSuccess() = runBlocking {
        // BEFORE: Add real estate
        mRealEstateDAO.insertRealEstate(mRealEstate1)

        // THEN: Retrieve the real estate
        val realEstate = LiveDataTestUtil.getValue(mRealEstateDAO.getRealEstateById(1L))

        // THEN: Delete real estate
        val numberOfDeletedRow = mRealEstateDAO.deleteRealEstate(realEstate)

        // TEST: Number of deleted row
        assertEquals(1, numberOfDeletedRow)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteRealEstate_shouldBeFail() = runBlocking {
        // THEN: Delete real estate (Error)
        val numberOfDeletedRow = mRealEstateDAO.deleteRealEstate(mRealEstate1)

        // TEST: No delete
        assertEquals(0, numberOfDeletedRow)
    }
}