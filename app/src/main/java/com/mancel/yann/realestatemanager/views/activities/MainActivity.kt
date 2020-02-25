package com.mancel.yann.realestatemanager.views.activities

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.bases.BaseActivity
import com.mancel.yann.realestatemanager.views.fragments.FragmentListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Yann MANCEL on 19/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.activities
 *
 * A [BaseActivity] subclass which implements [FragmentListener].
 */
class MainActivity : BaseActivity(), FragmentListener {

    // ENUMS ---------------------------------------------------------------------------------------

    private enum class Mode {PHONE_MODE, TABLET_MODE}
    private lateinit var mMode: Mode

    // METHODS -------------------------------------------------------------------------------------

    // -- BaseActivity --

    @LayoutRes
    override fun getActivityLayout(): Int = R.layout.activity_main

    override fun getToolBar(): Toolbar? = this.activity_main_Toolbar

    override fun configureDesign() {
        // UI
        this.configureToolbar()

        // Mode
        this.checkMode()

        // Navigation
        this.configureFragmentNavigation()
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

    // -- FragmentListener interface --

    override fun onClickOnListFragment() {
        // NavController
        val navController = this.findNavController(R.id.activity_main_NavHostFragment)

        when (this.mMode) {
            Mode.PHONE_MODE -> {
                // By action
                navController.navigate(R.id.action_listFragment_to_detailsFragment)
            }

            Mode.TABLET_MODE -> {
                // By destination
                navController.navigate(R.id.navigation_detailsFragment)
            }
        }
    }

    // -- Mode --

    /**
     * Checks the device type: phone or tablet
     */
    private fun checkMode() {
        // Checks the mode
        this.mMode = if (this.findViewById<View>(R.id.activity_main_fragment) != null) {
                         Mode.TABLET_MODE
                     }
                     else {
                         Mode.PHONE_MODE
                     }
    }

    // -- Navigation --

    /**
     * Configures the fragment navigation
     */
    private fun configureFragmentNavigation() {
        // NavController
        val navController = this.findNavController(R.id.activity_main_NavHostFragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val homeFragments = when (this.mMode) {
                                Mode.PHONE_MODE -> {
                                    setOf(R.id.navigation_listFragment,
                                          R.id.navigation_settingsFragment)
                                }

                                Mode.TABLET_MODE -> {
                                    setOf(R.id.navigation_detailsFragment,
                                          R.id.navigation_settingsFragment)
                                }
                            }

        // AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(homeFragments,
                                                      this.activity_main_drawer_layout)

        // Toolbar
        this.activity_main_Toolbar.setupWithNavController(navController,
                                                          appBarConfiguration)

        // NavigationView
        this.activity_main_navigation_view.setupWithNavController(navController)
    }
}