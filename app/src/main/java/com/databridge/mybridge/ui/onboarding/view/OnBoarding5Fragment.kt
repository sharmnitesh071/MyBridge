package com.databridge.mybridge.ui.onboarding.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.databinding.FragmentOnboarding5Binding
import com.databridge.mybridge.ui.BaseFragment

class OnBoarding5Fragment : BaseFragment() {


    lateinit var binding : FragmentOnboarding5Binding
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding= DataBindingUtil.inflate<FragmentOnboarding5Binding>(
            /* inflater = */ inflater,
            /* layoutId = */ R.layout.fragment_onboarding5,
            /* parent = */ container,
            /* attachToParent = */ false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
    }
}