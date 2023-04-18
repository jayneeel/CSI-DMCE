package com.example.csi_dmce.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.csi_dmce.R
import com.example.csi_dmce.ui.WelcomeActivity

class OnboardingMain : AppCompatActivity() {

    private lateinit var onBoardingItemsAdapter: OnBoardingItemsAdapter
    private lateinit var indicatorsContainer:LinearLayout
    private lateinit var btnGetStarted: Button

    private lateinit var btnNext: ImageView
    private lateinit var bgNext: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_main)

        btnGetStarted = findViewById(R.id.btnGetStarted)
        btnNext = findViewById(R.id.nextBtn)
        bgNext = findViewById(R.id.viewBg)

        setOnboardingItems()
        setupIndicators()
        setCurrentIndicator(0)
        Log.d("ONBOARDING", "HERE NOW")
    }

    private fun setOnboardingItems(){
        onBoardingItemsAdapter = OnBoardingItemsAdapter(
            listOf(
                OnboardingItem(
                    R.drawable.onboarding_one,"Eventful events!","Brace yourself for the upcoming fun events!"
                ),
                OnboardingItem(
                    R.drawable.onboarding_two,"Reimbursements!","Want to claim or approve expense requests? We've got you covered!"
                ),
                OnboardingItem(
                    R.drawable.onboarding_three,"Attention to attendance","Your presence is valuable! We've made sure to capture it safely!"
                )
            )
        )
        var onBoardingViewPager = findViewById<ViewPager2>(R.id.onBoardingViewPager)
        onBoardingViewPager.adapter = onBoardingItemsAdapter
        onBoardingViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })
        (onBoardingViewPager.getChildAt(0) as RecyclerView).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        findViewById<ImageView>(R.id.nextBtn).setOnClickListener {
            if(onBoardingViewPager.currentItem+1 < onBoardingItemsAdapter.itemCount){
                onBoardingViewPager.currentItem+=1
            }else{

            navigateToHomeActivity()
            }
        }
        findViewById<TextView>(R.id.textSkip).setOnClickListener {
            navigateToHomeActivity()
        }
        btnGetStarted.setOnClickListener {
            navigateToHomeActivity()
        }
    }

    private fun navigateToHomeActivity(){
        finish()
        val wIntent = Intent(this, WelcomeActivity::class.java)
        startActivity(wIntent)
    }

    private fun setupIndicators(){
        indicatorsContainer = findViewById(R.id.indicatorsContainer)
        val indicators = arrayOfNulls<ImageView>(onBoardingItemsAdapter.itemCount)
        val layoutParams : LinearLayout.LayoutParams = LinearLayout.LayoutParams(WRAP_CONTENT,
            WRAP_CONTENT)
        layoutParams.setMargins(8,0,8,8)
        for (i in indicators.indices){
            indicators[i] = ImageView(applicationContext)
            indicators[i]?.let {
                it.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,R.drawable.indicator_inactive_background))
                it.layoutParams = layoutParams
                indicatorsContainer.addView(it)
            }
        }
    }

    private fun setCurrentIndicator(position: Int){
        val childCount = indicatorsContainer.childCount
        for (i in 0 until childCount){
            if (position == 2) {
                btnGetStarted.visibility = View.VISIBLE
                btnNext.visibility = View.GONE
                bgNext.visibility = View.GONE
            }
            val imageView = indicatorsContainer.getChildAt(i) as ImageView
            if (i == position){
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,R.drawable.indicator_active_background
                ))
            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(
                    applicationContext,R.drawable.indicator_inactive_background
                ))
            }

        }
    }
}