package com.mancel.yann.realestatemanager.views.activities

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.mancel.yann.realestatemanager.R
import com.mancel.yann.realestatemanager.views.bases.BaseActivity
import com.mancel.yann.realestatemanager.views.fragments.DetailsFragmentArgs
import com.mancel.yann.realestatemanager.views.fragments.FragmentListener
import com.mancel.yann.realestatemanager.views.fragments.ListFragmentDirections
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

    // FIELDS --------------------------------------------------------------------------------------

    private val mNavController by lazy {this.findNavController(R.id.activity_main_NavHostFragment)}

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

        // Test
        this.mViewModel.getCountOfRealEstatesByUserId(1L).observe(this@MainActivity, Observer {
            count -> Log.w(this@MainActivity::class.simpleName, "COUNT = $count")

            //this.mViewModel.getCountOfRealEstatesByUserId(1L).removeObserver(this)
        })

        Log.w(this@MainActivity::class.simpleName, "NAVIGATION")

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
        return item!!.onNavDestinationSelected(this.mNavController) || super.onOptionsItemSelected(item)
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

    override fun navigateToDetailsFragment(v: View?) {
        // Tag
        val itemId = v?.tag as Long

        // According to the device type
        when (this.mMode) {
            Mode.PHONE_MODE -> {
                // By action (Safe Args)
                val action = ListFragmentDirections.actionListFragmentToDetailsFragment(itemId = itemId)
                this.mNavController.navigate(action)
            }

            Mode.TABLET_MODE -> {
                // By destination (Safe Args)
                val bundle = DetailsFragmentArgs(itemId = itemId).toBundle()
                this.mNavController.navigate(R.id.navigation_detailsFragment, bundle)
            }
        }
    }

    // -- Mode --

    /**
     * Checks the device type: phone or tablet
     */
    private fun checkMode() {
        // Checks the mode
        this.mMode = if (this.findViewById<View>(R.id.activity_main_fragment) != null)
                         Mode.TABLET_MODE
                     else
                         Mode.PHONE_MODE
    }

    // -- Navigation --

    /**
     * Configures the fragment navigation
     */
    private fun configureFragmentNavigation() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val homeFragments = when (this.mMode) {
                                Mode.PHONE_MODE -> {
                                    setOf(R.id.navigation_listFragment)
                                }

                                Mode.TABLET_MODE -> {
                                    setOf(R.id.navigation_detailsFragment)
                                }
                            }

        // AppBarConfiguration
        val appBarConfiguration = AppBarConfiguration(homeFragments,
                                                      this.activity_main_drawer_layout)

        // Toolbar
        this.activity_main_Toolbar.setupWithNavController(this.mNavController,
                                                          appBarConfiguration)

        // NavigationView
        this.activity_main_navigation_view.setupWithNavController(this.mNavController)
    }
}