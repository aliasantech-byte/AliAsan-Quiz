package com.aliasantech.quiz

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class GameActivity : Activity() {

    private var questionIndex = 0
    private var score = 0
    private var answered = false

    private lateinit var questionText: TextView
    private lateinit var scoreText: TextView
    private lateinit var numberText: TextView
    private lateinit var optionsLayout: LinearLayout

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.gravity = Gravity.CENTER
        root.setPadding(dp(20), dp(20), dp(20), dp(20))
        root.setBackgroundColor(Color.rgb(15, 25, 50))

        numberText = TextView(this)
        numberText.textSize = 20f
        numberText.setTextColor(Color.YELLOW)
        numberText.gravity = Gravity.CENTER

        questionText = TextView(this)
        questionText.textSize = 22f
        questionText.setTextColor(Color.WHITE)
        questionText.gravity = Gravity.CENTER

        scoreText = TextView(this)
        scoreText.textSize = 19f
        scoreText.setTextColor(Color.GREEN)
        scoreText.gravity = Gravity.CENTER

        optionsLayout = LinearLayout(this)
        optionsLayout.orientation = LinearLayout.VERTICAL

        root.addView(numberText)
        root.addView(questionText)

        root.addView(
            scoreText,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(60)
            )
        )

        root.addView(optionsLayout)

        setContentView(root)

        showQuestion()
    }

    private fun showQuestion() {

        if (questionIndex >= QuestionBank.questions.size) {
            showResult()
            return
        }

        answered = false

        val question = QuestionBank.questions[questionIndex]

        numberText.text =
            "سؤال ${questionIndex + 1} از ۲۰"

        questionText.text = question.text

        scoreText.text = "امتیاز شما: $score"

        optionsLayout.removeAllViews()

        for (i in question.options.indices) {

            val button = Button(this)

            button.text = question.options[i]
            button.textSize = 17f

            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(60)
            )

            params.bottomMargin = dp(10)

            optionsLayout.addView(button, params)

            button.setOnClickListener {

                if (answered) return@setOnClickListener

                answered = true

                if (i == question.correctAnswer) {
                    score += 10
                    button.setTextColor(Color.GREEN)
                } else {
                    button.setTextColor(Color.RED)
                }

                scoreText.text = "امتیاز شما: $score"

                optionsLayout.postDelayed({

                    questionIndex++
                    showQuestion()

                }, 1000)
            }
        }
    }

    private fun showResult() {

        optionsLayout.removeAllViews()

        numberText.text = "پایان مسابقه"

        questionText.text =
            "آفرین! مسابقه تمام شد."

        scoreText.text =
            "امتیاز نهایی شما: $score از ۲۰۰"

    }
}
