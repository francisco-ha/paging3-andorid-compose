package com.example.paging3_andorid_compose.data

import com.example.paging3_andorid_compose.data.response.ResponseWrapper
import retrofit2.http.GET
import retrofit2.http.Query

interface RMApiService {

    @GET("/api/character/")
    suspend fun getCharacters(@Query("page") page:Int): ResponseWrapper

}