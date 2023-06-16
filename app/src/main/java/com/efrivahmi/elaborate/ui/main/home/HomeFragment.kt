package com.efrivahmi.elaborate.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.databinding.FragmentHomeBinding
import com.efrivahmi.elaborate.ui.main.MainViewModel
import com.efrivahmi.elaborate.ui.main.diagnose.DiagnoseActivity
import com.efrivahmi.elaborate.ui.main.result.ResultActivity
import com.efrivahmi.elaborate.ui.underdevelop.UnderDevelopmentActivity
import com.efrivahmi.elaborate.utils.ViewModelFactory

class HomeFragment : Fragment() {
    private  var _binding: FragmentHomeBinding? = null
    private val factory: ViewModelFactory by lazy {
        ViewModelFactory.getInstance(requireContext().applicationContext)
    }
    private val mainViewModel: MainViewModel by viewModels { factory }
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        activityDiagnose()
        activityResult()
        activityMedicine()
        activityWorkout()
        activityDoctor()
        setupUserViewModel()
    }

    private fun activityDoctor() {
        binding.ivDoctor.setOnClickListener {
            activity?.let {
                val intent = Intent(it, UnderDevelopmentActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun activityWorkout() {
        binding.ivWorkout.setOnClickListener {
            activity?.let {
                val intent = Intent(it, UnderDevelopmentActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun activityMedicine() {
        binding.ivMedicine.setOnClickListener {
            activity?.let {
                val intent = Intent(it, UnderDevelopmentActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun setupUserViewModel() {
        mainViewModel.getUser().observe(viewLifecycleOwner) { user ->
            val username = user?.username
            if (!username.isNullOrEmpty()) {
                binding.tvUsername.text = getString(R.string.hello, user.username)
            }
        }
    }

    private fun activityResult() {
        binding.ivResult.setOnClickListener {
            activity?.let {
                val intent = Intent(it, ResultActivity::class.java)
                it.startActivity(intent)
            }
        }
    }

    private fun activityDiagnose() {
        binding.ivDiagnose.setOnClickListener {
            activity?.let {
                val intent = Intent (it, DiagnoseActivity::class.java)
                it.startActivity(intent)
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}