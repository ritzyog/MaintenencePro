package com.example.finalproject1

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity3 : AppCompatActivity() {

    private lateinit var recordsContainer: LinearLayout
    private lateinit var vehicleId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val detailsTextView: TextView = findViewById(R.id.displayDetails)
        val recordInput: EditText = findViewById(R.id.recordInput)
        val addRecordButton: Button = findViewById(R.id.recordButton)
        recordsContainer = findViewById(R.id.recordsContainer)
        val backButton: Button = findViewById(R.id.back2)

        // Get the data passed from MainActivity
        val year = intent.getStringExtra("YEAR") ?: ""
        val make = intent.getStringExtra("MAKE") ?: ""
        val model = intent.getStringExtra("MODEL") ?: ""

        detailsTextView.text = "$year $make $model"
        
        // Create a unique key for this specific vehicle to save its records
        vehicleId = "RECORDS_${year}_${make}_${model}".replace(" ", "_")

        // Load existing records from storage
        loadSavedRecords()

        // Logic to add a new record
        addRecordButton.setOnClickListener {
            val recordText = recordInput.text.toString()
            if (recordText.isNotEmpty()) {
                addRecordToUI(recordText)
                saveRecordToStorage(recordText)
                recordInput.text.clear()
            }
        }

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun addRecordToUI(recordText: String) {
        val newRecordView = TextView(this)
        newRecordView.text = "• $recordText"
        newRecordView.textSize = 18f
        newRecordView.setPadding(0, 8, 0, 8)
        recordsContainer.addView(newRecordView)
    }

    private fun saveRecordToStorage(recordText: String) {
        val sharedPref = getSharedPreferences("VehicleAppPrefs", Context.MODE_PRIVATE)
        val existingRecords = sharedPref.getStringSet(vehicleId, mutableSetOf()) ?: mutableSetOf()
        
        // Create a new set to trigger the SharedPreferences change listener correctly
        val updatedRecords = existingRecords.toMutableSet()
        updatedRecords.add(recordText)
        
        with(sharedPref.edit()) {
            putStringSet(vehicleId, updatedRecords)
            apply()
        }
    }

    private fun loadSavedRecords() {
        val sharedPref = getSharedPreferences("VehicleAppPrefs", Context.MODE_PRIVATE)
        val savedRecords = sharedPref.getStringSet(vehicleId, null)
        
        savedRecords?.forEach { record ->
            addRecordToUI(record)
        }
    }
}
