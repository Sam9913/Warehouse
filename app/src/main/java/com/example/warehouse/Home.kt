package com.example.warehouse

import android.Manifest
import android.Manifest.permission.*
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.warehouse.Database.AppDatabase


class Home : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "hotayi-warehouse"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val materialDao = db.materialDao()
        val rackDao = db.rackDao()
        val materialRackDao = db.materialRackDao()

        checkPermissionAndOpenCamera(1)

        //navigation
        val imgBtnReport: ImageButton = findViewById<View>(R.id.imgBtnReport) as ImageButton
        imgBtnReport.setOnClickListener {
            val intent = Intent(this, Report::class.java)
            startActivity(intent)
        }

        val btnReceive: Button = findViewById<View>(R.id.btnReceive) as Button
        btnReceive.setOnClickListener {
            if (checkPermissionAndOpenCamera(0) == 1) {
                val intent = Intent(this, BarcodeScan::class.java)
                intent.putExtra("type", 0)
                startActivity(intent)
            }
        }

        val btnCheck: Button = findViewById<View>(R.id.btnCheck) as Button
        btnCheck.setOnClickListener {
            if (checkPermissionAndOpenCamera(0) == 1) {
                val intent = Intent(this, BarcodeScan::class.java)
                intent.putExtra("type", 1)
                startActivity(intent)
            }
        }

        val btnIssue: Button = findViewById<View>(R.id.btnIssue) as Button
        btnIssue.setOnClickListener {
            if (checkPermissionAndOpenCamera(0) == 1) {
                val intent = Intent(this, BarcodeScan::class.java)
                intent.putExtra("type", 2)
                startActivity(intent)
            }
        }
    }

    fun checkPermissionAndOpenCamera(runCount: Int): Int {
        if (ContextCompat.checkSelfPermission(applicationContext, CAMERA)
                == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(applicationContext, WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED || ContextCompat.checkSelfPermission(applicationContext, READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            if (runCount == 1) {
                ActivityCompat.requestPermissions(this, arrayOf(CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE), 1)
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            }

            return 0
        }

        return 1
    }
}