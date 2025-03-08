package com.example.learnspanish

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.activities.LoginActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)


        //add topic,subtopic(adds new data once)
        //TestActivity().saveSampleData()

        //add task to Firebase update previous data by id
        //TestActivity().loadQuestionsToFirebase()




    }
}