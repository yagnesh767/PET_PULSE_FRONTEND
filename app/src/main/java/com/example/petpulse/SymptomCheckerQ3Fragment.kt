package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment

class SymptomCheckerQ3Fragment : Fragment() {

    private lateinit var optionsGroup: RadioGroup
    private lateinit var nextButton: Button
    private var selectedOption: Int = -1
    private var q1Answer: Int = -1
    private var q2Answer: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_symptom_checker_q3, container, false)

        arguments?.let {
            q1Answer = it.getInt("q1_answer", -1)
            q2Answer = it.getInt("q2_answer", -1)
        }

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        optionsGroup = view.findViewById(R.id.options_group)
        nextButton = view.findViewById(R.id.next_button)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        optionsGroup.setOnCheckedChangeListener { group, checkedId ->
            nextButton.isEnabled = true
            selectedOption = checkedId
            for (i in 0 until group.childCount) {
                val radioButton = group.getChildAt(i) as RadioButton
                radioButton.isSelected = (radioButton.id == checkedId)
            }
        }

        nextButton.setOnClickListener {
            val fragment = SymptomCheckerQ4Fragment()
            val bundle = Bundle()
            bundle.putInt("q1_answer", q1Answer)
            bundle.putInt("q2_answer", q2Answer)
            bundle.putInt("q3_answer", selectedOption)
            fragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        if (selectedOption != -1) {
            optionsGroup.check(selectedOption)
        }
    }
}