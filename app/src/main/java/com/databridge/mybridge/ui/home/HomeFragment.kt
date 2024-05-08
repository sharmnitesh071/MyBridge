package com.databridge.mybridge.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.databridge.mybridge.R
import com.databridge.mybridge.adapter.ViewPagerAdapter
import com.databridge.mybridge.base.isNullorEmptyNot
import com.databridge.mybridge.base.loadImage
import com.databridge.mybridge.base.loadRoundedImage
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.FragmentHomeBinding
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.post.createpost.view.CreatePostActivity
import com.databridge.mybridge.ui.profile.view.ProfileActivity
import com.databridge.mybridge.utils.Debug
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : BaseFragment() {

    // binding
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initBottomNavigation()
        initTabLayoutWithViewPager()
    }

    private fun initTabLayoutWithViewPager() {
        if (sharedPref.getPrefBoolean("prefIsLogin")) {
            binding.tabLayout.visibility = View.GONE
            binding.viewPager.setUserInputEnabled(false)
        } else {
            binding.tabLayout.visibility = View.VISIBLE
            binding.viewPager.setUserInputEnabled(true)
        }

        val adapter = ViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = if (position == 0) {
                getString(R.string.post)
            } else {
                getString(R.string.job)
            }
        }.attach()

        // icon
        binding.tabLayout.getTabAt(0)?.setIcon(R.drawable.ic_post);
        binding.tabLayout.getTabAt(1)?.setIcon(R.drawable.ic_job);

    }

    private fun initBottomNavigation() {
        if (sharedPref.getPrefBoolean("prefIsLogin")) {
            (activity as MainActivity).binding.bottomNavigation.visibility = View.VISIBLE
        } else {
            (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE
        }
    }

    private fun initToolbar() {
        (activity as MainActivity).binding.toolbar.tvTitle.visibility = View.GONE
        (activity as MainActivity).binding.toolbar.toolbarLayout.visibility = View.VISIBLE
        if (sharedPref.getPrefBoolean("prefIsLogin")) {
            // login
            (activity as MainActivity).binding.toolbar.groupIcon.visibility = View.VISIBLE
            (activity as MainActivity).binding.toolbar.tilSearch.visibility = View.GONE
            (activity as MainActivity).binding.toolbar1.toolbarLayout1.visibility = View.VISIBLE
//            (activity as MainActivity).binding.toolbar1.imgProfilePic.loadImage(
//                sharedPref.getPrefString(
//                    Conts.PREF_PROFILE_PIC
//                ) ?: ""
//            )

            val profile = sharedPref.getPrefString(
                Conts.PREF_PROFILE_PIC
            )
            if (profile?.isNullorEmptyNot() == true) {
                Debug.e("---","--profile-3-"+profile)
                (activity as MainActivity).binding.toolbar1.imgProfilePic.loadRoundedImage(
                    profile,
                    R.drawable.profile_frame
                )
            } else {
                (activity as MainActivity).binding.toolbar1.imgProfilePic.setImageResource(R.drawable.profile_frame)
                (activity as MainActivity).binding.toolbar1.tvImageName.text = sharedPref.getPrefString(
                    Conts.PREF_PROFILE_PIC_CHARS)
            }

            (activity as MainActivity).binding.toolbar1.imgProfilePic.setOnClickListener {
                goToProfile()
            }

            (activity as MainActivity).binding.toolbar1.imgGallery.setOnClickListener {
                goToCreatePost()
            }

        } else {
            // not login
            (activity as MainActivity).binding.toolbar.groupIcon.visibility = View.GONE
            (activity as MainActivity).binding.toolbar.tilSearch.visibility = View.VISIBLE
            (activity as MainActivity).binding.toolbar1.toolbarLayout1.visibility = View.GONE
        }
    }

    private fun goToCreatePost() {
        val intent = Intent(activity, CreatePostActivity::class.java)
        startActivity(intent)
    }

    private fun goToProfile() {
        requireActivity().showToast("Coming soon")
//        val intent = Intent(activity, ProfileActivity::class.java)
//        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}