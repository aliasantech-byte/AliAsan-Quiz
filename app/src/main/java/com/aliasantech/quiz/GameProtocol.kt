
package com.aliasantech.quiz

object GameProtocol {

    const val READY = "READY"
    const val START = "START"
    const val ANSWER = "ANSWER"
    const val RESULT = "RESULT"
    const val NEXT = "NEXT"
    const val FINISH = "FINISH"
    const val DISCONNECT = "DISCONNECT"

    fun startMessage(): String {
        return START
    }

    fun readyMessage(): String {
        return READY
    }

    fun answerMessage(
        questionIndex: Int,
        answerIndex: Int
    ): String {
        return "$ANSWER|$questionIndex|$answerIndex"
    }

    fun resultMessage(
        questionIndex: Int,
        hostScore: Int,
        guestScore: Int
    ): String {
        return "$RESULT|$questionIndex|$hostScore|$guestScore"
    }

    fun nextMessage(questionIndex: Int): String {
        return "$NEXT|$questionIndex"
    }

    fun finishMessage(
        hostScore: Int,
        guestScore: Int
    ): String {
        return "$FINISH|$hostScore|$guestScore"
    }
}
