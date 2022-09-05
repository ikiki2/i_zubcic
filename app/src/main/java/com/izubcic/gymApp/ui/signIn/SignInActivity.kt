package com.izubcic.gymApp.ui.signIn

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.izubcic.gymApp.App
import com.izubcic.gymApp.R
import com.izubcic.gymApp.commons.constants.EMAIL_ERROR
import com.izubcic.gymApp.commons.constants.ERROR_EMAIL_OR_PASSWORD
import com.izubcic.gymApp.commons.constants.PASSWORD_ERROR
import com.izubcic.gymApp.commons.extensions.hide
import com.izubcic.gymApp.commons.extensions.onClick
import com.izubcic.gymApp.commons.extensions.show
import com.izubcic.gymApp.commons.utils.checkEmailEmpty
import com.izubcic.gymApp.commons.utils.checkPasswordEmpty
import com.izubcic.gymApp.commons.utils.isValidEmail
import com.izubcic.gymApp.data.model.User
import com.izubcic.gymApp.firebase.SignUpRequestListener
import com.izubcic.gymApp.firebase.authentication.AuthenticationHelperImpl
import com.izubcic.gymApp.firebase.database.DatabaseHelperImpl
import com.izubcic.gymApp.preferences.PreferencesHelperImpl
import com.izubcic.gymApp.ui.registration.RegistrationActivity
import com.izubcic.gymApp.ui.trainings.GymsActivity
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), SignInView, SignUpRequestListener {

    private val auth: AuthenticationHelperImpl by lazy {
        AuthenticationHelperImpl(
            App.auth,
            DatabaseHelperImpl(App.database),
            PreferencesHelperImpl(App.prefs)
        )
    }

    private val prefs: PreferencesHelperImpl by lazy {
        PreferencesHelperImpl(App.prefs)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        initListeners()
    }

    private fun initListeners() {
        signIn.onClick {
            onSignInClick(
                emailSignIn.text.toString(),
                passwordSignIn.text.toString()
            )
        }
        goToRegistration.onClick { startActivity(Intent(this, RegistrationActivity::class.java)) }
    }

    // logic functions
    private fun onSignInClick(email: String, password: String) {
        this.showProgressAndHideOther()
        if (!email.isEmpty() && !password.isEmpty() && password.length > 5) {
            tryToSignIn(email, password)
        } else {
            this.hideProgressAndShowOther()
        }
        checkInputEmpty(email, password)
    }

    private fun tryToSignIn(email: String, password: String) {
        auth.logTheUserIn(email, password, this)
    }

    private fun checkInputEmpty(email: String, password: String) {
        if (checkEmailEmpty(email.trim()) || !isValidEmail(email.trim()))
            this.showEmailError()
        else this.hideEmailError()

        if (checkPasswordEmpty(password.trim()) || password.trim().length < 6)
            this.showPasswordError()
        else this.hidePasswordError()
    }

    override fun onSuccessfulRequest(user: User) {
        prefs.saveId(user.id)
        this.startGymActivity(user)
        this.hideProgressAndShowOther()
    }

    override fun onFailedRequest() {
        this.hideProgressAndShowOther()
        this.showMessage(ERROR_EMAIL_OR_PASSWORD)
    }

    // view functions
    override fun hidePasswordError() {
        layoutPasswordSign.isErrorEnabled = false
    }

    override fun hideEmailError() {
        layoutEmailSign.isErrorEnabled = false
    }

    override fun showPasswordError() {
        layoutPasswordSign.error = PASSWORD_ERROR
    }

    override fun showEmailError() {
        layoutEmailSign.error = EMAIL_ERROR
    }

    override fun showProgressAndHideOther() {
        progressSign.show()
        layoutWithoutImageSign.hide()
    }

    override fun hideProgressAndShowOther() {
        progressSign.hide()
        layoutWithoutImageSign.show()
    }

    override fun startGymActivity(user: User) {
        val intent = Intent(this, GymsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun showMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
