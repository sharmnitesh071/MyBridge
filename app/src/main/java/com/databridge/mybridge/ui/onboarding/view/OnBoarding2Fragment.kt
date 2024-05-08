package com.databridge.mybridge.ui.onboarding.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.indices
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.FragmentOnboarding2Binding
import com.databridge.mybridge.domain.model.onboarding.DateYear
import com.databridge.mybridge.domain.model.onboarding.EmployeeType
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.ui.onboarding.utils.DateYearAdapter
import com.databridge.mybridge.ui.onboarding.utils.EmpTypeAdapter
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoarding2ViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class OnBoarding2Fragment : BaseFragment(), View.OnClickListener {

    var isStudent = false
    lateinit var _binding: FragmentOnboarding2Binding
    private val binding get() = _binding!!

    private val viewModel: OnBoarding2ViewModel by viewModels()
    var eType = ""
    lateinit var activity: Activity
    var schoolData = ""

    var current = 1

    val empList = listOf(
        EmployeeType("Full-Time", "FT"),
        EmployeeType("Part-Time", "PT"),
        EmployeeType("Contractor", "CT"),
        EmployeeType("Temporary", "T"),
        EmployeeType("Other", "O")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate<FragmentOnboarding2Binding>(
            inflater,
            R.layout.fragment_onboarding2,
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
        activity = requireActivity()
        init()
        initObserver()
    }

    private fun init() {

        // text watcher
        binding.etJobTitle.addTextChangedListener(viewModel.jobTextWatcher())
//        binding.etEmpType.addTextChangedListener(viewModel.eTypeTextWatcher())
        binding.etCompany.addTextChangedListener(viewModel.companyTextWatcher())

        binding.tvOnBoardingStep.iv2.setImageResource(R.drawable.onboarding_step)
        setStudentData()
        binding.tvStudent.setOnClickListener {
            isStudent = !isStudent
            setStudentData()
        }

        setEmpSpinner()
//        binding.spEmpType.setOnClickListener(this)
        binding.tvSchool.setOnClickListener(this)
        binding.etCompany.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)
        binding.tvBack.setOnClickListener(this)

        setStudentProfile()
    }

    private fun setStudentProfile() {

        setStartYearSpinner()
        setEndYearSpinner()
        setDaySpinner()
        setYearSpinner()

    }

    private fun setDaySpinner() {
        val monthSpinner = binding.spMonth


        // Populate month Spinner
        val month = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"
        )

        val months = mutableListOf<DateYear>()
        for (element in month) {
            months.add(DateYear(element))
        }


        val monthAdapter = DateYearAdapter(activity, months)
        monthSpinner.adapter = monthAdapter

        // Set listener for month Spinner
        monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
//                val selectedMonth = position + 1 // Months are zero-based in Calendar, so add 1
                val selectedMonth = position  // Months are zero-based in Calendar, so add 1
                Debug.e("----", "--selectedMonth-" + selectedMonth)
                val daysInMonth = getDaysInMonth(selectedMonth)
                updateDaySpinner(daysInMonth)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
    }

    private fun getDaysInMonth(month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    private fun updateDaySpinner(daysInMonth: Int) {
        val daySpinner = binding.spDay
        Debug.e("----", "--daysInMonth-" + daysInMonth)
        val alldays =
            Array(daysInMonth) { i -> (i + 1).toString() } // Create an array of day numbers
        val days = mutableListOf<DateYear>()
        for (m in alldays) {
            days.add(DateYear(m))
        }

        val dayAdapter = DateYearAdapter(activity, days)
        daySpinner.adapter = dayAdapter
    }

    private fun setStartYearSpinner() {
        val yearSpinner = binding.spStartYear
        val startYear = 1900 //  start year
        val endYear = 0 // current year
        val years = getYearList(startYear, endYear)
        val adapter = DateYearAdapter(activity, years)
        yearSpinner.adapter = adapter
    }

    private fun setEndYearSpinner() {
        val yearSpinner = binding.spEndYear
        val startYear = 1900 //  start year
        val endYear = 0 // current year
        val years = getYearList(startYear, endYear)
        val adapter = DateYearAdapter(activity, years)
        yearSpinner.adapter = adapter
    }

    private fun setYearSpinner() {
        val yearSpinner = binding.spYear
        val startYear = 1900 //  start year
        val endYear = 0 // current year
        val years = getYearList(startYear, endYear)
        val adapter = DateYearAdapter(activity, years)
        yearSpinner.adapter = adapter
    }

    fun getYearList(startYear: Int, endYear: Int): List<DateYear> {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = mutableListOf<DateYear>()
        for (year in startYear..currentYear) {
            years.add(DateYear(year.toString()))
        }
        years.reverse()
        return years
    }

    private fun setEmpSpinner() {

        val adapter = EmpTypeAdapter(requireContext(), empList)
        binding.spEmpType.adapter = adapter
    }

    private fun initObserver() {
        // country error
        viewModel.jobTitleError.observe(viewLifecycleOwner) {
            binding.tvJobTitleError.visibility = View.VISIBLE
            binding.tvJobTitleError.text = it
        }
        // Emp error
        viewModel.empError.observe(viewLifecycleOwner) {
            binding.tvEmpError.visibility = View.VISIBLE
            binding.tvEmpError.text = it
        }

        viewModel.companyError.observe(viewLifecycleOwner) {
            binding.tvCompanyError.visibility = View.VISIBLE
            binding.tvCompanyError.text = it
        }

        viewModel.collageError.observe(viewLifecycleOwner) {
            binding.tvSchoolError.visibility = View.VISIBLE
            binding.tvSchoolError.text = it
        }

        viewModel.yearError.observe(viewLifecycleOwner) {
            binding.tvYearError.visibility = View.VISIBLE
            binding.tvYearError.text = it
        }

        viewModel.dobError.observe(viewLifecycleOwner) {
            binding.tvDaterror.visibility = View.VISIBLE
            binding.tvDaterror.text = it
        }


//        viewModel.countryresponse.observe(viewLifecycleOwner) {
//            var country = it.countryName
//
//            if (it.city != null) {
//                binding.etCity.text = it.city
//                if (it.region != null)
//                    binding.etCity.text = it.city + ", " + it.region
//            }
//            binding.etJobTitle.text = country
//            countryCode = it.countryCode!!
//            viewModel.countryCode = countryCode
//            setPostalCodeVisibility()
//        }

        // login api response
        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.saveInPref(binding, isStudent)
                        viewModel.saveOnBoardingDataInPref(it)
                        if (sharedPref.getPrefBoolean(Conts.PREF_IS_EMAIL_VERIFIED).not())
                            goToConfirmEmail()
                        else goToNext()
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    requireContext().showToast(getString(R.string.api_error))
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
                    try {
                        response?.data?.let {
                            it?.let {

                                if (it.isStudent == true) {
                                    if (it.data != null) {
                                        binding.tvSchool.setText(it.data!!.name)
                                        val sYear = it.data!!.startYear ?: ""

                                        for (i in binding.spStartYear.indices) {
                                            if (sYear == binding.spStartYear.getItemAtPosition(i)) {
                                                binding.spStartYear.setSelection(i, true)
                                                break
                                            }
                                        }
                                        val eYear = it.data!!.endYear ?: ""

                                        for (i in binding.spEndYear.indices) {
                                            if (eYear == binding.spEndYear.getItemAtPosition(i)) {
                                                binding.spEndYear.setSelection(i, true)
                                                break
                                            }
                                        }
                                    }
                                } else {
                                    if (it.data != null) {
                                        binding.etJobTitle.setText(it.data!!.title)
                                        binding.etCompany.text = it.data!!.companyName
                                        val eType = it.data!!.employmentType

                                        for (i in empList.indices) {
                                            if (eType.equals(empList[i].type)) {
                                                binding.spEmpType.setSelection(i, true)
                                                break
                                            }
                                        }
                                    }
                                }


//                            binding.etJobTitle.text = it.data!!.country.toString()
//                            binding.etCity.text =
//                                it.data!!.city.toString() + "," + it.data!!.state.toString()
//                            if (it.data!!.postalCode != null) {
//                                binding.etPostalcode.setText(it.data!!.postalCode.toString())
//                            }
//                            binding.etCountry.text = it.data!!.country.toString()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
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

    private fun setStudentData() {
        if (isStudent) {
            binding.tvStudent.text = getText(R.string.i_m_not_a_student)
            binding.llStudent.visibility = View.VISIBLE
            binding.llMain.visibility = View.GONE
        } else {
            binding.tvStudent.text = getText(R.string.i_m_student)
            binding.llStudent.visibility = View.GONE
            binding.llMain.visibility = View.VISIBLE
        }
    }

    private fun showFullScreenDialog() {
        Debug.e("-----", "---showFullScreenDialog-")
        val intent = Intent(activity, SearchCompanyActivity::class.java)
        searchactivityResultLauncher.launch(intent)

    }

    var searchactivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val company = data?.extras?.getString("company")

                Debug.e("-----", "---country--$company")
                if (company != null) {
                    binding.etCompany.text = company
                }
            }
        }

    private fun showSearchCollageScreen() {
        Debug.e("-----", "---showSearchCollageScreen-")
        val intent = Intent(activity, SearchCollageActivity::class.java)
        searchCollageactivityResultLauncher.launch(intent)

    }

    var searchCollageactivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val school = data?.extras?.getString("school")
                val sata = data?.extras?.getString("data")

                Debug.e("-----", "---school--$school")
                if (school != null) {
                    if (sata != null) {
                        schoolData = sata
                    }
                    binding.tvSchool.text = school
                    if (schoolData.isNullOrEmpty())
                        schoolData = school

                }
            }
        }

    var verifyEmailResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                goToNext()
            }
        }

    private fun goToConfirmEmail() {
        val intent = Intent(activity, ConfirmEmailActivity::class.java)
        verifyEmailResultLauncher.launch(intent)
    }

    private fun goToNext() {
        (requireActivity() as OnboardingActivity).viewPager.setCurrentItem(current + 1, true)
    }

    private fun goToBack() {
        (requireActivity() as OnboardingActivity).viewPager.setCurrentItem(current - 1, true)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvBack -> {
                goToBack()
            }

            binding.tvSchool -> {
                showSearchCollageScreen()
            }

            binding.etCompany -> {
                showFullScreenDialog()
            }

            binding.btnNext -> {
                Debug.e("-----", "---onClick-btnNext-")
                binding.tvJobTitleError.visibility = View.GONE
                binding.tvEmpError.visibility = View.GONE
                binding.tvCompanyError.visibility = View.GONE
                binding.tvSchoolError.visibility = View.GONE
                binding.tvDaterror.visibility = View.GONE
                binding.tvYearError.visibility = View.GONE
                Debug.e("----", "--btnLogin-click-")
                with(binding) {

                    val is_student = isStudent

                    if (is_student) {

                        val sYear: DateYear = binding.spStartYear.selectedItem as DateYear
                        val eYear: DateYear = binding.spEndYear.selectedItem as DateYear

                        val bdate: DateYear = binding.spDay.selectedItem as DateYear
                        val bMon: Int = binding.spMonth.selectedItemPosition
                        val bYear: DateYear = binding.spYear.selectedItem as DateYear

                        val m: String = convertMonth((bMon + 1).toString()).toString()
                        val date: String = convertMonth(bdate.value).toString()
                        val dateOfB = "${bYear.value}-${m}-${date}"

                        Debug.e("----", "---dateOfB---" + dateOfB)

                        val name = schoolData
                        val start_year = sYear.value
                        val end_year = eYear.value
                        val eligibility = binding.swover13.isChecked
                        val dob = dateOfB

                        with(viewModel) {
                            var valid =
                                isStudentValidation(name, start_year, end_year, dob, eligibility)
                            if (!valid) {
                                collageValidation(name)
                                startYearValidation(dob)
                                endYearValidation(dob)
                                dobValidation(dob)
                                return
                            }
                            requireActivity().hideKeyboard()
                            if (isInternetConnected(activity))
                                studentapiCall(
                                    name,
                                    start_year,
                                    end_year,
                                    isStudent,
                                    eligibility,
                                    dob
                                )
                        }
                    } else {

                        val title = etJobTitle.text.toString()
                        val sPos = binding.spEmpType.selectedItemPosition
                        eType = empList[sPos].type

                        val employment_type = eType
                        val company_name = binding.etCompany.text.toString()
                        val is_student = isStudent

                        with(viewModel) {
                            var valid = validation(title, employment_type, company_name)
//                        if (ifPostalCode)
//                            valid = validation(country, city, postalCode)
                            if (!valid) {
                                jobtitleValidation(title)
                                empTypeValidation(employment_type)
                                companyValidation(company_name)
//                            if (ifPostalCode)
//                                postalcodeValidation(postalCode)
                                return
                            }
                            requireActivity().hideKeyboard()
                            if (isInternetConnected(activity))
                                apiCall(title, employment_type, company_name, is_student)
                        }
                    }
                }
            }
        }
    }

    private fun convertMonth(input: String): String {
        val inputFormat = SimpleDateFormat("M", Locale.US)
        val outputFormat = SimpleDateFormat("MM", Locale.US)

        try {
            val inputDate: Date = inputFormat.parse(input)
            val outputDateStr: String = outputFormat.format(inputDate)
            println(outputDateStr) // Output will be:
            return outputDateStr
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return input
    }
}