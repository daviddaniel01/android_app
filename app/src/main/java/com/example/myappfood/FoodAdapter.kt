package com.example.myappfood

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter: RecyclerView.Adapter<FoodAdapter.FoodViewHodel>() {

    private var foodList: ArrayList<FoodModel> = ArrayList()
    private var onClickItem:((FoodModel)->Unit)? = null
    private var onClickDeleteItem:((FoodModel)->Unit)? = null


    fun addItems(items: ArrayList<FoodModel>){
        this.foodList = items
        notifyDataSetChanged()
    }
    
    fun setOnClickItem(callback: (FoodModel)->Unit){
        this.onClickItem = callback
    }

    fun setOnClickDeleteItem(callback: (FoodModel) -> Unit){
        this.onClickDeleteItem = callback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FoodViewHodel(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_food, parent, false)
    )

    override fun onBindViewHolder(holder: FoodViewHodel, position: Int) {
        var food  = foodList[position]
        holder.bindView(food)
        holder.itemView.setOnClickListener{ onClickItem?.invoke(food)}
        holder.btnDelete.setOnClickListener{ onClickDeleteItem?.invoke(food)}
    }

    override fun getItemCount(): Int {
        return foodList.size
    }


    class FoodViewHodel(var view:View):RecyclerView.ViewHolder(view){
        private var id = view.findViewById<TextView>(R.id.tvId)
        private var name = view.findViewById<TextView>(R.id.tvName)
        private var form = view.findViewById<TextView>(R.id.tvForm)
        var btnDelete = view.findViewById<Button>(R.id.btnDelete)


        fun bindView(food: FoodModel){
            id.text = food.id.toString()
            name.text = food.name
            form.text = food.form
        }
    }
}