package com.example.learnspanish.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R
import com.example.learnspanish.services.AuthService

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerLink = findViewById<TextView>(R.id.registerLink)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // We check login details
                AuthService.loginUser(email, password, { user ->
                    // Get user data
                    AuthService.getUserData(user.uid, { user ->
                        if (user.level == 0) {
                            Toast.makeText(this, "You have logged in successfully!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainMenuActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "You have logged in successfully!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainMenuActivity::class.java))
                            finish()
                        }
                    }, { errorMessage ->
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    })
                }, { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                })
            } else {
                Toast.makeText(this, "Complete all fields.", Toast.LENGTH_SHORT).show()
            }
        }

        registerLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}