package com.example.csi_dmce.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.csi_admin.complaint.ComplaintLodge
import com.example.csi_admin.complaint.ComplaintsActivity
import com.example.csi_admin.expense.ApprovalExpenseActivity
import com.example.csi_admin.expense.ExpenseRequest
import com.example.csi_admin.user.UserListActivity
import com.example.csi_dmce.R
import com.example.csi_dmce.attendance.CsvGeneration
import com.example.csi_dmce.auth.CsiAuthWrapper
import com.example.csi_dmce.database.Student
import com.example.csi_dmce.database.StudentWrapper
import com.example.csi_dmce.ui.WelcomeActivity
import com.example.csiappdashboard.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.runBlocking


class DashMainActivity : AppCompatActivity() {
    lateinit var bottomNavBar : BottomNavigationView
    lateinit var toggle: ActionBarDrawerToggle
    private lateinit var studentObject: Student

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbars))

        studentObject = runBlocking {
            StudentWrapper.getStudent(CsiAuthWrapper.getStudentId(applicationContext))!!
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView : NavigationView = findViewById(R.id.nav_view)
        val navHeaderView = navView.getHeaderView(0)

        if (CsiAuthWrapper.getRoleFromToken(this).isAdmin()) {
            navView.menu.clear()
            navView.inflateMenu(R.menu.admin_side_nav)
        }

        navView.itemIconTintList = null

        val navHeaderImage: ImageView = navHeaderView.findViewById(R.id.nav_header_image)

        runBlocking {
            StudentWrapper.getStudentAvatarUrl(studentObject.student_id!!, studentObject.avatar_extension!!) {
                Glide.with(applicationContext)
                    .setDefaultRequestOptions(RequestOptions())
                    .load(it?: R.drawable.ic_baseline_person_24)
                    .into(navHeaderImage)
            }
        }


        val navHeaderName: TextView = navHeaderView.findViewById(R.id.nav_header_name)
        navHeaderName.setText(studentObject.name?: "User")

        val navHeaderDesignation: TextView = navHeaderView.findViewById(R.id.text_view_csi_designation)
        navHeaderDesignation.setText(
            if (CsiAuthWrapper.getRoleFromToken(this).isAdmin()) {
                "CSI Admin"
            } else {
                "CSI Member"
            }
        )


        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (CsiAuthWrapper.getRoleFromToken(this).isAdmin()) {
            navView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.nav_item_admin_approve_expenses -> {
                        val eIntent = Intent(this, ApprovalExpenseActivity::class.java)
                        startActivity(eIntent)
                    }
                    R.id.nav_item_admin_registered_users -> {
                        val rIntent = Intent(this, UserListActivity::class.java)
                        startActivity(rIntent)
                    }
                    R.id.nav_item_admin_user_complaints -> {
                        val cIntent = Intent(this, ComplaintsActivity::class.java)
                        startActivity(cIntent)
                    }
                    R.id.nav_item_admin_export_sheets -> {
                        val cIntent = Intent(this, CsvGeneration::class.java)
                        startActivity(cIntent)
                    }
                    R.id.nav_item_admin_logout ->  {
                        CsiAuthWrapper.deleteAuthToken(applicationContext)
                        val intent = Intent(applicationContext, WelcomeActivity::class.java)
                        finishAffinity()
                        startActivity(intent)
                    }
                }
                true
            }

            } else {
                navView.setNavigationItemSelectedListener {
                    when(it.itemId){
                        R.id.nav_expenses_claim -> {
                            val eIntent = Intent(this, ExpenseRequest::class.java)
                            startActivity(eIntent)
                        }
                        R.id.nav_starred_events -> Toast.makeText(this,"Starred Events",Toast.LENGTH_LONG).show()
                        R.id.nav_past_events -> Toast.makeText(this,"Past Events",Toast.LENGTH_LONG).show()
                        R.id.nav_complaint -> {
                            val cIntent = Intent(this, ComplaintLodge::class.java)
                            startActivity(cIntent)
                        }
                        R.id.nav_logout ->  {
                            CsiAuthWrapper.deleteAuthToken(applicationContext)
                            val intent = Intent(applicationContext, WelcomeActivity::class.java)
                            finishAffinity()
                            startActivity(intent)
                        }
                    }
                    true
                }
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
