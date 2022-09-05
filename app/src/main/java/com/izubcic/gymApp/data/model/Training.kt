package com.izubcic.gymApp.data.model

import java.io.Serializable

data class Training(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val repetitions: Long = 0,
    val series: Long = 0,
    val weight: Long = 0,
    val userWeight: Long = 0,
    val date: String = ""
) : Serializable
