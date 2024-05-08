package com.databridge.mybridge.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.databridge.mybridge.R
import com.databridge.mybridge.common.CommonUtil
import com.databridge.mybridge.common.SharedPref
import com.databridge.mybridge.domain.model.post.UserProfileItem
import com.databridge.mybridge.exceptions.networks.NoInternetException
import com.databridge.mybridge.ui.MainActivity
import com.databridge.mybridge.utils.Utils
import com.databridge.mybridge.utils.Utils.showToast
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.regex.Pattern

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.hideKeyboard() {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    //Find the currently focused view, so we can grab the correct window token from it.
    var view = this.currentFocus
    //If no view currently has focus, create a new one, just so we can grab a window token from it
    if (view == null) {
        view = View(this)
    }
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun isInternetConnected(mContext: Context?): Boolean {
    var outcome = false
    try {
        if (mContext != null) {
            val cm = mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.allNetworkInfo
            for (tempNetworkInfo in networkInfo) {
                if (tempNetworkInfo.isConnected) {
                    outcome = true
                    break
                }
            }
        }
    } catch (e: NoInternetException) {
        e.printStackTrace()
    }
    if (outcome.not())
        showToast(mContext!!, mContext.getString(R.string.no_internet))

    return outcome
}

fun String.createShortName(): String {
    val names = this.split(" ")
    var shortName = ""
    for (name in names) {
        if (name.isNotEmpty()) {
            shortName += name[0]
        }
    }
    return shortName.uppercase()
}


fun encodeString(str: String): String {
    return URLEncoder.encode(str, StandardCharsets.UTF_8.toString())
}

fun String.matches(input: String): Boolean {
    val pattern = Pattern.compile(input)
    return pattern.matcher(this).matches()
}

fun String.isNullorEmptyNot(): Boolean {
    return this.isNullOrEmpty().not() && (this == "null").not()
}


fun String.notMatches(email: String): Boolean {
    return !matches(email)
}

fun ImageView.loadImage(url: String) {
    Glide.with(this)
        .load(url)
        .placeholder(R.color.grey_very_light)
        .error(R.color.grey_very_light)
        .into(this)
}

fun ImageView.loadRoundedImage(url: String) {
    val options: RequestOptions = RequestOptions()
        .placeholder(R.color.grey_very_light)
        .transforms(CenterCrop(), RoundedCorners(Utils.getPx(context, 5)))
        .error(R.color.grey_very_light)
    Glide.with(this)
        .load(url)
        .apply(options)
        .into(this)
}

fun ImageView.loadRoundedImage(url: String, placeholder: Int) {
    val options: RequestOptions = RequestOptions()
        .placeholder(placeholder)
        .transforms(CenterCrop(), RoundedCorners(Utils.getPx(context, 5)))
        .error(placeholder)
    Glide.with(this)
        .load(url)
        .apply(options)
        .into(this)
}

fun TextView.setPostUserTitle(author: UserProfileItem?, taggedUsers: List<UserProfileItem?>?) {
    // user name
    val userName = CommonUtil.getPostUserName(
        author?.firstName ?: "",
        author?.formerName,
        author?.lastName ?: ""
    )

    // tagged user name with comma separate
    val taggedUserName =
        if (taggedUsers.isNullOrEmpty()) "" else CommonUtil.taggedUserName(taggedUsers)

    // set text
    val title =
        if (taggedUserName.isEmpty()) userName else "$userName <small>with</small> $taggedUserName"
    this.text = HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY)

}

// agree and disagree post
fun TextView.setLikeDislikeCount(likeCount: Int, dislikeCount: Int) {
    val count = likeCount + dislikeCount
    this.text = count.toString()
}

fun ImageView.setLikeDislikeImage(likeCount: Int, dislikeCount: Int) {
    if (likeCount != 0 && dislikeCount == 0) {
        // set agree
        this.setImageResource(R.drawable.ic_agree_round)
    } else if (likeCount == 0 && dislikeCount != 0) {
        // set disagree
        this.setImageResource(R.drawable.ic_disagree_round)
    } else {
        // set agree and disagree
        this.setImageResource(R.drawable.ic_agree_disagree_round)
    }
}

fun logout(sharedPref: SharedPref, activity: Activity) {
    sharedPref.clearAllPref()
    val intent = Intent(activity, MainActivity::class.java)
    activity.startActivity(intent)
    activity.finishAffinity()
}

//fun TextView.setDurationText(createdDate: String?) {
//    this.text = CommonUtil.offsetFrom(createdDate ?: "")
//}

fun TextView.convertDateTime(dateTime: String?, timeZone: String) {
    this.text = dateTime?.let { date ->
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val outputFormat = SimpleDateFormat("MMM d, hh:mm a", Locale.US)
        outputFormat.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)

        try {
            val parsedDate = inputFormat.parse(date)
            "Event - ${outputFormat.format(parsedDate!!)}"
        } catch (e: Exception) {
            "Event"
        }
    } ?: "Event"
}