package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.models.User

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 */
interface UserRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    suspend fun insertUser(user: User): Long

    suspend fun insertUsers(vararg users: User): List<Long>

    // -- Read --

    fun getUserById(userId: Long): LiveData<User>

    fun getAllUsers(): LiveData<List<User>>

    // -- Update --

    suspend fun updateUser(user: User): Int

    // -- Delete --

    suspend fun deleteUser(user: User): Int

    suspend fun deleteUserById(userId: Long): Int
}