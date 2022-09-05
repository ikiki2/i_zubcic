package com.izubcic.gymApp.firebase

import com.izubcic.gymApp.data.model.User

interface SignUpRequestListener {

    fun onSuccessfulRequest(user: User)

    fun onFailedRequest()
}