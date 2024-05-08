package com.databridge.mybridge.ui.onboarding.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.databinding.ActivitySearchLocationBinding
import com.databridge.mybridge.domain.model.onboarding.CollageResults
import com.databridge.mybridge.ui.onboarding.utils.CollageAdapter
import com.databridge.mybridge.ui.onboarding.viewmodel.SearchSchoolViewModel
import com.google.gson.Gson


class SearchCollageActivity : BaseActivity(), View.OnClickListener {

    // binding
    private var _binding: ActivitySearchLocationBinding? = null
    private val binding get() = _binding!!

    private val dataList: ArrayList<CollageResults> = ArrayList()

    private val viewModel: SearchSchoolViewModel by viewModels()
    private lateinit var adapter: CollageAdapter
    var countryCode = "IN"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(activity, R.layout.activity_search_location)

        init()
        initObserver()
    }

    private fun initObserver() {
    }

    fun init() {
        countryCode = intent.extras?.getString("countryCode").toString()


        binding.tvTitle.text = getString(R.string.search_collage)

        adapter = CollageAdapter(activity, dataList)
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val data = dataList[position]
            val intent = Intent()
            intent.putExtra("data", Gson().toJson(data))
            intent.putExtra("school", data.name)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }

        binding.tvDone.setOnClickListener {
            finish()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                if (query!!.length > 2)
//                    searchData(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { text ->
                    filterData(text)
                }
                return true
            }
        })

        viewModel.countryCode = countryCode
        viewModel.searchResponse.observe(activity) {
            it.let {
//                Debug.e("----", "--searchResponse-data-" + it.data!!.size)
                if (it.data != null) {
                    dataList.clear()
                    dataList.addAll(it.data.results)
                    adapter.notifyDataSetChanged()
                }
            }

        }

        filterData("")
    }

    private fun filterData(query: String) {
        searchData(query)
    }

    private fun searchData(query: String) {
        with(viewModel) {
            if (isInternetConnected(activity))
                searchapiCall(query)
        }

    }

    override fun onClick(view: View?) {
        when (view) {

        }
    }

}