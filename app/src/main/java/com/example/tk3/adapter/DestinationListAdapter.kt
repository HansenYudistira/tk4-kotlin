package com.example.tk3.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tk3.MapsActivity
import com.example.tk3.R
import com.example.tk3.database.DatabaseHelper
import com.example.tk3.model.DestinationListModel

class DestinationListAdapter(
    private val destinationList: MutableList<DestinationListModel>,
    private val context: Context)
    : RecyclerView.Adapter<DestinationListAdapter.DestinationViewHolder>() {

    private val dbHelper = DatabaseHelper(context)

    inner class DestinationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.txt_name)
        val description: TextView = view.findViewById(R.id.txt_description)
        val btnEdit: Button = view.findViewById(R.id.btn_edit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_destination_list, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = destinationList[position]
        holder.name.text = destination.name
        holder.description.text = destination.description

        holder.btnEdit.setOnClickListener {
            MapsActivity.open(
                context = context,
                mode = MapsActivity.EDIT,
                destinationId = destination.id,
                lat = destination.latitude,
                lng = destination.longitude
            )
        }
    }

    override fun getItemCount(): Int {
        return destinationList.size
    }

    fun removeItemById(destinationId: String) {
        if (destinationId.isBlank()) {
            Log.e("DestinationListAdapter", "Cannot remove item with blank ID.")
            return
        }

        val position = destinationList.indexOfFirst { it.id == destinationId }
        if (position != -1) {
            dbHelper.deleteDestination(destinationId).addOnSuccessListener {
                destinationList.removeAt(position)
                notifyItemRemoved(position)
            }.addOnFailureListener { e ->
                // Handle any errors here, like showing a message to the user
                e.printStackTrace()
            }
        } else {
            Log.d("DestinationListAdapter", "Destination ID not found: $destinationId")
        }
    }
}
