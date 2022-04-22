package com.example.warehouse.Adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.room.Room
import com.example.warehouse.Class.MaterialsRacks
import com.example.warehouse.Database.AppDatabase
import com.example.warehouse.R

class ReportAdapter(private val context: Context,
                    private val dataSource: List<MaterialsRacks>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        val holder: ViewHolder

        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item_material, parent, false)

            holder = ViewHolder()
            holder.positionTextView = view.findViewById(R.id.position) as TextView
            holder.serialNoTextView = view.findViewById(R.id.serial_no) as TextView
            holder.qtyTextView = view.findViewById(R.id.qty) as TextView
            holder.rackNameTextView = view.findViewById(R.id.rack_name) as TextView
            holder.rackInDateTextView = view.findViewById(R.id.rack_in_date) as TextView
            holder.rackOutDateTextView = view.findViewById(R.id.rack_out_date) as TextView

            view.tag = holder
        } else {
            view = convertView
            holder = convertView.tag as ViewHolder
        }

        val db = Room.databaseBuilder(
                context,
                AppDatabase::class.java, "hotayi-warehouse"
        ).allowMainThreadQueries().build()

        val rackDao = db.rackDao()
        val materialDao = db.materialDao()

        val positionTextView = holder.positionTextView
        val serialNoTextView = holder.serialNoTextView
        val qtyTextView = holder.qtyTextView
        val rackNameTextView = holder.rackNameTextView
        val rackInDateTextView = holder.rackInDateTextView
        val rackOutDateTextView = holder.rackOutDateTextView

        val material = getItem(position) as MaterialsRacks

        positionTextView.text = (position + 1).toString()
        serialNoTextView.text = "Serial No: " + material.SerialNo
        qtyTextView.text = "Quantity: " + materialDao.findBySerialNo(material.SerialNo).Quantity.toString()
        rackNameTextView.text = "Rack Name: " + rackDao.findByRackNo(material.RackNo).RackName
        rackInDateTextView.text = "Rack In Date: " + material.RackInDate
        if(material.RackOutDate.isBlank()){
            rackOutDateTextView.text = "Rack Out Date: " + material.RackOutDate
        }else{
            rackOutDateTextView.text = "Rack Out Date: " + material.RackOutDate
        }

        return view
    }

    private class ViewHolder {
        lateinit var positionTextView: TextView
        lateinit var serialNoTextView: TextView
        lateinit var qtyTextView: TextView
        lateinit var rackNameTextView: TextView
        lateinit var rackInDateTextView: TextView
        lateinit var rackOutDateTextView: TextView
    }

}