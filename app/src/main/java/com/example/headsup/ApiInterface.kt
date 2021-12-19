package com.example.headsup

import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @GET("celebrities/")
    fun getData():Call<celeb>

    @POST( "celebrities/")
    fun addData(@Body userData:celebItem): retrofit2.Call<celebItem>

    @PUT( "celebrities/{id}")
    fun updateData(@Path("id") id :Int, @Body userData:celebItem): retrofit2.Call<celebItem>

    @DELETE( "celebrities/{id}")
    fun deleteData(@Path ("id") id :Int):Call<Void>
}