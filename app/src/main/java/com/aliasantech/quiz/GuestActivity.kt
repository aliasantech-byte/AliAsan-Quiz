
package com.aliasantech.quiz

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.EditText

class GuestActivity : Activity() {

    private lateinit var networkManager: NetworkManager
    private lateinit var statusText: TextView
    private lateinit var ipInput: EditText

    private fun dp(value: Int): Int {
        return (value * resources.displayMetrics.density).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkManager = NetworkManager()

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(dp(25), dp(25), dp(25), dp(25))
        layout.setBackgroundColor(Color.rgb(15, 25, 50))

        val title = TextView(this)
        title.text = "ورود به بازی"
        title.textSize = 26f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        ipInput = EditText(this)
        ipInput.hint = "آدرس IP میزبان را وارد کنید"
        ipInput.textSize = 17f
        ipInput.setSingleLine(true)
        ipInput.inputType = 0x00001001
        ipInput.setTextColor(Color.WHITE)
        ipInput.setHintTextColor(Color.LTGRAY)

        val connectButton = Button(this)
        connectButton.text = "اتصال به میزبان"

        statusText = TextView(this)
        statusText.text = "هنوز متصل نشده‌اید"
        statusText.textSize = 18f
        statusText.setTextColor(Color.YELLOW)
        statusText.gravity = Gravity.CENTER

        val sendButton = Button(this)
        sendButton.text = "ارسال پیام آزمایشی"
        sendButton.isEnabled = false

        layout.addView(title)

        layout.addView(
            ipInput,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(60)
            )
        )

        layout.addView(connectButton)
        layout.addView(statusText)
        layout.addView(sendButton)

        setContentView(layout)

        connectButton.setOnClickListener {
            val ip = ipInput.text.toString().trim()

            if (ip.isEmpty()) {
                ipInput.error = "آدرس IP را وارد کنید"
                return@setOnClickListener
            }

            connectButton.isEnabled = false
            statusText.text = "در حال اتصال..."

            networkManager.connectGuest(
                ip,
                onStatus = { message ->
                    statusText.text = message

                    if (message == "دو گوشی متصل شدند!") {
                        sendButton.isEnabled = true
                    } else {
                        connectButton.isEnabled = true
                    }
                },
                onMessage = { message ->
                    statusText.text = "پیام میزبان: $message"
                }
            )
        }

        sendButton.setOnClickListener {
            networkManager.sendMessage("سلام از بازیکن دوم!")
        }
    }

    override fun onDestroy() {
        networkManager.close()
        super.onDestroy()
    }
}
