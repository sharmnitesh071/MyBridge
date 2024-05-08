package com.databridge.mybridge.ui.login.forgotpass.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.databinding.ActivityForgotPassBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.forgotpass.viewmodel.ForgotPasswordViewModel
import com.databridge.mybridge.ui.login.verifycode.view.VerifyCodeActivity
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource

class ForgotPasswordActivity : BaseActivity(), View.OnClickListener {

    internal lateinit var binding: ActivityForgotPassBinding

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_forgot_pass)

        init()
        initObserver()
    }

    private fun initObserver() {
        // email error
        viewModel.emailError.observe(this) {
            binding.tvEmailError.visibility = View.VISIBLE
            binding.tvEmailError.text = it
        }
        // login api response
        viewModel.response.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        goToVerify()
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    val msg = getErrorMessage(response.message, response.errorBody)
                    showToast(msg)
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
        binding.etEmail.addTextChangedListener(viewModel.emailTextWatcher())
        // click event
        binding.tvBack.setOnClickListener(this)
        binding.btnReset.setOnClickListener(this)

//        goToVerify()
    }

    private fun goToVerify() {
        val i = Intent(activity, VerifyCodeActivity::class.java)
        i.putExtra("email", binding.etEmail.text.toString())
        startActivity(i)
    }

    override fun onClick(view: View?) {
        when (view) {

            binding.tvBack -> {
                finish()
            }

            binding.btnReset -> {
                binding.tvEmailError.visibility = View.GONE
                Debug.e("----", "--btnLogin-click-")
                with(binding) {
                    val email = etEmail.text.toString()
                    with(viewModel) {
                        if (!validation(email)) {
                            emailValidation(email)
                            return
                        }
                        hideKeyboard()
                        if (isInternetConnected(activity))
                            sendOtpToVerify(email)
                    }
                }
            }
        }
    }
}