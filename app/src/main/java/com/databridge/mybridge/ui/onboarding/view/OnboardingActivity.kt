package com.databridge.mybridge.ui.onboarding.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.databridge.mybridge.R
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.databinding.ActivityOnboardingBinding
import com.databridge.mybridge.domain.model.login.OnboardingDataResponse
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.onboarding.utils.OnboardingPagerAdapter
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoardingViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource


class OnboardingActivity : BaseActivity(), View.OnClickListener {

    // binding
    private var _binding: ActivityOnboardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnBoardingViewModel by viewModels()

    public lateinit var pagerAdapter: OnboardingPagerAdapter
    public lateinit var viewPager: ViewPager2

    private val onviewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(activity, R.layout.activity_onboarding)

        init()
        initObserver()
    }

    private fun initObserver() {
        // login api response
        viewModel.response.observe(activity) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        handleState(it)
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }

        onviewModel.userInforesponse.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
//                        if (it!!.isOnboardingCompleted == true) {
//                            goToDashbaord()
//                        }
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    showToast(getString(R.string.user_not_found))
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }

        if (isInternetConnected(activity)) {
            viewModel.apiCall()
            onviewModel.profileInfoApiCall()
        }
    }

    private fun goToDashbaord() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    var step = 0
    private fun handleState(it: OnboardingDataResponse?) {
        it?.data?.let {
            if (it.profile == true)
                step = 1
            if (it.employment == true)
                step = 2
            if (it.photo == true)
                step = 3
            if (it.jobAvailabilty == true)
                step = 4
            if (it.connect == true)
                step = 5

//            if (it.accountActive == true)
//                goToDashbaord()
//                step = 5

        }
        Debug.e("----", "--step--$step")
        viewPager.setCurrentItem(step, true)

    }

    fun init() {
        val fragments = listOf(
            OnBoarding1Fragment(),
            OnBoarding2Fragment(),
            OnBoarding3Fragment(),
            OnBoarding4Fragment(),
            OnBoarding6Fragment()
        ) // Replace with your fragment instances

        pagerAdapter = OnboardingPagerAdapter(activity, fragments)
        viewPager = binding.pager
        viewPager.adapter = pagerAdapter

        viewPager.isUserInputEnabled = false

    }

    override fun onClick(v: View?) {
    }

}