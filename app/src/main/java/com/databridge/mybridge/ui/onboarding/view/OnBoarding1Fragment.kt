package com.databridge.mybridge.ui.onboarding.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.databinding.FragmentOnboardingBinding
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoarding1ViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.Conts

class OnBoarding1Fragment : BaseFragment(), View.OnClickListener {


    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnBoarding1ViewModel by viewModels()

    var countryCode = "IN"
    var ifPostalCode = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<FragmentOnboardingBinding>(
            inflater,
            R.layout.fragment_onboarding,
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

    private fun initObserver() {
        // country error
        viewModel.countryError.observe(viewLifecycleOwner) {
            binding.tvCountryError.visibility = View.VISIBLE
            binding.tvCountryError.text = it
        }
        // city error
        viewModel.cityError.observe(viewLifecycleOwner) {
            binding.tvCityError.visibility = View.VISIBLE
            binding.tvCityError.text = it
        }

        viewModel.postalCodeError.observe(viewLifecycleOwner) {
            binding.tvPostalCodeError.visibility = View.VISIBLE
            binding.tvPostalCodeError.text = it
        }

        viewModel.countryresponse.observe(viewLifecycleOwner) {
            var country = it.countryName

            if (it.city != null) {
                binding.etCity.text = it.city
                if (it.region != null)
                    binding.etCity.text = it.city + ", " + it.region
            }
            binding.etCountry.text = country
            countryCode = it.countryCode!!
            viewModel.countryCode = countryCode
            setPostalCodeVisibility()
        }

        // onboarding api response
        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.saveInPref(binding)
                        viewModel.saveOnBoardingDataInPref(it)
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
//                        if (it?.data?.profile == true) {
                        Debug.e("----", "--onboaridng api-country-" + it?.data!!.country)
                        it?.data?.let { it ->
                            binding.etCountry.text = it.country ?:""
                            binding.etCity.text = it.city ?: ("" + ", " + it.state) ?: ""
                            if (it.postalCode != null) {
                                binding.etPostalcode.setText(it.postalCode)
                            }
                        }

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

    private fun setPostalCodeVisibility() {
        if (countryCode == "US" || countryCode == "UK") {
            binding.llPostal.visibility = View.VISIBLE
            binding.llCity.visibility = View.GONE
            ifPostalCode = true
        } else {
            binding.llCity.visibility = View.VISIBLE
            binding.llPostal.visibility = View.GONE
            ifPostalCode = false
        }

    }

    private fun goToNext() {
        (requireActivity() as OnboardingActivity).viewPager.setCurrentItem(1, true)
    }

    private fun init() {

        try {
            binding.tvWelName.text =
                getString(R.string.welcome_) + sharedPref.getPrefString(Conts.PREF_DISPLAY_NAME) + "!"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        viewModel.getIpAddress()
        // text watcher
        binding.etCountry.addTextChangedListener(viewModel.countryextWatcher())
        binding.etCity.addTextChangedListener(viewModel.cityTextWatcher())
        binding.etPostalcode.addTextChangedListener(viewModel.postalTextWatcher())

        // click event
        binding.btnNext.setOnClickListener(this)
        binding.etCountry.setOnClickListener(this)
        binding.etCity.setOnClickListener(this)


    }

    private fun showFullScreenDialog() {
        Debug.e("-----", "---showFullScreenDialog-")
        val intent = Intent(activity, SearchLocationActivity::class.java)
        intent.putExtra("countryCode", countryCode)
//        startActivity(intent)

        searchactivityResultLauncher.launch(intent)

    }

    var searchactivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val country = data?.extras?.getString("country")
                val city = data?.extras?.getString("city")
                val state = data?.extras?.getString("state")
                val postalCode = data?.extras?.getString("postalCode")

                Debug.e("-----", "---country--$country")
                Debug.e("-----", "---city--$city")
                Debug.e("-----", "---state--$state")
                Debug.e("-----", "---postalCode--$postalCode")
                binding.etCountry.text = country
                if (city != null) {
                    binding.etCity.text = city
                    if (state != null)
                        binding.etCity.text = "$city, $state"
                }
                binding.etPostalcode.setText(postalCode)

                if (binding.llCity.visibility == View.GONE) {
                    if (city != null) {
                        binding.etCity.text = city
                        if (state != null)
                            binding.etCity.text = "$city, $state"
                        binding.tvCityTitle.text =
                            getString(R.string.city_state_or_region_within_this_area)
                        binding.llCity.visibility = View.VISIBLE
                    }
                }

                // Do something with the result data
            }
        }

    override fun onClick(v: View?) {
        Debug.e("-----", "---onClick-")
        when (v) {
            binding.etCountry -> {
                showFullScreenDialog()
            }

            binding.etCity -> {
                showFullScreenDialog()
            }

            binding.btnNext -> {
                Debug.e("-----", "---onClick-btnNext-")
                binding.tvCountryError.visibility = View.GONE
                binding.tvCityError.visibility = View.GONE
                binding.tvPostalCodeError.visibility = View.GONE
                Debug.e("----", "--btnLogin-click-")
                with(binding) {
                    val cityState = etCity.text.toString()
                    val cS = cityState.split(",")
                    val country = etCountry.text.toString()
                    var city = ""
                    var state = ""
                    val postalCode = etPostalcode.text.toString()
                    if (cS.isNotEmpty()) {
                        city = cS[0].trim()

                        if (cS.size > 1)
                            state = cS[1].trim()
                    }
                    with(viewModel) {
                        var valid = validation(country, city)
                        if (ifPostalCode)
                            valid = validation(country, city, postalCode)
                        if (!valid) {
                            countryValidation(country)
                            cityValidation(city)
                            if (ifPostalCode)
                                postalcodeValidation(postalCode)
                            return
                        }
                        requireActivity().hideKeyboard()
                        if (isInternetConnected(activity))
                            apiCall(country, city, state, postalCode)
                    }
                }
            }
        }
    }

}