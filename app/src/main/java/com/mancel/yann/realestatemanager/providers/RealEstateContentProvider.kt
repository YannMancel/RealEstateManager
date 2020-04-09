package com.mancel.yann.realestatemanager.providers

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.mancel.yann.realestatemanager.databases.AppDatabase
import com.mancel.yann.realestatemanager.models.User
import kotlinx.coroutines.*
import java.lang.IllegalArgumentException

/**
 * Created by Yann MANCEL on 09/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.providers
 *
 * A [ContentProvider] subclass.
 */
class RealEstateContentProvider : ContentProvider() {

    // ENUMS ---------------------------------------------------------------------------------------

    enum class Table(val mName: String) {
        USER("user"),
        USER_ID("user/#"),
    }

    // FIELDS --------------------------------------------------------------------------------------

    private val mUriMatch = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI(AUTHORITY, Table.USER.mName, Table.USER.ordinal)
        addURI(AUTHORITY, Table.USER_ID.mName, Table.USER_ID.ordinal)
    }

    companion object {
        private const val AUTHORITY = "com.mancel.yann.realestatemanager.providers"
        val TABLE_USER = "content://$AUTHORITY/${Table.USER.mName}"
    }

    // METHODS -------------------------------------------------------------------------------------

    // -- ContentProvider --

    override fun onCreate(): Boolean = true

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? = runBlocking<Uri> {
        when (this@RealEstateContentProvider.mUriMatch.match(uri)) {
            Table.USER.ordinal -> {
                this@RealEstateContentProvider.context?.let {
                    val user = this@RealEstateContentProvider.getUserFromContentValues(values)

                    val userId: Long = AppDatabase.getDatabase(it)
                                                  .userDAO()
                                                  .insertUser(user)

                    if (userId != 0L) {
                        it.contentResolver.notifyChange(uri, null)
                        return@runBlocking ContentUris.withAppendedId(uri, userId)
                    }
                }
            }

            Table.USER_ID.ordinal -> { /* Do nothing */ }

            else -> { /* Ignore all other Uri */ }
        }

        throw IllegalArgumentException("Failed to insert row for uri $uri")
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        when (this.mUriMatch.match(uri)) {
            Table.USER.ordinal -> { /* Do nothing */
                this.context?.let {
                    val cursor: Cursor = AppDatabase.getDatabase(it)
                                                    .userDAO()
                                                    .getAllUsersWithCursor()

                    cursor.setNotificationUri(it.contentResolver, uri)

                    return cursor
                }
            }

            Table.USER_ID.ordinal -> {
                this.context?.let {
                    val userId = ContentUris.parseId(uri)

                    val cursor: Cursor = AppDatabase.getDatabase(it)
                                                    .userDAO()
                                                    .getUserByIdWithCursor(userId)

                    cursor.setNotificationUri(it.contentResolver, uri)

                    return cursor
                }
            }

            else -> { /* Ignore all other Uri */ }
        }

        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = runBlocking {
        when (this@RealEstateContentProvider.mUriMatch.match(uri)) {
            Table.USER.ordinal,
            Table.USER_ID.ordinal -> {
                this@RealEstateContentProvider.context?.let {
                    val user = this@RealEstateContentProvider.getUserFromContentValues(values)

                    val count: Int = AppDatabase.getDatabase(it)
                                                .userDAO()
                                                .updateUser(user)

                    it.contentResolver.notifyChange(uri, null)

                    return@runBlocking count
                }
            }

            else -> { /* Ignore all other Uri */ }
        }

        throw IllegalArgumentException("Failed to update row for uri $uri")
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int = runBlocking {
        when (this@RealEstateContentProvider.mUriMatch.match(uri)) {
            Table.USER.ordinal -> { /* Do nothing */ }

            Table.USER_ID.ordinal -> {
                this@RealEstateContentProvider.context?.let {
                    val count: Int = AppDatabase.getDatabase(it)
                                                .userDAO()
                                                .deleteUserById(ContentUris.parseId(uri))

                    it.contentResolver.notifyChange(uri, null)

                    return@runBlocking count
                }
            }

            else -> { /* Ignore all other Uri */ }
        }

        throw IllegalArgumentException("Failed to delete row for uri $uri")
    }

    override fun getType(uri: Uri): String? {
        return when (this.mUriMatch.match(uri)) {
            Table.USER.ordinal -> "vnd.android.cursor.dir/${Table.USER.mName}"
            Table.USER_ID.ordinal -> "vnd.android.cursor.item/${Table.USER.mName}"

            else -> null
        }
    }

    // -- User --

    /**
     * Gets [User] from [ContentValues]
     * @param values a [ContentValues]
     * @return a [User]
     */
    private fun getUserFromContentValues(values: ContentValues?): User {
        return User().apply {
            values?.let {
                mId = it.getAsLong("id_user") ?: 0L
                mUsername = it.getAsString("username")
                mEmail = it.getAsString("email")
                mUrlPicture = it.getAsString("url_picture")
            }
        }
    }
}