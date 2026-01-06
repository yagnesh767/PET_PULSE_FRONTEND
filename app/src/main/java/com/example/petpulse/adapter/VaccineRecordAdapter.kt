package com.example.petpulse.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.petpulse.R
import com.example.petpulse.model.MedicalRecord
import com.example.petpulse.network.ApiClient

class VaccineRecordAdapter : RecyclerView.Adapter<VaccineRecordAdapter.VH>() {

    private val records = mutableListOf<MedicalRecord>()

    fun setRecords(list: List<MedicalRecord>) {
        records.clear()
        records.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_vaccine_record, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount() = records.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(record: MedicalRecord) {

            itemView.findViewById<TextView>(R.id.tvVaccineName).text = record.title
            itemView.findViewById<TextView>(R.id.tvVaccineDate).text =
                "Date: ${record.record_date}"

            val statusText = record.description ?: "Status: Unknown"
            itemView.findViewById<TextView>(R.id.tvVaccineStatus).text = statusText

            val btnFile = itemView.findViewById<ImageView>(R.id.btnViewCertificate)

            if (!record.file_path.isNullOrBlank()) {
                btnFile.visibility = View.VISIBLE
                btnFile.setOnClickListener {
                    try {
                        val fullUrl = ApiClient.getFullImageUrl(record.file_path)
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(fullUrl))
                        itemView.context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            itemView.context,
                            "Unable to open certificate",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                btnFile.visibility = View.GONE
            }
        }
    }
}
