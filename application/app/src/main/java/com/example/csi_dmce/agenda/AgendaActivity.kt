package com.example.csi_dmce.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.csi_dmce.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class AgendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        val add = findViewById<ImageView>(R.id.imageView14)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager2>(R.id.viewp)

        val tabsAdapter = TabsAdapter(this)
        viewPager.adapter = tabsAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pending"
                1 -> "Done"
                else -> null
            }
        }.attach()

        add.setOnClickListener {
            val intent = Intent(this, CreateAgendaActivity::class.java)
            startActivity(intent)
        }
    }
}
