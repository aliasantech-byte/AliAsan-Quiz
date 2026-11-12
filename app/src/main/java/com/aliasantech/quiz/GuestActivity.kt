l
package com.aliasantech.quiz

import android.app.Activity
import android.os.Bundle
import android.content.Intent
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
    private lateinit var connectButton: Button

    private var connected = false

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
        title.text = "ورود به مسابقه"
        title.textSize = 27f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        ipInput = EditText(this)
        ipInput.hint = "IP میزبان"
        ipInput.setSingleLine(true)
        ipInput.inputType = 0x00001001
        ipInput.setTextColor(Color.WHITE)
        ipInput.setHintTextColor(Color.LTGRAY)

        connectButton = Button(this)
        connectButton.text = "اتصال به میزبان"

        statusText = TextView(this)
        statusText.text = "منتظر اتصال..."
        statusText.textSize = 18f
        statusText.setTextColor(Color.YELLOW)
        statusText.gravity = Gravity.CENTER

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

        setContentView(layout)

        connectButton.setOnClickListener {

            val ip = ipInput.text.toString().trim()

            if (ip.isEmpty()) {
                ipInput.error = "IP را وارد کنید"
                return@setOnClickListener
            }

            connectButton.isEnabled = false
            statusText.text = "در حال اتصال..."

            networkManager.connectGuest(
                ip,
                onStatus = { message ->

                    statusText.text = message

                    if (message == "دو گوشی متصل شدند!") {
                        connected = true

                        statusText.text =
                            "متصل شدید! منتظر شروع میزبان باشید."

                        networkManager.sendMessage(
                            GameProtocol.READY
                        )
                    } else {
                        connectButton.isEnabled = true
                    }
                },
                onMessage = { message ->

                    if (message == GameProtocol.START) {

                        GameSession.isHost = false
                        GameSession.isConnected = true
                        GameSession.gameStarted = true

                        startActivity(
                            Intent(this, GameActivity::class.java)
                        )

                    } else {
                        statusText.text = "پیام میزبان: $message"
                    }
                }
            )
        }
    }

    override fun onDestroy() {
        if (!connected || isFinishing) {
            networkManager.close()
        }
        super.onDestroy()
    }
}
