package com.example.learnspanish.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class AccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val database = Firebase.database.reference
    private var currentUsername: String = ""
    private var currentEmail: String = ""
    private lateinit var usernameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        auth = Firebase.auth
        val userId = auth.currentUser?.uid
        usernameTextView = findViewById(R.id.usernameTextView)

        if (userId != null) {
            database.child("users").child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    currentUsername = snapshot.child("username").getValue(String::class.java) ?: ""
                    currentEmail = snapshot.child("email").getValue(String::class.java) ?: auth.currentUser?.email ?: ""
                    usernameTextView.text = currentUsername
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AccountActivity, "Failed to load user data.", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val changeUsernameButton = findViewById<Button>(R.id.changeUsernameButton)
        val changeEmailButton = findViewById<Button>(R.id.changeEmailButton)
        val changePasswordButton = findViewById<Button>(R.id.changePasswordButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val closeButton = findViewById<ImageView>(R.id.closeButton)

        changeUsernameButton.setOnClickListener { showChangeUsernameDialog() }
        changeEmailButton.setOnClickListener { showChangeEmailDialog() }
        changePasswordButton.setOnClickListener { showChangePasswordDialog() }
        logoutButton.setOnClickListener { logoutUser() }
        closeButton.setOnClickListener {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.changeLevelButton).setOnClickListener {
            startActivity(Intent(this, LanguageLevelActivity::class.java))
        }
    }

    private fun showChangeUsernameDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_username, null)
        val editText = dialogView.findViewById<EditText>(R.id.usernameEditText)
        editText.setText(currentUsername)

        AlertDialog.Builder(this)
            .setTitle("Change username")
            .setView(dialogView)
            .setPositiveButton("change") { _, _ ->
                val newUsername = editText.text.toString().trim()
                if (newUsername.isNotEmpty()) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        database.child("users").child(userId).child("username").setValue(newUsername)
                            .addOnSuccessListener {
                                currentUsername = newUsername
                                usernameTextView.text = newUsername
                                Toast.makeText(this, "Username has been changed!", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error changing username.", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "The field cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showChangeEmailDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_email, null)
        val editText = dialogView.findViewById<EditText>(R.id.emailEditText)

        editText.setText(currentEmail)

        AlertDialog.Builder(this)
            .setTitle("Change email")
            .setView(dialogView)
            .setPositiveButton("Change") { _, _ ->
                val newEmail = editText.text.toString().trim()
                if (newEmail.isNotEmpty()) {
                    auth.currentUser?.updateEmail(newEmail)
                        ?.addOnSuccessListener {
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                database.child("users").child(userId).child("email").setValue(newEmail)
                            }
                            currentEmail = newEmail
                            Toast.makeText(this, "Email has been changed!", Toast.LENGTH_SHORT).show()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(this, "Email change error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "The field cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun showChangePasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val editText = dialogView.findViewById<EditText>(R.id.passwordEditText)

        AlertDialog.Builder(this)
            .setTitle("Change password")
            .setView(dialogView)
            .setPositiveButton("Change") { _, _ ->
                val newPassword = editText.text.toString()
                if (newPassword.length >= 8) {
                    auth.currentUser?.updatePassword(newPassword)
                        ?.addOnSuccessListener {
                            Toast.makeText(this, "Password has been changed!", Toast.LENGTH_SHORT).show()
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(this, "Password change error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    Toast.makeText(this, "Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }

    private fun logoutUser() {
        auth.signOut()
        Toast.makeText(this, "You were successfully logged out.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}
