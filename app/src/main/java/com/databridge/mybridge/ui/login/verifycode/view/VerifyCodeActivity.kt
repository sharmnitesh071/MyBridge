package com.databridge.mybridge.ui.login.verifycode.view

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.databinding.ActivityVerifyCodeBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.ui.login.setnewpass.view.SetNewPasswordActivity
import com.databridge.mybridge.ui.login.verifycode.viewmodel.VerifyCodeViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.databridge.mybridge.utils.Utils


class VerifyCodeActivity : BaseActivity(), View.OnClickListener {

    internal lateinit var binding: ActivityVerifyCodeBinding

    var email: String? = null
    var isEmail = true
    var timer: CountDownTimer? = null

    private val viewModel: VerifyCodeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_verify_code)

        email = intent.extras?.getString("email")
//        val viewModel: VerifyCodeView1Model = ViewModelProvider(this)[VerifyCodeView1Model::class.java]
//        viewModel.setBinder(binding);

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
        binding!!.tvResend.visibility = View.INVISIBLE
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
                        it?.data?.resetToken?.let { it1 -> goToVerify(it1) }
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
                    showToast(getString(R.string.user_not_found))
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun init() {

        // text watcher
        binding.etVCode.addTextChangedListener(viewModel.otpTextWatcher())
        // click event
        binding.tvSignin.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.tvDes.setOnClickListener(this)
        binding.tvResend.setOnClickListener(this)


        if (isEmail) {
//            val html = ("<font color=" + ContextCompat.getColor(this,R.color.clr_txt)
//                    + ">+"+ title +"</font><font color="
//                    + ContextCompat.getColor(this,R.color.error_txt) + "> ("+ email +")</font>")
//            binding.tvTitle.text  = Utils.fromHtml(html)

            val htmlDes = ("<font color=" + ContextCompat.getColor(this, R.color.clr_txt)
                    + ">" + getString(R.string.verification_code_info) + " " + email + "</font><font color="
                    + ContextCompat.getColor(
                this,
                R.color.colorAccent
            ) + "> " + getString(R.string.change) + "</font>")

            binding.tvDes.text = Utils.fromHtml(htmlDes)
        }


//        goToVerify()
    }

    private fun goToVerify(token: String) {
        val i = Intent(activity, SetNewPasswordActivity::class.java)
        i.putExtra("token", token)
        startActivity(i)

//        val i = Intent(activity, VerifyCodeActivity::class.java)
//        i.putExtra("email",binding.etEmail.text.toString())
//        startActivity(i)
    }

    override fun onClick(view: View?) {
        when (view) {

            binding.tvResend -> {
                setTimer()
                with(viewModel) {
                    email?.let {
                        if (isInternetConnected(activity))
                            sendOtpToVerify(it)
                    }
                }
            }

            binding.tvDes -> {
                finish()
            }

            binding.tvSignin -> {
                onSignin()
            }

            binding.btnSubmit -> {
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
                        email?.let {
                            if (isInternetConnected(activity))
                                verifyPasswordReset(it, otp)
                        }
                    }
                }
            }
        }
    }
}