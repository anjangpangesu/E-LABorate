package com.efrivahmi.elaborate.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.databinding.FragmentProfileBinding
import com.efrivahmi.elaborate.ui.main.MainViewModel
import com.efrivahmi.elaborate.ui.main.profile.edit.EditProfileActivity
import com.efrivahmi.elaborate.ui.underdevelop.UnderDevelopmentActivity
import com.efrivahmi.elaborate.utils.ViewModelFactory

class ProfileFragment : Fragment() {
    private  var _binding: FragmentProfileBinding? = null
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext().applicationContext)
    }
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProfileBinding.bind(view)

        binding.editProfile.setOnClickListener {
            activity?.let {
                val intent = Intent(it, EditProfileActivity::class.java)
                it.startActivity(intent)
            }
        }
        activityTranscation()
        activityReviews()
        activityLanguage()
        activityLogout()
        setupUserViewModel()
    }

    private fun activityLanguage() {
        binding.ivLanguageSetting.setOnClickListener {
            activity?.let {
                val intent = Intent(it, UnderDevelopmentActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun activityReviews() {
        binding.ivReviews.setOnClickListener {
            activity?.let {
                val intent = Intent(it, UnderDevelopmentActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun activityTranscation() {
        binding.ivTransactionsList.setOnClickListener {
            activity?.let {
                val intent = Intent(it, UnderDevelopmentActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun setupUserViewModel() {
        mainViewModel.getUser().observe(viewLifecycleOwner) { user ->
            val username = user?.username
            val email = user?.email
            if (!username.isNullOrEmpty() && !email.isNullOrEmpty()) {
                binding.username.text = user.username
                binding.email.text = user.email
            }
        }
    }

    private fun activityLogout(): Boolean {
        binding.layoutLogout.setOnClickListener {
                mainViewModel.logout()
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}