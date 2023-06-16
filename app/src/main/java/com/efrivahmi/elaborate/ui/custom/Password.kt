package com.efrivahmi.elaborate.ui.custom

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.efrivahmi.elaborate.R

class Password : AppCompatEditText, View.OnTouchListener {
    private var isPasswordValid: Boolean = false
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var keyIcon: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun onShowVisibilityIcon(icon: Drawable) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, icon, null)
    }

    private fun init() {
        keyIcon =
            ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable
        transformationMethod = PasswordTransformationMethod.getInstance()
        onShowVisibilityIcon(keyIcon)
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                isPasswordValid = (s?.length ?: 0) >= 8
                checkPasswordValidity()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        setOnTouchListener(this)
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_UP) {
            val drawableRight = compoundDrawablesRelative[2]
            if (drawableRight != null && event.rawX >= (right - drawableRight.bounds.width())) {
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setConfirmPasswordEditText(editText: EditText) {
        confirmPasswordEditText = editText
    }

    private fun checkPasswordValidity() {
        val password = text?.toString()?.trim()
        val confirmPassword = confirmPasswordEditText.text?.toString()?.trim()
        if (password.isNullOrEmpty() || confirmPassword.isNullOrEmpty()) {
            isPasswordValid = false
            error = resources.getString(R.string.input_password)
        } else if (password != confirmPassword) {
            isPasswordValid = false
            error = resources.getString(R.string.password_not_match)
        } else {
            isPasswordValid = true
        }
    }

    fun isPasswordValid(): Boolean {
        checkPasswordValidity()
        return isPasswordValid
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) {
            checkPasswordValidity()
        }
    }
}
