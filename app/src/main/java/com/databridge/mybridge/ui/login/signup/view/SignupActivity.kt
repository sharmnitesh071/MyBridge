package com.databridge.mybridge.ui.login.signup.view

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.databinding.ActivitySignupBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.ui.login.signup.viewmodel.SignupViewModel
import com.databridge.mybridge.ui.onboarding.view.OnboardingActivity
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoardingViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.databridge.mybridge.utils.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth

class SignupActivity : BaseActivity(), View.OnClickListener {

    internal lateinit var binding: ActivitySignupBinding

    private val viewModel: SignupViewModel by viewModels()

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private val onviewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_signup)

        if (Debug.DEBUG) {
//            binding.etFirstname.setText("Krupali")
//            binding.etLastName.setText("Vaghasiya")
//            binding.etEmail.setText("kvaghasiya+1@databridgeconsultants.com")
//            binding.etPassword.setText("Ktest@123")
//            binding.etConfirmPassword.setText("Ktest@123")
        }

        init()
        initObserver()
        initGoogleLogin()
    }

    private fun initObserver() {
        // email error
        viewModel.emailError.observe(this) {
            binding.tvEmailError.visibility = View.VISIBLE
            binding.tvEmailError.text = it
        }
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
                        viewModel.saveLoginDataInPref(it)
                        viewModel.saveInPref(binding)
                        showToast("Registration Successfully")
                        goToOnboarding()

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

        viewModel.socialresponse.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.saveLoginDataInPref(it)
                        viewModel.saveInPref(binding)

                        onviewModel.profileInfoApiCall()
//                        goToOnboarding()

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

        onviewModel.userInforesponse.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        if (it!!.isOnboardingCompleted == true) {
                            goToDashbaord()
                        } else {
                            goToOnboarding()
                        }
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

    private fun init() {
        // focus change for icon color
//        viewModel.endIconColorChange(requireContext(), binding)
        // remove white space in edit text
        binding.etPassword.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.toString().filterNot { it.isWhitespace() }
        })
        // text watcher
        binding.etFirstname.addTextChangedListener(viewModel.fnameTextWatcher())
        binding.etLastName.addTextChangedListener(viewModel.lnameTextWatcher())
        binding.etEmail.addTextChangedListener(viewModel.emailTextWatcher())
        binding.etPassword.addTextChangedListener(viewModel.passwordTextWatcher())
        binding.etPassword.addTextChangedListener(viewModel.cpasswordTextWatcher())
        // click event
        binding.btnLogin.setOnClickListener(this)
        binding.tvSignin.setOnClickListener(this)
    }

    fun onSignin() {
        val i = Intent(activity, LoginActivity::class.java)
        startActivity(i)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.tvSignin -> {
                onSignin()
            }

            binding.btnLogin -> {
                binding.tvFNameError.visibility = View.GONE
                binding.tvLNameError.visibility = View.GONE
                binding.tvEmailError.visibility = View.GONE
                binding.tvPasswordError.visibility = View.GONE
                binding.tvPasswordError.visibility = View.GONE
                with(binding) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    val fName = etFirstname.text.toString()
                    val lName = etLastName.text.toString()
                    val conpassword = etConfirmPassword.text.toString()
                    with(viewModel) {
                        if (!validation(email, password, conpassword, fName, lName)) {
                            fNameValidation(fName)
                            lNameValidation(lName)
                            emailValidation(email)
                            passwordValidation(password, password, false)
                            passwordValidation(password, conpassword, true)
                            return
                        }
                        hideKeyboard()
                        if (isInternetConnected(activity))
                            signupApi(email, fName, lName, password, conpassword)
                    }
                }
            }
        }
    }

    private fun initGoogleLogin() {
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.llLoginWithGoogle.setOnClickListener {
            if (isInternetConnected(activity))
                signIn()
        }

        binding.btnLoginGoogle.setOnClickListener {
            if (isInternetConnected(activity))
                signIn()
        }

    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Debug.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Debug.w(TAG, "signInWithCredential:failure" + task.exception)
//                    updateUI(null)
                }
            }
    }
    // [END auth_with_google]

    // START signin
    private fun signIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_web_client_id))
            .requestEmail()
            .requestProfile()
            .requestId()
            .build()
        googleSignInClient = GoogleSignIn.getClient(activity, gso)


        try {
            googleSignInClient.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val account = GoogleSignIn.getLastSignedInAccount(activity)

        if (account != null) {
        } else {
            val signInIntent: Intent = googleSignInClient.signInIntent
            resultCallbackForGoogle.launch(signInIntent)
        }
    }
    //END signin

    private var resultCallbackForGoogle = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Debug.e("----", "--resultCallbackForGoogle-ActivityResult--$result")
        if (result!!.resultCode == RESULT_OK) {
            val data = result!!.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                Debug.e("---", "----" + account.familyName)
                Debug.e("---", "----" + account.email)
                Debug.e("---", "----" + account.givenName)
                Debug.e("---", "--idToken--" + account.idToken)
                Debug.e("---", "--socailId--" + account.id)
                val fname = "" + account.givenName
                var lname = "" + account.familyName
                if (account.familyName == null) lname = ""

                val idToken: String = account.idToken!!
                val email: String = account.email!!
                val firstName: String = Utils.removeSpecialChar(fname)
                val lastName: String = Utils.removeSpecialChar(lname)
                val accessToken: String = ""

                if (isInternetConnected(activity))
                    callSocialLoginApi(idToken, email, firstName, lastName, accessToken)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "E -> " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun callSocialLoginApi(
        idToken: String,
        email: String,
        firstName: String,
        lastName: String,
        accessToken: String
    ) {
        with(viewModel) {
            if (isInternetConnected(activity))
                socialLoginapiCall(idToken, email, firstName, lastName, accessToken)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Debug.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Debug.w(TAG, "Google sign in failed" + e.message.toString())
            }
        }
    }

}