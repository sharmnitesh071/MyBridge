package com.databridge.mybridge.ui.login.previewsigninSignup.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.databinding.ActivityPreviewSignupBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.ui.login.previewsigninSignup.viewmodel.PreviewSigninSignupViewModel
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

class PreviewSignInSignupActivity : BaseActivity(), View.OnClickListener {

    internal lateinit var binding: ActivityPreviewSignupBinding
    var isSignup = true

    private val viewModel: PreviewSigninSignupViewModel by viewModels()
    private val onviewModel: OnBoardingViewModel by viewModels()
    var email = "";

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_preview_signup)
//        val viewModel: PreviewSigninSignup1ViewModel =
//            ViewModelProvider(this)[PreviewSigninSignup1ViewModel::class.java]
        try {
            if (intent.extras != null && intent.extras!!.getBoolean("isSignup") != null)
                isSignup = intent.extras!!.getBoolean("isSignup")
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        viewModel.setBinder(binding, isSignup)
        init()
        setData()

        initObserver()
        initGoogleLogin()
    }

    private fun initObserver() {


        viewModel.socialresponse.observe(this) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.saveLoginDataInPref(it)
                        viewModel.saveInPref(email)
//                        goToOnboarding()

                        onviewModel.profileInfoApiCall()

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

    fun init() {
        binding.tvSignin.setOnClickListener(this)
        binding.btnLoginGoogle.setOnClickListener(this)
        binding.llLoginWithGoogle.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
    }

    private fun setData() {
        Debug.e("---", "--setData--" + isSignup)
        if (isSignup) {
            binding.tvTitle.text = getString(R.string.new_to_mybridge)
            binding.tvDes.text = getString(R.string.preview_signup_info)
            binding.btnLogin.text = getString(R.string.sign_up_with_email)
            binding.btnLoginGoogle.text = getString(R.string.sign_up_with_google)
            binding.tvAlready.text = getString(R.string.already_on_mybridge)
            binding.tvSignin.text = getString(R.string.sign_in)
        } else {
            binding.tvTitle.text = getString(R.string.welcome_back)
            binding.tvDes.text = getString(R.string.preview_signin_info)
            binding.btnLogin.text = getString(R.string.signin_with_email)
            binding.btnLoginGoogle.text = getString(R.string.sign_in_with_google)
            binding.tvAlready.text = getString(R.string.account_dont)
            binding.tvSignin.text = getString(R.string.sign_up)
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.llLoginWithGoogle, binding.btnLoginGoogle -> {
                Debug.e("----", "---LoginGoogle-")
                signIn()
            }

            binding.tvSignin -> {
                isSignup = !isSignup
                setData()
            }

            binding.btnLogin -> {
                if (isSignup) {
                    val i = Intent(activity, SignupActivity::class.java)
                    startActivity(i)
                } else {
                    val i = Intent(activity, LoginActivity::class.java)
                    startActivity(i)
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

                email = account.email!!
                val idToken: String = account.idToken!!
                val email: String = account.email!!
                val firstName: String = Utils.removeSpecialChar(fname)
                val lastName: String = Utils.removeSpecialChar(lname)
                val accessToken: String = ""

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