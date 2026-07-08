package com.example.gpacalculatorreevaluationassistancetce

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.gpacalculatorreevaluationassistancetce.databinding.FragmentGoalBinding
import java.util.Locale

class GoalFragment : Fragment() {

    private var _binding: FragmentGoalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGoalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCalculateGoal.setOnClickListener {
            hideKeyboard()
            calculateGoal()
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun calculateGoal() {
        val targetCgpa = binding.etTargetCgpa.text.toString().toDoubleOrNull()
        val prevCgpa = binding.etPrevCgpaGoal.text.toString().toDoubleOrNull()
        val prevCredits = binding.etPrevCreditsGoal.text.toString().toDoubleOrNull()
        val currCredits = binding.etCurrCreditsGoal.text.toString().toDoubleOrNull()

        if (targetCgpa != null && prevCgpa != null && prevCredits != null && currCredits != null) {
            if (currCredits <= 0) {
                binding.tvGoalResult.text = "Current credits must be > 0"
                return
            }

            val totalCredits = prevCredits + currCredits
            val requiredSgpa = (targetCgpa * totalCredits - prevCgpa * prevCredits) / currCredits

            when {
                requiredSgpa > 10.0 -> {
                    binding.tvGoalResult.text = String.format(Locale.US, "Impossible! Need %.2f SGPA", requiredSgpa)
                }
                requiredSgpa < 0 -> {
                    binding.tvGoalResult.text = "Goal already achieved!"
                }
                else -> {
                    binding.tvGoalResult.text = String.format(Locale.US, "Required SGPA: %.2f", requiredSgpa)
                }
            }
        } else {
            binding.tvGoalResult.text = "Please fill all fields with valid numbers"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}