package com.izubcic.gymApp.preferences

interface PreferencesHelper {

    fun getId(): String

    fun removeId()

    fun saveId(userId: String)

    fun userIdExists(): Boolean
}