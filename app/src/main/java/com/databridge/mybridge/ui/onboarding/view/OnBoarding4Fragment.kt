package com.databridge.mybridge.ui.onboarding.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.databinding.FragmentOnboarding4Binding
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoarding4ViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource

class OnBoarding4Fragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentOnboarding4Binding

    var current = 3
    private val viewModel: OnBoarding4ViewModel by viewModels()
    var jobAvilable = 0
    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentOnboarding4Binding>(
            inflater,
            R.layout.fragment_onboarding4,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
        init()
        initObserver()
    }

    private fun init() {
        binding.tvOnBoardingStep.iv2.setImageResource(R.drawable.onboarding_step)
        binding.tvOnBoardingStep.iv3.setImageResource(R.drawable.onboarding_step)
        binding.tvOnBoardingStep.iv4.setImageResource(R.drawable.onboarding_step)

        binding.btnNext.setOnClickListener(this)
        binding.tvBack.setOnClickListener(this)

        binding.radioGp.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.tvC1) {
                jobAvilable = 0
            } else if (checkedId == R.id.tvC2) {
                jobAvilable = 1
            } else if (checkedId == R.id.tvC3) {
                jobAvilable = 2
            }

        }
    }

    private fun goToNext() {
        (requireActivity() as OnboardingActivity).viewPager.setCurrentItem(current + 1, true)
    }

    private fun goToBack() {
        (requireActivity() as OnboardingActivity).viewPager.setCurrentItem(current - 1, true)
    }

    private fun initObserver() {
        // onboarding api response
        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        goToNext()
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    requireContext().showToast(getString(R.string.user_not_found))
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getresponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {

                    }
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
//                    requireContext().showToast(getString(R.string.api_error))
                }

                Resource.Status.LOADING -> {
                }
            }
        }

        if (isInternetConnected(activity))
            viewModel.getOnboardingapiCall()
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvBack -> {
                goToBack()
            }

            binding.btnNext -> {
                Debug.e("-----", "---onClick-btnNext-")
                Debug.e("----", "--btnLogin-click-")
                with(binding) {
                    with(viewModel) {
                        requireActivity().hideKeyboard()
                        if (isInternetConnected(activity))
                            apiCall(jobAvilable.toString())
                    }
                }
            }
        }
    }

}