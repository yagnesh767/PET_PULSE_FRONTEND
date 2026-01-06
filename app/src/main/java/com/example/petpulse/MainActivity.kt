package com.example.petpulse

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope // Added import
import com.bumptech.glide.Glide // Added import
import com.example.petpulse.databinding.ActivityMainBinding // Added Binding
import com.example.petpulse.model.Pet
import com.example.petpulse.viewmodel.PetViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch // Ensure this is present if not already

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val petViewModel: PetViewModel by viewModels()

    private val addPetLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // UI Update handled by HomeFragment
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)
        if (userId != -1) {
            petViewModel.loadPets(userId)
        }

        binding.petSwitcherIcon.setOnClickListener {
            val bottomSheet = com.example.petpulse.ui.switchpet.SwitchPetBottomSheet(
                petViewModel.petsLiveData.value ?: emptyList()
            ) {
                // Handle pet selection update
            }
            bottomSheet.show(supportFragmentManager, "PetSwitch")
        }

        val bottomNavigationView = binding.bottomNavigationBar

        bottomNavigationView.setOnItemSelectedListener { item ->
            var selectedFragment: Fragment? = null

            when (item.itemId) {
                R.id.nav_home -> selectedFragment = HomeFragment()
                R.id.nav_store -> selectedFragment = StoreFragment()
                R.id.nav_ai -> selectedFragment = AiHubFragment()
                R.id.nav_settings -> selectedFragment = SettingsFragment()
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment)
            }
            true
        }

        petViewModel.selectedPet.observe(this) { pet ->
            supportActionBar?.title = pet?.pet_name
        }

        // Handle incoming notification
        val notificationType = intent.getStringExtra("notification_type")
        if (notificationType != null) {
            val petId = intent.getStringExtra("pet_id")
            handleNotification(notificationType, petId)
        } else {
             // Set default fragment if no notification
            if (savedInstanceState == null) {
                bottomNavigationView.selectedItemId = R.id.nav_home
            }
        }
    }

    private fun handleNotification(type: String, petId: String?) {
        when (type) {
            "Vaccine" -> {
                val intent = Intent(this, PetDetailsActivity::class.java)
                intent.putExtra("pet_id", petId)
                startActivity(intent)
            }
            "Appointment" -> {
                val intent = Intent(this, BookAppointmentActivity::class.java)
                intent.putExtra("pet_id", petId)
                startActivity(intent)
            }
            "Grooming" -> {
                val fragment = GroomingChecklistFragment()
                val bundle = bundleOf("pet_id" to petId)
                fragment.arguments = bundle
                loadFragment(fragment)
            }
            "AI" -> loadFragment(AiHubFragment())
            else -> loadFragment(HomeFragment())
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null) // Add to back stack for proper navigation
            .commit()
    }



    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            // UI Update handled by HomeFragment
        }
    }
}
