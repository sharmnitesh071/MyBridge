package com.databridge.mybridge.ui.base.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.Settings
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.databridge.mybridge.R
import com.databridge.mybridge.utils.Debug
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


open class BaseViewModel(application: Application) : AppViewModel(application) {

    lateinit var activity: Activity
    lateinit var mContext: Context
    var supportFragmentManager: FragmentManager? = null


    var imageBitmap: Bitmap? = null
    var fileUri: File? = null
    var imageUri: Uri? = null

    var fileUplod: File? = null
    val REQ_PERMISSION = 4460
    val REQ_CAPTURE_IMAGE = 4470
    val REQ_PICK_IMAGE = 4569
    val REQ_PICK_FILE = 4571

    fun goBack() {
        (mContext as Activity).finish()
    }

    inner class ViewClickHandler {

    }

//    fun chooseFile(mContext: Context) {
//        fileUri = null
//
//        Dexter.withActivity(mContext as Activity)
//            .withPermissions(
//                Manifest.permission.READ_EXTERNAL_STORAGE
////                , Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
//            .withListener(object : MultiplePermissionsListener {
//                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
//                    if (report.areAllPermissionsGranted()) {
//                        showFileChooser(mContext)
//                    }
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permissions: List<PermissionRequest>,
//                    token: PermissionToken
//                ) {
//                    Debug.e("onPermissionRationale", "" + permissions.size)
//                    token.continuePermissionRequest()
//                }
//            }).check()
//    }
//
//    fun captureProfie(mContext: Context) {
//        imageUri = null
//        fileUri = null
//        checkCameraPermission(mContext)
//    }

    var alertDialogRatinal: android.app.AlertDialog? = null
    fun checkRationale(permissions: Array<String>) {
        if (alertDialogRatinal != null && alertDialogRatinal!!.isShowing || permissions.size == 0) {
            return
        }
        var someDenied = false
        for (permission in permissions) {
            Debug.e("-------", "---permissions----" + permission)
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    mContext as Activity,
                    permission
                )
            ) {

            } else {
                if (ActivityCompat.checkSelfPermission(
                        mContext as Activity,
                        permission
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                } else {
                    someDenied = true
                }
            }
        }
        if (someDenied) {
            val alertDialogBuilder =
                android.app.AlertDialog.Builder(mContext as Activity)
            alertDialogRatinal = alertDialogBuilder.setTitle("Permissions Required")
                .setMessage(
                    "Please open settings, go to permissions and allow them."
                )
                .setPositiveButton(
                    "Settings"
                ) { dialog, which ->
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            "package",
                            (mContext as Activity).getPackageName(),
                            null
                        )
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    (mContext as Activity).startActivityForResult(intent, REQ_PERMISSION)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, which -> }
                .setCancelable(false)
                .create()
            alertDialogRatinal!!.show()
        }
    }

//    fun checkCameraPermission(mContext: Context) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            val permissions: ArrayList<String> = ArrayList<String>()
//            permissions.add(Manifest.permission.CAMERA)
////            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P)
////                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
//            val listPermissionsNeeded: ArrayList<String> = ArrayList()
//            for (p in permissions) {
//                val result = ContextCompat.checkSelfPermission(mContext, p)
//                if (result != PackageManager.PERMISSION_GRANTED) {
//                    listPermissionsNeeded.add(p)
//                }
//            }
//            Debug.e(
//                "---",
//                "--onPermissionsChecked-listPermissionsNeeded" + listPermissionsNeeded.size
//            )
//            if (listPermissionsNeeded.isNotEmpty()) {
//                ActivityCompat.requestPermissions(
//                    mContext as Activity,
//                    listPermissionsNeeded.toTypedArray(),
//                    REQ_PERMISSION
//                )
//            } else {
//                showPictureChooser(mContext)
//            }
//        } else {
//            showPictureChooser(mContext)
//        }
//    }

//    fun showFileChooser(mContext: Context) {
//
//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "*/*"
//        intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        try {
//            (mContext as BaseActivity).startActivityForResult(
//                Intent.createChooser(
//                    intent,
//                    mContext!!.getString(R.string.select_file)
//                ), REQ_PICK_FILE
//            )
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }
//    }

//    fun showPictureChooser(mContext: Context) {
//        val pd: AlertDialog.Builder =
//            AlertDialog.Builder((mContext as BaseActivity), R.style.MyAlertDialogStyle)
//        val binding = DataBindingUtil.inflate<DialogPicChooserBinding>(
//            LayoutInflater.from(mContext),
//            R.layout.dialog_pic_chooser, null, false
//        )
//        pd.setView(binding.root)
//        val dialog = pd.create()
//
//        binding.tvChooseGallery.setOnClickListener {
//            val intent = Intent(Intent.ACTION_GET_CONTENT)
//            intent.type = "image/*"
//            intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            try {
//                (mContext as BaseActivity).startActivityForResult(
//                    Intent.createChooser(
//                        intent,
//                        mContext!!.getString(R.string.err_select_image)
//                    ), REQ_PICK_IMAGE
//                )
//            } catch (ex: Exception) {
//                ex.printStackTrace()
//            }
//
//            dialog.dismiss()
//        }
//
//        binding.tvChooseCamera.setOnClickListener {
//            imageUri = null
//            fileUri = null
////            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
////            intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
////            intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION, true)
////            if (Utils.outputMediaFile != null && Utils.outputMediaFile!!.absolutePath != null) {
////                fileUri = File(Utils.outputMediaFile!!.absolutePath)
////                Debug.e("--fileUri---", "" + fileUri);
////                intent.putExtra(
////                    MediaStore.EXTRA_OUTPUT,
////                    Utils.getUriForShare(mContext!!, fileUri!!)
////                )
////                try {
////                    (mContext as BaseActivity).startActivityForResult(
////                        Intent.createChooser(
////                            intent,
////                            mContext!!.getString(R.string.err_select_image)
////                        ), REQ_CAPTURE_IMAGE
////                    )
////                    Debug.e("--createChooser---", "" + fileUri);
////                } catch (ex: Exception) {
////                    ex.printStackTrace()
////                }
////                dialog.dismiss()
////            } else
////                dialog.dismiss()
//
//
//            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
//                // Ensure that there's a camera activity to handle the intent
//                takePictureIntent.resolveActivity(mContext.packageManager)?.also {
//                    // Create the File where the photo should go
//                    val photoFile: File? = try {
//                        createImageFile()
//                    } catch (ex: IOException) {
//                        // Error occurred while creating the File
//                        ex.printStackTrace()
//                        null
//                    }
//                    // Continue only if the File was successfully created
//                    photoFile?.also {
////                        if (Utils.outputMediaFile != null && Utils.outputMediaFile!!.absolutePath != null)
//                        fileUri = photoFile
//                        val photoURI: Uri = FileProvider.getUriForFile(
//                            mContext,
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            it
//                        )
//                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
////                        takePictureIntent.putExtra(
////                            MediaStore.EXTRA_OUTPUT,
////                            Utils.getUriForShare(mContext!!, fileUri!!)
////                        )
//                        (mContext as BaseActivity).startActivityForResult(
//                            takePictureIntent,
//                            REQ_CAPTURE_IMAGE
//                        )
//                    }
//                }
//                dialog.dismiss()
//            }
//
//        }
//
//        dialog.show()
//    }

    lateinit var currentPhotoPath: String

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            (mContext as Activity).getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    fun showSnackBar(view: View, msg: String, excelFile: File) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
            .setAction("OPEN") {

            }
            .setActionTextColor(
                ContextCompat.getColor(
                    mContext as Activity,
                    R.color.colorAccent
                )
            )
            .show()
    }



}
