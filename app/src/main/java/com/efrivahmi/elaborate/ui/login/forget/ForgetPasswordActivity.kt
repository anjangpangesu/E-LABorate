package com.efrivahmi.elaborate.ui.login.forget

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.response.FpResponse
import com.efrivahmi.elaborate.data.response.VerifyCode
import com.efrivahmi.elaborate.databinding.ActivityForgetPasswordBinding
import com.efrivahmi.elaborate.ui.login.LoginActivity
import com.efrivahmi.elaborate.ui.login.editpassword.EditPasswordActivity
import com.efrivahmi.elaborate.utils.HelperToast
import com.efrivahmi.elaborate.utils.ViewModelFactory

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var factory: ViewModelFactory
    private val forgetPasswordViewModel: ForgetPasswordViewModel by viewModels { factory }

    private var isVerificationCodeSent = false
    private var isVerificationCodeDeleted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)
        binding.verifyButton.isEnabled = false

        val intent = intent
        val email = intent.getStringExtra("email")
        email.let {
            binding.emailUser.text = it
        }

        binding.generatedVerifyButton.setOnClickListener {
            val email = binding.emailUser.text.toString().trim()
            if (email.isNotEmpty()) {
                forgetPasswordViewModel.forgotPassword(email)
            } else {
                showToastMessage(HelperToast("Email is required"))
            }
        }

        val verificationCodeEditTexts = listOf(
            binding.box1,
            binding.box2,
            binding.box3,
            binding.box4,
            binding.box5,
            binding.box6
        )

        for (editText in verificationCodeEditTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    var verificationCode = ""
                    for (editText in verificationCodeEditTexts) {
                        verificationCode += editText.text.toString()
                    }
                    binding.verifyButton.isEnabled = verificationCode.length == 6
                }
            })
        }

        binding.verifyButton.setOnClickListener {
            val email = binding.emailUser.text.toString().trim()
            val verificationCode = getVerificationCodeFromEditTexts(verificationCodeEditTexts)
            if (email.isNotEmpty()) {
                forgetPasswordViewModel.sendVerificationCode(email, verificationCode)
                forgetPasswordViewModel.verifyCode.observe(this) { response ->
                    isVerificationCodeSent = true
                    isVerificationCodeDeleted = response.verificationCodeDeleted
                    saveSession(
                        VerifyCode(
                            response.code,
                            response.error,
                            response.message,
                            response.userId,
                            response.username,
                            email,
                            response.resetToken,
                            response.verificationCodeDeleted
                        )
                    )
                }
            }
        }

        binding.arrow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        forgetPasswordViewModel.toastText.observe(this) { toast ->
            toast?.let {
                showToastMessage(toast)
            }
        }

        forgetPasswordViewModel.verifyCode.observe(this) { verifyCode ->
            handleVerifyCodeResponse(verifyCode)
        }

        forgetPasswordViewModel.forgot.observe(this) { fpResponse ->
            handleForgotPasswordResponse(fpResponse)
        }
    }

    private fun saveSession(verifyCode: VerifyCode) {
        forgetPasswordViewModel.saveSession(verifyCode)
    }

    private fun showLoading() {
        forgetPasswordViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showToastMessage(toast: HelperToast<String>) {
        val message = toast.getContentIfNotHandled()
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    @Suppress("DEPRECATION")
    private val itemForgetHandler = Handler()

    @SuppressLint("SetTextI18n")
    private fun handleVerifyCodeResponse(verifyCode: VerifyCode) {
        if (!verifyCode.error) {
            showLoading()
            binding.itemForget.visibility = View.VISIBLE
            binding.error.text = "Successful verification, please change the password"
            val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            binding.itemForget.startAnimation(slideUpAnimation)
            itemForgetHandler.postDelayed({
                try {
                    val intent = Intent(this, EditPasswordActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, 3000)
        } else if (isVerificationCodeDeleted) {
            showToastMessage(HelperToast("Verification code has already been deleted"))
        }
    }

    private fun handleForgotPasswordResponse(fpResponse: FpResponse) {
        if (fpResponse.error) {
            showToastMessage(HelperToast("Forgot password request sent"))
            binding.generatedVerifyButton.isEnabled = false
            binding.generatedVerifyButton.text = getString(R.string.request_sent)

            finish()
        }
    }

    private fun getVerificationCodeFromEditTexts(editTexts: List<EditText>): String {
        var verificationCode = ""
        for (editText in editTexts) {
            verificationCode += editText.text.toString()
        }
        return verificationCode
    }
}