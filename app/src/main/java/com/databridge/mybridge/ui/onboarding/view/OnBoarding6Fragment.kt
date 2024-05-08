package com.databridge.mybridge.ui.onboarding.view

import android.app.Activity
import android.content.Intent
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
import com.databridge.mybridge.databinding.FragmentOnboarding6Binding
import com.databridge.mybridge.domain.model.onboarding.UserSuggestionList
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.onboarding.utils.SuggeastionsAdapter
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoarding6ViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource

class OnBoarding6Fragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentOnboarding6Binding

    private val viewModel: OnBoarding6ViewModel by viewModels()
    lateinit var activity: Activity
    lateinit var suggestionAdapter: SuggeastionsAdapter
    var followCount = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate<FragmentOnboarding6Binding>(
            /* inflater = */ inflater,
            /* layoutId = */ R.layout.fragment_onboarding6,
            /* parent = */ container,
            /* attachToParent = */ false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
        activity = requireActivity()
        init()
        initObserver()
    }

    private fun init() {
        binding.tvOnBoardingStep.iv2.setImageResource(R.drawable.onboarding_step)
        binding.tvOnBoardingStep.iv3.setImageResource(R.drawable.onboarding_step)
        binding.tvOnBoardingStep.iv4.setImageResource(R.drawable.onboarding_step)
        binding.tvOnBoardingStep.iv5.setImageResource(R.drawable.onboarding_step)

        binding.btnFinish.setOnClickListener(this)

        setSuggestionAdapter()

    }

    private fun setSuggestionAdapter() {
        suggestionAdapter = SuggeastionsAdapter(activity) { position, view ->
            when (view.id) {
                R.id.tvHandshake -> {
                    followCount++
                    Debug.e("TAG", "---->> Handshake button Clicked: $position")
                    val cData = suggestionAdapter.datalList[position]
                    if (isInternetConnected(activity)) {
                        viewModel.sendHandshakeRequestApi(cData)
                        cData.isHandshake = true
                        suggestionAdapter.update(position,cData)
                        handleFinishBtnVisibility()
                    }
                }

                R.id.tvDelete -> {
                    Debug.e("TAG", "---->> Follow button Clicked: $position")
                    followCount++
                    val cData = suggestionAdapter.datalList[position]
                    if (isInternetConnected(activity)) {
                        viewModel.sendFollowRequestApi(cData)
                        cData.isFollowing = true
                        suggestionAdapter.update(position,cData)
                        handleFinishBtnVisibility()
                    }
                }
            }

        }
        binding.rvSuggestions.adapter = suggestionAdapter
    }

    private fun handleFinishBtnVisibility() {
        if(followCount > 0){
            binding.btnFinish.visibility = View.VISIBLE
        }
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
                    binding.pbLoader.visibility = View.GONE
                    response.data.let {
                        val dataList: ArrayList<UserSuggestionList> = ArrayList()
                        it?.university?.let { it1 -> dataList.addAll(it1) }
                        it?.role?.let { it1 -> dataList.addAll(it1) }
                        it?.industry?.let { it1 -> dataList.addAll(it1) }
                        it?.degree?.let { it1 -> dataList.addAll(it1) }
                        it?.location?.let { it1 -> dataList.addAll(it1) }
                        it?.follow?.let { it1 -> dataList.addAll(it1) }

                        Debug.e("----", "--dataList--" + dataList.size)

//                        val dList: ArrayList<UserSuggestionList> = ArrayList()
//                        if (dataList.size > 2) {
//                            for (i in 0..2) {
//                                dList.add(dataList[i])
//                            }
//                        }else dList.addAll(dataList)
//                        Debug.e("----", "--dList--" + dList.size)
                        suggestionAdapter.swapList(dataList)

                    }

                }

                Resource.Status.ERROR -> {
                    binding.pbLoader.visibility = View.GONE
                    Debug.e("----", "--message--" + response.message)
//                    requireContext().showToast(getString(R.string.api_error))
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }
        if (isInternetConnected(activity))
            viewModel.getUserSuggestions()
    }

    private fun goToNext() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        activity.finishAffinity()
    }

    override fun onClick(v: View?) {
        when (v) {

            binding.btnFinish -> {
                Debug.e("-----", "---onClick-btnNext-")
                Debug.e("----", "--btnLogin-click-")
                with(binding) {
                    with(viewModel) {
                        requireActivity().hideKeyboard()
                        if (isInternetConnected(activity))
                            apiCall()
                    }
                }
            }
        }
    }
}