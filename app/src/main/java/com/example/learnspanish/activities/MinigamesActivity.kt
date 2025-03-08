package com.example.learnspanish.activities

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R

class MinigamesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activ_main)

        val listView: ListView = findViewById(R.id.listViewMinigames)

        // List of quizzes available
        val quizzes = listOf("Basic Spanish Quiz", "Food & Drinks Quiz", "Travel Spanish Quiz")

        // Adapter to display the quizzes in a list
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, quizzes)
        listView.adapter = adapter

        // Handle item clicks
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedQuiz = quizzes[position]

            // Start QuizActivity and pass the quiz title
            val intent = Intent(this, QuizActivity::class.java)
            intent.putExtra("quizTitle", selectedQuiz)
            startActivity(intent)
        }
    }
}
