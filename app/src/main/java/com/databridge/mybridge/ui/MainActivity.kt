package com.databridge.mybridge.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.databridge.mybridge.R
import com.databridge.mybridge.base.hideKeyboard
import com.databridge.mybridge.base.isNullorEmptyNot
import com.databridge.mybridge.base.loadRoundedImage
import com.databridge.mybridge.base.logout
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.ActivityMainBinding
import com.databridge.mybridge.ui.login.previewsigninSignup.view.PreviewSignInSignupActivity
import com.databridge.mybridge.ui.onboarding.view.ConfirmEmailActivity
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoardingViewModel
import com.databridge.mybridge.ui.post.PostViewModel
import com.databridge.mybridge.ui.profile.view.ProfileActivity
import com.databridge.mybridge.utils.Debug


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce: Boolean = false
    private lateinit var navController: NavController

    // view model
    private val viewModel: PostViewModel by viewModels()
    private val onviewModel: OnBoardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initAPIcall()
        initToolbar()
        initNav()
        initNavigationDrawer()
        initBottomNavigationView()
        iniClick()
    }

    private fun initAPIcall() {
        onviewModel.profileInfoApiCall()

        if (sharedPref.getPrefBoolean("prefIsLogin") && sharedPref.getPrefBoolean(Conts.PREF_IS_EMAIL_VERIFIED)
                .not()
        )
            goToConfirmEmail()
    }

    private fun goToConfirmEmail() {
        val intent = Intent(activity, ConfirmEmailActivity::class.java)
        verifyEmailResultLauncher.launch(intent)
    }


    var verifyEmailResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

            }
        }


    private fun initToolbar() {
        binding.toolbar.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                this.hideKeyboard()
                binding.toolbar.etSearch.setText("")
                // todo open search screen ...
            }
            true
        }
    }

    private fun iniClick() {
        with(binding) {
            toolbar.imgToolbarStart.setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
            layoutHeader.imgProfile.setOnClickListener {
                goToProfile()
            }

            layoutHeader.btnCreateAccount.setOnClickListener {
                goToPreviewSignin(true)
            }

            layoutHeader.btnLogin.setOnClickListener {
                goToPreviewSignin(false)
            }

            layoutHeader.btnLogout.setOnClickListener {
                logout(sharedPref, activity)
            }
        }
    }

    // region navigation
    private fun initNav() {
        initializeNavigation()
        navigationListener()
    }

    fun getForegroundFragment(): Fragment? {
        return try {
            // initialize navigation
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.navigationFragment) as NavHostFragment
            navHostFragment.childFragmentManager.fragments[0]
        } catch (e: Exception) {
            null
        }
    }

    private fun initializeNavigation() {
        // initialize navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navigationFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun navigationListener() {
        navController.addOnDestinationChangedListener { navController, _, _ ->
            // toolbar setup // todo
            /*with(binding.toolbar) {
                when (navController.currentDestination?.id) {
                    R.id.homeFragment -> {
                        toolbarLayout.visibility = View.VISIBLE
                        imgToolbarStart.visibility = View.GONE
                        imgToolbarEnd.visibility = View.VISIBLE
                    }

                    R.id.loginFragment -> {
                        toolbarLayout.visibility = View.GONE
                    }

                    else -> {
                        toolbarLayout.visibility = View.VISIBLE
                        imgToolbarStart.visibility = View.VISIBLE
                        imgToolbarEnd.visibility = View.GONE
                    }
                }
            }*/
        }
    }
    // endregion

    // region navigation drawer
    private fun initNavigationDrawer() {
        if (sharedPref.getPrefBoolean("prefIsLogin")) {
            val profile = sharedPref.getPrefString(
                Conts.PREF_PROFILE_PIC
            )
            if (profile?.isNullorEmptyNot() == true) {
                Debug.e("---", "--profile-3-" + profile)
                binding.layoutHeader.imgProfile.loadRoundedImage(
                    profile,
                    R.drawable.profile_frame
                )
            } else {
                binding.layoutHeader.imgProfile.setImageResource(R.drawable.profile_frame)
                binding.layoutHeader.tvImageName.text = sharedPref.getPrefString(
                    Conts.PREF_PROFILE_PIC_CHARS
                ) ?: ""
            }

            binding.layoutHeader.tvName.text =
                sharedPref.getPrefString(Conts.PREF_DISPLAY_NAME) ?: ""
            binding.layoutHeader.tvDesignation.text =
                sharedPref.getPrefString(Conts.PREF_EXP_TITLE) ?: ""
            binding.layoutHeader.tvName.text =
                sharedPref.getPrefString(Conts.PREF_DISPLAY_NAME) ?: ""

            val company =
                if (sharedPref.getPrefString(Conts.PREF_EXP_COMPANY).isNullOrEmpty().not())
                    "at ${sharedPref.getPrefString(Conts.PREF_EXP_COMPANY)}" else ""

            val designation = "${sharedPref.getPrefString(Conts.PREF_EXP_TITLE)} $company"
            binding.layoutHeader.tvDesignation.text = designation

            binding.layoutHeader.groupProfile.visibility = View.VISIBLE
            binding.layoutHeader.groupSignIn.visibility = View.GONE

            binding.layoutHeader.imgProfile.setOnClickListener {
                goToProfile()
            }

        } else {
            binding.layoutHeader.groupProfile.visibility = View.GONE
            binding.layoutHeader.groupSignIn.visibility = View.VISIBLE

            binding.layoutHeader.btnCreateAccount.setOnClickListener {
                goToPreviewSignin(true)
            }

            binding.layoutHeader.btnLogin.setOnClickListener {
                goToPreviewSignin(false)
            }
        }
        binding.navigationView.setupWithNavController(navController)

        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            when (menuItem.itemId) {
                /*R.id.teamFragment -> {
                    // Navigate to the HomeFragment
                    navController.navigate(R.id.teamFragment)
                    true
                }*/

                else -> false
            }
        }
    }

    private fun goToPreviewSignin(isSignup: Boolean) {
        val intent = Intent(activity, PreviewSignInSignupActivity::class.java)
        intent.putExtra("isSignup", isSignup)
        startActivity(intent);
    }

    private fun goToProfile() {
        showToast("Coming soon")
//        val intent = Intent(activity, ProfileActivity::class.java)
//        startActivity(intent)
    }
    // endregion

    // region bottom navigation view
    private fun initBottomNavigationView() {
        binding.bottomNavigation.setOnApplyWindowInsetsListener(null)
        binding.bottomNavigation.setupWithNavController(navController)
    }
    // endregion

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination!!.id == R.id.homeFragment) {
            if (doubleBackToExitPressedOnce) {
                finish()
                return
            }
            doubleBackToExitPressedOnce = true
            Toast.makeText(
                this,
                "${getString(R.string.press_again_to_close)} ${getString(R.string.app_name)}",
                Toast.LENGTH_SHORT
            ).show()

            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2500)
        } else {
            super.onBackPressed()
        }
    }

}