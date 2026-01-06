package com.example.petpulse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class StoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        // Online Stores
        findViewById<MaterialCardView>(R.id.amazon_card).setOnClickListener {
            openUrl("https://www.amazon.com/s?k=pet+supplies")
        }
        findViewById<MaterialCardView>(R.id.chewy_card).setOnClickListener {
            openUrl("https://www.chewy.com/")
        }
        findViewById<MaterialCardView>(R.id.petco_card).setOnClickListener {
            openUrl("https://www.petco.com/")
        }
        findViewById<MaterialCardView>(R.id.petsmart_card).setOnClickListener {
            openUrl("https://www.petsmart.com/")
        }

        // View Map button
        findViewById<TextView>(R.id.view_map_button).setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:0,0?q=pet+stores")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }

        // Nearby Store Clicks
        findViewById<MaterialCardView>(R.id.store_card_1).setOnClickListener {
            openMapForStore("PetSmart Downtown")
        }
        findViewById<MaterialCardView>(R.id.store_card_2).setOnClickListener {
            openMapForStore("Happy Paws Boutique")
        }
        findViewById<MaterialCardView>(R.id.store_card_3).setOnClickListener {
            openMapForStore("City Pet Supplies")
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    private fun openMapForStore(storeName: String) {
        val gmmIntentUri = Uri.parse("geo:0,0?q=$storeName")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}