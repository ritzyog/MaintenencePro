package com.example.finalproject1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val yearEditText: TextInputEditText = findViewById(R.id.yearTextBox)
        val makeEditText: TextInputEditText = findViewById(R.id.makeTextBox)
        val modelEditText: TextInputEditText = findViewById(R.id.modelTextBox)
        val createButton: Button = findViewById(R.id.createButton)
        val backButton: Button = findViewById(R.id.back1)

        createButton.setOnClickListener {
            val year = yearEditText.text.toString()
            val make = makeEditText.text.toString()
            val model = modelEditText.text.toString()

            val resultIntent = Intent()
            resultIntent.putExtra("YEAR", year)
            resultIntent.putExtra("MAKE", make)
            resultIntent.putExtra("MODEL", model)
            
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}
