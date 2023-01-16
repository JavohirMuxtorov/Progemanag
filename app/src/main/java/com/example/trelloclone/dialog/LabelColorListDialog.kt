package com.example.trelloclone.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.trelloclone.R
import com.example.trelloclone.adapters.LabelColorListItemsAdapter
import com.example.trelloclone.databinding.DialogListBinding
import kotlinx.android.synthetic.main.dialog_list.view.*

// https://youtube.com/shorts/U2mjZSl6Uoo?feature=share
abstract class LabelColorListDialog(context: Context,
                           private val list: ArrayList<String>,
                           private var title: String = "",
                           private var mSelectedColor: String =""
): Dialog(context) {
    private var adapter: LabelColorListItemsAdapter? = null
    @SuppressLint("InflateParams")
    lateinit var binding: DialogListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(binding.root)
    }
    @SuppressLint("CutPasteId")
    private fun setUpRecyclerView(view: View){
        view.tvTitle.text = title
        view.rvList.layoutManager = LinearLayoutManager(context)
        adapter = LabelColorListItemsAdapter(context, list, mSelectedColor)
        view.rvList.adapter = adapter
        adapter!!.onClickListener = object : LabelColorListItemsAdapter.OnClickListener{
            override fun onClick(position: Int, color: String) {
                dismiss()
                onItemSelected(color)
            }
        }
    }
    protected abstract fun onItemSelected(color: String)
}