
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
import java.net.NetworkInterface

class HostActivity : Activity() {

    private lateinit var networkManager: NetworkManager
    private lateinit var statusText: TextView
    private lateinit var startButton: Button

    private var guestConnected = false

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
        title.text = "اتاق میزبان"
        title.textSize = 27f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        val ipText = TextView(this)
        ipText.text = "IP میزبان:\n${getLocalIp()}"
        ipText.textSize = 20f
        ipText.setTextColor(Color.YELLOW)
        ipText.gravity = Gravity.CENTER

        statusText = TextView(this)
        statusText.text = "در انتظار بازیکن دوم..."
        statusText.textSize = 18f
        statusText.setTextColor(Color.WHITE)
        statusText.gravity = Gravity.CENTER

        val connectButton = Button(this)
        connectButton.text = "ایجاد اتاق"

        startButton = Button(this)
        startButton.text = "شروع مسابقه"
        startButton.isEnabled = false

        layout.addView(title)
        layout.addView(ipText)
        layout.addView(statusText)
        layout.addView(connectButton)
        layout.addView(startButton)

        setContentView(layout)

        connectButton.setOnClickListener {

            connectButton.isEnabled = false
            statusText.text = "در حال ایجاد اتاق..."

            networkManager.startHost(
                onStatus = { message ->
                    statusText.text = message

                    if (message == "دو گوشی متصل شدند!") {
                        guestConnected = true
                        startButton.isEnabled = true
                    }
                },
                onMessage = { message ->

                    if (message == GameProtocol.READY) {
                        statusText.text = "بازیکن دوم آماده است!"
                    }
                }
            )
        }

        startButton.setOnClickListener {

            if (guestConnected) {

                networkManager.sendMessage(
                    GameProtocol.START
                )

                startActivity(
                    Intent(this, GameActivity::class.java)
                )
            }
        }
    }

    private fun getLocalIp(): String {

        try {
            val interfaces = NetworkInterface.getNetworkInterfaces()

            while (interfaces.hasMoreElements()) {

                val network = interfaces.nextElement()
                val addresses = network.inetAddresses

                while (addresses.hasMoreElements()) {

                    val address = addresses.nextElement()

                    if (!address.isLoopbackAddress &&
                        address.hostAddress?.contains(":") == false) {

                        return address.hostAddress ?: "نامشخص"
                    }
                }
            }

        } catch (e: Exception) {
            return "نامشخص"
        }

        return "نامشخص"
    }

    override fun onDestroy() {
        networkManager.close()
        super.onDestroy()
    }
}
