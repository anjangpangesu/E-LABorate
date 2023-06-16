package com.efrivahmi.elaborate.ui.login.editpassword

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.data.response.RpResponse
import com.efrivahmi.elaborate.data.response.VerifyCode
import com.efrivahmi.elaborate.databinding.ActivityEditPasswordBinding
import com.efrivahmi.elaborate.ui.login.LoginActivity
import com.efrivahmi.elaborate.utils.HelperToast
import com.efrivahmi.elaborate.utils.ViewModelFactory

class EditPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding
    private lateinit var factory: ViewModelFactory
    private val viewModel: EditPasswordViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        binding.editButton.setOnClickListener {
            viewModel.getResetToken().observe(this) { patient: VerifyCode ->
                val resetToken = patient.resetToken
                val newPassword = binding.passwordEditText3.text.toString().trim()
                if (resetToken.isNotEmpty() && newPassword.length >= 8) {
                    showLoading()
                    viewModel.resetPassword(resetToken, newPassword)
                } else {
                    Toast.makeText(
                        this,
                        "Password must have a\n" +
                                "minimum of 8 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        viewModel.resetPasswordResult.observe(this) { rpResponse ->
            handleNewPasswordResponse(rpResponse)
        }

        viewModel.toastText.observe(this) { toast ->
            toast?.let {
                showToastMessage(toast)
            }
        }

        binding.arrow.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    @Suppress("DEPRECATION")
    private val itemEditHandler = Handler()

    private fun showLoading() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar5.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showToastMessage(toast: HelperToast<String>) {
        val message = toast.getContentIfNotHandled()
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleNewPasswordResponse(rpResponse: RpResponse) {
        if (rpResponse.error) {
            Toast.makeText(this, "Failed to change password", Toast.LENGTH_SHORT).show()
            binding.editButton.text = "Password must be unique characters"
        } else {
            Toast.makeText(
                this,
                "Password changed successfully",
                Toast.LENGTH_SHORT
            ).show()
            binding.itemEdit.visibility = View.VISIBLE
            binding.error.text =
                "Password successfully changed, please sign in to the application."
            val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            binding.itemEdit.startAnimation(slideUpAnimation)
            itemEditHandler.postDelayed({
                try{
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                finishAffinity()
            }, 4000)
        }
    }
}