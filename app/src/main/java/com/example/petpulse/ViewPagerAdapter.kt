package com.example.petpulse.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.petpulse.OverviewFragment
import com.example.petpulse.ui.pet.MedicalFragment
import com.example.petpulse.ui.vaccines.VaccinesFragment
import com.example.petpulse.ui.pet.WeightFragment

class ViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OverviewFragment()
            1 -> MedicalFragment()
            2 -> VaccinesFragment()
            3 -> WeightFragment()
            else -> OverviewFragment()
        }
    }
}
