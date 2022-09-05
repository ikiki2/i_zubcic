package com.izubcic.gymApp.data.model

import java.io.Serializable

data class User(
    val id: String = "",
    val email: String = "",
    var userDisplayName: String = "",
    var height: String = "175",
    var weight: String = "90",
    var age: Int = 0

) : Serializable