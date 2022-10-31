package com.example.myappfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var edName: EditText
    private lateinit var edForm: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnView: Button
    private lateinit var btnUpdate: Button


    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: FoodAdapter? = null
    private var food:FoodModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        initRecycleView()
        sqLiteHelper = SQLiteHelper(this)

        btnAdd.setOnClickListener { addFood() }
        btnView.setOnClickListener { getFoods() }
        btnUpdate.setOnClickListener{ updateFood() }

        adapter?.setOnClickItem {
            Toast.makeText(this, it.name, Toast.LENGTH_SHORT).show()
            edName.setText(it.name)
            edForm.setText(it.form)
            food = it
        }

        adapter?.setOnClickDeleteItem {
            deleteFood(it.id)
        }

    }

    private fun updateFood() {
        val name = edName.text.toString()
        val form = edForm.text.toString()
        if(name == food?.name && form == food?.form){
            Toast.makeText(this, "Khong thay doi", Toast.LENGTH_SHORT).show()
            return
        }
        if(food == null) {
            return
        }
        val food = FoodModel(id = food!!.id, name = name, form = form)
        val status = sqLiteHelper.updateFood(food)
        if(status > -1 ){
            clearEditText()
            getFoods()
        }
        else{
            Toast.makeText(this, "Update that bai", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFoods() {
        val foodList = sqLiteHelper.getAllFood()
        Log.e("ppp", "${foodList.size}")

        adapter?.addItems(foodList)
    }

    private fun addFood() {
        val name = edName.text.toString()
        val form = edForm.text.toString()

        if(name.isEmpty() || form.isEmpty()){
            Toast.makeText(this,"Can nhap de tiep tuc", Toast.LENGTH_LONG).show()
        }
        else{
            val food = FoodModel(name = name, form = form)
            val status = sqLiteHelper.insertFood(food)
            if(status>-1){
                Toast.makeText(this,"Thanh cong", Toast.LENGTH_LONG).show()
                clearEditText()
            }
            else{
                Toast.makeText(this,"That bai", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteFood(id:Int){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Ban co chac muon xoa khong? ")
        builder.setCancelable(true)
        builder.setPositiveButton("Co"){ dialog, _ ->
            sqLiteHelper.deleteFoodById(id)
            getFoods()
            dialog.dismiss()
        }
        builder.setNegativeButton("Khong"){ dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    private fun clearEditText() {
        edName.setText("")
        edForm.setText("")
        edName.requestFocus()
    }

    private fun initRecycleView(){
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = FoodAdapter()
        recyclerView.adapter = adapter
    }

    private fun initView() {
        edName = findViewById(R.id.foodName)
        edForm = findViewById(R.id.foodForm)
        btnAdd = findViewById(R.id.btnAdd)
        btnView = findViewById(R.id.btnView)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)
    }
}