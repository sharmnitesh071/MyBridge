package com.databridge.mybridge.ui.splash.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.databridge.mybridge.R
import com.databridge.mybridge.databinding.ActivitySplashBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.appintro.view.AppintroActivity
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.ui.login.previewsigninSignup.view.PreviewSignInSignupActivity
import com.databridge.mybridge.ui.onboarding.view.OnboardingActivity
import com.databridge.mybridge.utils.Debug


class SplashActivity : BaseActivity() {

    var binding: ActivitySplashBinding? = null
    var delayInMinis: Long = 1000
//    var activity: Activity? = null

    companion object {
        var authToken = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = window
            val background = ContextCompat.getDrawable(this, R.drawable.bg_splash)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(android.R.color.transparent)
            window.navigationBarColor = resources.getColor(android.R.color.transparent)
            window.setBackgroundDrawable(background)
        }
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
//        activity = this

//        sharedPref.setPrefString("prefAccess","eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbl90eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzEwMzk0Njk2LCJpYXQiOjE3MTAyNzE4OTksImp0aSI6IjAwNmI3ZWYxNzIyOTRlNjRhNGU5NDc5MmFkNzY5MjYzIiwidXNlcl9pZCI6ODUyfQ.FDNphGmsbwC1vofUJpAY7Voz98hw9EKwXHNGcOx4xes")

        authToken = sharedPref.getPrefString("prefAccess")!!
        Debug.e("----", "--authToken-prefAccess-" + authToken)
//        Debug.e("----","--authToken-prefRefresh-"+sharedPref.getPrefString("prefRefresh"))
//        authToken = sharedPref.getPrefString("prefRefresh")!!
        goToNext()

    }

    private fun goToNext() {
        Debug.e("----", "--goToNext---")
        Handler(Looper.getMainLooper()).postDelayed({
            if (sharedPref.getPrefBoolean("prefIsIntro")) {
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent);
                finish()

//                if (sharedPref.getPrefBoolean("prefIsLogin")) {
//                    val intent = Intent(activity, OnboardingActivity::class.java)
//                    startActivity(intent);
//                    finish()
//                } else {
////                    val intent = Intent(activity, PreviewSignInSignupActivity::class.java)
//                    val intent = Intent(activity, LoginActivity::class.java)
//                    startActivity(intent);
//                    finish()
//                }
            } else {
                val intent = Intent(activity, AppintroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, delayInMinis)
    }

}