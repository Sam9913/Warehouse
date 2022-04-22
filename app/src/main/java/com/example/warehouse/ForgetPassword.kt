package com.example.warehouse

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.warehouse.Database.AppDatabase
import com.google.android.material.internal.CheckableImageButton
import com.google.android.material.textfield.TextInputLayout


class ForgetPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_password)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "hotayi-warehouse"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val btnForgetPassword: Button = findViewById<View>(R.id.resetbtn) as Button
        val username: EditText = findViewById<View>(R.id.txtUsername) as EditText
        val newPass: TextInputLayout = findViewById<View>(R.id.txtNewPassword) as TextInputLayout
        val conNewPass: TextInputLayout = findViewById<View>(R.id.txtConfirmPassword) as TextInputLayout

        newPass.editText?.transformationMethod  = PasswordTransformationMethod()
        conNewPass.editText?.transformationMethod = PasswordTransformationMethod()

        btnForgetPassword.setOnClickListener {
            if(username.text.isNullOrEmpty()) {
                Toast.makeText(
                    this,
                    "No username inserted",
                    Toast.LENGTH_SHORT
                ).show()
            }else if(userDao.findByUsername(username.text.toString()) == null){
                Toast.makeText(
                    this,
                    "No user with username(${username.text}) found",
                    Toast.LENGTH_SHORT
                ).show()
            }else if (newPass.editText?.text.toString()
                    .equals(conNewPass.editText?.text.toString()) && userDao.findByUsername(username.text.toString()) != null
            ) {
                userDao.updatePassword(username.text.toString(), newPass.editText?.text.toString())
                Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(
                    this,
                    "New Password and Confirm New Password not match",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}
