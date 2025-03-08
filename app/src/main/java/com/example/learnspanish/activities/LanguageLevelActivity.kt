package com.example.learnspanish.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LanguageLevelActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_level)

        auth = FirebaseAuth.getInstance()

        findViewById<Button>(R.id.levelA1Button).setOnClickListener { updateLevel(1, "A1") }
        findViewById<Button>(R.id.levelA2Button).setOnClickListener { updateLevel(2, "A2") }
        findViewById<Button>(R.id.levelB1Button).setOnClickListener { updateLevel(3, "B1") }
        findViewById<Button>(R.id.levelB2Button).setOnClickListener { updateLevel(4, "B2") }

        findViewById<Button>(R.id.testButton).setOnClickListener {
            startActivity(Intent(this, TestLanguageActivity::class.java))
            finish()
        }

        val backButton = findViewById<Button>(R.id.backButton)
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).child("level").get()
                .addOnSuccessListener { snapshot ->
                    val userLevel = snapshot.getValue(Int::class.java) ?: 0
                    if (userLevel > 0) {
                        backButton.setOnClickListener { finish() }
                    } else {
                        backButton.isEnabled = false
                        backButton.visibility = Button.GONE
                    }
                }
        }
    }

    private fun updateLevel(level: Int, levelName: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).child("level").setValue(level)
                .addOnSuccessListener {
                    Toast.makeText(this, "Level changed to $levelName", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainMenuActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error while changing level: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
