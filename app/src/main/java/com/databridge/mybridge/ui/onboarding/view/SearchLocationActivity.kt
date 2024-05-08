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
import com.databridge.mybridge.domain.model.onboarding.SearchCityResponse
import com.databridge.mybridge.ui.onboarding.utils.LocationAdapter
import com.databridge.mybridge.ui.onboarding.viewmodel.SearchLocationViewModel


class SearchLocationActivity : BaseActivity(), View.OnClickListener {

    // binding
    private var _binding: ActivitySearchLocationBinding? = null
    private val binding get() = _binding!!

    private val dataList: ArrayList<SearchCityResponse> = ArrayList()

    private val viewModel: SearchLocationViewModel by viewModels()
    private lateinit var adapter: LocationAdapter
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

        adapter = LocationAdapter(activity, dataList)
        binding.listView.adapter = adapter
        binding.listView.setOnItemClickListener { parent, view, position, id ->
            val data = dataList[position]
            val intent = Intent()
            intent.putExtra("country", data.country)
            intent.putExtra("city", data.city)
            intent.putExtra("state", data.state)
            intent.putExtra("postalCode", data.postalCode)
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
                    dataList.addAll(it.data)
                    adapter.notifyDataSetChanged()
                }
            }

        }
    }

    private fun filterData(query: String) {
        searchData(query)

//        val filteredList = dataList.filter { it.city!!.contains(query, ignoreCase = true) }
//        adapter.clear()
//        adapter.addAll(filteredList)
//        adapter.notifyDataSetChanged()
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