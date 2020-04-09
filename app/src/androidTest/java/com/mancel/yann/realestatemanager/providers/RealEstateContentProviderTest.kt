package com.mancel.yann.realestatemanager.providers

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Yann MANCEL on 09/04/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.providers
 *
 * An android test on [ContentResolver].
 */
@RunWith(AndroidJUnit4::class)
class RealEstateContentProviderTest {

    // FIELDS --------------------------------------------------------------------------------------

    private val mContentResolver = ApplicationProvider.getApplicationContext<Context>()
                                                      .contentResolver

    private val mContentValues = ContentValues().apply {
        put("id_user", 0L)
        put("username", "_username_")
        put("email", "_email_")
        put("url_picture", "_url_picture_")
    }

    companion object {
        const val USER_ID = 1L
    }

    // METHODS -------------------------------------------------------------------------------------

    @Test
    fun query_shouldBeSuccess() {
        // BEFORE: Retrieve the User (impossible)
        // Uri: content://com.mancel.yann.realestatemanager.providers/user/1
        this.mContentResolver.query(
            ContentUris.withAppendedId(
                Uri.parse(RealEstateContentProvider.TABLE_USER),
                USER_ID
            ),
            null,
            null,
            null,
            null
        )?.use {
            // TEST: No user
            assertThat(it, notNullValue())

            // The database is pre populated with at least on User
            //assertEquals(0, it.count)
        }
    }

    @Test
    fun all_methods_shouldBeSuccess() {
        var rows = 0

        // BEFORE: Retrieve all users
        // Uri: content://com.mancel.yann.realestatemanager.providers/user
        this.mContentResolver.query(
            Uri.parse(RealEstateContentProvider.TABLE_USER),
            null,
            null,
            null,
            null
        )?.use {
            // TEST: All users
            assertThat(it, notNullValue())
            rows = it.count
        }

        // THEN: Add user
        // Uri: content://com.mancel.yann.realestatemanager.providers/user
        val userUri = this.mContentResolver.insert(
            Uri.parse(RealEstateContentProvider.TABLE_USER),
            this.mContentValues
        )

        // THEN: Retrieve the User
        // Uri: content://com.mancel.yann.realestatemanager.providers/user/[X]
        this.mContentResolver.query(
            userUri!!,
            null,
            null,
            null,
            null
        )?.use {
            // TEST: Only one user
            assertThat(it, notNullValue())
            assertEquals(1, it.count)
            assertTrue(it.moveToFirst())
            assertEquals(
                this.mContentValues.getAsString("username"),
                it.getString(
                    it.getColumnIndexOrThrow("username")
                )
            )
        }

        // THEN: Retrieve all users
        // Uri: content://com.mancel.yann.realestatemanager.providers/user
        this.mContentResolver.query(
            Uri.parse(RealEstateContentProvider.TABLE_USER),
            null,
            null,
            null,
            null
        )?.use {
            // TEST: All users
            assertThat(it, notNullValue())
            assertEquals(rows + 1, it.count)
        }

        // THEN: Update the User
        val updateContentValue = ContentValues(this.mContentValues).apply {
            put("id_user", ContentUris.parseId(userUri))
            put("username", "_Update_Name_")
        }

        // Uri: content://com.mancel.yann.realestatemanager.providers/user/[X]
        this.mContentResolver.update(
            userUri,
            updateContentValue,
            null,
            null
        ).let {
            // TEST: Only one row updated
            assertEquals(1, it)
        }

        // THEN: Delete the User
        // Uri: content://com.mancel.yann.realestatemanager.providers/user/[X]
        this.mContentResolver.delete(
            userUri,
            null,
            null
        ).let {
            // TEST: Only one row deleted
            assertEquals(1, it)
        }

        // THEN: Retrieve all users
        // Uri: content://com.mancel.yann.realestatemanager.providers/user
        this.mContentResolver.query(
            Uri.parse(RealEstateContentProvider.TABLE_USER),
            null,
            null,
            null,
            null
        )?.use {
            // TEST: All users
            assertThat(it, notNullValue())
            assertEquals(rows, it.count)
        }
    }
}