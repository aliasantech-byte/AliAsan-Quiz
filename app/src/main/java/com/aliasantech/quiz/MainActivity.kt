
package com.aliasantech.quiz

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.gravity = Gravity.CENTER
        root.setPadding(dp(24), dp(24), dp(24), dp(24))
        root.setBackgroundColor(Color.rgb(15, 25, 50))

        val title = TextView(this)
        title.text = "مسابقه هوش علی آسان"
        title.textSize = 27f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        root.addView(title)

        val subtitle = TextView(this)
        subtitle.text = "یک رقابت فکری دونفره!"
        subtitle.textSize = 17f
        subtitle.setTextColor(Color.LTGRAY)
        subtitle.gravity = Gravity.CENTER

        val subtitleParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        subtitleParams.topMargin = dp(15)
        subtitleParams.bottomMargin = dp(35)

        root.addView(subtitle, subtitleParams)

        val hostButton = Button(this)
        hostButton.text = "ساخت بازی (میزبان)"

        val guestButton = Button(this)
        guestButton.text = "ورود به بازی (مهمان)"

        val buttonParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(60)
        )
        buttonParams.bottomMargin = dp(20)

        root.addView(hostButton, buttonParams)
        root.addView(guestButton, buttonParams)

        hostButton.setOnClickListener {
            showMessage("بخش ساخت اتاق به زودی آماده می‌شود")
        }

        guestButton.setOnClickListener {
            showMessage("بخش ورود به اتاق به زودی آماده می‌شود")
        }

        setContentView(root)
    }

    private fun showMessage(message: String) {
        android.widget.Toast.makeText(
            this,
            message,
            android.widget.Toast.LENGTH_SHORT
        ).show()
    }
}
