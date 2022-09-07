package com.izubcic.gymApp.commons.extensions

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.izubcic.gymApp.data.model.User

fun Context?.toast(message: String) {
    this?.let { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
}

fun FirebaseUser.mapToUser(): User = User(
    this.uid,
    this.email ?: "", this.displayName ?: "", ""
)