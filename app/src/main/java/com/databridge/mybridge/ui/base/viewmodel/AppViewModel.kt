package com.databridge.mybridge.ui.base.viewmodel


import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel

open class AppViewModel : AndroidViewModel {

    internal lateinit var toast: Toast

    constructor(application: Application) : super(application) {
        initToast()
    }

    fun initToast() {
        toast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG)

    }



    fun getContext(): Context {
        return (getApplication() as Application).applicationContext
    }

    fun showToast(text: String?) {

        if (text.isNullOrEmpty().not()) {
            toast.setText(text)
            toast.duration = Toast.LENGTH_LONG
            toast.show()
        }
    }

    fun showToast(resId: Int) {
        showToast(getContext().getString(resId))
    }

    fun getLabelText(resId: Int): String {
        return getContext().getString(resId)
    }

//    fun isInternetAvailable(context: Context, callbackListener: CallbackListener) {
//        if (Utils.isInternetConnected(getContext())) {
//            callbackListener.onSuccess()
//            return
//        }
//        AlertDialogHelper.showNoInternetDialog(context, callbackListener)
//    }
//
//    fun showHttpError(context: Context) {
//        AlertDialogHelper.showHttpExceptionDialog(context)
//    }


//    fun showDatePickerDialog(
//        context: Context,
//        selectedTime: Long,
//        minTime: Long,
//        maxTime: Long,
//        eventListener: DateEventListener?,
//        isTimePicker: Boolean
//    ) {
//        val calendarNext = Calendar.getInstance()
//        calendarNext.time = Date(selectedTime)
//        //calendarNext.add(Calendar.DAY_OF_MONTH, 0);
//        val mYear = calendarNext.get(Calendar.YEAR)
//        val mMonth = calendarNext.get(Calendar.MONTH)
//        val mDay = calendarNext.get(Calendar.DAY_OF_MONTH)
//
//        val mDatePicker = DatePickerDialog(
//            context,
//            DatePickerDialog.OnDateSetListener { datepicker, selectedyear, selectedmonth, selectedday ->
//                var selectedmonth = selectedmonth
//                selectedmonth += 1
//                when {
//                    isTimePicker -> {
//                    }
//                    else -> {
//                        val date = Utils.parseTime(
//                            "$selectedday/$selectedmonth/$selectedyear",
//                            "dd/MM/yyyy"
//                        )
//                        eventListener?.onDateSelected(
//                            date,
//                            "$selectedday/$selectedmonth/$selectedyear"
//                        )
//                    }
//                }
//            },
//            mYear,
//            mMonth,
//            mDay
//        )
////        mDatePicker.datePicker.minDate = Calendar.getInstance().timeInMillis
//        if (minTime != 0L)
//            mDatePicker.datePicker.minDate = minTime
//        if (maxTime != 0L)
//            mDatePicker.datePicker.maxDate = maxTime
//        mDatePicker.show()
//    }
//
//    fun showTimePickerDialog(context: Context, date: Date, eventListener: DateEventListener?) {
//        val c = Calendar.getInstance()
//        c.time = date
//        val hour = c.get(Calendar.HOUR_OF_DAY)
//        val minute = c.get(Calendar.MINUTE)
//
//        val timePicker = TimePickerDialog(
//            context,
//            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
//                Debug.e(
//                    "TAG",
//                    "onTimeSet() called with: view = [$view], hourOfDay = [$hourOfDay], minute = [$minute]"
//                )
//
//                //Date date = new Date(selectedyear, selectedmonth, selectedday, hourOfDay, minute, 0);
//                val date = Utils.parseTime(
//                    c.get(Calendar.DAY_OF_MONTH)
//                        .toString() + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR) + " " + hourOfDay + ":" + minute + ":00",
//                    "dd/MM/yyyy HH:mm:ss"
//                )
//                eventListener?.onDateSelected(
//                    date,
//                    c.get(Calendar.DAY_OF_MONTH)
//                        .toString() + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR) + " " + hourOfDay + ":" + minute + ":00"
//                )
//            },
//            hour,
//            minute,
//            false
//        )
//        timePicker.show()
//    }
//
//    fun showDialog(context: Context) {
//        showDialog("", (context as Activity))
//    }

//    fun logout() {
//        Utils.delLatLong(getContext())
//        Utils.clearLoginCredentials(getContext())
//        Utils.deleteUserAuthToken(getContext())
//        LocalBroadcastManager.getInstance(getContext())
//            .sendBroadcast(Intent(Constant.FINISH_ACTIVITY))
////        val intent = Intent(getContext(),LoginActivity::class.java)
////        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
////        getContext().startActivity(intent)
//    }


//    fun isLocationPermissionAllowed(context: Context): Boolean {
//        val list = listOf<String>(
//            Manifest.permission.INTERNET,
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//        val managePermissions = ManagePermissionsImp((context as Activity), list, 0)
//        if (managePermissions.isPermissionsGranted() == 0) {
//            return true
//        }
//        return false
//    }
//
//    fun hideKeyboard(view: View) {
//        Utils.hideKeyBoard(getContext(), view)
//    }
}
