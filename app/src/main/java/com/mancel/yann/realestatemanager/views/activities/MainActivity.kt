package com.mancel.yann.realestatemanager.views.activities

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.utils.convertDollarToEuro
import com.mancel.yann.realestatemanager.utils.convertEuroToDollar
import com.mancel.yann.realestatemanager.utils.getTodayDateDDMMYYYY
import com.mancel.yann.realestatemanager.utils.getTodayDateYYYYMMDD
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

        // Navigation
        this.configureFragmentNavigation()

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // NavController
        val navController = this.findNavController(R.id.activity_main_NavHostFragment)

        return item!!.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (this.activity_main_drawer_layout.isDrawerOpen(GravityCompat.START)) {
            this.activity_main_drawer_layout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }

    // -- Navigation --

    /**
     * Configures the fragment navigation
     */
    private fun configureFragmentNavigation() {
        // NavController
        val navController = this.findNavController(R.id.activity_main_NavHostFragment)

        // AppBarConfiguration
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_listFragment,
                                                            R.id.navigation_settingsFragment),
                                                      this.activity_main_drawer_layout)

        // Toolbar
        this.activity_main_Toolbar.setupWithNavController(navController,
                                                          appBarConfiguration)

        // NavigationView
        this.activity_main_navigation_view.setupWithNavController(navController)
    }
}