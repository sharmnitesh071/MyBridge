package com.databridge.mybridge.ui.onboarding.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.databridge.mybridge.R
import com.databridge.mybridge.base.createShortName
import com.databridge.mybridge.base.isInternetConnected
import com.databridge.mybridge.base.showToast
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.FragmentOnboarding3Binding
import com.databridge.mybridge.ui.BaseFragment
import com.databridge.mybridge.ui.onboarding.viewmodel.OnBoarding3ViewModel
import com.databridge.mybridge.utils.Debug
import com.databridge.mybridge.utils.Resource
import com.databridge.mybridge.utils.UriHelper
import com.databridge.mybridge.utils.Utils
import com.databridge.mybridge.utils.Utils.loadRoundedImage
import java.io.File

class OnBoarding3Fragment : BaseFragment(), View.OnClickListener {


    lateinit var binding: FragmentOnboarding3Binding
    private val viewModel: OnBoarding3ViewModel by viewModels()

    var fileUri: File? = null
    var imageUri: Uri? = null
    val listPermissionsNeeded: ArrayList<String> = ArrayList()

    lateinit var activity: Activity
    var current = 2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return inflater.inflate(R.layout.fragment_onboarding, container, false)
        binding = DataBindingUtil.inflate<FragmentOnboarding3Binding>(
            /* inflater = */ inflater,
            /* layoutId = */ R.layout.fragment_onboarding3,
            /* parent = */ container,
            /* attachToParent = */ false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
        activity = requireActivity()
        init()
        initObserver()
    }

    private fun initObserver() {
        viewModel.response.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        viewModel.saveOnBoardingDataInPref(it)
                        goToNext()
                    }
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
                    requireContext().showToast(getString(R.string.user_not_found))
                    binding.pbLoader.visibility = View.GONE
                }

                Resource.Status.LOADING -> {
                    binding.pbLoader.visibility = View.VISIBLE
                }
            }
        }

        viewModel.getresponse.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Resource.Status.SUCCESS -> {
                    response.data.let {
                        if (it?.data?.photo == true) {
                        }
                    }
                }

                Resource.Status.ERROR -> {
                    Debug.e("----", "--message--" + response.message)
//                    requireContext().showToast(getString(R.string.api_error))
                }

                Resource.Status.LOADING -> {
                }
            }
        }

        if (isInternetConnected(activity))
            viewModel.getOnboardingapiCall()
    }

    private fun init() {

        displayData()

        val profile = sharedPref.getPrefString(Conts.PREF_PROFILE_PIC)
        if (profile.isNullOrEmpty().not())
            profile?.let {
                Utils.loadRoundedImage(
                    binding.ivPhotoPreview, it, activity,
                    R.drawable.profile_frame
                )
            }
        else {
            binding.tvShortName.text = sharedPref.getPrefString(Conts.PREF_DISPLAY_NAME)
                ?.let { it.createShortName() }
        }

        binding.tvOnBoardingStep.iv2.setImageResource(R.drawable.onboarding_step)
        binding.tvOnBoardingStep.iv3.setImageResource(R.drawable.onboarding_step)

        binding.ivPhoto.setOnClickListener(this)
        binding.tvSkip.setOnClickListener(this)
        binding.btnSetProfile.setOnClickListener(this)
        binding.tvBack.setOnClickListener(this)
    }

    private fun displayData() {
        try {
            binding.tvName.text = sharedPref.getPrefString(Conts.PREF_DISPLAY_NAME)
            binding.tvRole.text = sharedPref.getPrefString(Conts.PREF_PROFILE_ROLE)
            binding.tvLocation.text = sharedPref.getPrefString(Conts.PREF_PROFILE_LOCATION)

//            binding.tvRole.text =
//                sharedPref.getPrefString(Conts.prefExpTitle) + " at " + sharedPref.getPrefString(
//                    Conts.prefExpCompany
//                )
//            binding.tvLocation.text =
//                sharedPref.getPrefString(Conts.prefCity) + "," + sharedPref.getPrefString(Conts.prefState) + "," + sharedPref.getPrefString(
//                    Conts.prefCountry
//                )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    fun captureProfie(mContext: Context) {
        imageUri = null
        fileUri = null
        checkCameraPermission(mContext)
    }

    var alertDialogRatinal: android.app.AlertDialog? = null
    fun checkRationale(permissions: Array<String>) {
        if (alertDialogRatinal != null && alertDialogRatinal!!.isShowing || permissions.size == 0) {
            return
        }
        var someDenied = false
        for (permission in permissions) {
            Debug.e("-------", "---permissions----" + permission)
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission
                )
            ) {

            } else {
                if (ActivityCompat.checkSelfPermission(
                        activity,
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
                android.app.AlertDialog.Builder(activity)
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
                            activity.getPackageName(),
                            null
                        )
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    getImageResult.launch(intent)
//                    activity.startActivityForResult(intent, REQ_PERMISSION)
                }
                .setNegativeButton(
                    "Cancel"
                ) { dialog, which -> }
                .setCancelable(false)
                .create()
            alertDialogRatinal!!.show()
        }
    }

    fun checkCameraPermission(mContext: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val permissions: ArrayList<String> = ArrayList<String>()
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
            else
                permissions.add(Manifest.permission.READ_MEDIA_IMAGES)

            listPermissionsNeeded.clear()
            for (p in permissions) {
                val result = ContextCompat.checkSelfPermission(mContext, p)
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p)
                }
            }
            Debug.e(
                "---",
                "--onPermissionsChecked-listPermissionsNeeded" + listPermissionsNeeded.size
            )
            if (listPermissionsNeeded.isNotEmpty()) {
                requestPermissions(listPermissionsNeeded)
//                ActivityCompat.requestPermissions(
//                    mContext as Activity,
//                    listPermissionsNeeded.toTypedArray(),
//                    REQ_PERMISSION
//                )
            } else {
                chooseImageFromGallery()
            }
        } else {
            chooseImageFromGallery()
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val grantedPermissions = permissions.entries.filter { it.value }.map { it.key }
            val deniedPermissions = permissions.entries.filterNot { it.value }.map { it.key }

            // Handle granted and denied permissions
            if (grantedPermissions.containsAll(listPermissionsNeeded)) {
                // All permissions are granted
                // Proceed with further actions
                fileUri = null
                captureProfie(activity)

            } else {
                checkRationale(listPermissionsNeeded.toTypedArray())
                // Some permissions are denied
                // Handle denied permissions accordingly
            }
        }

    fun requestPermissions(listPermissionsNeeded: ArrayList<String>) {
        // Launch coroutine to request permissions

        requestPermissionLauncher.launch(listPermissionsNeeded.toTypedArray())

    }

    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Debug.d("PhotoPicker", "Selected URI: $uri")

                try {
                    val tmpFileUri = uri
                    Debug.e("", "tmp_fileUri : " + tmpFileUri!!.path!!)
                    val selectedImagePath = UriHelper.getPath(
                        activity,
                        tmpFileUri
                    )

                    Debug.e("", "selectedImagePath : $selectedImagePath")
                    imageUri = tmpFileUri
                    fileUri = File(selectedImagePath!!)
                    if (fileUri != null && fileUri!!.exists()) {
                        afterImageSelected(fileUri!!)
                        Debug.e("", "fileUri : " + fileUri!!.absolutePath)
                        /*  addEventViewModel!!.onImagePicked(fileUri!!)*/
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                Debug.d("PhotoPicker", "No media selected")
            }
        }

    fun chooseImageFromGallery() {
        // Registers a photo picker activity launcher in single-select mode.


//        val intent = Intent(Intent.ACTION_GET_CONTENT)
//        intent.type = "image/*"
//        intent.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        try {
////            activity.startActivityForResult(
////                Intent.createChooser(
////                    intent,
////                   getString(R.string.err_select_image)
////                ), REQ_PICK_IMAGE
////            )
//            getImageResult.launch(
//                Intent.createChooser(
//                    intent,
//                    getString(R.string.err_select_image)
//                )
//            )
//        } catch (ex: Exception) {
//            ex.printStackTrace()
//        }

        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }


    private val getImageResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val data = it.data
//                try {
//                    val tmpFileUri = data!!.data
//                    Debug.e("", "onActivityResult tmp_fileUri : " + tmpFileUri!!.path!!)
//                    val selectedImagePath = UriHelper.getPath(
//                        requireContext(),
//                        tmpFileUri
//                    )
//                    imageUri = tmpFileUri
//                    fileUri = File(selectedImagePath!!)
//
//                    imageUri = Uri.fromFile(fileUri)
//
//                    activity?.let { it1 ->
//                        loadRoundedImage(
//                            binding.ivPhoto, imageUri.toString(),
//                            it1, R.color.transparent
//                        )
//                    }
//
//                    Debug.e("", "onActivityResult fileUri : " + fileUri)
//                    if (fileUri != null && fileUri!!.exists()) {
////                        val intent = Intent(activity, CropImageActivity::class.java)
////                        intent.putExtra("Uri", imageUri!!.toString())
////                        cropresultLauncher.launch(intent)
//
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }

                try {
                    val tmpFileUri = data!!.data
                    Debug.e("", "tmp_fileUri : " + tmpFileUri!!.path!!)
                    val selectedImagePath = UriHelper.getPath(
                        activity,
                        tmpFileUri
                    )

                    Debug.e("", "selectedImagePath : $selectedImagePath")
                    imageUri = tmpFileUri
                    fileUri = File(selectedImagePath!!)
                    if (fileUri != null && fileUri!!.exists()) {
                        afterImageSelected(fileUri!!)
                        Debug.e("", "fileUri : " + fileUri!!.absolutePath)
                        /*  addEventViewModel!!.onImagePicked(fileUri!!)*/
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    private fun afterImageSelected(fileUri: File) {
        imageUri = Uri.fromFile(fileUri)

        loadRoundedImage(
            binding.ivPhotoPreview,
            fileUri,
            activity,
            R.color.transparent
        )
    }

    var cropresultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Debug.e(
                "---cropresultLauncher---",
                "-onActivityResult-" + "-resultCode-" + result.resultCode
            )
            if (result != null && result.data != null && result.data!!.extras != null && result.data!!.extras!!.getString(
                    "Uri"
                ) != null
            ) {

                val uri = result.data!!.extras!!.getString("Uri")
                val uriFilePath = result.data!!.extras!!.getString("uriFilePath")
//                val from = result.data!!.extras!!.getString("from")
//                val pos = result.data!!.extras!!.getInt("pos")

                imageUri = Uri.parse(uri)
                fileUri = File(uriFilePath)

                Debug.e("----", "--imageUri--1" + imageUri)

            }
        }
    }

    private fun goToNext() {
        (requireActivity() as OnboardingActivity).viewPager.setCurrentItem(current + 1, true)
    }

    private fun goToBack() {
        (requireActivity() as OnboardingActivity).viewPager.setCurrentItem(current - 1, true)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.tvBack -> {
                goToBack()
            }

            binding.ivPhoto -> {
                captureProfie(activity)

            }

            binding.tvSkip -> {
                with(binding) {
                    with(viewModel) {
                        if (isInternetConnected(activity))
                            apiCall(true)
                    }
                }
            }

            binding.btnSetProfile -> {
                Debug.e("-----", "---onClick-btnSetProfile-")

                with(viewModel) {
                    fileUri?.let {
                        if (isInternetConnected(activity))
                            apiCallPhoto(activity, it)
                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()
        displayData()
    }
}