package com.databridge.mybridge.ui.profile.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.isNullorEmptyNot
import com.databridge.mybridge.base.loadImage
import com.databridge.mybridge.base.loadRoundedImage
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.login.viewmodel.LoginViewModel
import com.databridge.mybridge.databinding.ActivityProfileBinding
import com.databridge.mybridge.domain.model.login.UserInfo
import com.databridge.mybridge.domain.model.profile.ProfileDetail
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.login.forgotpass.view.ForgotPasswordActivity
import com.databridge.mybridge.ui.login.signup.view.SignupActivity
import com.databridge.mybridge.ui.onboarding.view.OnboardingActivity
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoardingViewModel
import com.databridge.mybridge.ui.profile.utils.ProfileDetailAdapter
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource


class ProfileActivity : BaseActivity(), View.OnClickListener {

    // binding
    private var _binding: ActivityProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private val onviewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(activity, R.layout.activity_profile)

        init()
        initObserver()

    }

    private fun initObserver() {

        setProfileData()

        onviewModel.userInforesponse.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        setProfileDetail(it)

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

        onviewModel.profileInfoApiCall()

    }

    private fun setProfileDetail(uInfo: UserInfo?) {

        uInfo?.let { it ->
            it.connectCount?.let {
                binding.tvConnections.text = it + " " + getString(R.string.connections)
            }
            it.followCount?.let { it ->
                binding.tvFollow.text = it.toString()
            }
        }

        setProfileData()
        setProfileAdapter(uInfo)
    }

    private fun setProfileData() {
        with(binding) {
//            imgProfile.loadImage(
//                sharedPref.getPrefString(Conts.PREF_PROFILE_PIC) ?: ""
//            )

            val profile = sharedPref.getPrefString(
                Conts.PREF_PROFILE_PIC
            )
            Debug.e("---","--profile--"+profile)
            Debug.e("---","--profile--"+sharedPref.getPrefString(
                Conts.PREF_PROFILE_PIC_CHARS
            ))
            if (profile?.isNullorEmptyNot() == true) {
                Debug.e("---","--profile-3-"+profile)
                imgProfile.loadRoundedImage(
                    profile!!,
                    R.drawable.profile_frame
                )
            } else {
                imgProfile.setImageResource(R.drawable.profile_frame)
                tvImageName.text = sharedPref.getPrefString(
                    Conts.PREF_PROFILE_PIC_CHARS
                ) ?: ""
            }


            tvName.text = sharedPref.getPrefString(Conts.PREF_DISPLAY_NAME) ?: ""
            tvDesignation.text = sharedPref.getPrefString(Conts.PREF_EXP_TITLE) ?: ""

            var location = ""

            sharedPref.getPrefString(Conts.PREF_CITY).let {
                location =
                    sharedPref.getPrefString(Conts.PREF_CITY).toString()
            }

            sharedPref.getPrefString(Conts.PREF_STATE).let {
                location += ", " + sharedPref.getPrefString(Conts.PREF_STATE).toString()
            }

            sharedPref.getPrefString(Conts.PREF_COUNTRY).let {
                location += ", " + sharedPref.getPrefString(Conts.PREF_COUNTRY).toString()
            }

            tvLocation.text = location
        }

    }

    private fun init() {

        // click event
//        binding.tvForgotPass.setOnClickListener(this)
//        binding.tvSignup.setOnClickListener(this)
        with(binding) {
            top.ivBack.setOnClickListener {
                finish()
            }
            btnAddSection.setOnClickListener {

            }

            ivMore.setOnClickListener {

            }

            ivProfileCamera.setOnClickListener {

            }

            ivBannerCamer.setOnClickListener {

            }
        }


    }

    private fun setProfileAdapter(userInfo: UserInfo?) {
        val profileDetailList = ArrayList<ProfileDetail>()
        userInfo?.let {
            it.email?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_email,
                            getString(R.string.email),
                            it
                        )
                    )
            }
            it.profileMobileNumber?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_phone,
                            getString(R.string.mobile_number),
                            userInfo.mobileNumber
                        )
                    )
            }
            it.langName?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_language,
                            getString(R.string.languages),
                            it
                        )
                    )
            }

            it.dateOfBirth?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.calendar_month,
                            getString(R.string.birth_date),
                            it
                        )
                    )
            }

            it.location?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_location_on,
                            getString(R.string.location),
                            it
                        )
                    )
            }

            it.interestedIn?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_interests,
                            getString(R.string.interests),
                            it
                        )
                    )
            }

            it.causesYouCare?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.animal_welfare,
                            getString(R.string.animal_wel_info),
                            it
                        )
                    )
            }

            it.website?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_language,
                            getString(R.string.website),
                            it
                        )
                    )
            }

            it.socialLinks?.let { it ->
                if (it.size > 0)
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_language,
                            getString(R.string.social_link),
                            it[0]
                        )
                    )
            }

            it.gender?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_gender,
                            getString(R.string.gender),
                            it
                        )
                    )
            }
            it.pronouns?.let { it ->
                if (it.isNullorEmptyNot())
                    profileDetailList.add(
                        ProfileDetail(
                            R.drawable.ic_pronouns,
                            getString(R.string.system_pronouns),
                            it
                        )
                    )
            }


        }

        with(binding) {
            val profileAdapter = ProfileDetailAdapter(activity) { position, view ->

            }
            rvProfile.adapter = profileAdapter
            profileAdapter.swapList(profileDetailList)
        }
    }

    fun onForgotPassword() {
        try {
            val i = Intent(activity, ForgotPasswordActivity::class.java)
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun goToDashbaord() {
        var intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun goToOnboarding() {
        var intent = Intent(activity, OnboardingActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun onRegister() {
        val i = Intent(activity, SignupActivity::class.java)
        startActivity(i)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivBannerCamer -> {
            }

            binding.ivProfileCamera -> {
                onRegister()
            }

            binding.ivMore -> {
                onForgotPassword()
            }

            binding.btnAddSection -> {

            }
        }
    }


}