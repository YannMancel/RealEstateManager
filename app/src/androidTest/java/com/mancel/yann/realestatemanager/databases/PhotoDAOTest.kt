package com.mancel.yann.realestatemanager.databases

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mancel.yann.realestatemanager.dao.PhotoDAO
import com.mancel.yann.realestatemanager.models.Photo
import com.mancel.yann.realestatemanager.models.RealEstate
import com.mancel.yann.realestatemanager.models.User
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
 * An android test on [PhotoDAO].
 */
@RunWith(AndroidJUnit4::class)
class PhotoDAOTest {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mDatabase: AppDatabase
    private lateinit var mPhotoDAO: PhotoDAO

    // The fields that correspond to an unique index or an unique indices couple must not be null.
    private val mPhoto1 = Photo(mUrlPicture = "URL1", mRealEstateId = 1L)
    private val mPhoto2 = Photo(mUrlPicture = "URL2", mRealEstateId = 1L)
    private val mPhoto3 = Photo(mUrlPicture = "URL3", mRealEstateId = 2L)

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

        // Add user and real estates to avoid the SQLiteConstraintException (FOREIGN KEY constraint)
        this.mDatabase.userDAO().insertUser(User(mUsername = "Yann"))
        this.mDatabase.realEstateDAO().insertRealEstates(RealEstate(mType = "Flat", mEstateAgentId = 1L),
                                                         RealEstate(mType = "House", mEstateAgentId = 1L))

        this.mPhotoDAO = this.mDatabase.photoDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- Create --

    @Test
    fun insertPhoto_shouldBeSuccess() {
        val id = this.mPhotoDAO.insertPhoto(this.mPhoto1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test
    fun insertPhoto_shouldBeFail() {
        // BEFORE: Add photo
        this.mPhotoDAO.insertPhoto(this.mPhoto1)

        var id = 0L

        // THEN: Add a new photo with the same indices (Error)
        try {
            id = this.mPhotoDAO.insertPhoto(this.mPhoto1)
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

        // TEST: No insert because the indices must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertPhotos_shouldBeSuccess() {
        val ids = this.mPhotoDAO.insertPhotos(this.mPhoto1,
                                              this.mPhoto2,
                                              this.mPhoto3)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
        assertEquals(3L, ids[2])
    }

    @Test
    fun insertPhotos_shouldBeFail() {
        var ids = emptyList<Long>()

        // THEN: Add 2 photos with the same indices (Error)
        try {
            ids = this.mPhotoDAO.insertPhotos(this.mPhoto1,
                                              this.mPhoto1)
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
    fun getPhotoByRealEstateId_shouldBeEmpty() {
        val photos = LiveDataTestUtil.getValue(this.mPhotoDAO.getPhotoByRealEstateId(1L))

        // TEST: Empty list
        assertEquals(0, photos.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getPhotoByRealEstateId_shouldBeSuccess() {
        // BEFORE: Add photos
        this.mPhotoDAO.insertPhotos(this.mPhoto1,
                                    this.mPhoto2,
                                    this.mPhoto3)

        // THEN: Retrieve photos by real estate Id
        val photos = LiveDataTestUtil.getValue(this.mPhotoDAO.getPhotoByRealEstateId(1L))

        // TEST: Just 2 photos
        assertEquals(2, photos.size)
        assertEquals(this.mPhoto1.mUrlPicture, photos[0].mUrlPicture)
        assertEquals(this.mPhoto2.mUrlPicture, photos[1].mUrlPicture)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllPhotos_shouldBeEmpty() {
        val photos = LiveDataTestUtil.getValue(this.mPhotoDAO.getAllPhotos())

        // TEST: Empty list
        assertEquals(0, photos.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllPhotos_shouldBeSuccess() {
        // BEFORE: Add photos
        this.mPhotoDAO.insertPhotos(this.mPhoto1,
                                    this.mPhoto2,
                                    this.mPhoto3)

        // THEN: Retrieve photos
        val photos = LiveDataTestUtil.getValue(this.mPhotoDAO.getAllPhotos())

        // TEST: All photos
        assertEquals(3, photos.size)
        assertEquals(this.mPhoto1.mUrlPicture, photos[0].mUrlPicture)
        assertEquals(this.mPhoto2.mUrlPicture, photos[1].mUrlPicture)
        assertEquals(this.mPhoto3.mUrlPicture, photos[2].mUrlPicture)
    }

    // -- Update --

    @Test
    @Throws(InterruptedException::class)
    fun updatePhoto_shouldBeSuccess() {
        // BEFORE: Add photo
        this.mPhotoDAO.insertPhoto(this.mPhoto1)

        // THEN: Retrieve the photo -> photos[0]
        val photosBeforeUpdate = LiveDataTestUtil.getValue(this.mPhotoDAO.getAllPhotos())

        // THEN: Update the photo
        val photoUpdated = photosBeforeUpdate[0].copy(mUrlPicture = "Random")
        val numberOfUpdatedRow = this.mPhotoDAO.updatePhoto(photoUpdated)

        // AFTER: Retrieve the photo -> photos[0]
        val photosAfterUpdate = LiveDataTestUtil.getValue(this.mPhotoDAO.getAllPhotos())

        // TEST: Number of updated row
        assertEquals(1, numberOfUpdatedRow)

        // TEST: Same photo
        assertEquals(photoUpdated.mUrlPicture, photosAfterUpdate[0].mUrlPicture)
    }

    @Test
    fun updatePhoto_shouldBeFail() {
        // BEFORE: Add photos
        this.mPhotoDAO.insertPhotos(this.mPhoto1,
                                    this.mPhoto2,
                                    this.mPhoto3)

        // THEN: Retrieve the photo -> photos[0]
        val photosBeforeUpdate = LiveDataTestUtil.getValue(this.mPhotoDAO.getAllPhotos())

        // THEN: Update the photo 1 with the url picture of the photo 2 (Error)
        val photoUpdated = photosBeforeUpdate[0].copy(mUrlPicture = this.mPhoto2.mUrlPicture)

        var numberOfUpdatedRow = 0

        try {
            numberOfUpdatedRow = this.mPhotoDAO.updatePhoto(photoUpdated)
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
    fun deletePhoto_shouldBeSuccess() {
        // BEFORE: Add photo
        this.mPhotoDAO.insertPhoto(this.mPhoto1)

        // THEN: Retrieve the photo -> photos[0]
        val photos = LiveDataTestUtil.getValue(this.mPhotoDAO.getAllPhotos())

        // THEN: Delete photo
        val numberOfDeletedRow = this.mPhotoDAO.deletePhoto(photos[0])

        // TEST: Number of deleted row
        assertEquals(1, numberOfDeletedRow)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deletePhoto_shouldBeFail() {
        // THEN: Delete photo (Error)
        val numberOfDeletedRow = this.mPhotoDAO.deletePhoto(this.mPhoto1)

        // TEST: No delete
        assertEquals(0, numberOfDeletedRow)
    }
}