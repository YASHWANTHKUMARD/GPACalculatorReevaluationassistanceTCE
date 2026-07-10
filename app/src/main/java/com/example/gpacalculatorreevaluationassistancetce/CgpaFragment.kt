package com.example.gpacalculatorreevaluationassistancetce

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.gpacalculatorreevaluationassistancetce.databinding.FragmentCgpaBinding
import java.util.Locale

class CgpaFragment : Fragment() {

    private var _binding: FragmentCgpaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCgpaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Animation for the logo
        binding.cvLogoCgpa.alpha = 0f
        binding.cvLogoCgpa.translationY = 50f
        binding.cvLogoCgpa.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800L)
            .setInterpolator(android.view.animation.DecelerateInterpolator())
            .start()

        binding.btnCalculateCgpa.setOnClickListener {
            hideKeyboard()
            calculateCgpa()
        }
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun calculateCgpa() {
        val prevCgpa = binding.etPrevCgpa.text.toString().toDoubleOrNull()
        val prevCredits = binding.etPrevCredits.text.toString().toDoubleOrNull()
        val currSgpa = binding.etCurrSgpa.text.toString().toDoubleOrNull()
        val currCredits = binding.etCurrCredits.text.toString().toDoubleOrNull()

        if (prevCgpa != null && prevCredits != null && currSgpa != null && currCredits != null) {
            val totalCredits = prevCredits + currCredits
            if (totalCredits > 0) {
                val cgpa = (prevCgpa * prevCredits + currSgpa * currCredits) / totalCredits
                binding.tvCgpaResult.text = String.format(Locale.US, "Resulting CGPA: %.2f", cgpa)
            } else {
                binding.tvCgpaResult.text = "Total credits must be > 0"
            }
        } else {
            binding.tvCgpaResult.text = "Please fill all fields with valid numbers"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}