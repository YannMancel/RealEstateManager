package com.mancel.yann.realestatemanager.views.activities

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.utils.*
import com.mancel.yann.realestatemanager.views.bases.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Yann MANCEL on 19/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.activities
 *
 * A [BaseActivity] subclass.
 */
class MainActivity : BaseActivity() {

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    override fun getActivityLayout(): Int = R.layout.activity_main

    override fun getToolBar(): Toolbar? = this.activity_main_Toolbar

    override fun configureDesign() {
        // UI
        this.configureToolbar()
        this.configureDrawerLayout()

        Log.d(this::class.java.simpleName, "convertDollarToEuro: ${convertDollarToEuro(dollars = 100)}")
        Log.d(this::class.java.simpleName, "convertEuroToDollar: ${convertEuroToDollar(euros = 81)}")
        Log.d(this::class.java.simpleName, "getTodayDateYYYYMMDD: ${getTodayDateYYYYMMDD()}")
        Log.d(this::class.java.simpleName, "getTodayDateDDMMYYYY: ${getTodayDateDDMMYYYY()}")
    }

    // -- Activity --

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Gets the MenuInflater to add the menu xml file into the Toolbar
        this.menuInflater.inflate(R.menu.toolbar_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when (item?.itemId) {
        // Add
        R.id.toolbar_menu_add -> {
            Log.d(this::class.java.simpleName, "Add")
            true
        }

        // Edit
        R.id.toolbar_menu_edit -> {
            Log.d(this::class.java.simpleName, "Edit")
            true
        }

        // Search
        R.id.toolbar_menu_search -> {
            Log.d(this::class.java.simpleName, "Search")
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (this.activity_main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            this.activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    // -- DrawerLayout --

    /**
     * Configures the DrawerLayout
     */
    private fun configureDrawerLayout() {
        // Creates the Hamburger button of the toolbar
        val toggle = ActionBarDrawerToggle(this,
                                            this.activity_main_drawer_layout,
                                            this.activity_main_Toolbar,
                                            R.string.navigation_drawer_open,
                                            R.string.navigation_drawer_close)

        // Adds the listener (the "Hamburger" button) to the DrawerLayout field
        this.activity_main_drawer_layout.addDrawerListener(toggle)

        // Synchronization
        toggle.syncState()
    }
}