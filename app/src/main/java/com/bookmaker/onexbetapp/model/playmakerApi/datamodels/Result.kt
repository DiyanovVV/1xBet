package com.bookmaker.onexbetapp.model.playmakerApi.datamodels


data class Result(
    val id: Long,
    val ids: Ids,
    val gameTitle: String,
    val categoryTitle: String,
    val tournamentTitle: String,
    val event: Event,
    val scores: Scores
)