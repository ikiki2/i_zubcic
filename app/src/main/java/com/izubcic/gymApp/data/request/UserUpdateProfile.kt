package com.izubcic.gymApp.data.request

import java.io.Serializable

data class UserUpdateProfile(
    val id: String = "",
    var userDisplayName: String = "",
    var height: String = "175",
    var weight: String = "90",
    var age: Int = 0

) : Serializable