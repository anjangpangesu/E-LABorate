package com.efrivahmi.elaborate.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.databinding.ActivityLoginBinding
import com.efrivahmi.elaborate.ui.main.MainActivity
import com.efrivahmi.elaborate.ui.register.RegisterActivity
import com.efrivahmi.elaborate.ui.login.forget.ForgetPasswordActivity
import com.efrivahmi.elaborate.ui.welcome.WelcomeActivity
import com.efrivahmi.elaborate.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }
    private var incorrectPasswordCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        clickButton()

        factory = ViewModelFactory.getInstance(this)
    }

    private fun clickButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText2.text.toString().trim()
            val password = binding.passwordEditText2.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                if (email.isEmpty()) {
                    binding.emailEditText2.error = FILL_EMAIL
                }
                if (password.isEmpty()) {
                    Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                }
            } else if (!isValidEmail(email)) {
                binding.emailEditText2.error = INVALID_EMAIL
            } else {
                handleIncorrectPassword()
                if (password.length < 8) {
                    Toast.makeText(this, "Password must have a\nminimum of 8 characters", Toast.LENGTH_SHORT).show()
                } else {
                    showLoading()
                    uploadData()
                    loginViewModel.login()
                    showToast()
                }
            }
        }
        binding.arrow.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
        }
        binding.loginAction.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.tvForgetPasswordAction.setOnClickListener {
            val email = binding.emailEditText2.text.toString().trim()

            if (email.isNotEmpty()) {
                handleForgetEmail(email)
            }
        }
    }

    private fun showLoading() {
        loginViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun uploadData() {
        binding.apply {
            loginViewModel.uploadLoginData(
                emailEditText2.text.toString(),
                passwordEditText2.text.toString()
            )
        }
        loginViewModel.login.observe(this) { response ->
                saveSession(
                    UserModel(
                        response.userData.userId,
                        response.userData.username,
                        response.userData.email,
                        "Bearer ${response.userData.token}",
                        true
                    )
                )
                navigateToMain()
            }
        }

    private fun showToast() {
        loginViewModel.toast.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun saveSession(session: UserModel) {
        loginViewModel.saveSession(session)
    }

    private fun handleForgetEmail(email: String){
        val intent = Intent(this, ForgetPasswordActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

    private fun handleIncorrectPassword() {
        val password = binding.passwordEditText2.text.toString().trim()
        val email = binding.emailEditText2.text.toString().trim()

        if (password.isNotEmpty()) {
            incorrectPasswordCount++

            if (incorrectPasswordCount == 3) {
                binding.loginButton.isEnabled = true
                val toastMessage = "You have entered\nincorrect password 3 times."
                val toast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 250)
                toast.show()
            } else if (incorrectPasswordCount == 10) {
                binding.loginButton.isEnabled = false
                val toastMessage = "Please use the Forgot Password feature."
                val toast = Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 250)
                toast.show()
            }
        } else {
            incorrectPasswordCount = 0
            if (email.isNotEmpty()) {
                val toastMessage = "Invalid password"
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            } else {
                val toastMessage = "Invalid password or email"
                Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val FILL_EMAIL = "Please fill in the email field"
        private const val INVALID_EMAIL = "Please enter a valid email address"
    }
}