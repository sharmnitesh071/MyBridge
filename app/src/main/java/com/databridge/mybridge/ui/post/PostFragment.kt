package com.databridge.mybridge.ui.post

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.databridge.mybridge.R
import com.databridge.mybridge.adapter.PostRecyclerAdapter
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.CommonUtil
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.common.Conts.SHARE_POST_URL
import com.databridge.mybridge.common.mainNav
import com.databridge.mybridge.databinding.DialogDeletePostBinding
import com.databridge.mybridge.databinding.DialogOwnPostOptionMenuBinding
import com.databridge.mybridge.databinding.DialogPostOptionMenuBinding
import com.databridge.mybridge.databinding.DialogReportBlockBinding
import com.databridge.mybridge.databinding.DialogRepostBinding
import com.databridge.mybridge.databinding.FragmentPostBinding
import com.databridge.mybridge.databinding.SharePostOptionMenuBinding
import com.databridge.mybridge.domain.model.post.PostItem
import com.databridge.mybridge.domain.model.post.UserConnectionResponse
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.utils.Resource
import com.google.android.material.bottomsheet.BottomSheetDialog

class PostFragment : BaseFragment() {

    // binding
    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: PostViewModel by activityViewModels()

    // adapter
    private lateinit var mAdapter: PostRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        // Api call
        if (viewModel.response.value?.data == null) {
            initObserver()
            viewModel.apiCall()
        }
    }

    private fun initAdapter() {
        // adapter
        val email = sharedPref.getPrefString(Conts.PREF_EMAIL) ?: ""
        mAdapter =
            PostRecyclerAdapter(viewModel.postList, requireContext(), email) { position, view ->
                when (view.id) {
                    R.id.btnShare -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        sharePostOptionMenuBottomDialog(item = viewModel.postList[position])
                    }

                    R.id.btnComment -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        val args = Bundle().apply {
                            putInt("position", position)
                        }
                        mainNav.navigate(R.id.action_postFragment_to_postDetailFragment, args)
                    }

                    R.id.btnDisagree -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        viewModel.disagreeApiCall(position)
                    }

                    R.id.btnAgree -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        viewModel.agreeApiCall(position)
                    }

                    R.id.tvCommentTitle, R.id.tvCommentCount -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        Log.e("TAG", "---->> comment count Clicked: $position")
                    }

                    R.id.tvShareTitle, R.id.tvShareCount -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        Log.e("TAG", "---->> share Clicked: $position")
                    }

                    R.id.imgMoreInfo -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        if (viewModel.postList[position]?.author?.email ==
                            sharedPref.getPrefString(Conts.PREF_EMAIL)
                        )
                            ownPostOptionMenuBottomDialog(viewModel.postList[position])
                        else
                            postOptionMenuBottomDialog(viewModel.postList[position])

                    }

                    R.id.btnRSVP -> {
                        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
                            viewModel.openLoginScreen(requireActivity())
                            return@PostRecyclerAdapter
                        }
                        viewModel.rsvpCallApi(position)
                    }

                    R.id.imgProfile, R.id.tvProfileName, R.id.tvDesignation, R.id.tvDuration -> {
                        Log.e("TAG", "---->> profile Clicked: $position")
                        Toast.makeText(
                            requireActivity(),
                            "Open Profile screen, coming soon",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        binding.rvPost.apply {
            this.adapter = mAdapter
            this.layoutManager = LinearLayoutManager(requireContext())
            val itemDecoration = DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL)
            itemDecoration.setDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_divider
                )!!
            )
            addItemDecoration(itemDecoration)
        }

        // pagination
        binding.rvPost.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    viewModel.isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val currentItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!viewModel.isApiCalling && viewModel.isScrolling && totalItemCount < viewModel.totalItemCount) {
                    if (currentItemCount + lastVisibleItemPosition >= totalItemCount && lastVisibleItemPosition >= 0 && dy >= 0) {
                        viewModel.isScrolling = false
                        viewModel.currentPage += 1
                        binding.progressBarFootBar.visibility = View.VISIBLE
                        viewModel.apiCall()
                    }
                }
            }
        })
    }

    private fun initObserver() {
        viewModel.responsePostNotInterested.observe(viewLifecycleOwner) { postId ->
            val iterator = viewModel.postList.iterator()
            while (iterator.hasNext()) {
                val item = iterator.next()
                if (item?.id == postId) {
                    iterator.remove()
                    mAdapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.responseUserConnection.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.userConnection = it ?: UserConnectionResponse()
                    }
                }

                else -> {}
            }
        }

        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        it?.results?.let { it1 ->
                            viewModel.postList.addAll(it1)
                        }

                        viewModel.totalItemCount = it?.count ?: 0
                    }
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarFootBar.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.progressBarFootBar.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    if (viewModel.currentPage == 1) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBarFootBar.visibility = View.VISIBLE
                    }
                }
            }
        }

        viewModel.responsePostDetail.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let { it1 ->
                        var isRepost = true
                        viewModel.postList.mapIndexed { index, item ->
                            if (item?.id == it1?.id) {
                                isRepost = false
                                // if match than data change in existing list
                                viewModel.postList[index] = it1
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                        if (isRepost) {
                            // add new data at 0 position and recycler scroll
                            viewModel.postList.add(0, it1)
                            mAdapter.notifyDataSetChanged()
                            binding.rvPost.layoutManager?.scrollToPosition(0)
                        }
                    }
                    // loader gone
                }

                Resource.Status.ERROR -> {
                    // loader gone
                }

                Resource.Status.LOADING -> {
                    // loader visible
                }
            }
        }
    }

    private fun sharePostOptionMenuBottomDialog(item: PostItem?) {
        val dialog = BottomSheetDialog(requireContext())
        val binding = SharePostOptionMenuBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)


        binding.tvRepost.setOnClickListener {
            repostDialog(item)
            dialog.dismiss()
        }

        binding.tvQuotePost.setOnClickListener {
            dialog.dismiss()
        }

        binding.tvCopyLink.setOnClickListener {
            val shareUrl = SHARE_POST_URL + item?.id
            CommonUtil.copyToClipboard(requireContext(), shareUrl)
            requireActivity().showToast(getString(R.string.copied))
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun postOptionMenuBottomDialog(item: PostItem?) {
        val dialog = BottomSheetDialog(requireContext())
        val binding = DialogPostOptionMenuBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        /*val userName = CommonUtil.getPostUserName(
            item?.author?.firstName ?: "",
            item?.author?.formerName,
            item?.author?.lastName ?: ""
        )*/

        val followOrUnfollow = if (viewModel.isFollowUser(item?.author?.id ?: 0))
            getString(R.string.unfollow)
        else getString(R.string.follow)

        val followText = "$followOrUnfollow @${item?.author?.slug}"
        binding.tvFollow.text = followText

        binding.tvNotInterested.setOnClickListener {
            viewModel.notInterestedPostApiCall(item?.id ?: 0, requireActivity())
            dialog.dismiss()
        }

        binding.tvFollow.setOnClickListener {
            Log.e("TAG", "---->> postOptionMenuBottomDialog: ${viewModel.userConnection}")
            val fullName = "${item?.author?.firstName} ${item?.author?.lastName}"
            if (viewModel.isFollowUser(item?.author?.id ?: 0))
                viewModel.userUnfollowCallApi(item?.author?.id ?: 0, requireActivity(), fullName)
            else
                viewModel.userFollowCallApi(item?.author?.id ?: 0, requireActivity(), fullName)

            dialog.dismiss()
        }

        binding.tvHandShakeRequest.setOnClickListener {

        }
        binding.tvBlockReport.setOnClickListener {
            blockReportDialog(item)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun blockReportDialog(item: PostItem?) {
        val binding = DialogReportBlockBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context)
            .setView(binding.root)

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val fullName = "${item?.author?.firstName} ${item?.author?.lastName}"
        val title = "${getString(R.string.block_report_title)} $fullName"
        binding.tvTitle.text = title

        binding.btnCancel.setOnClickListener { alertDialog.dismiss() }

        var selectedRadio = ""
        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = binding.root.findViewById<RadioButton>(checkedId)
            selectedRadio = radioButton.text.toString()
        }

        binding.btnReport.setOnClickListener {
            viewModel.reportBlockPostApiCall(item?.id ?: 0, selectedRadio, requireActivity())
            alertDialog.dismiss()
        }

        alertDialog.show()

    }

    private fun ownPostOptionMenuBottomDialog(item: PostItem?) {
        val dialog = BottomSheetDialog(requireContext())
        val binding = DialogOwnPostOptionMenuBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)


        binding.tvDelete.setOnClickListener {
            postDeleteDialog(item)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun repostDialog(item: PostItem?) {
        val binding = DialogRepostBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context)
            .setView(binding.root)

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnCancel.setOnClickListener { alertDialog.dismiss() }
        binding.btnRepost.setOnClickListener {
            viewModel.repostCallApi(item?.id ?: 0)
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun postDeleteDialog(item: PostItem?) {
        val binding = DialogDeletePostBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(context)
            .setView(binding.root)

        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnCancel.setOnClickListener { alertDialog.dismiss() }

        binding.btnDelete.setOnClickListener {
            viewModel.deletePostApiCall(item?.id ?: 0, requireActivity())
            alertDialog.dismiss()
        }

        alertDialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}