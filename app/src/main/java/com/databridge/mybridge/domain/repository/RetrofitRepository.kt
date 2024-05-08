package com.databridge.mybridge.domain.repository

import com.databridge.mybridge.domain.model.post.PostItem
import com.databridge.mybridge.domain.model.post.PostResponse
import com.databridge.mybridge.domain.model.post.RepostResponse
import com.databridge.mybridge.domain.model.post.UserConnectionResponse
import com.databridge.mybridge.domain.model.post.request.AgreeDisagreeRequest
import com.databridge.mybridge.domain.model.post.request.FollowUnfollowRequest
import com.databridge.mybridge.domain.model.post.request.ReportBlockPostRequest
import com.databridge.mybridge.domain.rest.RetrofitService
import com.databridge.mybridge.utils.BaseApiResponse
import com.databridge.mybridge.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RetrofitRepository @Inject constructor(
    private val service: RetrofitService
) : BaseApiResponse() {

    // search public post list (User is Offline)
    suspend fun getSearchPublicPost(
        reqParam: Map<String, Int>
    ): Flow<Resource<PostResponse>> {
        return flow {
            emit(safeApiCall { service.getSearchPublicPost(reqParam) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPosts(reqParam: Map<String, Int>): Flow<Resource<PostResponse>> {
        return flow {
            emit(safeApiCall { service.getPosts(reqParam) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getPostDetail(postId: Int): Flow<Resource<PostItem>> {
        return flow {
            emit(safeApiCall { service.getPostDetail(postId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun setRsvpEvent(postId: Int): Flow<Resource<PostItem>> {
        return flow {
            emit(safeApiCall { service.setRsvpEvent(postId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postAgree(
        postId: Int,
        requestBody: AgreeDisagreeRequest
    ): Flow<Resource<Any>> {
        return flow {
            emit(safeApiCall { service.postAgree(postId, requestBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun postDisagree(
        postId: Int,
        requestBody: AgreeDisagreeRequest
    ): Flow<Resource<Any>> {
        return flow {
            emit(safeApiCall { service.postDisagree(postId = postId, requestBody = requestBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun notInterestedPost(
        postId: Int
    ): Flow<Resource<List<String>>> {
        return flow {
            emit(safeApiCall { service.setNotInterestedPost(postId = postId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun followUser(
        requestBody: FollowUnfollowRequest
    ): Flow<Resource<Any>> {
        return flow {
            emit(safeApiCall { service.followUser(requestBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun unfollowUser(
        requestBody: FollowUnfollowRequest
    ): Flow<Resource<Any>> {
        return flow {
            emit(safeApiCall { service.unFollowUser(requestBody) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun userProfileConnection(
    ): Flow<Resource<UserConnectionResponse>> {
        return flow {
            emit(safeApiCall { service.userProfileConnection() })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun reportAndBlockPost(
        reportBlockPostRequest: ReportBlockPostRequest
    ): Flow<Resource<Any>> {
        return flow {
            emit(safeApiCall { service.reportBlockPost(reportBlockPostRequest) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun deletePost(
        postId: Int
    ): Flow<Resource<Any>> {
        return flow {
            emit(safeApiCall { service.deletePost(postId) })
        }.flowOn(Dispatchers.IO)
    }

    suspend fun rePost(
        postId: Int
    ): Flow<Resource<RepostResponse>> {
        return flow {
            emit(safeApiCall { service.rePost(postId) })
        }.flowOn(Dispatchers.IO)
    }


}