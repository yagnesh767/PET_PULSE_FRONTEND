package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class SymptomCheckerQ3Activity : AppCompatActivity() {

    private lateinit var optionsGroup: RadioGroup
    private lateinit var nextButton: Button
    private var selectedOption: Int = -1
    private var q1Answer: Int = -1
    private var q2Answer: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_checker_q3)

        q1Answer = intent.getIntExtra("q1_answer", -1)
        q2Answer = intent.getIntExtra("q2_answer", -1)

        val backButton = findViewById<ImageView>(R.id.back_button)
        optionsGroup = findViewById(R.id.options_group)
        nextButton = findViewById(R.id.next_button)

        backButton.setOnClickListener {
            finish()
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
            val intent = Intent(this, SymptomCheckerQ4Activity::class.java)
            intent.putExtra("q1_answer", q1Answer)
            intent.putExtra("q2_answer", q2Answer)
            intent.putExtra("q3_answer", selectedOption)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (selectedOption != -1) {
            optionsGroup.check(selectedOption)
        }
    }
}
