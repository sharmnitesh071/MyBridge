package com.databridge.mybridge.ui.onboarding.view

import android.app.Activity
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
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.ActivityEditEmailBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.forgotpass.view.ForgotPasswordActivity
import com.databridge.mybridge.ui.onboarding.viewmodel.EditEmailViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource

class EditEmailActivity : BaseActivity(), View.OnClickListener {

    internal lateinit var binding: ActivityEditEmailBinding

    private val viewModel: EditEmailViewModel by viewModels()
    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_edit_email)

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
        viewModel.emailError.observe(this) {
            binding.tvCurrEmailError.visibility = View.VISIBLE
            binding.tvCurrEmailError.text = it
        }

        viewModel.newemailError.observe(this) {
            binding.tvNewEmailError.visibility = View.VISIBLE
            binding.tvNewEmailError.text = it
        }

        // login api response
        viewModel.response.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
//                        showToast("Registration Successfully")
                        onEditEmail(binding.etNewEmail.text.toString().trim())

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

    private fun onEditEmail(email: String) {
        val intent = Intent()
        intent.putExtra("email", email)
        sharedPref.setPrefString(Conts.PREF_EMAIL, email)
        setResult(Activity.RESULT_OK)
        finish()
    }


    private fun init() {
        // focus change for icon color
//        viewModel.endIconColorChange(requireContext(), binding)
        // remove white space in edit text
        binding.etPassword.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.toString().filterNot { it.isWhitespace() }
        })
        // text watcher
        binding.etCurrEmail.addTextChangedListener(viewModel.emailTextWatcher())
        binding.etNewEmail.addTextChangedListener(viewModel.newEmailTextWatcher())
        binding.etPassword.addTextChangedListener(viewModel.passwordTextWatcher())
        // click event
        binding.btnSubmit.setOnClickListener(this)
    }

    private fun onForgotPassword() {
        try {
            val i = Intent(activity, ForgotPasswordActivity::class.java)
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onClick(view: View?) {
        when (view) {
            binding.tvForgotPass -> {
                onForgotPassword()
            }

            binding.btnSubmit -> {
                binding.tvCurrEmailError.visibility = View.GONE
                binding.tvNewEmailError.visibility = View.GONE
                binding.tvPasswordError.visibility = View.GONE
                with(binding) {
                    val email = etCurrEmail.text.toString()
                    val password = etPassword.text.toString()
                    val newEmail = etNewEmail.text.toString()
                    with(viewModel) {
                        if (!validation(email, newEmail, password)) {
                            emailValidation(email)
                            newemailValidation(newEmail)
                            passwordValidation(password)
                            return
                        }
                        hideKeyboard()
                        if (isInternetConnected(activity))
                            editEmail(newEmail, password)
                    }
                }
            }
        }
    }
}