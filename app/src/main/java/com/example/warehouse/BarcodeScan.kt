package com.example.warehouse

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.example.warehouse.Class.Issue
import com.example.warehouse.Class.Materials
import com.example.warehouse.Class.MaterialsRacks
import com.example.warehouse.Class.User
import com.example.warehouse.Database.AppDatabase
import java.text.SimpleDateFormat
import java.util.*


class BarcodeScan : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val sharedPrefFile = "kotlinsharedpreference"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barcode_scanner)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
                sharedPrefFile,
                Context.MODE_PRIVATE
        )

        val db = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "hotayi-warehouse"
        ).allowMainThreadQueries().build()

        val userDao = db.userDao()
        val materialDao = db.materialDao()
        val rackDao = db.rackDao()
        val materialRackDao = db.materialRackDao()
        val issueDao = db.issueDao()

        val txtView: TextView = findViewById<View>(R.id.txtViewTemp) as TextView
        val txtPartNo: TextView = findViewById<View>(R.id.txtPartNo) as TextView
        val txtQty: TextView = findViewById<View>(R.id.txtQuantity) as TextView
        val txtSerialNo: TextView = findViewById<View>(R.id.txtSerialNo) as TextView
        val txtRack: TextView = findViewById<View>(R.id.txtRack) as TextView
        val btnDone: Button = findViewById<View>(R.id.btnConfirm) as Button
        val btnCancel: Button = findViewById<View>(R.id.btnCancel) as Button
        val user: User = userDao.findByUid(sharedPreferences.getInt("uid", 0))

        val issueView: View = findViewById(R.id.txtIssue)
        val rackView: View = findViewById(R.id.txtRack)
        val qtyView: View = findViewById(R.id.txtQuantity)
        val issueLinearView: View = findViewById(R.id.issueLinear)

        when (intent.getIntExtra("type", 3)) {
            0 -> {
                txtView.text = getString(R.string.receive_material)
                btnDone.text = getString(R.string.receive)
                if (issueView.parent != null) {
                    (issueView.parent as ViewGroup).removeView(issueView)
                }

                if (issueLinearView.parent != null) {
                    (issueLinearView.parent as ViewGroup).removeView(issueLinearView)
                }

                if (rackView.parent != null) {
                    (rackView.parent as ViewGroup).removeView(rackView)
                }

                btnDone.setOnClickListener {
                    val materialID = materialDao.getAll().size + 1

                    if (txtSerialNo.text.substring(11).isNullOrEmpty()) {
                        Toast.makeText(this, "Serial No is not generated yet", Toast
                                .LENGTH_SHORT).show()
                    } else if (txtQty.text.substring(10).isNullOrEmpty()) {
                        Toast.makeText(this, "Quantity is not detected", Toast
                                .LENGTH_SHORT).show()
                    } else if(materialDao.findBySerialNo(txtSerialNo.text.substring(11)) != null){
                        Toast.makeText(this, "${txtSerialNo.text.substring(11)} already received",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        materialDao.insertAll(Materials(materialID, txtSerialNo.text.substring(11),
                                txtQty.text.substring(10).toInt(), 1, SimpleDateFormat
                        ("yyyy-MM-dd").format(Date()), user.uid))
                        Toast.makeText(this, "Received ${txtQty.text.substring(10)} " +
                                "material", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    }
                }
            }
            1 -> {
                txtView.text = getString(R.string.check_rack)
                btnDone.text = getString(R.string.check)

                if (issueView.parent != null) {
                    (issueView.parent as ViewGroup).removeView(issueView)
                }

                if (issueLinearView.parent != null) {
                    (issueLinearView.parent as ViewGroup).removeView(issueLinearView)
                }

                if (qtyView.parent != null) {
                    (qtyView.parent as ViewGroup).removeView(qtyView)
                }

                txtSerialNo.addTextChangedListener(object : TextWatcher {
                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    }

                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                                   after: Int) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        if(!txtSerialNo.text.substring(11).isNullOrEmpty()){
                            val material = materialDao.findBySerialNo(txtSerialNo.text.substring(11))
                            if (material.Status == 2) {
                                btnDone.text = getString(R.string.rack_out)
                            } else {
                                btnDone.text = getString(R.string.check)
                            }
                        }
                    }
                })

                btnDone.setOnClickListener {
                    val material = materialDao.findBySerialNo(txtSerialNo.text.substring(11))

                    if (txtSerialNo.text.substring(11).isNullOrEmpty()) {
                        Toast.makeText(this, "Serial No is not generated yet", Toast
                                .LENGTH_SHORT).show()
                    } else if (txtPartNo.text.substring(7).isNullOrEmpty()) {
                        Toast.makeText(this, "Part No is not detected", Toast
                                .LENGTH_SHORT).show()
                    } else if (material == null) {
                        Toast.makeText(this, "Material(S/N: ${txtSerialNo.text.substring(11)}) is not received", Toast
                                .LENGTH_SHORT).show()
                    } else {
                        if (btnDone.text == getString(R.string.check)) {
                            val materialRackID = materialRackDao.getAll().size + 1
                            val rack = rackDao.findByRackName(txtSerialNo.text.substring(11, 14))

                            materialDao.updateStatus(2, txtSerialNo.text.substring(11))
                            materialRackDao.insertAll(MaterialsRacks(materialRackID, rack.RackID, txtPartNo.text
                                    .substring(7), txtSerialNo.text.substring(11), SimpleDateFormat("yyyy-MM-dd").format(Date()), ""))

                            Toast.makeText(this, "Inserted ${txtSerialNo.text.substring(11)} to specific rack", Toast
                                    .LENGTH_SHORT).show()

                            txtRack.text = getString(R.string.rack_name) + rack.RackName
                            btnDone.text = getString(R.string.rack_out)
                        } else {
                            materialDao.updateStatus(1, txtSerialNo.text.substring(11))
                            materialRackDao.updateRackOutDate(SimpleDateFormat("yyyy-MM-dd").format
                            (Date()), txtSerialNo.text.substring(11))

                            Toast.makeText(this, "Racked out ${txtSerialNo.text.substring(11)}",
                                    Toast.LENGTH_SHORT).show()

                            btnDone.text = getString(R.string.check)
                        }
                    }
                }
            }
            2 -> {
                val txtIssue: EditText = findViewById<View>(R.id.txtIssue) as EditText
                txtView.text = getString(R.string.issue)
                btnDone.text = getString(R.string.report)

                if (qtyView.parent != null) {
                    (qtyView.parent as ViewGroup).removeView(qtyView)
                }

                val btnAdd: Button = findViewById<View>(R.id.btnAdd) as Button
                val btnMinus: Button = findViewById<View>(R.id.btnMinus) as Button
                val txtvwQty: TextView = findViewById<View>(R.id.txtvwQty) as TextView

                btnAdd.setOnClickListener {
                    if(txtSerialNo.text.substring(11).isNullOrEmpty()){
                        Toast.makeText(this, "Serial No is not generated yet", Toast
                                .LENGTH_SHORT).show()
                    }else{
                        val maxQty: Int = materialDao.findBySerialNo(txtSerialNo.text.substring(11)).Quantity
                        val tempQty = txtvwQty.text.toString().toInt() + 1

                        if (tempQty > maxQty) {
                            Toast.makeText(this, "Exceed exist quantity", Toast
                                    .LENGTH_SHORT).show()
                        } else {
                            txtvwQty.text = tempQty.toString()
                        }
                    }
                }

                btnMinus.setOnClickListener {
                    val tempQty = txtvwQty.text.toString().toInt() - 1
                    if (tempQty < 1) {
                        Toast.makeText(this, "Cannot be less then 1", Toast
                                .LENGTH_SHORT).show()
                    } else {
                        txtvwQty.text = tempQty.toString()
                    }
                }

                btnDone.setOnClickListener {
                    val materials = materialDao.findBySerialNo(txtSerialNo.text.substring(11))

                    if (txtSerialNo.text.substring(11).isNullOrEmpty()) {
                        Toast.makeText(this, "Serial No is not generated yet", Toast
                                .LENGTH_SHORT).show()
                    } else if (txtIssue.text.toString().isNullOrEmpty()) {
                        Toast.makeText(this, "Blank issue not accepted", Toast
                                .LENGTH_SHORT).show()
                    } else if (materials == null) {
                        Toast.makeText(this, "Material(S/N: ${txtSerialNo.text.substring(11)}) is not received", Toast
                                .LENGTH_SHORT).show()
                    } else if (materials.Status == 3) {
                        Toast.makeText(this, "Material(S/N: ${txtSerialNo.text.substring(11)}) already reported",
                                Toast.LENGTH_SHORT).show()
                    } else {
                        val issueID = issueDao.getAll().size + 1

                        materialDao.updateQuantity(materials.Quantity - (txtvwQty.text.toString()
                                .toInt()), txtSerialNo.text.substring(11))
                        materialDao.updateStatus(3, txtSerialNo.text.substring(11))
                        issueDao.insertAll(Issue(issueID, txtIssue.text.toString(), txtSerialNo.text
                                .substring(11)))
                        Toast.makeText(this, "Reported ${txtSerialNo.text.substring(11)} to production", Toast
                                .LENGTH_SHORT).show()
                        val intent = Intent(this, Home::class.java)
                        startActivity(intent)
                    }
                }
            }
        }

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        btnCancel.setOnClickListener {
            codeScanner.startPreview()
        }

        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                //<P>CUST P/N:GRM31CR60J476ME19L
                //<Q>QTY:200
                if (it.text.contains("<P>")) {
                    txtPartNo.text = getString(R.string.part_no) + it.text.substring(12)
                    txtSerialNo.text = getString(R.string.serial_no) + "TST${it.text.substring(26)}"
                } else if (it.text.contains("<Q>")) {
                    txtQty.text = getString(R.string.quantity) + it.text.substring(7)
                }
                codeScanner.startPreview()
                //Toast.makeText(this, " ${it.text}",Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback {
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}