package com.databridge.mybridge.ui.appintro.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.databridge.mybridge.R
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.databinding.ActivityAppintroBinding
import com.databridge.mybridge.databinding.ActivitySignupBinding
import com.databridge.mybridge.ui.appintro.datamodel.AppIntroData
import com.databridge.mybridge.ui.appintro.utils.SlidingImageAdapter
import com.databridge.mybridge.ui.base.viewmodel.BaseViewModel
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.ui.onboarding.view.OnboardingActivity

class AppintroViewModel(application: Application) : BaseViewModel(application) {
    var binding: ActivityAppintroBinding? = null
    var handler: Handler? = null
    var timeDelay = 3000
    var pagerAdapter: SlidingImageAdapter? = null
    var appIntrodata: ArrayList<AppIntroData> = ArrayList()
    lateinit var sharedPref: SharedPref

    fun setBinder(binding: ActivityAppintroBinding, sharedPref: SharedPref) {
        this.binding = binding
        binding.setViewClickListener(ViewClickHandler())
        mContext = binding.root.context
        this.sharedPref = sharedPref
        init()
    }

    fun init() {
        handler = Handler()


        appIntrodata.add(
            AppIntroData(
                mContext!!.getString(R.string.title1),
                mContext!!.getString(R.string.intro1),
                R.drawable.intro_1
            )
        );
        appIntrodata.add(
            AppIntroData(
                mContext!!.getString(R.string.title2),
                mContext!!.getString(R.string.intro2),
                R.drawable.intro_2
            )
        );

        appIntrodata.add(
            AppIntroData(
                mContext!!.getString(R.string.title3),
                mContext!!.getString(R.string.intro3),
                R.drawable.intro_3
            )
        );
        pagerAdapter = SlidingImageAdapter(mContext!!)
        binding!!.pagerProductInfo.isNestedScrollingEnabled = false
        binding!!.pagerProductInfo.setOffscreenPageLimit(pagerAdapter!!.count)
        binding!!.pagerProductInfo.setAdapter(pagerAdapter)
        pagerAdapter!!.addAll(appIntrodata)
        binding!!.pagerProductInfo.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 2) {
                    binding!!.btnGetStarted.visibility = View.VISIBLE
                } else binding!!.btnGetStarted.visibility = View.INVISIBLE
            }

            override fun onPageSelected(position: Int) {
                startAnimatingBanner()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding!!.indicator.setViewPager(binding!!.pagerProductInfo)
        //        binder.tabIndicatorHomePage!!.setViewPager(binder.pagerProductInfo)
//        binder.tabIndicatorHomePage!!.setAnimationType(AnimationType.SLIDE)
//        startAnimatingBanner()
        pagerAdapter!!.registerDataSetObserver(binding!!.indicator.dataSetObserver)
    }

    var runnable: Runnable = object : Runnable {
        override fun run() {
            binding!!.pagerProductInfo.post {
                if (binding!!.pagerProductInfo.currentItem == pagerAdapter!!.count - 1) {
                } else if (pagerAdapter!!.count > 0) {
                    binding!!.pagerProductInfo.setCurrentItem(
                        (binding!!.pagerProductInfo.currentItem + 1) % pagerAdapter!!.count,
                        true
                    )
                }
            }
            handler!!.postDelayed(this, timeDelay.toLong())
        }
    }

    inner class ViewClickHandler {
        fun onNextClick(v: View?) {
            try {
                if (binding!!.pagerProductInfo.currentItem == pagerAdapter!!.count - 1) {
                    onNext()
                } else if (pagerAdapter!!.count > 0) {
                    binding!!.pagerProductInfo.setCurrentItem(
                        (binding!!.pagerProductInfo.currentItem + 1) % pagerAdapter!!.count,
                        true
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveInPref(binding: ActivitySignupBinding) {
        sharedPref.setPrefString("prefEmail", binding.etEmail.text.toString())
        sharedPref.setPrefBoolean("prefIsLogin", true)
    }

    fun onNext() {
        sharedPref.setPrefBoolean("prefIsIntro", true)

        if(sharedPref.getPrefBoolean("prefIsLogin")){
            val intent = Intent(mContext, OnboardingActivity::class.java)
            (mContext as Activity).startActivity(intent);
            (mContext as Activity).finish()
        }else {
//            intent.putExtra("isSignup", false)
            val intent = Intent(mContext, LoginActivity::class.java)
            (mContext as Activity).startActivity(intent);
            (mContext as Activity).finish()
        }


    }

    private fun startAnimatingBanner() {
        handler!!.removeCallbacks(runnable)
        handler!!.postDelayed(runnable, timeDelay.toLong())
    }

    fun onDestroy() {
        handler!!.removeCallbacks(runnable)
    }
}
