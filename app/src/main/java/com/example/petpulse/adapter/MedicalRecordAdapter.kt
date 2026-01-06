package com.example.petpulse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.petpulse.R
import com.example.petpulse.model.MedicalRecord

class MedicalRecordAdapter : RecyclerView.Adapter<MedicalRecordAdapter.VH>() {

    private val records = mutableListOf<MedicalRecord>()

    fun setRecords(list: List<MedicalRecord>) {
        records.clear()
        records.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medical_record, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(records[position])
    }

    override fun getItemCount() = records.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvCondition = itemView.findViewById<TextView>(R.id.tvCondition)
        private val tvDateVet = itemView.findViewById<TextView>(R.id.tvDateVet)
        private val fileContainer = itemView.findViewById<View>(R.id.fileContainer)

        fun bind(record: MedicalRecord) {
            tvCondition.text = record.title
            tvDateVet.text = "${record.record_date ?: ""} • ${record.veterinarian ?: ""}"

            if (!record.file_path.isNullOrBlank()) {
                fileContainer.visibility = View.VISIBLE
                fileContainer.setOnClickListener {
                    val fullUrl = com.example.petpulse.network.ApiClient.getFullImageUrl(record.file_path)
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW)
                    intent.data = android.net.Uri.parse(fullUrl)
                    itemView.context.startActivity(intent)
                }
            } else {
                fileContainer.visibility = View.GONE
            }
        }
    }
}
