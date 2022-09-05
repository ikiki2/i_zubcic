package com.izubcic.gymApp.ui.splash

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.izubcic.gymApp.App
import com.izubcic.gymApp.preferences.PreferencesHelperImpl
import com.izubcic.gymApp.ui.signIn.SignInActivity
import com.izubcic.gymApp.ui.trainings.GymsActivity

class SplashActivity : Activity(), SplashView {


    private val prefs: PreferencesHelperImpl by lazy {
        PreferencesHelperImpl(App.prefs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (prefs.userIdExists())
            this.startApp()
        else
            this.startSignIn()
    }


    override fun startApp() {
        startActivity(Intent(this, GymsActivity::class.java))
        finish()
    }

    override fun startSignIn() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}