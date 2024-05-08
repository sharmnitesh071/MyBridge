package com.databridge.mybridge.ui.login.login.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.databinding.ActivityLoginBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.login.viewmodel.LoginViewModel
import android.text.InputFilter
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.login.forgotpass.view.ForgotPasswordActivity
import com.databridge.mybridge.ui.login.signup.view.SignupActivity
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

class LoginActivity : BaseActivity(), View.OnClickListener {

    // binding
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()
    private val onviewModel: OnBoardingViewModel by viewModels()

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(activity, R.layout.activity_login)
//        val viewModel: LoginViewModel1 = ViewModelProvider(this)[LoginViewModel1::class.java]
//        viewModel.setBinder(binding)

        if (Debug.DEBUG) {
//            binding.etEmail.setText("kvaghasiya+1@databridgeconsultants.com")
//            binding.etPassword.setText("Ktest@123")
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
        // login api response
        viewModel.response.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.saveLoginDataInPref(it)
                        viewModel.saveInPref(binding)

                        sharedPref.setPrefString("prefAccess", sharedPref.getPrefString("prefAccess")+"aaa")
                        onviewModel.profileInfoApiCall()
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    Debug.e("----", "--message--" + response.data)
                    Debug.e("----", "--message--" + response.errorBody)
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
//                        onviewModel.apiCall()
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
                        goToOnboarding()
//                        if (it!!.isOnboardingCompleted == true) {
//                            goToDashbaord()
//                        } else {
//                            goToOnboarding()
//                        }
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

        onviewModel.response.observe(this) {
            it?.data?.let {
                it?.data?.let {
                    if (it.profile == true && it.employment == true && it.photo == true && it.jobAvailabilty == true && it.accountActive == true) {
                        goToDashbaord()
                    } else {
                        goToOnboarding()
                    }
                }

            }
        }
    }

    private fun init() {
        // focus change for icon color
//        viewModel.endIconColorChange(requireContext(), binding)
        // remove white space in edit text
        binding.etPassword.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            source.toString().filterNot { it.isWhitespace() }
        })
        // text watcher
        binding.etEmail.addTextChangedListener(viewModel.emailTextWatcher())
        binding.etPassword.addTextChangedListener(viewModel.passwordTextWatcher())
        // click event
        binding.tvForgotPass.setOnClickListener(this)
        binding.tvSignup.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    fun onForgotPassword() {
        try {
            val i = Intent(activity, ForgotPasswordActivity::class.java)
            startActivity(i)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun goToDashbaord() {
//        var intent = Intent(activity, ProfileActivity::class.java)
        var intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun goToOnboarding() {
        var intent = Intent(activity, OnboardingActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    fun onRegister() {
        val i = Intent(activity, SignupActivity::class.java)
        startActivity(i)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.llLoginWithGoogle, binding.btnLoginGoogle -> {
                Debug.e("----", "---LoginGoogle-")
                signIn()
            }

            binding.tvSignup -> {
                onRegister()
            }

            binding.tvForgotPass -> {
                onForgotPassword()
            }

            binding.btnLogin -> {
                binding.tvEmailError.visibility = View.GONE
                binding.tvPasswordError.visibility = View.GONE
                Debug.e("----", "--btnLogin-click-")
                with(binding) {
                    val email = etEmail.text.toString()
                    val password = etPassword.text.toString()
                    val checkRemember = checkRemember.isChecked
                    with(viewModel) {
                        if (!validation(email, password)) {
                            emailValidation(email)
                            passwordValidation(password)
                            return
                        }
                        hideKeyboard()
                        if (isInternetConnected(activity))
                            apiCall(email, password, checkRemember)
                    }
                }
            }
        }
    }


    private fun initGoogleLogin() {
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.google_web_client_id))
//            .requestEmail()
//            .build()
//
//        googleSignInClient = GoogleSignIn.getClient(this, gso)
        // [END config_signin]

        // [START initialize_auth]
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

//        signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(mContext.getString(R.string.google_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            .build()
    }

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

    // [START signin]
    private fun signIn() {
        Debug.e("---", "--signIn--")
//        signInRequest = BeginSignInRequest.builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    // Your server's client ID, not your Android client ID.
//                    .setServerClientId(getString(R.string.google_web_client_id))
//                    // Only show accounts previously used to sign in.
//                    .setFilterByAuthorizedAccounts(true)
//                    .build())
//            .build()

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
//            handleGoogleAccountData(account)
        } else {
            val signInIntent: Intent = googleSignInClient.getSignInIntent()
            resultCallbackForGoogle.launch(signInIntent)
        }

//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    var resultCallbackForGoogle = registerForActivityResult<Intent, ActivityResult>(
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
            handleGoogleAccountData(account)
        } catch (e: ApiException) {
            Toast.makeText(this, "E -> " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleGoogleAccountData(account: GoogleSignInAccount) {
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

    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true
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
//        if (requestCode == REQ_ONE_TAP) {
//            val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
//            val idToken = googleCredential.googleIdToken
//            when {
//                idToken != null -> {
//                    // Got an ID token from Google. Use it to authenticate
//                    // with Firebase.
//                    val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
//                    auth.signInWithCredential(firebaseCredential)
//                        .addOnCompleteListener(this) { task ->
//                            if (task.isSuccessful) {
//                                // Sign in success, update UI with the signed-in user's information
//                                Debug.d(TAG, "signInWithCredential:success")
//                                val user = auth.currentUser
//                                updateUI(user)
//                            } else {
//                                // If sign in fails, display a message to the user.
//                                Debug.w(TAG, "signInWithCredential:failure"+ task.exception)
//                                updateUI(null)
//                            }
//                        }
//                }
//
//                else -> {
//                    // Shouldn't happen.
//                    Debug.d(TAG, "No ID token!")
//                }
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
//        val currentUser = auth.currentUser
//        updateUI(currentUser)
    }
}