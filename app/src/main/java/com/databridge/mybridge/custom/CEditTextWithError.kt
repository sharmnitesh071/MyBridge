//package com.databridge.mybridge.custom
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import android.widget.LinearLayout
//import androidx.core.content.ContextCompat
//import com.databridge.mybridge.R
//import io.proximety.hilitemall.R
//
//class CEditTextWithError @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : LinearLayout(context, attrs, defStyleAttr) {
//
//    private lateinit var errorText: CMTextView
//    public lateinit var edtMobileNumber: CMEditTextView
//    public lateinit var edtCountryCode: CMEditTextView
//    private lateinit var viewMobile: View
//    public lateinit var ivIcon: CImageView
//    public lateinit var title: CMTextView
//    public lateinit var ivClear: CImageView
//    private lateinit var errorLayout: LinearLayout
//
//    init {
//        inflate(context, R.layout.custom_edittext_error, this)
//
//        // Find and reference the views from the inflated layout
//        errorText = findViewById(R.id.errorText)
//        errorLayout = findViewById(R.id.errorLayout)
//        viewMobile = findViewById(R.id.viewMobile)
//        edtMobileNumber = findViewById(R.id.edt_mobile_number)
//        edtCountryCode = findViewById(R.id.edt_country_code)
//        ivIcon = findViewById(R.id.ivIcon)
//        ivClear = findViewById(R.id.ivClear)
//        title = findViewById(R.id.title)
//
//        ivClear.setOnClickListener {
//            edtMobileNumber.text!!.clear()
//            ivClear.visibility = View.GONE
//        }
//    }
//
//    fun setValidationSuccess(text: String) {
//        errorText.text = text
//        errorLayout.visibility = View.GONE
//        viewMobile.setBackgroundColor(ContextCompat.getColor(context!!, R.color.verified_txt_color))
//        showHideClearButton()
//    }
//
//
//    fun setErrorText(text: String) {
//        errorText.text = text
//        errorLayout.visibility = View.VISIBLE
//        viewMobile.setBackgroundColor(ContextCompat.getColor(context!!, R.color.error_txt_color))
//        showHideClearButton()
//    }
//
//
//    fun setFocused(text: String) {
//        errorText.text = text
//        errorLayout.visibility = View.GONE
//        viewMobile.setBackgroundColor(ContextCompat.getColor(context!!, R.color.ed_sel_color))
//        showHideClearButton()
//    }
//
//    fun setFocuseLost(text: String) {
//        errorText.text = text
//        errorLayout.visibility = View.GONE
//        viewMobile.setBackgroundColor(ContextCompat.getColor(context!!, R.color.ed_insel_color))
//        showHideClearButton()
//    }
//
//
//    private fun showHideClearButton() {
//        if (edtMobileNumber.text.toString().isNotEmpty())
//            ivClear.visibility = View.VISIBLE
//        else
//            ivClear.visibility = View.GONE
//    }
//
//
//    fun setHintText(buttonText: String) {
//        errorText.text = buttonText
//    }
//}