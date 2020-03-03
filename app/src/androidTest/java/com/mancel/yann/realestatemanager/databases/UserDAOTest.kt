package com.mancel.yann.realestatemanager.databases

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mancel.yann.realestatemanager.dao.UserDAO
import com.mancel.yann.realestatemanager.models.User
import com.mancel.yann.realestatemanager.utils.LiveDataTestUtil
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
class UserDAOTest {

    // FIELDS --------------------------------------------------------------------------------------

    private lateinit var mDatabase: AppDatabase
    private lateinit var mUserDAO: UserDAO

    // The fields that correspond to an unique index or an unique indices couple must not be null.
    private val mUser1 = User(mUsername = "Yann", mEmail = "yann@com")
    private val mUser2 = User(mUsername = "Melina", mEmail = "melina@com")

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

        this.mUserDAO = this.mDatabase.userDAO()
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        this.mDatabase.close()
    }

    // -- Create --

    @Test
    fun insertUser_shouldBeSuccess() {
        val id = this.mUserDAO.insertUser(this.mUser1)

        // TEST: Good Id
        assertEquals(1L, id)
    }

    @Test
    fun insertUser_shouldBeFail() {
        // BEFORE: Add user
        this.mUserDAO.insertUser(this.mUser1)

        var id = 0L

        // THEN: Add a new user with the same indices (Error)
        try {
            id = this.mUserDAO.insertUser(this.mUser1)
        }
        catch (e: SQLiteConstraintException) {
            // Do nothing
        }

        // TEST: No insert because the indices must be unique
        assertEquals(0L, id)
    }

    @Test
    fun insertUsers_shouldBeSuccess() {
        val ids = this.mUserDAO.insertUsers(this.mUser1,
                                            this.mUser2)

        // TEST: Good Ids
        assertEquals(1L, ids[0])
        assertEquals(2L, ids[1])
    }

    @Test
    fun insertUsers_shouldBeFail() {
        var ids = emptyList<Long>()

        // THEN: Add 2 users with the same indices (Error)
        try {
            ids = this.mUserDAO.insertUsers(this.mUser1,
                                            this.mUser1)
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
    fun getUserById_shouldBeNull() {
        val user = LiveDataTestUtil.getValue(this.mUserDAO.getUserById(1L))

        // TEST: No user
        assertNull(user)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getUserById_shouldBeSuccess() {
        // BEFORE: Add user
        this.mUserDAO.insertUser(this.mUser1)

        // THEN: Retrieve user by its Id
        val user = LiveDataTestUtil.getValue(this.mUserDAO.getUserById(1L))

        // TEST: Same user except the id because it is 0 for user1 and 1 for user2
        assertEquals(this.mUser1.mUsername, user.mUsername)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllUsers_shouldBeEmpty() {
        val users = LiveDataTestUtil.getValue(this.mUserDAO.getAllUsers())

        // TEST: Empty list
        assertEquals(0, users.size)
    }

    @Test
    @Throws(InterruptedException::class)
    fun getAllUsers_shouldBeSuccess() {
        // BEFORE: Add users
        this.mUserDAO.insertUsers(this.mUser1,
                                  this.mUser2)

        // THEN: Retrieve users
        val users = LiveDataTestUtil.getValue(this.mUserDAO.getAllUsers())

        // TEST: Same users
        assertEquals(this.mUser1.mUsername, users[0].mUsername)
        assertEquals(this.mUser2.mUsername, users[1].mUsername)
    }

    // -- Update --

    @Test
    @Throws(InterruptedException::class)
    fun updateUser_shouldBeSuccess() {
        // BEFORE: Add user
        this.mUserDAO.insertUser(this.mUser1)

        // THEN: Retrieve the user
        val userBeforeUpdate = LiveDataTestUtil.getValue(this.mUserDAO.getUserById(1L))

        // THEN: Update the user
        val userUpdated = userBeforeUpdate.copy(mUsername = "Melina")
        val numberOfUpdatedRow = this.mUserDAO.updateUser(userUpdated)

        // AFTER: Retrieve the user
        val userAfterUpdate = LiveDataTestUtil.getValue(this.mUserDAO.getUserById(userBeforeUpdate.mId))

        // TEST: Number of updated row
        assertEquals(1, numberOfUpdatedRow)

        // TEST: Same user
        assertEquals(userUpdated.mUsername, userAfterUpdate.mUsername)
    }

    @Test
    @Throws(InterruptedException::class)
    fun updateUser_shouldBeFail() {
        // BEFORE: Add users
        this.mUserDAO.insertUsers(this.mUser1,
                                  this.mUser2)

        // THEN: Retrieve the user
        val userBeforeUpdate = LiveDataTestUtil.getValue(this.mUserDAO.getUserById(1L))

        // THEN: Update the user 1 with the username and the email of the user 2 (Error)
        val userUpdated = userBeforeUpdate.copy(mUsername = this.mUser2.mUsername,
                                                mEmail = this.mUser2.mEmail)

        var numberOfUpdatedRow = 0

        try {
            numberOfUpdatedRow = this.mUserDAO.updateUser(userUpdated)
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
    fun deleteUser_shouldBeSuccess() {
        // BEFORE: Add user
        this.mUserDAO.insertUser(this.mUser1)

        // THEN: Retrieve the user
        val user = LiveDataTestUtil.getValue(this.mUserDAO.getUserById(1L))

        // THEN: Delete user
        val numberOfDeletedRow = this.mUserDAO.deleteUser(user)

        // TEST: Number of deleted row
        assertEquals(1, numberOfDeletedRow)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteUser_shouldBeFail() {
        // THEN: Delete user (Error)
        val numberOfDeletedRow = this.mUserDAO.deleteUser(this.mUser1)

        // TEST: No delete
        assertEquals(0, numberOfDeletedRow)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteUserById_shouldBeSuccess() {
        // BEFORE: Add user
        this.mUserDAO.insertUser(this.mUser1)

        // THEN: Retrieve user by its Id
        val user = LiveDataTestUtil.getValue(this.mUserDAO.getUserById(1L))

        // THEN: Delete user thanks to its Id
        val numberOfDeletedRow = this.mUserDAO.deleteUserById(user.mId)

        // TEST: Number of deleted row
        assertEquals(1, numberOfDeletedRow)
    }

    @Test
    @Throws(InterruptedException::class)
    fun deleteUserById_shouldBeFail() {
        // THEN: Delete user thanks to its Id (Error)
        val numberOfDeletedRow = this.mUserDAO.deleteUserById(0L)

        // TEST: No delete
        assertEquals(0, numberOfDeletedRow)
    }
}