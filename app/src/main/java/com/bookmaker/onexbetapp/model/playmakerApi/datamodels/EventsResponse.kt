package com.bookmaker.onexbetapp.model.playmakerApi.datamodels

data class EventsResponse(
    val category: Category,
    val id: Long,
    val liveDatetime: String,
    val liveId: Long,
    val liveUrl: String,
    val prematchId: Long,
    val prematchUrl: String,
    val sport: Sport,
    val teams: List<String>,
    val tournament: Tournament
)