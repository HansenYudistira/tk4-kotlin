package com.example.tk3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tk3.MapsActivity
import com.example.tk3.R
import com.example.tk3.model.DestinationListModel

class DestinationListAdapter(private val destinationList: List<DestinationListModel>, private val context: Context)
    : RecyclerView.Adapter<DestinationListAdapter.DestinationViewHolder>() {

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
}
