package com.databridge.mybridge.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.databridge.mybridge.domain.model.post.Attendees
import com.databridge.mybridge.domain.model.post.UserProfileItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class CommonUtil {
    // global function

    companion object {
        fun getPostUserName(
            firstName: String,
            formerName: String?,
            lastName: String
        ): String {
            return if (formerName.isNullOrEmpty() || formerName == "null") {
                "$firstName $lastName"
            } else {
                "$firstName ($formerName) $lastName"
            }
        }

        fun taggedUserName(taggedUserList: List<UserProfileItem?>): String {
            return taggedUserList.joinToString(separator = ", ") {
                getPostUserName(it?.firstName ?: "", it?.formerName, it?.lastName ?: "")
            }
        }

        fun isVideoType(file: String): Boolean {
            val videoExtensions = listOf("mp4", "mov", "avi", "wmv", "mkv", "flv")
            val fileExtension = file.substringAfterLast('.', "")
            return videoExtensions.contains(fileExtension.lowercase())
        }

        fun offsetFrom(createdDate: String): String {
            if (createdDate.isEmpty()) {
                return ""
            } else {
                // Parse the string into a Date
                val dateFormat =
                    SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US)
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                val parsedDate = dateFormat.parse(createdDate)

                // Convert Date to Calendar
                val date = Calendar.getInstance()
                date.time = parsedDate as Date

                val now = Calendar.getInstance()
                val diff = now.timeInMillis - date.timeInMillis

                val SECOND_MILLIS: Long = 1000
                val MINUTE_MILLIS = 60 * SECOND_MILLIS
                val HOUR_MILLIS = 60 * MINUTE_MILLIS
                val DAY_MILLIS = 24 * HOUR_MILLIS
                val MONTH_MILLIS = 30 * DAY_MILLIS
                val YEAR_MILLIS = 12 * MONTH_MILLIS

                val seconds = "a few seconds ago"
                val minutes = "${diff / MINUTE_MILLIS}m ago"
                val hours = "${diff / HOUR_MILLIS}h ago"
                val days = "${diff / DAY_MILLIS}d ago"
                val months = "${diff / MONTH_MILLIS}m ago"
                val years = "${diff / YEAR_MILLIS}y ago"

                when {
                    diff < MINUTE_MILLIS -> {
                        return seconds
                    }

                    diff < 60 * MINUTE_MILLIS -> {
                        return minutes
                    }

                    diff < 24 * HOUR_MILLIS -> {
                        return hours
                    }

                    diff < 48 * HOUR_MILLIS -> {
                        return "a day ago"
                    }

                    diff < 30 * DAY_MILLIS -> {
                        return days
                    }

                    diff < 12 * MONTH_MILLIS -> {
                        return months
                    }

                    else -> {
                        return years
                    }
                }
            }
        }

        fun ownPostLiked(list: List<UserProfileItem?>?, email: String): Boolean {
            if (email.isEmpty())
                return false
            return list?.any { it?.email == email } ?: false
        }

        fun ownPostDisliked(list: List<UserProfileItem?>?, email: String): Boolean {
            if (email.isEmpty())
                return false
            return list?.any { it?.email == email } ?: false
        }

        fun rsvpButtonEnable(attendees: List<Attendees?>?, email: String): Boolean {
            return attendees?.any { it?.user?.email == email } ?: false
        }

        fun copyToClipboard(context: Context, text: String) {
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("text", text)
            clipboardManager.setPrimaryClip(clipData)
        }
    }
}
