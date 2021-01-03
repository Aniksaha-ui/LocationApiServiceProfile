package com.example.locationapiservices

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.locationapiservices.AppDb.Usersettings
import kotlinx.android.synthetic.main.recyclerview_row.view.*

class RecyclerViewAdapter(val listener: RowClickListener): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    var items  = ArrayList<Usersettings>()

    fun setListData(data: ArrayList<Usersettings>) {
        this.items = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(
            R.layout.recyclerview_row,
            parent,
            false
        )
        return MyViewHolder(inflater, listener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            listener.onItemClickListener(items[position])
        }
        holder.bind(items[position])

    }



    class MyViewHolder(view: View, val listener: RowClickListener): RecyclerView.ViewHolder(view) {

        val recieveMassage=view.recievemassage1
        val longitude = view.longitude
        val latetude = view.latetude
//        val usermood = view.usermood
        val mediaVolume = view.mediavolume
        val callVolume =view.callvolume
        val alarmVolume=view.alarmvolume
        val delete =view.delete


        fun bind(data: Usersettings) {

            recieveMassage.text=data.settingsName
            longitude.text = data.longitude!!.toDouble().toString()
            latetude.text = data.latitude!!.toDouble().toString()
//            usermood.text = data.usermood
            mediaVolume.progress= data.MediaVolume.toString().toInt()
            callVolume.progress=data.callVolume.toString().toInt()
            alarmVolume.progress=data.AlarmVolume.toString().toInt()

            delete.setOnClickListener {
                listener.onDeleteUserClickListener(data)
            }
        }
    }

    interface RowClickListener{
        fun onDeleteUserClickListener(user: Usersettings)
        fun onItemClickListener(user: Usersettings)
    }


}