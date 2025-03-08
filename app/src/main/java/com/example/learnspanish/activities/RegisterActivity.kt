package com.example.learnspanish.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R
import com.example.learnspanish.services.AuthService

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.confirmPasswordEditText)
        val usernameEditText = findViewById<EditText>(R.id.nameEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginLink = findViewById<TextView>(R.id.loginLink)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()
            val username = usernameEditText.text.toString().trim()


            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty()) {
                Toast.makeText(this, "All fields must be completed.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (password != confirmPassword) {
                Toast.makeText(this, "The passwords do not match.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (password.length < 8) {
                Toast.makeText(this, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            AuthService.registerUser(email, password, username, {
                Toast.makeText(this, "Registration completed successfully!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }, { errorMessage ->
                Log.d("tag","here")
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            })
        }

        loginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
