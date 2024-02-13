package com.example.cloudfirestore

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cloudfirestore.databinding.ActivityMainBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Firebase.firestore
        var ref = db.collection("cities")

        val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            if (it.resultCode == Activity.RESULT_OK) {
//                Log.d("meet","Result code is : "+it.resultCode.toString())
                if (it.data != null) {
//                    Log.d("meet","Data Is : "+it.data.toString())
                    val ref = Firebase.storage.reference.child("ProfilePhotos/"+System.currentTimeMillis()+generateRandomString(10))
//                    Log.d("secondss",System.currentTimeMillis().toString())
                    ref.putFile(it.data!!.data!!).addOnSuccessListener {
//                        Log.d("meet","Image Data Is : "+it.toString())
                        Toast.makeText(this,"Successfully Uploaded",Toast.LENGTH_SHORT).show()
                        ref.downloadUrl.addOnSuccessListener {
                            imageUri = it.toString()
                            Picasso.get()
                                .load(imageUri)
                                .resize(100, 100)
                                .centerCrop()
                                .into(binding.uploadPhoto)
                        }
                    }
                }
            }
        }

        binding.uploadBTN.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imageLauncher.launch(intent)
            Log.d("meeet","intent : $intent")
        }


        binding.button.setOnClickListener {

            val name = binding.editTextText.text.toString()
            val email = binding.editTextTextEmailAddress.text.toString()
            val mobile = binding.editTextPhone.text.toString().toLong()
            val pass = binding.editTextText2.text.toString()
            Log.d("imageuri",imageUri.toString())

            val addData = UserData(imageUri.toString(),name, email, mobile, pass)
            val ref2 = ref.document()

            ref2
                .set(addData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Inserted", Toast.LENGTH_SHORT).show()

                    // update and add unique id
                    ref2.update("id",ref2.id)
                        .addOnSuccessListener {
                            Log.d("uiiid", ref.id)
                        }
                        .addOnFailureListener { updateFailResult ->
                            Log.d("abcd", "Failed to update document with ID: ${ref.id}, $updateFailResult")
                        }
                }
                .addOnFailureListener { failResult ->
                    Toast.makeText(this, "Failed To Insert $failResult", Toast.LENGTH_SHORT).show()
                    Log.d("abcd", failResult.toString())
                }
        }

        binding.dataView.setOnClickListener {
            startActivity(Intent(this,Viewdata::class.java))
        }


    }

    fun generateRandomString(length: Int): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}