package com.example.learnspanish.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R

class ResActivity : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private lateinit var tvCoinsEarned: TextView
    private lateinit var btnPlayAgain: Button
    private lateinit var btnMainMenu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activ_result)

        // Initialize UI components
        tvScore = findViewById(R.id.tvScore)
        tvCoinsEarned = findViewById(R.id.tvCoinsEarned)
        btnPlayAgain = findViewById(R.id.btnPlayAgain)
        btnMainMenu = findViewById(R.id.btnMainMenu)

        // Get score from intent
        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 10)

        // Calculate earned coins (10 coins if quiz completed)
        val coinsEarned = if (score > 0) 10 else 0

        // Display score and coins earned
        tvScore.text = "Your Score: $score / $totalQuestions"
        tvCoinsEarned.text = "Coins Earned: $coinsEarned"

        // Play again button
        btnPlayAgain.setOnClickListener {
            finish() // Close this activity and go back
        }

        // Back to main menu button
        btnMainMenu.setOnClickListener {
            val intent = Intent(this, MinigamesActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }
    }
}
