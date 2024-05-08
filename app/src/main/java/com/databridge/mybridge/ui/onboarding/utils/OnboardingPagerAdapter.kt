package com.databridge.mybridge.ui.onboarding.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class OnboardingPagerAdapter(fragmentManager: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fragmentManager){

//    override fun getCount(): Int  = 100
//
//    override fun getItem(i: Int): Fragment {
//        val fragment = OnBoardingFragment()
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
////            putInt(ARG_OBJECT, i + 1)
//        }
//        return fragment
//    }
//
//    override fun getPageTitle(position: Int): CharSequence {
//        return "OBJECT ${(position + 1)}"
//    }

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}