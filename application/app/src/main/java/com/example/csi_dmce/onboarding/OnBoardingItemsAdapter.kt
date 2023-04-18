package com.example.csi_dmce.onboarding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.csi_dmce.R

class OnBoardingItemsAdapter(private val onBoardingItems: List<OnboardingItem>): RecyclerView.Adapter<OnBoardingItemsAdapter.OnboardingItemViewHolder>()
{

    inner class OnboardingItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        private val imageOnboarding = view.findViewById<ImageView>(R.id.imageOnboarding)
        private val titleOnboarding = view.findViewById<TextView>(R.id.titleOnboarding)
        private val descriptionOnboarding = view.findViewById<TextView>(R.id.descriptionOnboarding)


        fun bind(onBoardingItem : OnboardingItem){
            imageOnboarding.setImageResource(onBoardingItem.onBoardingImage)
            titleOnboarding.text = onBoardingItem.title
            descriptionOnboarding.text = onBoardingItem.description

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingItemViewHolder {
        return OnboardingItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.onboarding_item_container,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: OnboardingItemViewHolder, position: Int) {
        holder.bind(onBoardingItems[position])
    }

    override fun getItemCount(): Int {
        return onBoardingItems.size
    }
}