package com.example.tk3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.tk3.databinding.ActivityAddDestinationBinding
import com.example.tk3.model.DestinationListModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class AddTask : AppCompatActivity() {

    private var isEditMode: Boolean = false
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var destinationId: String = ""

    private var mode = ""

    private lateinit var binding: ActivityAddDestinationBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDestinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        getIntentExtra()

        if (mode == EDIT) {
            // Update mode
            isEditMode = true
            with(binding) {
                btnSave.text = "Update Data"
                btnDel.visibility = View.VISIBLE
                fetchDestination(destinationId)
            }
        } else {
            // Add mode
            isEditMode = false
            with(binding) {
                btnSave.text = "Save Data"
                btnDel.visibility = View.GONE
            }
        }

        with(binding) {
            tvLatitude.text = "Latitude: $latitude"
            tvLongitude.text = "Longitude: $longitude"

            btnSave.setOnClickListener {
                if (etName.text.isNullOrEmpty() || etDescription.text.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, "Please fill all fields", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                saveOrUpdateDestination()
            }

            btnDel.setOnClickListener {
                val dialog = AlertDialog.Builder(this@AddTask)
                    .setTitle("Info")
                    .setMessage("Click yes if you want to delete")
                    .setPositiveButton("YES") { dialog, _ ->
                        deleteDestination(destinationId)
                        dialog.dismiss()
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                dialog.show()
            }
        }
    }

    private fun getIntentExtra() {
        intent?.let {
            mode = it.getStringExtra(MODE) ?: ""
            latitude = it.getDoubleExtra(LAT_LOCATION, 0.0)
            longitude = it.getDoubleExtra(LONG_LOCATION, 0.0)
            destinationId = it.getStringExtra(DESTINATION_ID) ?: ""
        }
    }

    private fun fetchDestination(destinationId: String) {
        firestore.collection("destinations").document(destinationId).get()
            .addOnSuccessListener { documentSnapshot ->
                val destination = documentSnapshot.toObject<DestinationListModel>()
                destination?.let {
                    binding.etName.setText(it.name)
                    binding.etDescription.setText(it.description)
                    if (latitude.isNullOrZero()) latitude = it.latitude
                    if (longitude.isNullOrZero()) longitude = it.longitude
                    binding.tvLatitude.text = "Latitude: $latitude"
                    binding.tvLongitude.text = "Longitude: $longitude"
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to load destination: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun saveOrUpdateDestination() {
        val destination = DestinationListModel().apply {
            id = destinationId
            name = binding.etName.text.toString()
            description = binding.etDescription.text.toString()
            latitude = this@AddTask.latitude
            longitude = this@AddTask.longitude
        }

        if (isEditMode) {
            // Update
            firestore.collection("destinations").document(destinationId).set(destination)
                .addOnSuccessListener {
                    Toast.makeText(this, "Destination updated successfully", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to update destination: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            // Insert
            firestore.collection("destinations").add(destination)
                .addOnSuccessListener {
                    Toast.makeText(this, "Destination added successfully", Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to add destination: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun deleteDestination(destinationId: String) {
        firestore.collection("destinations").document(destinationId).delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Destination deleted successfully", Toast.LENGTH_SHORT).show()
                navigateToMainActivity()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Failed to delete destination: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun navigateToMainActivity() {
        MainActivity.open(this@AddTask)
        finishAffinity()
    }

    private fun Double.isNullOrZero() = this == 0.0

    companion object {
        const val MODE = "MODE"
        const val EDIT = "EDIT"
        const val NEW_DATA = "NEW_DATA"
        const val DESTINATION_ID = "destination_id"
        const val LAT_LOCATION = "lat_location"
        const val LONG_LOCATION = "long_location"

        fun open(context: Context, mode: String, destinationId: String? = null, lat: Double, long: Double) {
            val intent = Intent(context, AddTask::class.java).apply {
                putExtra(LAT_LOCATION, lat)
                putExtra(LONG_LOCATION, long)
                putExtra(DESTINATION_ID, destinationId)
                putExtra(MODE, mode)
            }
            context.startActivity(intent)
        }
    }
}
