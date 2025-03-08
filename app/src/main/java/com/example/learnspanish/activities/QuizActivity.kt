package com.example.learnspanish.activities

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R
import com.example.learnspanish.models.Question

class QuizActivity : AppCompatActivity() {

    private lateinit var tvQuestionNumber: TextView
    private lateinit var tvQuestion: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var rbOption1: RadioButton
    private lateinit var rbOption2: RadioButton
    private lateinit var rbOption3: RadioButton
    private lateinit var rbOption4: RadioButton
    private lateinit var btnSubmit: Button

    private var currentQuestionIndex = 0
    private var score = 0

    // Define the quiz directly in the activity
    private val questions = listOf(
        Question("What is the Spanish word for 'apple'?", listOf("Manzana", "Pera", "Naranja", "Uva"), 0),
        Question("How do you say 'Hello' in Spanish?", listOf("Hola", "Adiós", "Gracias", "Por favor"), 0),
        Question("What is 'dog' in Spanish?", listOf("Gato", "Perro", "Caballo", "Pez"), 1),
        Question("Translate 'Thank you' to Spanish.", listOf("Hola", "Perdón", "Gracias", "Sí"), 2),
        Question("What is 'blue' in Spanish?", listOf("Rojo", "Verde", "Azul", "Negro"), 2)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize UI components
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber)
        tvQuestion = findViewById(R.id.tvQuestion)
        radioGroup = findViewById(R.id.radioGroup)
        rbOption1 = findViewById(R.id.rbOption1)
        rbOption2 = findViewById(R.id.rbOption2)
        rbOption3 = findViewById(R.id.rbOption3)
        rbOption4 = findViewById(R.id.rbOption4)
        btnSubmit = findViewById(R.id.btnSubmit)

        // Display first question
        displayQuestion()

        // Handle submit button
        btnSubmit.setOnClickListener {
            checkAnswer()
        }
    }

    private fun displayQuestion() {
        if (currentQuestionIndex >= questions.size) {
            goToResult()
            return
        }

        val question = questions[currentQuestionIndex]
        tvQuestionNumber.text = "Question ${currentQuestionIndex + 1}/${questions.size}"
        tvQuestion.text = question.questionText
        rbOption1.text = question.choices[0]
        rbOption2.text = question.choices[1]
        rbOption3.text = question.choices[2]
        rbOption4.text = question.choices[3]
        radioGroup.clearCheck()
    }

    private fun checkAnswer() {
        val selectedOption = when (radioGroup.checkedRadioButtonId) {
            R.id.rbOption1 -> 0
            R.id.rbOption2 -> 1
            R.id.rbOption3 -> 2
            R.id.rbOption4 -> 3
            else -> -1
        }

        if (selectedOption == -1) return // No option selected

        if (selectedOption == questions[currentQuestionIndex].correctAnswer) {
            score++
        }

        currentQuestionIndex++
        displayQuestion()
    }

    private fun goToResult() {
        val intent = Intent(this, ResActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("totalQuestions", questions.size)
        startActivity(intent)
        finish()
    }
}


