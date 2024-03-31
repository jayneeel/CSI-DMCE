package com.example.csi_dmce.agenda

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.csi_dmce.R
import com.google.android.material.tabs.TabLayout

lateinit var  Myadapter: tabsadapter
class AgendaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agenda)

        val  add=findViewById<ImageView>(R.id.imageView14)

        var tabl=findViewById<TabLayout>(R.id.tabLayout)
        var viewp=findViewById<ViewPager2>(R.id.viewp)
        Myadapter = tabsadapter(supportFragmentManager,lifecycle)
        viewp.adapter= Myadapter

        tabl.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    viewp.currentItem=tab.position
                }

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        viewp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabl.selectTab(tabl.getTabAt(position))
            }
        })

        add.setOnClickListener {
            val cIntent = Intent(this, CreateAgendaActivity::class.java)
            startActivity(cIntent)
        }


    }
}