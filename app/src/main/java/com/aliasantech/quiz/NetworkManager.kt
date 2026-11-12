
package com.aliasantech.quiz

import android.os.Handler
import android.os.Looper
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket
import java.net.Socket
import java.net.InetSocketAddress

class NetworkManager {

    companion object {
        const val PORT = 45454
    }

    private var serverSocket: ServerSocket? = null
    private var socket: Socket? = null
    private var writer: PrintWriter? = null

    private val handler = Handler(Looper.getMainLooper())

    @Volatile
    private var running = false

    fun startHost(
        onStatus: (String) -> Unit,
        onMessage: (String) -> Unit
    ) {
        Thread {
            try {
                serverSocket = ServerSocket(PORT)
                running = true

                handler.post {
                    onStatus("منتظر اتصال بازیکن دوم...")
                }

                socket = serverSocket!!.accept()

                setupConnection(socket!!, onStatus, onMessage)

            } catch (e: Exception) {
                handler.post {
                    onStatus("خطا در ایجاد اتاق: ${e.message}")
                }
            }
        }.start()
    }

    fun connectGuest(
        ip: String,
        onStatus: (String) -> Unit,
        onMessage: (String) -> Unit
    ) {
        Thread {
            try {
                val newSocket = Socket()
                newSocket.connect(
                    InetSocketAddress(ip, PORT),
                    10000
                )

                socket = newSocket
                running = true

                setupConnection(
                    newSocket,
                    onStatus,
                    onMessage
                )

            } catch (e: Exception) {
                handler.post {
                    onStatus("اتصال ناموفق: ${e.message}")
                }
            }
        }.start()
    }

    private fun setupConnection(
        connection: Socket,
        onStatus: (String) -> Unit,
        onMessage: (String) -> Unit
    ) {
        writer = PrintWriter(
            connection.getOutputStream(),
            true
        )

        handler.post {
            onStatus("دو گوشی متصل شدند!")
        }

        Thread {
            try {
                val reader = BufferedReader(
                    InputStreamReader(
                        connection.getInputStream(),
                        "UTF-8"
                    )
                )

                while (running) {
                    val message = reader.readLine() ?: break

                    handler.post {
                        onMessage(message)
                    }
                }

            } catch (e: Exception) {
                // Connection closed
            }
        }.start()
    }

    fun sendMessage(message: String) {
        Thread {
            try {
                synchronized(this) {
                    writer?.println(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    fun close() {
        running = false

        try {
            socket?.close()
            serverSocket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        socket = null
        serverSocket = null
        writer = null
    }
}
