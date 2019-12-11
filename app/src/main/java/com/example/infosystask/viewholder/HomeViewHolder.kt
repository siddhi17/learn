package com.example.infosystask.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.infosystask.R

class HomeViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    var tvName: TextView = itemView.findViewById(R.id.tvName)
    var tvStore: TextView = itemView.findViewById(R.id.tvStore)
    var imgCard: ImageView = itemView.findViewById(R.id.imgCard)
}
