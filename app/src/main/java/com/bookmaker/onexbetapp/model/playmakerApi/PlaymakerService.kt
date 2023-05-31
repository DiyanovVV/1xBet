package com.bookmaker.onexbetapp.model.playmakerApi

import com.bookmaker.onexbetapp.model.playmakerApi.datamodels.EventsResponse
import com.bookmaker.onexbetapp.model.playmakerApi.datamodels.IdPostBody
import com.bookmaker.onexbetapp.model.playmakerApi.datamodels.ScoreInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface PlaymakerService {
    @GET("widget-api/Events")
    suspend fun getGames(@Query("key") key: String): Response<List<EventsResponse>>

    @POST("lds-api/actionLine")
    suspend fun getOtherGameInfo(@Query("key") key: String, @Body idPostBody: IdPostBody): Response<ScoreInfo>
}