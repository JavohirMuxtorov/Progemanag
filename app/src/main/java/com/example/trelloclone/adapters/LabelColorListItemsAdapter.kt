package com.example.trelloclone.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.databinding.ItemCardBinding
import com.example.trelloclone.databinding.ItemLabelColorBinding
import com.example.trelloclone.databinding.ItemMemberBinding
import com.example.trelloclone.models.Board

class LabelColorListItemsAdapter(private val context: Context, private var list: ArrayList<String>, private val mSelectedColor: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemLabelColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                    false
            )
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = list[position]
        if (holder is MyViewHolder){
            holder.binding.viewMain.setBackgroundColor(Color.parseColor(item))
            if (item == mSelectedColor){
                holder.binding.ivSelectedColor.visibility = View.VISIBLE
            }else{

                holder.binding.ivSelectedColor.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                if (onClickListener != null){
                    onClickListener!!.onClick(position, item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface OnClickListener{
        fun onClick(position: Int, color: String)
    }
    private class MyViewHolder(val binding: ItemLabelColorBinding): RecyclerView.ViewHolder(binding.root)

}