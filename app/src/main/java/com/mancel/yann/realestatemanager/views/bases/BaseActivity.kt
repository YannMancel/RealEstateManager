package com.mancel.yann.realestatemanager.views.bases

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Created by Yann MANCEL on 19/02/2020.
 * Name of the project: RealEstateManager
 * Name of the package: com.mancel.yann.realestatemanager.views.bases
 *
 * A [AppCompatActivity] subclass.
 */
abstract class BaseActivity : AppCompatActivity() {

    // METHODS -------------------------------------------------------------------------------------

    /**
     * Gets the integer value of the activity layout
     * @return an integer that corresponds to the activity layout
     */
    protected abstract fun getActivityLayout(): Int

    /**
     * Get the [Toolbar]
     * @return a [Toolbar]
     */
    protected abstract fun getToolBar(): Toolbar?

    /**
     * Configures the design of each daughter class
     */
    protected abstract fun configureDesign()

    // -- Activity --

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(this.getActivityLayout())

        // Configures the design
        this.configureDesign()
    }

    // -- Toolbar --

    /**
     * Configures the [Toolbar]
     */
    protected fun configureToolbar() {
        // No Toolbar
        if (this.getToolBar() == null) {
            return
        }

        this.setSupportActionBar(this.getToolBar())
    }
}