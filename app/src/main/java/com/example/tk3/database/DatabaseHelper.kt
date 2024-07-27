package com.example.tk3.database

import android.content.Context
import com.example.tk3.model.DestinationListModel
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class DatabaseHelper(private val context: Context) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionName = "destinations"

    fun getAllDestinations(): Task<QuerySnapshot> {
        return firestore.collection(collectionName).get()
    }

    fun addDestination(destination: DestinationListModel): Task<Void> {
        val document = firestore.collection(collectionName).document()
        destination.id = document.id
        return document.set(destination)
    }

    fun getDestination(id: String): Task<DocumentSnapshot> {
        return firestore.collection(collectionName).document(id).get()
    }

    fun updateDestination(destination: DestinationListModel): Task<Void> {
        return firestore.collection(collectionName).document(destination.id).set(destination)
    }

    fun deleteDestination(id: String): Task<Void> {
        if (id.isBlank()) {
            throw IllegalArgumentException("Invalid document reference. Document references must have a valid ID.")
        }
        return firestore.collection(collectionName).document(id).delete()
    }
}
