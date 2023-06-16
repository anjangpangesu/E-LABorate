package com.efrivahmi.elaborate.ui.main.profile.edit

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.viewModels
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.databinding.ActivityEditProfileBinding
import com.efrivahmi.elaborate.ui.login.LoginActivity
import com.efrivahmi.elaborate.ui.main.MainActivity
import com.efrivahmi.elaborate.ui.main.MainViewModel
import com.efrivahmi.elaborate.ui.main.profile.ProfileFragment
import com.efrivahmi.elaborate.ui.welcome.WelcomeActivity
import com.efrivahmi.elaborate.utils.ViewModelFactory

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var factory: ViewModelFactory
    private val editViewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        clickButton()
    }

    private fun clickButton() {
        binding.editButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()
            val addressUser = binding.addressEditText.text.toString().trim()

            if (username.isEmpty() && email.isEmpty() && phone.isEmpty() && addressUser.isEmpty()) {
                binding.usernameEditText.error = FILL_USERNAME
                binding.emailEditText.error = FILL_EMAIL
                binding.phoneEditText.error = FILL_PHONE
                binding.addressEditText.error = FILL_ADDRESS
            } else if (addressUser.length < 20) {
                Toast.makeText(
                    this,
                    "You have input an Full Address",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                showToast()
                showLoading()
                uploadData(username, email, phone, addressUser)
            }
        }

        binding.arrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @Suppress("DEPRECATION")
    private val itemEditHandler = Handler()

    @SuppressLint("SetTextI18n")
    private fun uploadData(username: String, email: String, phone: String, address: String) {
        editViewModel.getUser().observe(this) { user ->
            val userId = user.userId
            editViewModel.uploadEditData(userId, username, email, phone, address)
            editViewModel.editProfile.observe(this) { response ->
                if (!response.error) {
                    binding.itemEdit.visibility = View.VISIBLE
                    binding.error.text =
                        "Edit profile successfully, please check your profile again"
                    val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                    binding.itemEdit.startAnimation(slideUpAnimation)
                    itemEditHandler.postDelayed({
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }, 4000)
                } else {
                    binding.itemEdit.visibility = View.GONE
                }
            }
        }
    }

    private fun showToast() {
        editViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading() {
        editViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar6.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    companion object {
        private const val FILL_USERNAME = "Have to fill your name"
        private const val FILL_PHONE = "Have to fill Phone Number"
        private const val FILL_EMAIL = "Have to fill email first"
        private const val FILL_ADDRESS = "Have to fill your Address"
    }
}