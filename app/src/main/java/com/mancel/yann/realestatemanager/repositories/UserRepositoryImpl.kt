package com.mancel.yann.realestatemanager.repositories

import androidx.lifecycle.LiveData
import com.mancel.yann.realestatemanager.dao.UserDAO
import com.mancel.yann.realestatemanager.models.User

/**
 * Created by Yann MANCEL on 05/03/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.repositories
 *
 * A class which implements [UserRepository].
 */
class UserRepositoryImpl(private val mUserDAO: UserDAO) : UserRepository {

    // METHODS -------------------------------------------------------------------------------------

    // -- Create --

    override suspend fun insertUser(user: User): Long = this.mUserDAO.insertUser(user)

    override suspend fun insertUsers(vararg users: User): List<Long> = this.mUserDAO.insertUsers(*users)

    // -- Read --

    override fun getUserById(userId: Long): LiveData<User> = this.mUserDAO.getUserById(userId)

    override fun getAllUsers(): LiveData<List<User>> = this.mUserDAO.getAllUsers()

    // -- Update --

    override suspend fun updateUser(user: User): Int = this.mUserDAO.updateUser(user)

    override suspend fun deleteUser(user: User): Int = this.mUserDAO.deleteUser(user)

    // -- Delete --

    override suspend fun deleteUserById(userId: Long): Int = this.mUserDAO.deleteUserById(userId)
}