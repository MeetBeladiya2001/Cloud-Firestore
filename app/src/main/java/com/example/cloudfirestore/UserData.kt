package com.example.cloudfirestore

import java.io.Serializable

data class UserData(
    val image: String,
    val name: String,
    val email: String,
    val mobile: Long,
    val password: String
): java.io.Serializable

data class UserDataSend(
    val image: String,
    val id : String,
    val name: String,
    val email: String,
    val mobile: Long,
    val password: String
): java.io.Serializable


