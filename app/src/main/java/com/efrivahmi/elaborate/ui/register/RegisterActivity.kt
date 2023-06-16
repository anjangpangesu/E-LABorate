package com.efrivahmi.elaborate.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.databinding.ActivityRegisterBinding
import com.efrivahmi.elaborate.ui.custom.Password
import com.efrivahmi.elaborate.ui.login.LoginActivity
import com.efrivahmi.elaborate.ui.welcome.WelcomeActivity
import com.efrivahmi.elaborate.utils.ViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var passwordEditText: Password
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var agreementCheckBox: CheckBox
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactory.getInstance(this)

        passwordEditText = findViewById(R.id.cpasswordEditText)
        confirmPasswordEditText = findViewById(R.id.passwordEditText)
        agreementCheckBox = findViewById(R.id.agreement)
        passwordEditText.setConfirmPasswordEditText(confirmPasswordEditText)

        clickButton()
    }

    private fun clickButton() {
        binding.registerButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.cpasswordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty() && password == confirmPassword || !isValidEmail(email)) {
                binding.usernameEditText.error = FILL_NAME
                binding.emailEditText.error = FILL_EMAIL
                binding.passwordEditText.error = FILL_PASSWORD
            } else {
                if (agreementCheckBox.isChecked) {
                    if (password.length < 8) {
                        Toast.makeText(this, "Password must have a\nminimum of 8 characters", Toast.LENGTH_SHORT).show()
                    } else {
                        uploadData(username, email, password, confirmPassword)
                        showLoading()
                    }
                } else {
                    Toast.makeText(this, "Please agree to the Terms & Conditions", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.arrow.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
        binding.registerAction.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        binding.term.setOnClickListener {
            val intent = Intent(this, TermCondition::class.java)
            startActivity(intent)
        }
        binding.privacy.setOnClickListener {
            val intent = Intent(this, PrivacyPolicy::class.java)
            startActivity(intent)
        }
    }

    @Suppress("DEPRECATION")
    private val itemRegisterHandler = Handler()

    @SuppressLint("SetTextI18n")
    private fun uploadData(username: String, email: String, password: String, confirmPassword: String) {
        registerViewModel.uploadRegisData(username, email, password, confirmPassword)
        registerViewModel.regis.observe(this) { response ->
            if (!response.error) {
                binding.itemRegister.visibility = View.VISIBLE
                binding.error.text = "Successfully create an account, please sign in to access the application."
                val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                binding.itemRegister.startAnimation(slideUpAnimation)
                itemRegisterHandler.postDelayed({
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }, 4000)
            } else {
                binding.itemRegister.visibility = View.GONE
            }
        }
    }

    private fun showLoading() {
        registerViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    companion object {
        private const val FILL_NAME = "Have to fill your name"
        private const val FILL_PASSWORD = "Have to fill password first"
        private const val FILL_EMAIL = "Have to fill email first"
    }
}