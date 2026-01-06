package com.example.petpulse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment

class StoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_store, container, false)

        view.findViewById<CardView>(R.id.amazon_card).setOnClickListener {
            openUrl("https://www.amazon.com")
        }

        view.findViewById<CardView>(R.id.chewy_card).setOnClickListener {
            openUrl("https://www.chewy.com")
        }

        view.findViewById<CardView>(R.id.petco_card).setOnClickListener {
            openUrl("https://www.petco.com")
        }

        view.findViewById<CardView>(R.id.petsmart_card).setOnClickListener {
            openUrl("https://www.petsmart.com")
        }

        view.findViewById<TextView>(R.id.view_map_button).setOnClickListener {
            openMapForStore("pet stores")
        }

        view.findViewById<CardView>(R.id.store_card_1).setOnClickListener {
            openMapForStore("PetSmart Downtown")
        }

        view.findViewById<CardView>(R.id.store_card_2).setOnClickListener {
            openMapForStore("Happy Paws Boutique")
        }

        view.findViewById<CardView>(R.id.store_card_3).setOnClickListener {
            openMapForStore("City Pet Supplies")
        }

        return view
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun openMapForStore(storeName: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$storeName near me")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        startActivity(mapIntent)
    }
}
