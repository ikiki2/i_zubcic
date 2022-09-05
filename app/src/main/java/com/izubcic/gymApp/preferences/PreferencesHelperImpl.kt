package com.izubcic.gymApp.preferences

import android.content.SharedPreferences
import com.izubcic.gymApp.commons.constants.PREFS_USER_ID_KEY

class PreferencesHelperImpl constructor(private val preferences: SharedPreferences) :
    PreferencesHelper {

    override fun getId(): String = preferences.getString(PREFS_USER_ID_KEY, "").toString()

    override fun removeId() = preferences.edit().clear().apply()

    override fun saveId(userId: String) =
        preferences.edit().putString(PREFS_USER_ID_KEY, userId).apply()

    override fun userIdExists(): Boolean = getId().isNotBlank()
}