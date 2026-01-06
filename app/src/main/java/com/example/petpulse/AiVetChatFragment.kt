package com.example.petpulse

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class AiVetChatFragment : Fragment(R.layout.fragment_ai_vet_chat) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val input = view.findViewById<EditText>(R.id.messageEditText)
        val send = view.findViewById<ImageButton>(R.id.sendButton)
        val container = view.findViewById<LinearLayout>(R.id.chatContainer)

        val chips = listOf(
            view.findViewById<Button>(R.id.suggestionChip1),
            view.findViewById<Button>(R.id.suggestionChip2),
            view.findViewById<Button>(R.id.suggestionChip3),
            view.findViewById<Button>(R.id.suggestionChip4)
        )

        chips.forEach { chip ->
            chip.setOnClickListener {
                input.setText(chip.text)
            }
        }

        send.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                addUserMessage(container, text)
                addAiMessage(container, "Thanks for sharing. I’m analyzing Bella’s health data.")
                input.text.clear()
            }
        }
    }

    private fun addUserMessage(parent: LinearLayout, message: String) {
        val tv = TextView(requireContext())
        tv.text = message
        tv.setPadding(16, 12, 16, 12)
        tv.setBackgroundResource(R.drawable.bg_user_message)
        parent.addView(tv)
    }

    private fun addAiMessage(parent: LinearLayout, message: String) {
        val tv = TextView(requireContext())
        tv.text = message
        tv.setPadding(16, 12, 16, 12)
        tv.setBackgroundResource(R.drawable.bg_ai_message)
        parent.addView(tv)
    }
}
