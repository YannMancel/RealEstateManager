package com.mancel.yann.realestatemanager.repositories

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mancel.yann.realestatemanager.databases.AppDatabase
import com.mancel.yann.realestatemanager.models.User
import com.mancel.yann.realestatemanager.utils.LiveDataTestUtil
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.runner.RunWith
import java.io.IOException

/**
 * Created by Yann MANCEL on 03/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * An android test on [UserRepository].
 */
@RunWith(AndroidJUnit4::class)
class UserRepositoryTest {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mDatabase: AppDatabase
    private lateinit var mUserRepository: UserRepository

    // The fields that correspond to an unique index or an unique indices couple must not be null.
    private val mUser1 = User(mUsername = "Yann", mEmail = "yann@com")
    private val mUser2 = User(mUsername = "Melina", mEmail = "melina@com")

    // RULES ---------------------------------------------------------------------------------------

    // A JUnit rule that configures LiveData to execute each task synchronously
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

        this.mUserRepository = UserRepositoryImpl(this.mDatabase.userDAO())
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- Create --

    @Test
    fun insertUser_shouldBeSuccess() = runBlocking {
        // BEFORE: Add user
        val id = mUserRepository.insertUser(mUser1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insertUser_shouldBeFail() = runBlocking {
        // BEFORE: Add user
        mUserRepository.insertUser(mUser1)

        // THEN: Add a new user with the same indices (Error)
        val id = mUserRepository.insertUser(mUser1)

        // TEST: No insert because the indices must be unique
        assertEquals(0L, id)
    }

    // -- Read --

    @Test
    @Throws(InterruptedException::class)
    fun getUserById_shouldBeNull() {
        val user = LiveDataTestUtil.getValue(this.mUserRepository.getUserById(1L))

        // TEST: No user
        assertNull(user)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getUserById_shouldBeSuccess() = runBlocking {
        // BEFORE: Add user
        mUserRepository.insertUser(mUser1)

        // THEN: Retrieve user by its Id
        val user = LiveDataTestUtil.getValue(mUserRepository.getUserById(1L))

        // TEST: Same user except the id because it is 0 for user1 and 1 for user2
        assertEquals(mUser1.mUsername, user.mUsername)
    }

    // -- Update --

    @Test
    @Throws(InterruptedException::class)
    fun updateUser_shouldBeSuccess() = runBlocking {
        // BEFORE: Add user
        mUserRepository.insertUser(mUser1)

        // THEN: Retrieve the user
        val userBeforeUpdate = LiveDataTestUtil.getValue(mUserRepository.getUserById(1L))

        // THEN: Update the user
        val userUpdated = userBeforeUpdate.copy(mUsername = "Melina")
        val numberOfUpdatedRow = mUserRepository.updateUser(userUpdated)

        // AFTER: Retrieve the user
        val userAfterUpdate = LiveDataTestUtil.getValue(mUserRepository.getUserById(userBeforeUpdate.mId))

        // TEST: Number of updated row
        assertEquals(1, numberOfUpdatedRow)

        // TEST: Same user
        assertEquals(userUpdated.mUsername, userAfterUpdate.mUsername)
    }

    @Test(expected = SQLiteConstraintException::class)
    @Throws(InterruptedException::class)
    fun updateUser_shouldBeFail() = runBlocking {
        // BEFORE: Add users
        mUserRepository.insertUser(mUser1)
        mUserRepository.insertUser(mUser2)

        // THEN: Retrieve the user
        val userBeforeUpdate = LiveDataTestUtil.getValue(mUserRepository.getUserById(1L))

        // THEN: Update the user 1 with the username and the email of the user 2 (Error)
        val userUpdated = userBeforeUpdate.copy(
            mUsername = mUser2.mUsername,
            mEmail = mUser2.mEmail
        )

        val numberOfUpdatedRow = mUserRepository.updateUser(userUpdated)

        // TEST: No update because the indices must be unique
        assertEquals(0, numberOfUpdatedRow)
    }

    // -- Delete --

    @Test
    @Throws(InterruptedException::class)
    fun deleteUser_shouldBeSuccess() = runBlocking {
        // BEFORE: Add user
        mUserRepository.insertUser(mUser1)

        // THEN: Retrieve the user
        val user = LiveDataTestUtil.getValue(mUserRepository.getUserById(1L))

        // THEN: Delete user
        val numberOfDeletedRow = mUserRepository.deleteUser(user)

        // TEST: Number of deleted row
        assertEquals(1, numberOfDeletedRow)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteUser_shouldBeFail() = runBlocking {
        // THEN: Delete user (Error)
        val numberOfDeletedRow = mUserRepository.deleteUser(mUser1)

        // TEST: No delete
        assertEquals(0, numberOfDeletedRow)
    }
}