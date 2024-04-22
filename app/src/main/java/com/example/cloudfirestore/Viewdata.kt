package com.example.cloudfirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cloudfirestore.databinding.ActivityViewdataBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Viewdata : AppCompatActivity() {
    private lateinit var binding: ActivityViewdataBinding
    private lateinit var adapter : DataAdapter<Any?>
    var dataSend = ArrayList<UserDataSend>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewdataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = DataAdapter()
        binding.recView.adapter = adapter
        binding.recView.layoutManager = LinearLayoutManager(this)

        val db = Firebase.firestore

//        db.collection("cities").get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    val data = document.data
//                    val id = data?.get("id") as String
//                    val name = data?.get("name") as String
//                    val email = data?.get("email") as String
//                    val mobile = data?.get("mobile") as Long
//                    val pass = data?.get("password") as String
//
//                    val List = UserDataSend(id,name,email,mobile,pass)
//                    dataSend.addAll(listOf(List))
//                    adapter.setData(dataSend)
//                    adapter.notifyDataSetChanged()
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d("TAG2", "Error getting documents.", exception)
//            }
    }
}