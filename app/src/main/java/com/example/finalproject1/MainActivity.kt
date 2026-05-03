package com.example.finalproject1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var vehicleContainer: LinearLayout

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val year = data?.getStringExtra("YEAR")
            val make = data?.getStringExtra("MAKE")
            val model = data?.getStringExtra("MODEL")

            if (year != null && make != null && model != null) {
                addNewVehicleButton(year, make, model)
                saveVehicleToStorage(year, make, model)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        vehicleContainer = findViewById(R.id.vehicleContainer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val addVehicleButton: Button = findViewById(R.id.vehicle)
        addVehicleButton.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startForResult.launch(intent)
        }

        loadSavedVehicles()
    }

    private fun addNewVehicleButton(year: String, make: String, model: String) {
        val newButton = Button(this)
        newButton.text = "$year, $make, $model"
        
        newButton.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            intent.putExtra("YEAR", year)
            intent.putExtra("MAKE", make)
            intent.putExtra("MODEL", model)
            startActivity(intent)
        }

        vehicleContainer.addView(newButton)
    }

    private fun saveVehicleToStorage(year: String, make: String, model: String) {
        val sharedPref = getSharedPreferences("VehicleAppPrefs", Context.MODE_PRIVATE)
        val vehicleList = sharedPref.getStringSet("VEHICLE_LIST", mutableSetOf()) ?: mutableSetOf()
        
        val updatedList = vehicleList.toMutableSet()
        // Save as a single string "year|make|model"
        updatedList.add("$year|$make|$model")
        
        with(sharedPref.edit()) {
            putStringSet("VEHICLE_LIST", updatedList)
            apply()
        }
    }

    private fun loadSavedVehicles() {
        val sharedPref = getSharedPreferences("VehicleAppPrefs", Context.MODE_PRIVATE)
        val vehicleList = sharedPref.getStringSet("VEHICLE_LIST", null)
        
        vehicleList?.forEach { vehicleData ->
            val parts = vehicleData.split("|")
            if (parts.size == 3) {
                addNewVehicleButton(parts[0], parts[1], parts[2])
            }
        }
    }
}
