package com.databridge.mybridge.ui.post.createpost.view


import android.R.attr.path
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.databridge.mybridge.R
import com.databridge.mybridge.base.isNullorEmptyNot
import com.databridge.mybridge.base.loadRoundedImage
import com.databridge.mybridge.common.Conts
import com.databridge.mybridge.databinding.ActivityCreatePostBinding
import com.databridge.mybridge.databinding.DialogAllActionBinding
import com.databridge.mybridge.databinding.DialogChooseAudienceBinding
import com.databridge.mybridge.databinding.DialogWhoReplyBinding
import com.databridge.mybridge.ui.BaseActivity
import com.databridge.mybridge.ui.login.login.viewmodel.LoginViewModel
import com.databridge.mybridge.utils.Debug
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter


class CreatePostActivity : BaseActivity() {

    // binding
    private var _binding: ActivityCreatePostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    val CHOOSEPHOTO_REQUEST_CODE = 111

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(activity, R.layout.activity_create_post)

        init()
        initObserver()

    }

    private fun initObserver() {


    }


    private fun init() {

        val profile = sharedPref.getPrefString(
            Conts.PREF_PROFILE_PIC
        )
        if (profile?.isNullorEmptyNot() == true) {
            Debug.e("---", "--profile-3-" + profile)
            binding.imgProfilePic.loadRoundedImage(
                profile,
                R.drawable.profile_frame
            )
        } else {
            binding.imgProfilePic.setImageResource(R.drawable.profile_frame)
            binding.tvImageName.text = sharedPref.getPrefString(
                Conts.PREF_PROFILE_PIC_CHARS
            )
        }

        binding.tvName.text = sharedPref.getPrefString(
            Conts.PREF_DISPLAY_NAME
        )


        // click event
        with(binding) {
            top.ivClose.setOnClickListener {
                finish()
            }
            top.tvPost.setOnClickListener {

            }


            tvChooseAudien.setOnClickListener {
                createChooseAudienceDilaog()
            }

            tvChooseWhoReply.setOnClickListener {
                createWhoReplyDilaog()
            }
        }


        allActionDialog()
    }


    lateinit var dialogAllActionBinding: DialogAllActionBinding
    lateinit var allactionDialog: BottomSheetDialog

    private fun allActionDialog() {

        allactionDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTransStyle)
        dialogAllActionBinding =
            DialogAllActionBinding.inflate(LayoutInflater.from(activity))
        allactionDialog.setContentView(dialogAllActionBinding.root)
        allactionDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        allactionDialog.setOnShowListener(DialogInterface.OnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog

        })

        dialogAllActionBinding.llPhoto.setOnClickListener {
            openMultiImageSelector()
        }

        allactionDialog.setCanceledOnTouchOutside(true)
        allactionDialog.setCancelable(true)
        try {
            allactionDialog.show()

        } catch (ignored: Exception) {
        }
    }

    private fun openMultiImageSelector() {
        FishBun.with(this@CreatePostActivity)
            .setImageAdapter(GlideAdapter())
            .setPickerCount(50)
            .setPickerSpanCount(2)
            .setActionBarColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"), true)
            .setActionBarTitleColor(Color.parseColor("#000000"))
//            .setArrayPaths(path)
            .setAlbumSpanCount(1, 2)
            .setButtonInAlbumActivity(true)
            .setCamera(false)
            .exceptGif(true)
            .setReachLimitAutomaticClose(false)
            .setHomeAsUpIndicatorDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_close
                )
            )
//            .setOkButtonDrawable(ContextCompat.getDrawable(this, R.drawable.done))
            .setAllViewTitle("All of your photos")
            .setActionBarTitle("Gallery")
            .textOnImagesSelectionLimitReached("You can't select any more.")
            .textOnNothingSelected("I need a photo!")
            .startAlbumWithOnActivityResult(CHOOSEPHOTO_REQUEST_CODE)
    }

    lateinit var dialogChooseAudienceBinding: DialogChooseAudienceBinding
    lateinit var bottomSheetDialog: BottomSheetDialog

    private fun createChooseAudienceDilaog() {

        bottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTransStyle)
        dialogChooseAudienceBinding =
            DialogChooseAudienceBinding.inflate(LayoutInflater.from(activity))
        bottomSheetDialog.setContentView(dialogChooseAudienceBinding.root)
        bottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        bottomSheetDialog.setOnShowListener(DialogInterface.OnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog

        })

        bottomSheetDialog.setCanceledOnTouchOutside(true)
        bottomSheetDialog.setCancelable(true)
        try {
            bottomSheetDialog.show()

        } catch (ignored: Exception) {
        }
    }

    lateinit var dialogWhoReplyBinding: DialogWhoReplyBinding
    lateinit var whoReplybottomSheetDialog: BottomSheetDialog

    private fun createWhoReplyDilaog() {

        whoReplybottomSheetDialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTransStyle)
        dialogWhoReplyBinding =
            DialogWhoReplyBinding.inflate(LayoutInflater.from(activity))
        whoReplybottomSheetDialog.setContentView(dialogWhoReplyBinding.root)
        whoReplybottomSheetDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        whoReplybottomSheetDialog.setOnShowListener(DialogInterface.OnShowListener { dialogInterface ->
            val bottomSheetDialog = dialogInterface as BottomSheetDialog

        })

        whoReplybottomSheetDialog.setCanceledOnTouchOutside(true)
        whoReplybottomSheetDialog.setCancelable(true)
        try {
            whoReplybottomSheetDialog.show()

        } catch (ignored: Exception) {
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CHOOSEPHOTO_REQUEST_CODE -> if (resultCode === RESULT_OK) {
                // path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                // you can get an image path(ArrayList<String>) on <0.6.2
//                path = imageData.getParcelableArrayListExtra(INTENT_PATH)
                // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
//
            }
        }
    }

}