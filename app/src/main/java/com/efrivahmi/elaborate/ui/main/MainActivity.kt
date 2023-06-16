package com.efrivahmi.elaborate.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.databinding.ActivityMainBinding
import com.efrivahmi.elaborate.ui.main.home.HomeFragment
import com.efrivahmi.elaborate.ui.main.profile.ProfileFragment
import com.efrivahmi.elaborate.ui.welcome.WelcomeActivity
import com.efrivahmi.elaborate.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var factory: ViewModelFactory
    private val mainViewModel: MainViewModel by viewModels { factory }
    private var isItem1Selected = false
    private var isItem2Selected = false
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        factory = ViewModelFactory.getInstance(this)

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    if (!isItem1Selected) {
                        isItem1Selected = true
                        isItem2Selected = false
                        replaceFragment(HomeFragment())
                    }
                }
            }

            true
        }

        binding.bottomNavigationView2.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile -> {
                    if (!isItem2Selected) {
                        isItem1Selected = false
                        isItem2Selected = true
                        replaceFragment(ProfileFragment())
                    }
                }
            }

            true
        }
        isLogin()
    }

    private fun isLogin() {
        showLoading(true)
        mainViewModel.getUser().observe(this@MainActivity) {
            token = it.token
            if (!it.isLogin) {
                moveActivity()
            } else {
                showLoading(false)
            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar8.visibility = View.VISIBLE
        } else {
            binding.progressBar8.visibility = View.GONE
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}