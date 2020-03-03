package com.mancel.yann.realestatemanager.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.mancel.yann.realestatemanager.models.User

/**
 * Created by Yann MANCEL on 26/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.dao
 *
 * DAO of [User].
 */
@Dao
interface UserDAO {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    /**
     * Usage:
     * val id = dao.insertUser(user)
     */
    @Insert
    fun insertUser(user: User): Long

    /**
     * Usage:
     * val ids = dao.insertUsers(user1, user2)
     */
    @Insert
    fun insertUsers(vararg users: User): List<Long>

    // -- Read --

    /**
     * Usage:
     * dao.getUserById(userId)
     *    .observe(this, Observer { user -> ... })
     */
    @Query("SELECT * FROM user WHERE id_user = :userId")
    fun getUserById(userId: Long): LiveData<User>

    /**
     * Usage:
     * dao.getAllUsers()
     *    .observe(this, Observer { users -> ... })
     */
    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<User>>

    // -- Update --

    /**
     * Usage:
     * val numberOfUpdatedRow = dao.updateUser(user)
     */
    @Update
    fun updateUser(user: User): Int

    // -- Delete --

    /**
     * Usage:
     * val numberOfDeletedRow = dao.deleteUser(user)
     */
    @Delete
    fun deleteUser(user: User): Int

    /**
     * Usage:
     * val numberOfDeletedRow = dao.deleteUserById(userId)
     */
    @Query("DELETE FROM user WHERE id_user = :userId")
    fun deleteUserById(userId: Long): Int
}