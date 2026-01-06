package com.example.petpulse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChatAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_INTRO = 0
        private const val TYPE_USER = 1
        private const val TYPE_AI = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        return if (message.isIntro) {
            TYPE_INTRO
        } else if (message.isUserMessage) {
            TYPE_USER
        } else {
            TYPE_AI
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_INTRO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
                IntroViewHolder(view)
            }
            TYPE_USER -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
                UserViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_message, parent, false)
                AiViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is IntroViewHolder -> holder.bind(message)
            is UserViewHolder -> holder.bind(message)
            is AiViewHolder -> holder.bind(message)
        }
    }

    override fun getItemCount(): Int = messages.size

    class IntroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val introCard: View = itemView.findViewById(R.id.ai_intro_card)

        fun bind(chatMessage: ChatMessage) {
            introCard.visibility = View.VISIBLE
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userMessageLayout: View = itemView.findViewById(R.id.user_message_layout)
        private val userMessageTextView: TextView = itemView.findViewById(R.id.tv_user_message)

        fun bind(chatMessage: ChatMessage) {
            userMessageLayout.visibility = View.VISIBLE
            userMessageTextView.text = chatMessage.message
        }
    }

    class AiViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val aiMessageLayout: View = itemView.findViewById(R.id.ai_message_layout)
        private val aiMessageTextView: TextView = itemView.findViewById(R.id.tv_ai_message)

        fun bind(chatMessage: ChatMessage) {
            aiMessageLayout.visibility = View.VISIBLE
            aiMessageTextView.text = chatMessage.message
        }
    }
}
