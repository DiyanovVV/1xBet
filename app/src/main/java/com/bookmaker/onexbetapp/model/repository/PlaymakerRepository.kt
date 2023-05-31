package com.bookmaker.onexbetapp.model.repository

import com.bookmaker.onexbetapp.common.systemUtils.Constants.API_KEY
import com.bookmaker.onexbetapp.model.playmakerApi.PlaymakerRetrofitInstance
import com.bookmaker.onexbetapp.model.playmakerApi.datamodels.EventsResponse
import com.bookmaker.onexbetapp.model.playmakerApi.datamodels.IdPostBody
import com.bookmaker.onexbetapp.model.playmakerApi.datamodels.ScoreInfo
import retrofit2.Response

class PlaymakerRepository () {
    suspend fun getGames(): Response<List<EventsResponse>> {
        return PlaymakerRetrofitInstance.api.getGames(API_KEY)
    }

    suspend fun getOtherInfo(id: Long): Response<ScoreInfo>{
        return PlaymakerRetrofitInstance.api.getOtherGameInfo(API_KEY, IdPostBody(id))
    }
}