package com.example.petpulse.adapter

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.petpulse.R
import com.example.petpulse.model.Vaccine
import com.example.petpulse.network.ApiClient

class VaccineAdapter : RecyclerView.Adapter<VaccineAdapter.VH>() {

    private val list = mutableListOf<Vaccine>()

    fun setData(data: List<Vaccine>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vaccine, parent, false)
        return VH(v)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(list[position])
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(record: Vaccine) {
            itemView.findViewById<TextView>(R.id.tvVaccineName).text = record.vaccine_name
            itemView.findViewById<TextView>(R.id.tvVaccineDate).text = record.date_administered
            val statusTextView = itemView.findViewById<TextView>(R.id.tvVaccineStatus)

            // Trim the status to remove any leading/trailing whitespace from the backend
            val status = record.status?.trim() ?: "Status unknown"
            statusTextView.text = status

            val context = itemView.context
            // Set text color based on status (case-insensitive)
            when {
                status.equals("Due Soon", ignoreCase = true) -> {
                    statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                }
                status.equals("Completed", ignoreCase = true) -> {
                    statusTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_green_dark))
                }
                else -> {
                    // Set to a default color, e.g., black
                    statusTextView.setTextColor(Color.BLACK)
                }
            }

            val fileBtn = itemView.findViewById<ImageView>(R.id.btnViewFile)

            // Show button only if the server provides a file path
            if (!record.file_path.isNullOrEmpty()) {
                fileBtn.visibility = View.VISIBLE
                fileBtn.setOnClickListener {
                    val url = ApiClient.getFullImageUrl(record.file_path)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }
            } else {
                fileBtn.visibility = View.GONE
            }
        }
    }
}
