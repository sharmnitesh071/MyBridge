package com.databridge.mybridge.ui

import androidx.fragment.app.Fragment
import com.databridge.mybridge.common.CommonUtil
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.domain.repository.RetrofitRepository
import com.databridge.mybridge.domain.room.AppDataBase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment : Fragment() {

    @Inject
    lateinit var appDataBase: AppDataBase

    @Inject
    lateinit var sharedPref: SharedPref

    @Inject
    lateinit var retrofitRepository: RetrofitRepository

}