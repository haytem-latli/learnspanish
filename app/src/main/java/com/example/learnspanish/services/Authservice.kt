package com.example.learnspanish.services

import android.util.Log
import com.example.learnspanish.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

object AuthService {


    fun registerUser(email: String, password: String, username: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {

        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            onFailure("Please complete all fields.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onFailure("The email address provided is invalid.")
            return
        }


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val database = FirebaseDatabase.getInstance()


                    val userData = User(
                        uid = user?.uid ?: "",
                        username = username,
                        email = email,
                        level = 4,
                        xp = 0,
                        coins = 0,
                        purchasedItems = emptyList(),
                        topicsProgress = mapOf()
                    )

                    user?.uid?.let { uid ->

                        database.reference.child("users").child(uid).setValue(userData)
                            .addOnSuccessListener {
                                Log.d("AuthService", "User data saved successfully: $userData")
                                onSuccess()
                            }
                            .addOnFailureListener { exception ->
                                Log.e("AuthService", "Error saving user data: ${exception.message}")
                                onFailure("Error saving user data: ${exception.message}")
                            }
                    }
                } else {

                    if (task.exception is FirebaseAuthUserCollisionException) {
                        onFailure("A user with this email address already exists.")
                    } else if (task.exception is FirebaseAuthWeakPasswordException) {
                        onFailure("Password is too weak. At least 8 characters are required.")
                    } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        onFailure("Invalid email address format.")
                    } else {
                        onFailure("Registration error: ${task.exception?.message}")
                    }
                }
            }
    }
    fun updateUser(uid: String, updatedUser: User, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(uid)


        userRef.setValue(updatedUser)
            .addOnSuccessListener {
                Log.d("AuthService", "User data updated successfully: $updatedUser")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("AuthService", "Error updating user data: ${exception.message}")
                onFailure("Error updating user data: ${exception.message}")
            }
    }



    fun loginUser(email: String, password: String, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {

        if (email.isEmpty() || password.isEmpty()) {
            onFailure("Please complete all fields.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onFailure("The email address provided is invalid.")
            return
        }


        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = FirebaseAuth.getInstance().currentUser
                    val database = FirebaseDatabase.getInstance()

                    user?.uid?.let { uid ->

                        database.reference.child("users").child(uid).get()
                            .addOnSuccessListener { snapshot ->
                                if (snapshot.exists()) {

                                    val userData = snapshot.getValue(User::class.java)


                                    if (userData != null) {

                                        onSuccess(userData)
                                    } else {
                                        onFailure("Error retrieving user data.")
                                    }
                                } else {
                                    onFailure("The user does not exist in the database.")
                                }
                            }
                            .addOnFailureListener { exception ->
                                onFailure("User verification error: ${exception.message}")
                            }
                    }
                } else {

                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        onFailure("Incorrect email address or password.")
                    } else if (task.exception is FirebaseAuthUserCollisionException) {
                        onFailure("A user with this email address already exists.")
                    } else {
                        onFailure("Login error: ${task.exception?.message}")
                    }
                }
            }
    }


    fun getUserData(uid: String, onSuccess: (User) -> Unit, onFailure: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(uid)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        onSuccess(user)
                    } else {
                        onFailure("Could not retrieve user data.")
                    }
                } else {
                    onFailure("User does not exist.")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onFailure("Error getting user data:${error.message}")
            }
        })
    }
    fun updateUserField(uid: String, fieldName: String, newValue: Any, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(uid)

        val updateData = mapOf(fieldName to newValue)
        userRef.updateChildren(updateData)
            .addOnSuccessListener {
                Log.d("AuthService", "User data updated successfully: $fieldName")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("AuthService", "Error updating user data: ${exception.message}")
                onFailure("Error updating user data:${exception.message}")
            }
    }
    fun updateUserField(uid: String, fieldName: String, newValue: Int, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(uid)

        val updateData = mapOf(fieldName to newValue)
        userRef.updateChildren(updateData)
            .addOnSuccessListener {
                Log.d("AuthService", "User data updated successfully: $fieldName")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("AuthService", "Error updating user data: ${exception.message}")
                onFailure("Error updating user data: ${exception.message}")
            }
    }

    fun updateUserField(uid: String, fieldName: String, newValue: List<String>, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(uid)

        val updateData = mapOf(fieldName to newValue)
        userRef.updateChildren(updateData)
            .addOnSuccessListener {
                Log.d("AuthService", "User data updated successfully: $fieldName")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("AuthService", "Error updating user data: ${exception.message}")
                onFailure("Error updating user data: ${exception.message}")
            }
    }
    fun updateUserField(uid: String, fieldName: String, newValue: Map<String, Int>, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(uid)

        val updateData = mapOf(fieldName to newValue)
        userRef.updateChildren(updateData)
            .addOnSuccessListener {
                Log.d("AuthService", "User data updated successfully: $fieldName")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("AuthService", "Error updating user data: ${exception.message}")
                onFailure("Error updating user data: ${exception.message}")
            }
    }
    fun updateUserField(uid: String, fieldName: String, newValue: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(uid)

        val updateData = mapOf(fieldName to newValue)
        userRef.updateChildren(updateData)
            .addOnSuccessListener {
                Log.d("AuthService", "User data updated successfully:$fieldName")
                onSuccess()
            }
            .addOnFailureListener { exception ->
                Log.e("AuthService", "Error updating user data: ${exception.message}")
                onFailure("Error updating user data: ${exception.message}")
            }
    }
}