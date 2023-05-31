package com.bookmaker.onexbetapp.model.playmakerApi

import com.bookmaker.onexbetapp.common.systemUtils.Constants.BASE_API_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PlaymakerRetrofitInstance {
    private val retrofit = Retrofit.Builder().baseUrl(BASE_API_URL).addConverterFactory(GsonConverterFactory.create()).build()

    val api: PlaymakerService = retrofit.create(PlaymakerService::class.java)
}