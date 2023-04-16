package com.example.csi_dmce.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.csi_dmce.R
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.ui.WelcomeActivity
import com.example.csiappdashboard.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class DashMainActivity : AppCompatActivity() {
    lateinit var bottomNavBar : BottomNavigationView
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbars))

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        navView.itemIconTintList = null


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {


            when(it.itemId){
                R.id.nav_expenses_claim -> Toast.makeText(this,"Expense Claim",Toast.LENGTH_LONG).show()
                R.id.nav_about_us -> Toast.makeText(this,"About Us",Toast.LENGTH_LONG).show()
                R.id.nav_starred_events -> Toast.makeText(this,"Starred Events",Toast.LENGTH_LONG).show()
                R.id.nav_past_events -> Toast.makeText(this,"Past Events",Toast.LENGTH_LONG).show()
                R.id.nav_logout ->  {
                    CsiAuthWrapper.deleteAuthToken(applicationContext)
                    val intent = Intent(applicationContext, WelcomeActivity::class.java)
                    finishAffinity()
                    startActivity(intent)
                }
            }
            true
        }

        loadFragment(DashboardFragment())
        bottomNavBar = findViewById(R.id.bNav)
        bottomNavBar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(DashboardFragment())

                    true
                }
                R.id.search -> {
                    loadFragment(EventFragment())
                    true
                }
                R.id.profile -> {
                    loadFragment(ProfileFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)){
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameL,fragment)
        transaction.commit()
    }
}

