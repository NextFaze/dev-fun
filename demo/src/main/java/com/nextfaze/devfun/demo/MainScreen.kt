package com.nextfaze.devfun.demo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.snackbar.Snackbar
import com.nextfaze.devfun.demo.inject.ActivityInjector
import com.nextfaze.devfun.demo.kotlin.startActivity
import com.nextfaze.devfun.function.DeveloperFunction
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.nav_header_main.*
import javax.inject.Inject

class MainActivity : BaseActivity() {
    companion object {
        fun start(context: Context) = context.startActivity<MainActivity>(FLAG_ACTIVITY_CLEAR_TASK)
    }

    @Inject lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        setSupportActionBar(toolbarView)

        floatingActionButton.setOnClickListener {
            Snackbar.make(it, "Replace with your own action!", Snackbar.LENGTH_LONG).setAction("Action", null).show()
        }

        object : ActionBarDrawerToggle(this, drawerLayout, toolbarView, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            override fun onDrawerStateChanged(newState: Int) {
                navHeaderName?.text = getString(R.string.name_format, session.user?.givenName, session.user?.familyName)
                navHeaderEmail?.text = session.user?.email
            }
        }.apply {
            drawerLayout.addDrawerListener(this)
            syncState()
        }

        navigationView.setNavigationItemSelectedListener setSelected@{
            when (it.itemId) {
                R.id.nav_camera -> {
                }
                R.id.nav_gallery -> {
                }
                R.id.nav_slideshow -> {
                }
                R.id.nav_manage -> {
                }
                R.id.nav_share -> {
                }
                R.id.nav_send -> {
                }
            }

            drawerLayout.closeDrawer(GravityCompat.START)
            return@setSelected true
        }
    }

    override fun onBackPressed() = when {
        drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
        else -> super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when {
        item.itemId == R.id.action_settings -> true
        else -> super.onOptionsItemSelected(item)
    }

    override fun inject(injector: ActivityInjector) = injector.inject(this)

    @DeveloperFunction
    @SuppressLint("SetTextI18n")
    private fun doubleHelloWorld() {
        helloWorldTextView.text = helloWorldTextView.text.toString() + " " + helloWorldTextView.text
    }
}
