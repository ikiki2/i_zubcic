package com.izubcic.gymApp.ui.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.izubcic.gymApp.App
import com.izubcic.gymApp.R
import com.izubcic.gymApp.commons.constants.*
import com.izubcic.gymApp.commons.extensions.hide
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.commons.extensions.show
import com.izubcic.gymApp.commons.utils.checkEmailEmpty
import com.izubcic.gymApp.commons.utils.checkNameEmpty
import com.izubcic.gymApp.commons.utils.checkPasswordEmpty
import com.izubcic.gymApp.commons.utils.isValidEmail
import com.izubcic.gymApp.firebase.RegisterRequestListener
import com.izubcic.gymApp.firebase.authentication.AuthenticationHelperImpl
import com.izubcic.gymApp.firebase.database.DatabaseHelperImpl
import com.izubcic.gymApp.preferences.PreferencesHelperImpl
import com.izubcic.gymApp.ui.signIn.SignInActivity
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity(), RegistrationView, RegisterRequestListener {

    private val auth: AuthenticationHelperImpl by lazy {
        AuthenticationHelperImpl(
            App.auth,
            DatabaseHelperImpl(App.database),
            PreferencesHelperImpl(App.prefs)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        initListeners()
    }

    private fun initListeners() {
        registrationBtn.onClick {
            this.onRegistrationClick(
                email.text.toString(), password.text.toString(), name.text.toString()
            )
        }
    }

    // logic functions
    private fun onRegistrationClick(email: String, password: String, name: String) {
        this.showProgressAndHideOther()
        if (!email.isEmpty() && !password.isEmpty() && password.length > 5)
            tryToRegister(email, password, name)
        else
            this.hideProgressAndShowOther()
        chechInputEmpty(email, password, name)
    }

    private fun chechInputEmpty(email: String, password: String, name: String) {
        if (checkEmailEmpty(email.trim()) || !isValidEmail(email.trim())) this.showEmailError() else this.hideEmailError()

        if (checkPasswordEmpty(password.trim()) || password.trim().length < 6) this.showPasswordError() else this.hidePasswordError()

        if (checkNameEmpty(name.trim())) this.showNameError() else this.hideNameError()
    }

    private fun tryToRegister(email: String, password: String, name: String) {
        auth.attemptToRegisterTheUser(email, password, name, this)
    }

    override fun onSuccessfulRequest() {
        this.hideProgressAndShowOther()
        this.showMessage(SUCCESS_REGISTRATION)
        this.startSignIn()
    }

    override fun onFailedRequest() {
        this.hideProgressAndShowOther()
        this.showMessage(FAILED_REGISRATION)
    }

    // view functions
    override fun showProgressAndHideOther() {
        progress.show()
        layoutWithoutImage.hide()
    }

    override fun hideProgressAndShowOther() {
        progress.hide()
        layoutWithoutImage.show()
    }

    override fun showEmailError() {
        layoutEmail.error = EMAIL_ERROR
    }

    override fun hideEmailError() {
        layoutEmail.isErrorEnabled = false
    }

    override fun showPasswordError() {
        layoutPassword.error = PASSWORD_ERROR
    }

    override fun hidePasswordError() {
        layoutPassword.isErrorEnabled = false
    }

    override fun showNameError() {
        layoutName.error = NO_NAME_ERROR
    }

    override fun hideNameError() {
        layoutName.isErrorEnabled = false
    }

    override fun startSignIn() = startActivity(Intent(this, SignInActivity::class.java))

    override fun showMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

