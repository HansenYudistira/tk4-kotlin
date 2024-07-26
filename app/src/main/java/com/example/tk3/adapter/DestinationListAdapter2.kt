//package com.example.tk3.adapter
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.tk3.R
//import com.example.tk3.MapsActivity
//import com.example.tk3.model.DestinationListModel
//
//class DestinationListAdapter2(destinationlist: List<DestinationListModel>, internal var context: Context)
//    : RecyclerView.Adapter<DestinationListAdapter2.DestinationViewHolder>()
//{
//
//    internal  var destinationlist: List<DestinationListModel> = ArrayList()
//    init {
//        this.destinationlist = destinationlist
//    }
//    inner class DestinationViewHolder (view: View) : RecyclerView.ViewHolder(view) {
//        var name: TextView = view.findViewById(R.id.txt_name)
//        var description: TextView = view.findViewById(R.id.txt_description)
//        var btn_edit : Button = view.findViewById(R.id.btn_edit)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
//        val view = LayoutInflater.from(context).inflate(R.layout.recycler_destination_list, parent, false)
//        return DestinationViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
//        val destinations = destinationlist[position]
//        holder.name.text = destinations.name
//        holder.description.text = destinations.description
//
//        holder.btn_edit.setOnClickListener {
//            MapsActivity.open(
//                context = context,
//                mode = MapsActivity.EDIT,
//                destinationId = destinations.id,
//                lat = destinations.latitude,
//                lng = destinations.longitude,
//            )
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return destinationlist.size
//    }
//}