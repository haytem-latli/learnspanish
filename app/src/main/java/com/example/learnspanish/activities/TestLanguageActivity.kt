package com.example.learnspanish.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.learnspanish.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TestLanguageActivity : AppCompatActivity() {

    private lateinit var questionTextView: TextView
    private lateinit var answersRadioGroup: RadioGroup
    private lateinit var nextButton: Button
    private var currentQuestionIndex = 0
    private var correctAnswersCount = 0
    private val questions = listOf(
        Question(
            "Choose the correct translation:\n\"I go to school every day.\"",
            listOf(
                "Voy a la escuela todos los días.",
                "Fui a la escuela todos los días.",
                "Voy a la escuela cada día.",
                "Iré a la escuela todos los días."
            ),
            "Voy a la escuela todos los días."
        ),
        Question(
            "Insert the correct verb form:\nShe ___ (trabajar) en el jardín ahora.",
            listOf("trabaja", "trabajó", "está trabajando", "ha trabajado"),
            "está trabajando"
        ),
        Question(
            "Choose the correct answer:\nHow often do you watch TV?",
            listOf("En el fin de semana.", "Todos los días.", "A las 5 en punto.", "Por la noche."),
            "Todos los días."
        ),
        Question(
            "Complete the sentence:\nIf I were you, I ___.",
            listOf("iré allí.", "iría allí.", "fui allí.", "voy allí."),
            "iría allí."
        ),
        Question(
            "Choose the correct translation:\n\"She is never late to lessons.\"",
            listOf(
                "Ella nunca llega tarde a las clases.",
                "Ella nunca tarde a las clases.",
                "Ella no llega tarde nunca a las clases.",
                "Ella no llegó tarde nunca a las clases."
            ),
            "Ella nunca llega tarde a las clases."
        ),
        Question(
            "Choose the correct modal verb:\nYou ___ wear a helmet while riding a bike.",
            listOf("puedes", "debes", "deberías", "podrías"),
            "debes"
        ),
        Question(
            "Put the words in the correct order:\ncompró / un / ella / ayer / vestido / bonito.",
            listOf(
                "Ella compró ayer un vestido bonito.",
                "Ella vestido bonito compró ayer.",
                "Ella compró un vestido bonito ayer.",
                "Ayer bonito vestido ella compró."
            ),
            "Ella compró un vestido bonito ayer."
        ),
        Question(
            "Identify the tense in the sentence:\nThey have been living here for five years.",
            listOf("Presente Perfecto", "Presente Perfecto Continuo", "Pretérito Simple", "Presente Continuo"),
            "Presente Perfecto Continuo"
        ),
        Question(
            "Fill in the missing word:\nI am looking forward ___ seeing you.",
            listOf("a", "por", "en", "sobre"),
            "a"
        ),
        Question(
            "Choose the correct answer:\nWhat is the capital of the United Kingdom?",
            listOf("París", "Londres", "Dublín", "Edimburgo"),
            "Londres"
        ),
        Question(
            "Choose the correct answer:\nShe ___ her homework before dinner yesterday.",
            listOf("terminó", "ha terminado", "termina", "está terminando"),
            "terminó"
        ),
        Question(
            "Translate the sentence:\n\"This book is more interesting than that one.\"",
            listOf(
                "Este libro es el más interesante que aquel.",
                "Este libro es interesante que aquel.",
                "Este libro es más interesante que aquel.",
                "Este libro es interesante como aquel."
            ),
            "Este libro es más interesante que aquel."
        ),
        Question(
            "Combine the sentences:\nHe is very tired. He worked all day.",
            listOf(
                "Está muy cansado porque ha trabajado todo el día.",
                "Está muy cansado porque está trabajando todo el día.",
                "Está muy cansado porque trabajó todo el día.",
                "Está muy cansado porque trabaja todo el día."
            ),
            "Está muy cansado porque trabajó todo el día."
        ),
        Question(
            "Correct the mistake in the sentence:\nShe don't like coffee.",
            listOf(
                "Ella no le gusta el café.",
                "Ella no gusta café.",
                "Ella no le gustó el café.",
                "Ella no le gusta café."
            ),
            "Ella no le gusta el café."
        ),
        Question(
            "Translate the sentence:\n\"I need more time to think.\"",
            listOf(
                "Necesito más tiempo para pensar.",
                "Estoy necesitando más tiempo para pensar.",
                "He necesitado más tiempo para pensar.",
                "Necesito tiempo más para pensar."
            ),
            "Necesito más tiempo para pensar."
        ),
        Question(
            "Choose the correct variant:\nIf it rains tomorrow, we ___.",
            listOf(
                "nos quedaremos en casa.",
                "quedamos en casa.",
                "nos quedaríamos en casa.",
                "nos hemos quedado en casa."
            ),
            "nos quedaremos en casa."
        ),
        Question(
            "Form a sentence with \"used to\":",
            listOf(
                "Solía jugar al fútbol cuando era niño.",
                "Estoy acostumbrado a jugar al fútbol cuando era niño.",
                "Estaba acostumbrado a jugar al fútbol cuando era niño.",
                "Uso jugar al fútbol cuando era niño."
            ),
            "Solía jugar al fútbol cuando era niño."
        ),
        Question(
            "Choose the correct answer:\nWhat time does the train leave?",
            listOf(
                "El tren sale a las 10:00.",
                "El tren está saliendo a las 10:00.",
                "El tren ha salido a las 10:00.",
                "El tren salió a las 10:00."
            ),
            "El tren sale a las 10:00."
        ),
        Question(
            "Which option is the correct translation of:\n\"I have never seen such beautiful flowers.\"",
            listOf(
                "Nunca veo flores tan bonitas.",
                "Nunca vi flores tan bonitas.",
                "Nunca he visto flores tan bonitas.",
                "Nunca había visto flores tan bonitas."
            ),
            "Nunca he visto flores tan bonitas."
        ),
        Question(
            "Complete the sentence:\nBy the time we arrived, they ___ dinner.",
            listOf("han terminado", "terminaron", "habían terminado", "están terminando"),
            "habían terminado"
        )
    )


    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_level_test)

        questionTextView = findViewById(R.id.questionTextView)
        answersRadioGroup = findViewById(R.id.answersRadioGroup)
        nextButton = findViewById(R.id.nextButton)

        auth = FirebaseAuth.getInstance()

        loadQuestion()
        setupNextButton()
    }

    private fun loadQuestion() {
        val currentQuestion = questions[currentQuestionIndex]
        questionTextView.text = currentQuestion.question
        answersRadioGroup.removeAllViews()
        currentQuestion.answers.forEach { answer ->
            val radioButton = RadioButton(this)
            radioButton.text = answer
            answersRadioGroup.addView(radioButton)
        }
    }

    private fun setupNextButton() {

        nextButton.isEnabled = false


        answersRadioGroup.setOnCheckedChangeListener { _, _ ->

            nextButton.isEnabled = answersRadioGroup.checkedRadioButtonId != -1
        }


        nextButton.setOnClickListener {
            val selectedAnswer = getSelectedAnswer()
            if (isCorrectAnswer(selectedAnswer)) {
                correctAnswersCount++
            }
            currentQuestionIndex++
            if (currentQuestionIndex < questions.size) {
                loadQuestion()
            } else {
                showTestResult()
            }


            nextButton.isEnabled = false
        }
    }

    private fun getSelectedAnswer(): String {
        val selectedId = answersRadioGroup.checkedRadioButtonId
        if (selectedId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedId)
            return selectedRadioButton.text.toString()
        }
        return ""
    }

    private fun isCorrectAnswer(answer: String): Boolean {
        val currentQuestion = questions[currentQuestionIndex]
        return answer == currentQuestion.correctAnswer
    }

    private fun showTestResult() {
        val totalQuestions = questions.size
        val percentage = (correctAnswersCount.toDouble() / totalQuestions) * 100
        val level = when {
            percentage >= 80 -> 4 // B2
            percentage >= 60 -> 3 // B1
            percentage >= 40 -> 2 // A2
            else -> 1 // A1
        }

        val levelName = when (level) {
            1 -> "A1"
            2 -> "A2"
            3 -> "B1"
            4 -> "B2"
            else -> "A1"
        }

        AlertDialog.Builder(this)
            .setTitle("Test result")
            .setMessage("Your level is: $levelName\nCorrect answers: $correctAnswersCount/$totalQuestions")
            .setPositiveButton("OK") { _, _ ->
                updateUserLevel(level, levelName)
            }
            .setCancelable(false)
            .show()
    }

    private fun updateUserLevel(level: Int, levelName: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            database.child("users").child(userId).child("level").setValue(level)
                .addOnSuccessListener {
                    Toast.makeText(this, "Your level has been set to $levelName", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainMenuActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving level: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

data class Question(
    val question: String,
    val answers: List<String>,
    val correctAnswer: String
)