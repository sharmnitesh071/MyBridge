package com.databridge.mybridge.utils

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.preference.PreferenceManager
import android.text.*
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.databridge.mybridge.R
import com.databridge.mybridge.exceptions.networks.NoInternetException
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

object Utils {

    fun getHashKey() {
        val sha1 = "0f:04:66:e6:ee:a8:27:cb:3a:c9:42:6f:03:1c:14:e0:fd:fe:ec:45"
        val arr = sha1.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val byteArr = ByteArray(arr.size)

        for (i in arr.indices) {
            byteArr[i] = Integer.decode("0x" + arr[i]).toByte()
        }

        Debug.e("KeyHash 2 : ", Base64.encodeToString(byteArr, Base64.NO_WRAP))
    }

    fun getFileExt(fileName: String): String? {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length)
    }

    fun removeSpecialChar(s: String): String {
        return s.replace("[^a-zA-Z0-9]".toRegex(), "")
    }

    @Throws(NoInternetException::class)
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

        return outcome
    }

    fun random(min: Int, max: Int): Int {
        return Math.round((min + Math.random() * (max - min + 1)).toFloat())
    }

    fun hideKeyBoard(c: Context, v: View) {
        val imm = c
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
    }

    fun showKeyBoard(c: Context, v: View) {
        val imm = c
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        v.requestFocus()
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
//        imm.showSoftInput(v, 0)
    }

    fun getBold(c: Context): Typeface? {
        try {
            return Typeface.createFromAsset(c.assets,
                    "Raleway-Bold.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getSemiBold(c: Context): Typeface? {
        try {
            return Typeface.createFromAsset(c.assets,
                    "Raleway-SemiBold.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getRegular(c: Context): Typeface? {
        try {
            return Typeface.createFromAsset(c.assets,
                    "Raleway-Regular.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getLights(c: Context): Typeface? {
        try {
            return Typeface.createFromAsset(c.assets,
                    "Raleway-Light.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun getMedium(c: Context): Typeface? {
        try {
            return Typeface.createFromAsset(c.assets,
                    "Raleway-Medium.ttf")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }


    fun sendExceptionReport(e: Exception) {
        e.printStackTrace()

        try {
            // Writer result = new StringWriter();
            // PrintWriter printWriter = new PrintWriter(result);
            // e.printStackTrace(printWriter);
            // String stacktrace = result.toString();
            // new CustomExceptionHandler(c, URLs.URL_STACKTRACE)
            // .sendToServer(stacktrace);
        } catch (e1: Exception) {
            e1.printStackTrace()
        }

    }

    fun parseCalendarFormat(c: Calendar, pattern: String): String {
        val sdf = SimpleDateFormat(pattern,
                Locale.getDefault())
        return sdf.format(c.time)
    }

    fun parseTime(time: Long, pattern: String): String {
        val sdf = SimpleDateFormat(pattern,
                Locale.getDefault())
        return sdf.format(Date(time))
    }

    fun parseTime(time: String, pattern: String): Date {
        val sdf = SimpleDateFormat(pattern,
                Locale.getDefault())
        try {
            return sdf.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }

    fun parseTime(time: String, fromPattern: String,
                  toPattern: String): String {
        var sdf = SimpleDateFormat(fromPattern,
                Locale.getDefault())
        try {
            val d = sdf.parse(time)
            sdf = SimpleDateFormat(toPattern, Locale.getDefault())
            return sdf.format(d)
        } catch (e: Exception) {
            Log.i("parseTime", "" + e.message)
        }

        return ""
    }


    fun parseTime(time: String): String {
        return parseTime(time, "yyyy-MM-dd'T'HH:mm:ss", "MMM dd")
    }

    fun getTransactionDate(time: String): String {
        return parseTime(time, "yyyy-MM-dd'T'HH:mm:ss.SSS", "MM/dd/yyyy hh:mm")
    }


    fun parseDateTime(time: String, pattern: String): Date {
        var sdf = SimpleDateFormat(pattern,
                Locale.getDefault())
        try {
            val d = sdf.parse(time)
            sdf = SimpleDateFormat(pattern, Locale.getDefault())
            return sdf.parse(sdf.format(d))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Date()
    }

    fun parseTimeUTCtoDefault(time: String, pattern: String): Date {
        var sdf = SimpleDateFormat(pattern,
                Locale.getDefault())
        try {
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val d = sdf.parse(time)
            sdf = SimpleDateFormat(pattern, Locale.getDefault())
            sdf.timeZone = Calendar.getInstance().timeZone
            return sdf.parse(sdf.format(d))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Date()
    }

    fun parseTimeUTCtoDefault(time: Long): Date {

        try {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            cal.timeInMillis = time
            val d = cal.time
            val sdf = SimpleDateFormat()
            sdf.timeZone = Calendar.getInstance().timeZone
            return sdf.parse(sdf.format(d))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Date()
    }

    fun parseTimeUTCtoDefault(time: String, fromPattern: String,
                              toPattern: String): String {

        var sdf = SimpleDateFormat(fromPattern,
                Locale.getDefault())
        try {
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            val d = sdf.parse(time)
            sdf = SimpleDateFormat(toPattern, Locale.getDefault())
            sdf.timeZone = Calendar.getInstance().timeZone
            return sdf.format(d)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }

    fun daysBetween(startDate: Date, endDate: Date): Long {
        val sDate = getDatePart(startDate)
        val eDate = getDatePart(endDate)

        var daysBetween: Long = 0
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1)
            daysBetween++
        }
        return daysBetween
    }

    fun getDatePart(date: Date): Calendar {
        val cal = Calendar.getInstance()       // get calendar instance
        cal.time = date
        cal.set(Calendar.HOUR_OF_DAY, 0)            // set hour to midnight
        cal.set(Calendar.MINUTE, 0)                 // set minute in hour
        cal.set(Calendar.SECOND, 0)                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0)            // set millisecond in second
        return cal
    }

    fun nullSafe(content: String?): String {
        if (content == null) {
            return ""
        }

        return if (content.equals("null", ignoreCase = true)) {
            ""
        } else content

    }

    fun isGPSProviderEnabled(context: Context): Boolean {
        val locationManager = context
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun isNetworkProviderEnabled(context: Context): Boolean {
        val locationManager = context
                .getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    fun isUserLoggedIn(c: Context): Boolean {
        return getUid(c)!!.isNotEmpty()
    }


    fun getUid(c: Context): String? {
        return ""
//        return getPref(c, RequestParamsUtils.AUTHENTICATIONTOKEN, "")
    }

    fun clearLoginCredentials(c: Context) {
//        delPref(c, RequestParamsUtils.USER_ID)
//        delPref(c, RequestParamsUtils.SESSION_ID)
//        delPref(c, RequestParamsUtils.TOKEN)
//        delPref(c, RequestParamsUtils.USER_FIRST_NAME)
//        delPref(c, RequestParamsUtils.USER_LAST_NAME)
//        delPref(c, Constant.LOGIN_INFO)
//        delPref(c, RequestParamsUtils.AUTHENTICATIONTOKEN)
//        delPref(c, Constant.USER_LATITUDE)
//        delPref(c, Constant.USER_LONGITUDE)


        val nMgr = c.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nMgr.cancelAll()
    }

    fun asList(str: String): ArrayList<String?> {

        return ArrayList<String?>(Arrays.asList<String>(*str
                .split("\\s*,\\s*".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()))
    }

//    fun getUserToken(c: Context): String? {
//        return getPref(c, RequestParamsUtils.TOKEN, "")
//    }
//
//    fun getUserAuthToken(c: Context): String? {
//        return getPref(c, RequestParamsUtils.AUTHENTICATIONTOKEN, "")
//    }
//
//    fun deleteUserAuthToken(c: Context) {
//        delPref(c, RequestParamsUtils.AUTHENTICATIONTOKEN)
//    }

    fun showToast(context: Context, msg: String) {
        val toast = Toast.makeText(context,
                msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }

    fun showServerToast(context: Context) {
        val toast = Toast.makeText(context,
                context.getString(R.string.serverNotRespond), Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 100)
        toast.show()
    }

    fun formatToFloat(flt: Double?): String {
        val decimalFormat = DecimalFormat("0.00")
        decimalFormat.isGroupingUsed = true
        decimalFormat.groupingSize = 3
        return decimalFormat.format(flt)
    }

    private fun isPackageInstalled(packagename: String, context: Context): Boolean {
        val pm = context.packageManager
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES)
            return true
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
    }


    fun loadImage(img: ImageView, url: String, context: Context, resId: Int) {
        if (url != null) {
            run {
                val options: RequestOptions = RequestOptions()
                        .placeholder(resId)
                        .error(resId);
                Glide.with(context).load(url).apply(options).into(img)
            }
        }
    }

    fun loadRoundedImage(img: ImageView, url: String, context: Context, resId: Int) {
        if (url != null) {
            run {
                val options: RequestOptions = RequestOptions()
                        .placeholder(resId)
                        .transforms(CenterCrop(),RoundedCorners(getPx(context,5)))
                        .error(resId);
                Glide.with(context).load(url).apply(options).into(img)
            }
        }
    }

    fun loadRoundedImage(img: ImageView, url: Any, context: Context, resId: Int) {
        if (url != null) {
            run {
                val options: RequestOptions = RequestOptions()
                    .placeholder(resId)
                    .transforms(CenterCrop(),RoundedCorners(getPx(context,50)))
                    .error(resId);
                Glide.with(context).load(url).apply(options).into(img)
            }
        }
    }

    fun loadImage(img: ImageView, context: Context, resId: Int) {
        run {
            Glide.with(context).load(resId).into(img)
        }
    }

    fun getPx(context: Context, dp: Int): Int {
        return Math.round(dp * (context.getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    fun goToMap(mContext: Context, lat: String, lng: String) {

        var gmmIntentUri: Uri = Uri.parse("geo:" + lat + "," + lng);
        var mapIntent: Intent = Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(mContext.packageManager) != null) {
            mContext.startActivity(mapIntent);
        }
    }

    fun composeEmail(mContext: Context, addresses: Array<String>, subject: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_SUBJECT, subject)
//            putExtra(Intent.EXTRA_STREAM, attachment)
        }
        if (intent.resolveActivity(mContext.packageManager) != null) {
            mContext.startActivity(intent)
        }
    }

    fun fromHtml(html: String?): Spanned? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            HtmlCompat.fromHtml(html!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html!!)
        }
    }
    fun dialPhoneNumber(mContext: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        if (intent.resolveActivity(mContext.packageManager) != null) {
            mContext.startActivity(intent)
        }
    }
}
