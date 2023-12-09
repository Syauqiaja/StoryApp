package com.example.storyapp.ui.customviews

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R

class PasswordEditText: AppCompatEditText {
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

    private fun init(){
        addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, start: Int, before: Int, count: Int) {
                if(p0 != null){
                    if(p0.length >= 8){
                        error = null
                        canSubmit = true
                    }else{
                        error = context.getString(R.string.error_password)
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

    fun checkValid():Boolean{
        return if(canSubmit) true
        else {
            requestFocus()
            false
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        hint = context.getString(R.string.enter_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }
}