package com.izubcic.gymApp.firebase.database

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.izubcic.gymApp.data.model.Training
import com.izubcic.gymApp.data.model.User
import com.izubcic.gymApp.data.request.UserUpdateProfile

class DatabaseHelperImpl constructor(private val reference: DatabaseReference) : DatabaseHelper {

    override fun saveUser(user: User) {
        reference.child("users").child(user.id).setValue(user)
    }

    override fun getUser(id: String, returningUser: (User) -> Unit) {
        reference.child("users").child(id).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.run {
                    val user: User? = getValue(User::class.java)
                    user?.run {
                        returningUser(user)
                    }
                }
            }

        })
    }

    override fun addTraining(userId: String, training: Training, returningUser: () -> Unit) {
        val userTraining =
            reference.child("users").child(userId).child("trainings").child(training.id)
        userTraining.setValue(training)
            .addOnCompleteListener(object : DatabaseReference.CompletionListener,
                OnCompleteListener<Void> {
                override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
                    returningUser()
                }

                override fun onComplete(p0: Task<Void>) {
                    returningUser()
                }

            })
    }

    override fun onItemDeleted(userId: String, training: Training, lambda: () -> Unit) {
        val trainings =
            reference.child("users").child(userId).child("trainings").child(training.id)
        trainings.removeValue { _, _ -> lambda() }
    }

    override fun listenToTrainingList(
        userId: String,
        noTrainingsReceived: (List<Training>) -> Unit
    ) {
        reference.child("users").child(userId).child("trainings")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(datasnapshot: DataSnapshot) {
                    val values = if (datasnapshot.hasChildren()) datasnapshot.children.map {
                        it.getValue(Training::class.java)
                    } else listOf()

                    noTrainingsReceived(values.filterNotNull())
                }
            })
    }

    override fun saveUserDetails(user: UserUpdateProfile, lambda: (UserUpdateProfile) -> Unit) {
        reference.child("users").child(user.id).updateChildren(
            mapOf(
                "age" to user.age,
                "weight" to user.weight,
                "height" to user.height,
                "userDisplayName" to user.userDisplayName,
            )
        )
    }

    override fun saveUserWeight(user: UserUpdateProfile, lambda: () -> Unit) {
        reference.child("users").child(user.id).updateChildren(
            mapOf(
                "weight" to user.weight,
            )
        )
        lambda()
    }
}