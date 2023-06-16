package com.efrivahmi.elaborate.ui.main.diagnose

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
import com.efrivahmi.elaborate.data.model.Diagnose
import com.efrivahmi.elaborate.data.model.UserModel
import com.efrivahmi.elaborate.data.response.DResponse
import com.efrivahmi.elaborate.databinding.ActivityDiagnoseBinding
import com.efrivahmi.elaborate.ui.main.MainActivity
import com.efrivahmi.elaborate.utils.ViewModelFactoryMl

class DiagnoseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDiagnoseBinding
    private lateinit var factory: ViewModelFactoryMl
    private val diagnoseViewModel: DiagnoseViewModel by viewModels { factory }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiagnoseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        factory = ViewModelFactoryMl.getInstance(this)

        binding.interpretationButton.setOnClickListener {
            diagnoseViewModel.getPatient().observe(this) { patient: UserModel ->
                if (isDataValid()) {
                    val userId = patient.userId
                    val age = binding.inputage.text.toString().toInt()
                    val sex = binding.inputsex.text.toString().toInt()

                    if (sex != 0 && sex != 1) {
                        Toast.makeText(this, "Invalid sex input. Please enter 0 for male or 1 for female.", Toast.LENGTH_SHORT).show()
                    } else {

                    }
                    val rbc = binding.inputrbc.text.toString().toDouble()
                    val hgb = binding.inputhgb.text.toString().toDouble()
                    val hct = binding.inputhct.text.toString().toDouble()
                    val mcv = binding.inputmcv.text.toString().toDouble()
                    val mch = binding.inputmch.text.toString().toDouble()
                    val mchc = binding.inputmchc.text.toString().toDouble()
                    val rdwCv = binding.inputrdcv.text.toString().toDouble()
                    val wbc = binding.inputwbc.text.toString().toDouble()
                    val neu = binding.inputneugra.text.toString().toDouble()
                    val lym = binding.inputlymphs.text.toString().toDouble()
                    val mo = binding.inputmo.text.toString().toDouble()
                    val eos = binding.inputeos.text.toString().toDouble()
                    val ba = binding.inputbasophils.text.toString().toDouble()

                    val diagnose = Diagnose(
                        age = age,
                        sex = sex,
                        rbc = rbc,
                        hgb = hgb,
                        hct = hct,
                        mcv = mcv,
                        mch = mch,
                        mchc = mchc,
                        rdw_cv = rdwCv,
                        wbc = wbc,
                        neu = neu,
                        lym = lym,
                        mo = mo,
                        eos = eos,
                        ba = ba
                    )
                    binding.itemEdit.visibility = View.VISIBLE
                    binding.error.text =
                        "Diagnosis is successful, please check the diagnosis result in the result menu."
                    val slideUpAnimation = AnimationUtils.loadAnimation(this, R.anim.slide_up)
                    binding.itemEdit.startAnimation(slideUpAnimation)
                    itemForgetHandler.postDelayed({
                        diagnoseViewModel.saveDiagnoseRequest(diagnose)
                        diagnoseViewModel.diagnoseClient(userId, diagnose)
                        showToast()
                        showLoading()
                        moveAction()
                    }, 3000)
                } else {
                    Toast.makeText(this, "Please fill in all fields correctly", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        diagnoseViewModel.diagnoseResult.observe(this) { dResponse: DResponse ->
            diagnoseViewModel.saveDiagnose(dResponse)
            val prediction = dResponse.prediction
            val diagnosisId = dResponse.diagnosisId

            val result = "DResponse Result: " +
                    "Diagnosis ID: $diagnosisId, " +
                    "Prediction: $prediction"

            Toast.makeText(this, result,Toast.LENGTH_SHORT).show()
        }

        binding.arrow.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    @Suppress("DEPRECATION")
    private val itemForgetHandler = Handler()

    private fun moveAction(){
        val intent = Intent(this@DiagnoseActivity, MainActivity::class.java )
        startActivity(intent)
        finish()
    }

    private fun showLoading() {
        diagnoseViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar6.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showToast() {
        diagnoseViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isDataValid(): Boolean {
        val age = binding.inputage.text.toString()
        val sex = binding.inputsex.text.toString()
        val rbc = binding.inputrbc.text.toString()
        val hgb = binding.inputhgb.text.toString()
        val hct = binding.inputhct.text.toString()
        val mcv = binding.inputmcv.text.toString()
        val mch = binding.inputmch.text.toString()
        val mchc = binding.inputmchc.text.toString()
        val rdwCv = binding.inputrdcv.text.toString()
        val wbc = binding.inputwbc.text.toString()
        val neu = binding.inputneugra.text.toString()
        val lym = binding.inputlymphs.text.toString()
        val mo = binding.inputmo.text.toString()
        val eos = binding.inputeos.text.toString()
        val ba = binding.inputbasophils.text.toString()

        return age.isNotEmpty() && sex.isNotEmpty() && rbc.isNotEmpty() && hgb.isNotEmpty() &&
                hct.isNotEmpty() && mcv.isNotEmpty() && mch.isNotEmpty() && mchc.isNotEmpty() &&
                rdwCv.isNotEmpty() && wbc.isNotEmpty() && neu.isNotEmpty() && lym.isNotEmpty() &&
                mo.isNotEmpty() && eos.isNotEmpty() && ba.isNotEmpty()
    }
}