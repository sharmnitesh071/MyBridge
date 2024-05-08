package com.databridge.mybridge.ui.login.setnewpass.view

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.databinding.ActivitySetNewPasswordBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.ui.login.setnewpass.viewmodel.SetNewPasswordViewModel
import com.databridge.mybridge.ui.onboarding.view.OnboardingActivity
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource

class SetNewPasswordActivity : BaseActivity(), View.OnClickListener {

    internal lateinit var binding: ActivitySetNewPasswordBinding

    private val viewModel: SetNewPasswordViewModel by viewModels()
    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_set_new_password)

        token = intent.extras?.getString("token")

        init()
        initObserver()
    }

    private fun initObserver() {
        // password error
        viewModel.passwordError.observe(this) {
            binding.tvPasswordError.visibility = View.VISIBLE
            binding.tvPasswordError.text = it
        }
        // Confirm password error
        viewModel.conpasswordError.observe(this) {
            binding.tvConPasswordError.visibility = View.VISIBLE
            binding.tvConPasswordError.text = it
        }

        // login api response
        viewModel.response.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
//                        showToast("Registration Successfully")
                        onSignin()

                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    showToast(response.message.toString())
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun goToOnboarding() {
        val intent = Intent(activity, OnboardingActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun init() {
        // focus change for icon color
//        viewModel.endIconColorChange(requireContext(), binding)
        // remove white space in edit text
        binding.etPassword.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.toString().filterNot { it.isWhitespace() }
        })
        // text watcher
        binding.etPassword.addTextChangedListener(viewModel.passwordTextWatcher())
        binding.etPassword.addTextChangedListener(viewModel.cpasswordTextWatcher())
        // click event
        binding.btnSubmit.setOnClickListener(this)
        binding.tvSignin.setOnClickListener(this)
    }

    fun onSignin() {
        val i = Intent(activity, LoginActivity::class.java)
        startActivity(i)
        finishAffinity()
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.tvSignin -> {
                onSignin()
            }

            binding.btnSubmit -> {
                binding.tvPasswordError.visibility = View.GONE
                binding.tvPasswordError.visibility = View.GONE
                with(binding) {
                    val password = etPassword.text.toString()
                    val conpassword = etConfirmPassword.text.toString()
                    with(viewModel) {
                        if (!validation(password, conpassword)) {
                            passwordValidation(password, password, false)
                            passwordValidation(password, conpassword, true)
                            return
                        }
                        hideKeyboard()
                        token?.let {
                            if (isInternetConnected(activity))
                                confirmPasswordApi(it, password, conpassword)
                        }
                    }
                }
            }
        }
    }
}