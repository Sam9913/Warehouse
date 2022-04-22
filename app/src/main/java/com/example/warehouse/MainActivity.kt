package com.example.warehouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.warehouse.Class.Materials
import com.example.warehouse.Class.MaterialsRacks
import com.example.warehouse.Class.Racks
import com.example.warehouse.Class.User
import com.example.warehouse.Database.AppDatabase


class MainActivity : AppCompatActivity() {
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "hotayi-warehouse"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val materialDao = db.materialDao()
        val rackDao = db.rackDao()
        val materialRackDao = db.materialRackDao()
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        if (userDao.getAll().isEmpty()) {
            userDao.insertAll(User(1, "Tester", "123456"))
        }

        if (materialDao.getAll().isEmpty()) {
            materialDao.insertAll(
                    Materials(1, "TST1234", 10, 1, "2021-12-11", 1),
                    Materials(2, "TST5678", 10, 2, "2021-12-11", 1),
                    Materials(3, "TST9101", 10, 3, "2021-12-11", 1)
            )
        }

        if (rackDao.getAll().isEmpty()) {
            rackDao.insertAll(
                    Racks(1, "TST"),
                    Racks(2, "DLP")
            )
        }

        if(materialRackDao.getAll().isEmpty()){
            materialRackDao.insertAll(
                    MaterialsRacks(1,
                            1,
                            "RANDOMGENERATED123456GAGA",
                            "TST1234",
                            "2021-12-11",
                            ""),
                    MaterialsRacks(2,
                            1,
                            "RAND4545364564GAGAGAEAGAA",
                            "TST5678",
                            "2021-12-11",
                            ""),
                    MaterialsRacks(3,
                            1,
                            "ASDADASDASQW464651ASDA656",
                            "TST9101",
                            "2021-12-11",
                            "")
            )
        }

        if(sharedPreferences.getInt("uid", 0) != 0){
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
        }

        val username: EditText = findViewById<View>(R.id.username) as EditText
        val password: EditText = findViewById<View>(R.id.password) as EditText
        password.transformationMethod = PasswordTransformationMethod()
        val btnLogin: Button = findViewById<View>(R.id.loginbtn) as Button
        btnLogin.setOnClickListener {
            val user: User = userDao.findByUsernameAndPassword(username.text.toString(), password.text.toString())

            if (user == null) {
                Toast.makeText(this, "Wrong password or username", Toast.LENGTH_SHORT).show()
            } else {
                val editor: SharedPreferences.Editor =  sharedPreferences.edit()
                editor.putInt("uid", user.uid).apply()

                Toast.makeText(this, "Welcome back", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, Home::class.java)
                startActivity(intent)
            }
        }

        val forgetpass: TextView = findViewById<View>(R.id.forgetpass) as TextView
        forgetpass.setOnClickListener {
            val intent = Intent(this, ForgetPassword::class.java)
            startActivity(intent)
        }

    }
}