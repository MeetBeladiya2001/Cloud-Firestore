package com.example.cloudfirestore

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cloudfirestore.databinding.AdapterViewBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class DataAdapter<StorageReference> : RecyclerView.Adapter<DataAdapter.MyViewHolder>() {

    private var dataList = emptyList<UserDataSend>()
    private var selectedPosition = RecyclerView.NO_POSITION
    var editEnabled = false

    init {
        fetchLatestData()
    }

    class MyViewHolder(var binding: AdapterViewBinding) : ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(AdapterViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data = dataList[position]
        val db = Firebase.firestore
        val id = data.id
        var ref = db.collection("cities").document(id)

        Picasso.get()
            .load(data.image)
            .resize(100, 100)
            .centerCrop()
            .into(holder.binding.profileIMG)

        holder.binding.nameTXT.setText(data.name)
        holder.binding.mailTXT.setText(data.email)
        holder.binding.mobileTXT.setText(data.mobile.toString())
        holder.binding.passTXT.setText(data.password)

        editDisabled(holder)

        holder.binding.editBTN.setOnClickListener {
            Log.d("deleteUser", "Successfully Deleted 2")
            resetPreviousSelection(holder, position)

            if (editEnabled == false) {
                editEnabled = true
                editEnabled(holder)
                holder.binding.editBTN.setText("Save")
            } else {
                editEnabled = false
                editDisabled(holder)
                holder.binding.editBTN.setText("Edit")

                // get updated texts
                val uName = holder.binding.nameTXT.text.toString()
                val uEmail = holder.binding.mailTXT.text.toString()
                val uMobile = holder.binding.mobileTXT.text.toString().toLong()
                val uPassword = holder.binding.nameTXT.text.toString()

                ref.update("name", uName)
                ref.update("email", uEmail)
                ref.update("mobile", uMobile)
                ref.update("password", uPassword)
            }
        }

            holder.binding.deleteBTN.setOnClickListener {
                val deleteUser = db.collection("cities").document(id)

                    deleteUser.delete()
                    .addOnSuccessListener {
                        Toast.makeText(holder.itemView.context,"Data Deleted Successfully",Toast.LENGTH_SHORT).show()
                        fetchLatestData()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(holder.itemView.context,"Error : $e",Toast.LENGTH_SHORT).show()
                    }
            }

    }

    private fun fetchLatestData() {
        val db = Firebase.firestore
        db.collection("cities").get()
            .addOnSuccessListener { result ->
                val newDataList = mutableListOf<UserDataSend>()
                for (document in result) {
                    val imageuri = document.getString("image") ?: ""
                    val id = document.getString("id") ?: ""
                    val name = document.getString("name") ?: ""
                    val email = document.getString("email") ?: ""
                    val mobile = document.getLong("mobile") ?: 0
                    val password = document.getString("password") ?: ""

                    val userData = UserDataSend(imageuri, id, name, email, mobile, password)
                    newDataList.addAll(listOf( userData)
                    )
                }
                setData(newDataList)
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: $exception")
            }
    }

    private fun resetPreviousSelection(holder: MyViewHolder, position: Int) {
        holder.binding.root.isSelected = position == selectedPosition
        if (selectedPosition != position) {
            val previousSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousSelectedPosition)
        }
    }

    private fun editDisabled(holder: MyViewHolder) {
        holder.binding.nameTXT.isEnabled = false
        holder.binding.mailTXT.isEnabled = false
        holder.binding.mobileTXT.isEnabled = false
        holder.binding.passTXT.isEnabled = false
    }

    private fun editEnabled(holder: MyViewHolder) {
        holder.binding.nameTXT.isEnabled = true
        holder.binding.mailTXT.isEnabled = true
        holder.binding.mobileTXT.isEnabled = true
        holder.binding.passTXT.isEnabled = true
    }

    fun setData(data: List<UserDataSend>) {
        dataList = data
        notifyDataSetChanged()
    }
}