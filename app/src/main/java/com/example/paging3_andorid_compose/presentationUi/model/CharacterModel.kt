package com.example.paging3_andorid_compose.presentationUi.model

import com.google.gson.annotations.SerializedName

data class CharacterModel(
    val id:Int,
    val name:String,
    val isAlive: Boolean,
    val image: String
)
