package com.aliasantech.quiz

object GameSession {

    val networkManager = NetworkManager()

    var isHost = false
    var isConnected = false
    var gameStarted = false

    var hostScore = 0
    var guestScore = 0

    var currentQuestion = 0

    fun reset() {
        hostScore = 0
        guestScore = 0
        currentQuestion = 0
        gameStarted = false
        isConnected = false
    }
}
