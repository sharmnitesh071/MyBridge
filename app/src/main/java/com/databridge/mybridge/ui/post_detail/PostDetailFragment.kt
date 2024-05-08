package com.databridge.mybridge.ui.post_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.databridge.mybridge.base.convertDateTime
import com.databridge.mybridge.base.loadImage
import com.databridge.mybridge.base.setLikeDislikeCount
import com.databridge.mybridge.base.setLikeDislikeImage
import com.databridge.mybridge.base.setPostUserTitle
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.FragmentPostDetailBinding
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.ui.post.PostViewModel

private const val ARG_PARAM1 = "position"

class PostDetailFragment : BaseFragment() {
    private var position: Int = 0

    // binding
    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    // view model
    private val viewModel: PostViewModel by activityViewModels()

    var email = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_PARAM1, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPostDetailBinding.inflate(layoutInflater)
        email = sharedPref.getPrefString(Conts.PREF_EMAIL) ?: ""
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initBottomBar()
        initData()
        Log.e("TAG", "---->> onViewCreated: position - $position")
        Log.e("TAG", "---->> onViewCreated: list - ${viewModel.postList.size}")
    }

    private fun initData() {
        with(binding) {
            // back button visibility Gone
            layoutHeader.imgBack.visibility = View.GONE

            val author = viewModel.postList[position]?.author
            val taggedUsers = viewModel.postList[position]?.taggedUsers
            val media = viewModel.postList[position]?.media

            // region header
            layoutHeader.imgProfile.loadImage(author?.profilePic ?: "")
            layoutHeader.tvProfileName.setPostUserTitle(author, taggedUsers)
            layoutHeader.tvDesignation.text = author?.position ?: ""

            layoutHeader.tvDuration.text = com.databridge.mybridge.common.CommonUtil.offsetFrom(
                viewModel.postList[position]?.createdAt ?: ""
            )

            // endregion

            // region content
            if (viewModel.postList[position]?.content.isNullOrEmpty()) {
                layoutDescription.tvDesignation.visibility = View.GONE
            } else {
                layoutDescription.tvDesignation.visibility = View.VISIBLE
                layoutDescription.tvDesignation.text = viewModel.postList[position]?.content
            }
            // endregion

            // region post, event and article
            // post visibility
            layoutImage.imgPost.visibility = View.GONE
            // event visibility
            layoutHeader.imgEvents.visibility = View.GONE
            layoutHeader.tvEventTitle.visibility = View.GONE
            layoutDescription.tvPostTitle.visibility = View.GONE
            layoutButton.btnRSVP.visibility = View.GONE
            layoutButton.tvRSVP.visibility = View.GONE
            // article visibility
            if (viewModel.postList[position]?.type == Conts.POST_TYPE_NORMAL) {
                // by default all visibility GONE here .. gif, image, video
                if (viewModel.postList[position]?.gif.isNullOrEmpty().not()) {
                    // gif
                } else if (media.isNullOrEmpty().not()) {
                    if (com.databridge.mybridge.common.CommonUtil.isVideoType(
                            media!![0]?.file ?: ""
                        )
                    ) { //todo
                        // video
                    } else {
                        // image
                        layoutImage.imgPost.visibility = View.VISIBLE
                        layoutImage.imgPost.loadImage(
                            media[0]?.file ?: ""
                        ) //todo list image inside recyclerview
                    }
                }
            } else if (viewModel.postList[position]?.type == Conts.POST_TYPE_EVENT) {
                // event
                layoutHeader.imgEvents.visibility = View.VISIBLE
                layoutHeader.tvEventTitle.visibility = View.VISIBLE
                if (com.databridge.mybridge.common.CommonUtil.rsvpButtonEnable(
                        viewModel.postList[position]?.attendees,
                        email
                    )
                ) {
                    layoutButton.btnRSVP.visibility = View.GONE
                    layoutButton.tvRSVP.visibility = View.VISIBLE

                    if (viewModel.postList[position]?.eventType == Conts.EVENT_GENERAL) {
                        layoutButton.tvRSVP.text =
                            getString(com.databridge.mybridge.R.string.rsvp_ed)
                    } else if (viewModel.postList[position]?.eventType == Conts.EVENT_WEBINAR) {
                        layoutButton.tvRSVP.text =
                            getString(com.databridge.mybridge.R.string.registered)
                    }
                } else {
                    layoutButton.btnRSVP.visibility = View.VISIBLE
                    layoutButton.tvRSVP.visibility = View.GONE

                    if (viewModel.postList[position]?.eventType == Conts.EVENT_GENERAL) {
                        layoutButton.btnRSVP.text =
                            getString(com.databridge.mybridge.R.string.rsvp)
                    } else if (viewModel.postList[position]?.eventType == Conts.EVENT_WEBINAR) {
                        layoutButton.btnRSVP.text =
                            getString(com.databridge.mybridge.R.string.register)
                    }
                }

                layoutHeader.tvEventTitle.convertDateTime(
                    viewModel.postList[position]?.startTime,
                    viewModel.postList[position]?.timezone ?: "UTC"
                )

                viewModel.postList[position]?.title.let {
                    layoutDescription.tvPostTitle.visibility = View.VISIBLE
                    layoutDescription.tvPostTitle.text = viewModel.postList[position]?.title
                }
            } else if (viewModel.postList[position]?.type == Conts.POST_TYPE_ARTICLE) {
                // article
            }
            // endregion

            // region count
            // share count
            if (viewModel.postList[position]?.shareCount == 0) {
                layoutCount.tvShareCount.visibility = View.GONE
                layoutCount.tvShareTitle.visibility = View.GONE
            } else {
                layoutCount.tvShareCount.text = viewModel.postList[position]?.shareCount.toString()

                layoutCount.tvShareCount.visibility = View.VISIBLE
                layoutCount.tvShareTitle.visibility = View.VISIBLE
            }
            // comment count
            if (viewModel.postList[position]?.commentCount == 0) {
                layoutCount.tvCommentCount.visibility = View.GONE
                layoutCount.tvCommentTitle.visibility = View.GONE
            } else {
                layoutCount.tvCommentCount.text =
                    viewModel.postList[position]?.commentCount.toString()

                layoutCount.tvCommentCount.visibility = View.VISIBLE
                layoutCount.tvCommentTitle.visibility = View.VISIBLE
            }
            // like and dislike count
            if (viewModel.postList[position]?.likeCount == 0 && viewModel.postList[position]?.dislikeCount == 0) {
                layoutCount.tvLikeDislikeCount.visibility = View.GONE
                layoutCount.imgAgreeDisagree.visibility = View.GONE
            } else {
                // set like and dislike value
                layoutCount.tvLikeDislikeCount.visibility = View.VISIBLE
                layoutCount.imgAgreeDisagree.visibility = View.VISIBLE
                layoutCount.tvLikeDislikeCount.setLikeDislikeCount(
                    viewModel.postList[position]?.likeCount ?: 0,
                    viewModel.postList[position]?.dislikeCount ?: 0
                )
                layoutCount.imgAgreeDisagree.setLikeDislikeImage(
                    viewModel.postList[position]?.likeCount ?: 0,
                    viewModel.postList[position]?.dislikeCount ?: 0
                )
            }

            // endregion

            // region agree and disagree button
            layoutButton.btnAgree.isSelected =
                com.databridge.mybridge.common.CommonUtil.ownPostLiked(
                    viewModel.postList[position]?.likedBy,
                    email
                )

            layoutButton.btnDisagree.isSelected =
                com.databridge.mybridge.common.CommonUtil.ownPostDisliked(
                    viewModel.postList[position]?.dislikeBy,
                    email
                )
            // endregion
        }
    }

    private fun initBottomBar() {
        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE
    }

    private fun initToolbar() {
        (activity as MainActivity).binding.toolbar.toolbarLayout.visibility = View.GONE
        (activity as MainActivity).binding.toolbar1.toolbarLayout1.visibility = View.GONE
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}