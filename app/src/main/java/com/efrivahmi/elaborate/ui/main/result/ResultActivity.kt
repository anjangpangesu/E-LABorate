package com.efrivahmi.elaborate.ui.main.result

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.efrivahmi.elaborate.R
import com.efrivahmi.elaborate.data.response.DiagnoseResponse
import com.efrivahmi.elaborate.databinding.ActivityResultBinding
import com.efrivahmi.elaborate.ui.main.MainActivity
import com.efrivahmi.elaborate.utils.ViewModelFactoryMl

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private lateinit var factory: ViewModelFactoryMl
    private val resultViewModel: ResultViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactoryMl.getInstance(this)

        resultViewModel.diagnoseResult.observe(this) { diagnoseResponse ->
            if (diagnoseResponse != null) {
                    useDiagnosisData(diagnoseResponse)
                }
            handleDiagnoseResponse(diagnoseResponse)
            }



        val diagnosisIdLiveData = resultViewModel.getDiagnosisId()
        val userIdLiveData = resultViewModel.getUserId()

        userIdLiveData.observe(this) { userModel ->
            val userId = userModel.userId
            diagnosisIdLiveData.observe(this) { dResponse ->
                val diagnosisId = dResponse.diagnosisId

                resultViewModel.getResult(userId, diagnosisId)
            }
        }

        backHome()
        showLoading()
        showToast()

    }

    private fun backHome() {
        binding.arrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showLoading() {
        resultViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar9.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun handleDiagnoseResponse(diagnoseResponse: DiagnoseResponse) {
        val diagnosisId = diagnoseResponse.diagnosisId
        val inputData = diagnoseResponse.input_data
        val prediction = diagnoseResponse.prediction
        val message = "Diagnosis ID: $diagnosisId\nInput Data: $inputData\nPrediction: $prediction"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun useDiagnosisData(diagnosis: DiagnoseResponse) {
        val inputSex = diagnosis.input_data.sex

        val sex = if (inputSex == 0) {
            getString(R.string.male)
        } else if (inputSex == 1) {
            getString(R.string.female)
        } else {
            "Unknown"
        }

        val classResult = diagnosis.prediction.toString()
        val testResult = when (classResult) {
            "0" -> "Normal health status: no immediate need for medical consultation"
            "1" -> "It is advisable to seek a medical consultation in order to facilitate a comprehensive assessment for further diagnostic purposes."
            "2" -> "Medical consultation recommended for moderate condition"
            "3" -> "Immediate medical consultation required"
            else -> "Unknown"
        }


        binding.tvNeuGraResult.text = diagnosis.input_data.neu.toString()
        binding.tvLymphsResult.text = diagnosis.input_data.lym.toString()
        binding.tvMonocytesResult.text = diagnosis.input_data.mo.toString()
        binding.tvEosResult.text = diagnosis.input_data.eos.toString()
        binding.tvBasosResult.text = diagnosis.input_data.ba.toString()
        binding.tvAgePatient.text = diagnosis.input_data.age.toString()
        binding.tvSexPatient.text = sex
        binding.tvRbcResult.text = diagnosis.input_data.rbc.toString()
        binding.tvHgbResult.text = diagnosis.input_data.hgb.toString()
        binding.tvHctResult.text = diagnosis.input_data.hct.toString()
        binding.tvMcvResult.text = diagnosis.input_data.mcv.toString()
        binding.tvMchResult.text = diagnosis.input_data.mch.toString()
        binding.tvMchcResult.text = diagnosis.input_data.mchc.toString()
        binding.tvRdwCvResult.text = diagnosis.input_data.rdw_cv.toString()
        binding.tvWbcResult.text = diagnosis.input_data.wbc.toString()
        binding.classResult.text = diagnosis.prediction.toString()
        binding.tvResult.text = testResult
    }

    private fun showToast() {
        resultViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}