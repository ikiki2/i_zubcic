package com.izubcic.gymApp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.izubcic.gymApp.commons.constants.PREFS_NAME

class App : Application() {

    companion object {
        internal lateinit var database: DatabaseReference
        internal lateinit var auth: FirebaseAuth
        internal lateinit var prefs: SharedPreferences
        internal lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        prefs = this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        context = this.baseContext
    }
}