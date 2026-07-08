package com.example.gpacalculatorreevaluationassistancetce

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gpacalculatorreevaluationassistancetce.databinding.FragmentSgpaBinding
import com.example.gpacalculatorreevaluationassistancetce.databinding.ItemCourseBinding

class SgpaFragment : Fragment() {

    private var _binding: FragmentSgpaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSgpaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Restore state or add initial course
        if (savedInstanceState != null) {
            val grades = savedInstanceState.getStringArrayList("grades")
            val credits = savedInstanceState.getStringArrayList("credits")
            if (grades != null && credits != null) {
                binding.llCourseContainer.removeAllViews()
                for (i in grades.indices) {
                    addCourseRow(grades[i], credits[i])
                }
            }
        } else if (binding.llCourseContainer.childCount == 0) {
            addCourseRow()
        }

        binding.btnAddCourse.setOnClickListener {
            addCourseRow()
        }

        binding.btnCalculateSgpa.setOnClickListener {
            hideKeyboard()
            calculateSgpa()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val grades = ArrayList<String>()
        val credits = ArrayList<String>()
        for (i in 0 until binding.llCourseContainer.childCount) {
            val view = binding.llCourseContainer.getChildAt(i)
            val itemBinding = ItemCourseBinding.bind(view)
            grades.add(itemBinding.etGradePoints.text.toString())
            credits.add(itemBinding.etCredits.text.toString())
        }
        outState.putStringArrayList("grades", grades)
        outState.putStringArrayList("credits", credits)
    }

    private fun addCourseRow(initialGrade: String = "", initialCredit: String = "") {
        val itemBinding = ItemCourseBinding.inflate(layoutInflater, binding.llCourseContainer, false)
        itemBinding.etGradePoints.setText(initialGrade)
        itemBinding.etCredits.setText(initialCredit)
        itemBinding.btnRemove.setOnClickListener {
            binding.llCourseContainer.removeView(itemBinding.root)
        }
        binding.llCourseContainer.addView(itemBinding.root)
    }

    private fun hideKeyboard() {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    private fun calculateSgpa() {
        var totalPoints = 0.0
        var totalCredits = 0.0
        var hasError = false

        if (binding.llCourseContainer.childCount == 0) {
            Toast.makeText(context, "Please add at least one course", Toast.LENGTH_SHORT).show()
            return
        }

        for (i in 0 until binding.llCourseContainer.childCount) {
            val view = binding.llCourseContainer.getChildAt(i)
            val itemBinding = ItemCourseBinding.bind(view)

            val gradePointsStr = itemBinding.etGradePoints.text.toString()
            val creditsStr = itemBinding.etCredits.text.toString()

            val gp = gradePointsStr.toDoubleOrNull()
            val credits = creditsStr.toDoubleOrNull()

            if (gp != null && credits != null) {
                totalPoints += (gp * credits)
                totalCredits += credits
            } else {
                hasError = true
            }
        }

        if (hasError) {
            Toast.makeText(context, "Please fill all grade points and credits", Toast.LENGTH_SHORT).show()
        }

        if (totalCredits > 0) {
            val sgpa = totalPoints / totalCredits
            binding.tvSgpaResult.text = String.format(java.util.Locale.US, "SGPA: %.2f", sgpa)
        } else {
            binding.tvSgpaResult.text = "SGPA: 0.00"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}