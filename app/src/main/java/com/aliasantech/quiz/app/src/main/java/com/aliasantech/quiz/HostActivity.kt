
package com.aliasantech.quiz

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import java.net.NetworkInterface

class HostActivity : Activity() {

    private lateinit var networkManager: NetworkManager
    private lateinit var statusText: TextView
    private lateinit var ipText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkManager = NetworkManager()

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.gravity = Gravity.CENTER
        layout.setPadding(30, 30, 30, 30)
        layout.setBackgroundColor(Color.rgb(15, 25, 50))

        val title = TextView(this)
        title.text = "ساخت اتاق بازی"
        title.textSize = 25f
        title.setTextColor(Color.WHITE)
        title.gravity = Gravity.CENTER

        ipText = TextView(this)
        ipText.text = "آدرس IP میزبان:\n${getLocalIp()}"
        ipText.textSize = 19f
        ipText.setTextColor(Color.YELLOW)
        ipText.gravity = Gravity.CENTER

        statusText = TextView(this)
        statusText.text = "در انتظار اتصال مهمان..."
        statusText.textSize = 18f
        statusText.setTextColor(Color.WHITE)
        statusText.gravity = Gravity.CENTER

        val startButton = Button(this)
        startButton.text = "آماده‌سازی اتاق"

        layout.addView(title)
        layout.addView(ipText)
        layout.addView(statusText)
        layout.addView(startButton)

        setContentView(layout)

        startButton.setOnClickListener {
            startButton.isEnabled = false

            networkManager.startHost(
                onStatus = { message ->
                    statusText.text = message
                },
                onMessage = { message ->
                    statusText.text = "پیام مهمان: $message"
                }
            )
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
