package com.mancel.yann.realestatemanager.views.activities

import android.content.Intent
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
import com.mancel.yann.realestatemanager.utils.MessageTools
import com.mancel.yann.realestatemanager.views.fragments.DetailsFragmentArgs
import com.mancel.yann.realestatemanager.views.fragments.EditFragmentArgs
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

    private val mNavController by lazy {
        this.findNavController(R.id.activity_main_NavHostFragment)
    }

    private var mItemId: Long = 0L

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
        this.menuInflater.inflate(R.menu.toolbar_menu, menu)
        this.configureBehaviorOfToolBar()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // Edit fragment (Need to pass data to start fragment)
        item?.itemId.let {
            if (it == R.id.toolbar_menu_edit) {
                // By destination (Safe Args)
                val bundle = EditFragmentArgs(this.mItemId).toBundle()
                this.mNavController.navigate(R.id.navigation_editFragment, bundle)

                return true
            }
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Call the onActivityResult method of current Fragment
        this.supportFragmentManager
            .findFragmentById(R.id.activity_main_NavHostFragment)!!
            .childFragmentManager
            .fragments[0]
            .onActivityResult(requestCode, resultCode, data)
    }

    // -- FragmentListener interface --

    override fun showMessage(message: String) {
        MessageTools.showMassageWithSnackbar(
            this.activity_main_CoordinatorLayout,
            message
        )
    }

    override fun navigateToDetailsFragment(v: View?) {
        // Tag
        this.mItemId = v?.tag as Long

        // According to the device type
        when (this.mMode) {
            Mode.PHONE_MODE -> {
                // By action (Safe Args)
                val action = ListFragmentDirections.actionListFragmentToDetailsFragment(this.mItemId)
                this.mNavController.navigate(action)
            }

            Mode.TABLET_MODE -> {
                // By destination (Safe Args)
                val bundle = DetailsFragmentArgs(this.mItemId).toBundle()
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
        // AppBarConfiguration (According to the device type)
        val appBarConfiguration = when (this.mMode) {
            Mode.PHONE_MODE -> {
                AppBarConfiguration(
                    this.mNavController.graph,
                    this.activity_main_drawer_layout
                )
            }

            Mode.TABLET_MODE -> {
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                AppBarConfiguration(
                    setOf(R.id.navigation_detailsFragment),
                    this.activity_main_drawer_layout
                )
            }
        }

        // Toolbar
        this.activity_main_Toolbar.setupWithNavController(this.mNavController,
                                                          appBarConfiguration)

        // NavigationView
        this.activity_main_navigation_view.setupWithNavController(this.mNavController)
    }

    // -- Behaviors of Toolbar --

    /**
     * Configures the behavior of the [Toolbar]
     */
    private fun configureBehaviorOfToolBar() {
        this.mNavController.addOnDestinationChangedListener { _, destination, _ ->
            val addItem = this.getToolBar()!!
                              .menu!!
                              .findItem(R.id.navigation_creatorFragment)

            val editItem = this.getToolBar()!!
                               .menu!!
                               .findItem(R.id.toolbar_menu_edit)

            val searchItem = this.getToolBar()!!
                                 .menu!!
                                 .findItem(R.id.navigation_locationFragment)

            when (destination.id) {
                R.id.navigation_listFragment -> {
                    addItem.isVisible = true
                    editItem.isVisible = false
                    searchItem.isVisible = true
                }

                R.id.navigation_detailsFragment -> {
                    addItem.isVisible = true
                    editItem.isVisible = true
                    searchItem.isVisible = false
                }

                R.id.navigation_locationFragment -> {
                    addItem.isVisible = false
                    editItem.isVisible = false
                    searchItem.isVisible = false
                }

                R.id.navigation_creatorFragment,
                R.id.navigation_editFragment -> {
                    addItem.isVisible = true
                    editItem.isVisible = false
                    searchItem.isVisible = false
                }

                else -> { /* Ignore all other ids */ }
            }
        }
    }
}