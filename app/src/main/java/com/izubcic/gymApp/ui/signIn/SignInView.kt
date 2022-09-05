package com.izubcic.gymApp.ui.signIn

import com.izubcic.gymApp.data.model.User

interface SignInView {

    fun showEmailError()

    fun showPasswordError()

    fun hideEmailError()

    fun hidePasswordError()

    fun showProgressAndHideOther()

    fun hideProgressAndShowOther()

    fun startGymActivity(user: User)

    fun showMessage(message: String)
}