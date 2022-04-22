package com.example.warehouse

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.warehouse.Adapter.ReportAdapter
import com.example.warehouse.Database.AppDatabase

class Report : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.report)

        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "hotayi-warehouse"
        ).allowMainThreadQueries().build()

        val materialRackDao = db.materialRackDao()
        val viewMaterials: TextView = findViewById<View>(R.id.viewMaterials) as TextView

        val materialList = materialRackDao.getAll()
        viewMaterials.text = materialList.size.toString()

        val listView = findViewById<ListView>(R.id.material_list_view)
        val adapter = ReportAdapter(this, materialRackDao.getAll())
        listView.adapter = adapter
    }

}