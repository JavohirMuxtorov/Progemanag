package com.example.trelloclone.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.trelloclone.R
import com.example.trelloclone.databinding.ItemBoardBinding
import com.example.trelloclone.databinding.ItemCardBinding
import com.example.trelloclone.databinding.ItemMemberBinding
import com.example.trelloclone.models.User
import com.example.trelloclone.utils.Constants

class MemberListItemsAdapter(private val context: Context, private var list: ArrayList<User>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var onClickListener: OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            ItemMemberBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]
        if (holder is MyViewHolder){
            Glide
                .with(context)
                .load(model.image)
                .centerCrop()
                .placeholder(R.drawable.ic_user_place_holder)
                .into(holder.binding.ivMemberImage)
            holder.binding.tvMemberName.text = model.name
            holder.binding.tvMemberEmail.text = model.email
            if(model.selected){
                holder.binding.ivSelectedMember.visibility = View.VISIBLE
            }else{
                holder.binding.ivSelectedMember.visibility = View.GONE
            }
            holder.itemView.setOnClickListener {
                if (model.selected){
                    onClickListener?.onClick(position, model, Constants.UN_SELECT)
                }else {
                    onClickListener?.onClick(position, model, Constants.SELECT)
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }
    private class MyViewHolder(val binding: ItemMemberBinding): RecyclerView.ViewHolder(binding.root)
interface OnClickListener{
    fun onClick(position: Int, user: User, action: String)
}
}