package com.databridge.mybridge.ui.appintro.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.databridge.mybridge.R
import com.databridge.mybridge.databinding.ActivityAppintroBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.appintro.viewmodel.AppintroViewModel

class AppintroActivity : BaseActivity() {
    var viewModel: AppintroViewModel? = null
    var binding: ActivityAppintroBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_appintro)
        viewModel = ViewModelProvider(this)[AppintroViewModel::class.java]
        viewModel!!.setBinder(binding!!,sharedPref)
    }
}
