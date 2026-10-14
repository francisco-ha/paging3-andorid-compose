package com.example.paging3_andorid_compose.data.response

import com.example.paging3_andorid_compose.presentationUi.model.CharacterModel
import com.google.gson.annotations.SerializedName

data class ResponseWrapper(
    @SerializedName("info") val information:InfoResponse,
    @SerializedName("results") val results: List<CharacterResponse>,

    ){}

data class InfoResponse(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages:Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
){}

data class CharacterResponse (
    @SerializedName("id") val id:Int,
    @SerializedName("name") val name:String,
    @SerializedName("status") val status:String,
    @SerializedName("image") val image: String
){
    fun toPresentation(): CharacterModel{
        return CharacterModel(
            id = id,
            name = name,
            image = image,
            isAlive = status=="Alive"
        )
    }
}