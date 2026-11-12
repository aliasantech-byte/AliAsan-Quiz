
package com.aliasantech.quiz

import android.app.Activity
import android.os.Bundle
import android.content.Intent
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
        title.textSize = 28f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        val subtitle = TextView(this)
        subtitle.text = "یک رقابت فکری دونفره!"
        subtitle.textSize = 17f
        subtitle.setTextColor(Color.LTGRAY)
        subtitle.gravity = Gravity.CENTER

        val hostButton = Button(this)
        hostButton.text = "ساخت بازی (میزبان)"

        val guestButton = Button(this)
        guestButton.text = "ورود به بازی (مهمان)"

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp(60)
        )
        params.bottomMargin = dp(20)

        root.addView(title)

        val subParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        subParams.topMargin = dp(15)
        subParams.bottomMargin = dp(35)

        root.addView(subtitle, subParams)
        root.addView(hostButton, params)
        root.addView(guestButton, params)

        setContentView(root)

        hostButton.setOnClickListener {
            startActivity(
                Intent(this, HostActivity::class.java)
            )
        }

        guestButton.setOnClickListener {
            startActivity(
                Intent(this, GuestActivity::class.java)
            )
        }
    }
}
