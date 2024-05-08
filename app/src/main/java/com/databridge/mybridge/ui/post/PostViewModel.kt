package com.databridge.mybridge.ui.post

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.CommonUtil
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.domain.model.post.PostItem
import com.databridge.mybridge.domain.model.post.PostResponse
import com.databridge.mybridge.domain.model.post.UserConnectionResponse
import com.databridge.mybridge.domain.model.post.request.AgreeDisagreeRequest
import com.databridge.mybridge.domain.model.post.request.FollowUnfollowRequest
import com.databridge.mybridge.domain.model.post.request.ReportBlockPostRequest
import com.databridge.mybridge.domain.repository.RetrofitRepository
import com.databridge.mybridge.ui.login.login.view.LoginActivity
import com.databridge.mybridge.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: RetrofitRepository,
    private val sharedPref: SharedPref
) : ViewModel() {

    private val _response: MutableLiveData<Resource<PostResponse>> = MutableLiveData()
    val response: LiveData<Resource<PostResponse>> = _response

    private val _responseUserConnection: MutableLiveData<Resource<UserConnectionResponse>> =
        MutableLiveData()
    val responseUserConnection: LiveData<Resource<UserConnectionResponse>> = _responseUserConnection


    private val _responsePostDetail: MutableLiveData<Resource<PostItem>> = MutableLiveData()
    val responsePostDetail: LiveData<Resource<PostItem>> = _responsePostDetail

    private val _responsePostNotInterested: MutableLiveData<Int> =
        MutableLiveData()
    val responsePostNotInterested: LiveData<Int> = _responsePostNotInterested

    val postList: MutableList<PostItem?> = mutableListOf()
    var userConnection: UserConnectionResponse = UserConnectionResponse()

    var currentPage: Int = 1
    var totalItemCount: Int = 0
    var isScrolling = false
    var isApiCalling = false
    private val pageSize: Int = 10

    fun apiCall() {
        userConnectionApiCall()
        if (sharedPref.getPrefBoolean("prefIsLogin").not()) {
            apiSearchPublicPostApiCall()
        } else {
            apiPostApiCall()
        }
    }

    private fun apiSearchPublicPostApiCall() = viewModelScope.launch {
        _response.postValue(Resource.loading())
        isApiCalling = true


        val reqParam = HashMap<String, Int>()
        reqParam["page"] = currentPage
        reqParam["page_size"] = pageSize

        repository.getSearchPublicPost(reqParam).collect { values ->
            isApiCalling = false
            _response.value = values
        }
    }

    private fun apiPostApiCall() = viewModelScope.launch {
        _response.postValue(Resource.loading())
        isApiCalling = true

        val reqParam = HashMap<String, Int>()
        reqParam["page"] = currentPage
        reqParam["page_size"] = pageSize

        repository.getPosts(reqParam).collect { values ->
            isApiCalling = false
            _response.value = values
        }
    }

    fun openLoginScreen(activity: Activity) {
        activity.startActivity(Intent(activity, LoginActivity::class.java))
    }

    fun agreeApiCall(position: Int) = viewModelScope.launch {
        _responsePostDetail.postValue(Resource.loading())

        val postId = postList[position]?.id ?: 0
        val email = sharedPref.getPrefString(Conts.PREF_EMAIL) ?: ""

        val agreeList = postList[position]?.likedBy
        val postAgreed: Boolean = CommonUtil.ownPostLiked(agreeList, email)

        repository.postAgree(postId, AgreeDisagreeRequest(remove = postAgreed))
            .collect { values ->
                if (values.status == Resource.Status.SUCCESS) {
                    postDetail(position = position)
                } else {
                    _responsePostDetail.postValue(
                        Resource.error(
                            values.errorBody,
                            values.message ?: "",
                            null
                        )
                    )
                }
            }
    }

    fun disagreeApiCall(position: Int) = viewModelScope.launch {
        _responsePostDetail.postValue(Resource.loading())

        val postId = postList[position]?.id ?: 0
        val email = sharedPref.getPrefString(Conts.PREF_EMAIL) ?: ""

        val disagreeList = postList[position]?.dislikeBy
        val postDisagreed: Boolean = CommonUtil.ownPostDisliked(disagreeList, email)

        repository.postDisagree(postId, AgreeDisagreeRequest(remove = postDisagreed))
            .collect { values ->
                if (values.status == Resource.Status.SUCCESS) {
                    postDetail(position = position)
                } else {
                    _responsePostDetail.postValue(
                        Resource.error(
                            values.errorBody,
                            values.message ?: "",
                            null
                        )
                    )
                }
            }
    }

    private fun postDetail(position: Int = -1, newPostId: Int = -1) = viewModelScope.launch {
        val postId = if (newPostId == -1) {
            // for repost - add new data and add to existing list ...
            postList[position]?.id ?: 0
        } else {
            newPostId
        }
        repository.getPostDetail(postId).collect { values ->
            _responsePostDetail.value = values
        }
    }

    fun rsvpCallApi(position: Int) = viewModelScope.launch {
        val postId = postList[position]?.id ?: 0

        repository.setRsvpEvent(postId).collect { values ->
            _responsePostDetail.value = values
        }
    }

    fun notInterestedPostApiCall(postId: Int, activity: Activity) = viewModelScope.launch {
        repository.notInterestedPost(postId).collect { values ->
            if (values.status == Resource.Status.SUCCESS) {
                _responsePostNotInterested.value = postId
                activity.showToast("Post ignored successfully")
            }
        }
    }

    fun userFollowCallApi(userId: Int, activity: Activity, userName: String) =
        viewModelScope.launch {
            repository.followUser(FollowUnfollowRequest(follow_to = userId)).collect { values ->
                if (values.status == Resource.Status.SUCCESS) {
                    userConnectionApiCall()
                    activity.showToast("Following $userName")
                }
            }
        }

    fun userUnfollowCallApi(userId: Int, activity: Activity, userName: String) =
        viewModelScope.launch {
            repository.unfollowUser(FollowUnfollowRequest(follow_to = userId)).collect { values ->
                if (values.status == Resource.Status.SUCCESS) {
                    userConnectionApiCall()
                    activity.showToast("Unfollowing $userName")
                }
            }
        }

    private fun userConnectionApiCall() = viewModelScope.launch {
        repository.userProfileConnection().collect { values ->
            _responseUserConnection.value = values
        }
    }

    fun isFollowUser(userId: Int): Boolean {
        return userConnection.following?.any { it1 ->
            it1 == userId
        } ?: false
    }

    fun reportBlockPostApiCall(postId: Int, message: String, activity: Activity) =
        viewModelScope.launch {
            val reqParam = ReportBlockPostRequest(postId, 5, message) // todo
            repository.reportAndBlockPost(reqParam)
                .collect { values ->
                    if (values.status == Resource.Status.SUCCESS) {
                        activity.showToast("Blocking post")
                    }
                }
        }

    fun deletePostApiCall(postId: Int, activity: Activity) =
        viewModelScope.launch {
            repository.deletePost(postId)
                .collect { values ->
                    if (values.status == Resource.Status.SUCCESS) {
                        _responsePostNotInterested.value = postId
                        activity.showToast("Deleted post")
                    }
                }
        }

    fun repostCallApi(postId: Int) = viewModelScope.launch {
        repository.rePost(postId = postId).collect { values ->
            if (values.status == Resource.Status.SUCCESS) {
                postDetail(newPostId = values.data?.postId ?: 0)
            }
        }
    }


}