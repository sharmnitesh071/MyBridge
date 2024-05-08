package com.databridge.mybridge.ui.onboarding.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.ActivityConfirmEmailBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.ui.onboarding.viewmodel.ConfirmEmailViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource


class ConfirmEmailActivity : BaseActivity(), View.OnClickListener {

    internal lateinit var binding: ActivityConfirmEmailBinding

    var email: String? = null
    var isEmail = true
    var timer: CountDownTimer? = null

    private val viewModel: ConfirmEmailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_confirm_email)

        email = sharedPref.getPrefString(Conts.PREF_EMAIL)

        init()
        initObserver()
        setTimer()
    }

    private fun setTimer() {
        binding!!.tvTime.visibility = View.VISIBLE
        timer = object : CountDownTimer(61000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                var time = millisUntilFinished / 1000
                var resendTxt = "Resend in $time sec"
                if (time < 10)
                    resendTxt = "Resend in 0$time sec"
                binding!!.tvTime.text = resendTxt
            }

            override fun onFinish() {
                binding!!.tvTime.text = "00"
                binding!!.tvTime.visibility = View.GONE
                binding!!.tvResend.visibility = View.VISIBLE
                binding!!.tvResend.isEnabled = true
                binding!!.tvResend.setTextColor(
                    ContextCompat.getColor(
                        activity!!,
                        R.color.clr_btn
                    )
                )
            }
        }.start()
        binding!!.tvResend.visibility = View.GONE
        binding!!.tvResend.isEnabled = false
        binding!!.tvResend.setTextColor(ContextCompat.getColor(activity!!, R.color.clr_txt))
    }

    fun onSignin() {
        val i = Intent(activity, LoginActivity::class.java)
        startActivity(i)
        finishAffinity()
    }


    private fun initObserver() {
        // email error
        viewModel.otpError.observe(this) {
            binding.tvVcodeError.visibility = View.VISIBLE
            binding.tvVcodeError.text = it
        }
        // login api response
        viewModel.response.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    val msg = getErrorMessage(response.message, response.data)

                    Debug.e("----", "--message--" + response.message)
                    showToast(msg)
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }

        viewModel.resendResponse.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
//                       setTimer()
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

        if (isInternetConnected(activity))
            sendOtpAPI()
    }

    private fun init() {

        // text watcher
        binding.etVCode.addTextChangedListener(viewModel.otpTextWatcher())
        // click event
        binding.tvResend.setOnClickListener(this)
        binding.tvEditEmail.setOnClickListener(this)
        binding.btnNext.setOnClickListener(this)

        binding.tvEmail.text = email

    }

    var editEmailResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                email = result.data?.extras?.getString("email")
                if (isInternetConnected(activity))
                    sendOtpAPI()
            }
        }

    override fun onClick(view: View?) {
        when (view) {

            binding.tvEditEmail -> {
                val i = Intent(activity, EditEmailActivity::class.java)
                editEmailResultLauncher.launch(i)
            }

            binding.tvResend -> {
                if (isInternetConnected(activity))
                    sendOtpAPI()
            }

            binding.btnNext -> {
                binding.tvVcodeError.visibility = View.GONE
                Debug.e("----", "--btnSubmit-click-")
                with(binding) {
                    val otp = etVCode.text.toString()
                    with(viewModel) {
                        if (!validation(otp)) {
                            otpValidation(otp)
                            return
                        }
                        hideKeyboard()
                        email?.let { verifyActivateAccount(otp) }
                    }
                }
            }
        }
    }

    private fun sendOtpAPI() {
        setTimer()
        with(viewModel) {
            email?.let {
                if (isInternetConnected(activity))
                    sendOtpToVerify(it)
            }
        }
    }
}