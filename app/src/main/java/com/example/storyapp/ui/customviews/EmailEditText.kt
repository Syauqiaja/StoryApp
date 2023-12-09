package com.example.storyapp.ui.customviews

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R


class EmailEditText: AppCompatEditText {
    private var canSubmit: Boolean = false
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    private fun init() {
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(p0 != null){
                    if(isValidEmail(p0)){
                        error = null
                        canSubmit = true
                    }else{
                        error = context.getString(R.string.not_format_email)
                        canSubmit = false
                    }
                }else{
                    error = null
                    canSubmit = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }
    }
    fun checkValid() = canSubmit
}