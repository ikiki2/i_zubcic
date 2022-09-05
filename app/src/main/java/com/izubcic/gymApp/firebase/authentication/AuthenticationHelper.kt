package com.izubcic.gymApp.firebase.authentication

import com.google.firebase.auth.FirebaseUser
import com.izubcic.gymApp.firebase.RegisterRequestListener
import com.izubcic.gymApp.firebase.SignUpRequestListener

interface AuthenticationHelper {

    fun logTheUserIn(email: String, password: String, listener: SignUpRequestListener)

    fun attemptToRegisterTheUser(
        email: String,
        password: String,
        name: String,
        listener: RegisterRequestListener
    )

    fun logTheUserOut()

    fun checkIfUserIsLoggedIn(): Boolean

    fun getCurrentUserId(): String?

    fun getCurrentUser(): FirebaseUser?
}