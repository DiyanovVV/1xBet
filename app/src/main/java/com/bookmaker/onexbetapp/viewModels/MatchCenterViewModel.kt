package com.bookmaker.onexbetapp.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bookmaker.onexbetapp.common.systemUtils.Constants.FOOTBALL_IDS
import com.bookmaker.onexbetapp.common.systemUtils.Constants.HOCKEY_IDS
import com.bookmaker.onexbetapp.common.systemUtils.Constants.SPORTS
import com.bookmaker.onexbetapp.model.playmakerApi.datamodels.EventsResponse
import com.bookmaker.onexbetapp.view.adapters.Event
import com.bookmaker.onexbetapp.view.adapters.League
import com.fonsport.bookmakerapp.model.repository.Repository
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.*

data class Sport(var live: MutableList<League>, var soon: MutableList<League>)

class MatchCenterViewModel : ViewModel() {
    private val c: Calendar = Calendar.getInstance()

    private val year = c.get(Calendar.YEAR)
    private val month = c.get(Calendar.MONTH) + 1
    private val day = c.get(Calendar.DAY_OF_MONTH)

    private val hour = c.get(Calendar.HOUR_OF_DAY)
    private val minute = c.get(Calendar.MINUTE)

    private val currentUnix: LocalDateTime = LocalDateTime.of(year, month, day, hour, minute)

    private val _errors: MutableLiveData<String> = MutableLiveData()
    val errors: LiveData<String>
        get() = _errors

    var soonUpdated = false
    private val _events: MutableLiveData<Map<String, Sport>> = MutableLiveData()
    val events: LiveData<Map<String, Sport>>
        get() = _events
    private var resultData = mapOf(
        SPORTS[0] to Sport(mutableListOf(), mutableListOf()),
        SPORTS[1] to Sport(mutableListOf(), mutableListOf()),
        SPORTS[2] to Sport(mutableListOf(), mutableListOf()),
        SPORTS[3] to Sport(mutableListOf(), mutableListOf()),
        SPORTS[4] to Sport(mutableListOf(), mutableListOf())
    )
    private var liveIds = mutableListOf<Long>()

    fun updateData() {
        resultData = mapOf(
            SPORTS[0] to Sport(mutableListOf(), mutableListOf()),
            SPORTS[1] to Sport(mutableListOf(), mutableListOf()),
            SPORTS[2] to Sport(mutableListOf(), mutableListOf()),
            SPORTS[3] to Sport(mutableListOf(), mutableListOf()),
            SPORTS[4] to Sport(mutableListOf(), mutableListOf())
        )

        viewModelScope.launch {
            val allEvents = Repository.playmaker.getGames()
            if (allEvents.isSuccessful) {
                for (i in allEvents.body()!!) {
                    try {
                        when (i.sport.name) {
                            // FOOTBALL
                            SPORTS[0] -> {
                                if (i.liveId == 0.toLong()) { // If event not live
                                    val date =
                                        i.liveDatetime.split("T")[0].split("-") + i.liveDatetime.split(
                                            "T"
                                        )[1].split(":")
                                    val dateInUnix = LocalDateTime.of(
                                        date[0].toInt(),
                                        date[1].toInt(),
                                        date[2].toInt(),
                                        date[3].toInt(),
                                        date[4].toInt()
                                    )
                                    val difference =
                                        ChronoUnit.MINUTES.between(currentUnix, dateInUnix)
                                    val differenceDays = ChronoUnit.DAYS.between(
                                        currentUnix.toLocalDate().atStartOfDay(),
                                        dateInUnix.toLocalDate().atStartOfDay()
                                    )

                                    // check time
                                    if (difference > 0 && differenceDays <= 3) {
                                        val index =
                                            resultData[SPORTS[0]]!!.soon.map { league -> league.id }
                                                .indexOf(i.tournament.id)
                                        if (index != -1) { // if league already contains
                                            resultData[SPORTS[0]]!!.soon[index].list.add(
                                                Event(
                                                    i.prematchId,
                                                    i.teams[0],
                                                    i.teams[1],
                                                    "-",
                                                    "-",
                                                    "${date[2]}/${date[1]}",
                                                    "${date[3]}:${date[4]}"
                                                )
                                            )
                                        } else if (FOOTBALL_IDS.contains(i.tournament.id)) { // if league in top list
                                            resultData[SPORTS[0]]!!.soon.add(
                                                League(
                                                    i.tournament.id,
                                                    i.tournament.name,
                                                    i.category.name,
                                                    mutableListOf(
                                                        Event(
                                                            i.prematchId,
                                                            i.teams[0],
                                                            i.teams[1],
                                                            "-",
                                                            "-",
                                                            "${date[2]}/${date[1]}",
                                                            "${date[3]}:${date[4]}"
                                                        )
                                                    )
                                                )
                                            )
                                        } else if (resultData[SPORTS[0]]!!.soon.size < 200) { // if we can add one league
                                            resultData[SPORTS[0]]!!.soon.add(
                                                League(
                                                    i.tournament.id,
                                                    i.tournament.name,
                                                    i.category.name,
                                                    mutableListOf(
                                                        Event(
                                                            i.prematchId,
                                                            i.teams[0],
                                                            i.teams[1],
                                                            "-",
                                                            "-",
                                                            "${date[2]}/${date[1]}",
                                                            "${date[3]}:${date[4]}"
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                    }
                                } else { // if event is live
                                    liveIds.add(i.liveId)
                                }
                            }

                            // HOCKEY
                            SPORTS[1] -> {
                                if (i.liveId == 0.toLong()) { // If event not live
                                    val date =
                                        i.liveDatetime.split("T")[0].split("-") + i.liveDatetime.split(
                                            "T"
                                        )[1].split(":")
                                    val dateInUnix = LocalDateTime.of(
                                        date[0].toInt(),
                                        date[1].toInt(),
                                        date[2].toInt(),
                                        date[3].toInt(),
                                        date[4].toInt()
                                    )
                                    val difference =
                                        ChronoUnit.MINUTES.between(currentUnix, dateInUnix)
                                    val differenceDays = ChronoUnit.DAYS.between(
                                        currentUnix.toLocalDate().atStartOfDay(),
                                        dateInUnix.toLocalDate().atStartOfDay()
                                    )

                                    // check time
                                    if (difference > 0 && differenceDays <= 3) {
                                        val index =
                                            resultData[SPORTS[1]]!!.soon.map { league -> league.id }
                                                .indexOf(i.tournament.id)
                                        if (index != -1) { // if league already contains
                                            resultData[SPORTS[1]]!!.soon[index].list.add(
                                                Event(
                                                    i.prematchId,
                                                    i.teams[0],
                                                    i.teams[1],
                                                    "-",
                                                    "-",
                                                    "${date[2]}/${date[1]}",
                                                    "${date[3]}:${date[4]}"
                                                )
                                            )
                                        } else if (HOCKEY_IDS.contains(i.tournament.id)) { // if league in top list
                                            resultData[SPORTS[1]]!!.soon.add(
                                                League(
                                                    i.tournament.id,
                                                    i.tournament.name,
                                                    i.category.name,
                                                    mutableListOf(
                                                        Event(
                                                            i.prematchId,
                                                            i.teams[0],
                                                            i.teams[1],
                                                            "-",
                                                            "-",
                                                            "${date[2]}/${date[1]}",
                                                            "${date[3]}:${date[4]}"
                                                        )
                                                    )
                                                )
                                            )
                                        } else if (resultData[SPORTS[1]]!!.soon.size < 200) { // if we can add one league
                                            resultData[SPORTS[1]]!!.soon.add(
                                                League(
                                                    i.tournament.id,
                                                    i.tournament.name,
                                                    i.category.name,
                                                    mutableListOf(
                                                        Event(
                                                            i.prematchId,
                                                            i.teams[0],
                                                            i.teams[1],
                                                            "-",
                                                            "-",
                                                            "${date[2]}/${date[1]}",
                                                            "${date[3]}:${date[4]}"
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                    }
                                } else { // if event is live
                                    liveIds.add(i.liveId)
                                }
                            }

                            // BASKETBALL
                            SPORTS[2] -> {
                                if (i.liveId == 0.toLong()) { // If event not live
                                    val date =
                                        i.liveDatetime.split("T")[0].split("-") + i.liveDatetime.split(
                                            "T"
                                        )[1].split(":")
                                    val dateInUnix = LocalDateTime.of(
                                        date[0].toInt(),
                                        date[1].toInt(),
                                        date[2].toInt(),
                                        date[3].toInt(),
                                        date[4].toInt()
                                    )
                                    val difference =
                                        ChronoUnit.MINUTES.between(currentUnix, dateInUnix)
                                    val differenceDays = ChronoUnit.DAYS.between(
                                        currentUnix.toLocalDate().atStartOfDay(),
                                        dateInUnix.toLocalDate().atStartOfDay()
                                    )

                                    // check time
                                    if (difference > 0 && differenceDays <= 3) {
                                        val index =
                                            resultData[SPORTS[2]]!!.soon.map { league -> league.id }
                                                .indexOf(i.tournament.id)
                                        if (index != -1) { // if league already contains
                                            resultData[SPORTS[2]]!!.soon[index].list.add(
                                                Event(
                                                    i.prematchId,
                                                    i.teams[0],
                                                    i.teams[1],
                                                    "-",
                                                    "-",
                                                    "${date[2]}/${date[1]}",
                                                    "${date[3]}:${date[4]}"
                                                )
                                            )
                                        } else if (resultData[SPORTS[2]]!!.soon.size < 200) { // if we can add one league
                                            resultData[SPORTS[2]]!!.soon.add(
                                                League(
                                                    i.tournament.id,
                                                    i.tournament.name,
                                                    i.category.name,
                                                    mutableListOf(
                                                        Event(
                                                            i.prematchId,
                                                            i.teams[0],
                                                            i.teams[1],
                                                            "-",
                                                            "-",
                                                            "${date[2]}/${date[1]}",
                                                            "${date[3]}:${date[4]}"
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                    }
                                } else { // if event is live
                                    liveIds.add(i.liveId)
                                }
                            }

                            // TENNIS
                            SPORTS[3] -> {
                                if (i.liveId == 0.toLong()) { // If event not live
                                    val date =
                                        i.liveDatetime.split("T")[0].split("-") + i.liveDatetime.split(
                                            "T"
                                        )[1].split(":")
                                    val dateInUnix = LocalDateTime.of(
                                        date[0].toInt(),
                                        date[1].toInt(),
                                        date[2].toInt(),
                                        date[3].toInt(),
                                        date[4].toInt()
                                    )
                                    val difference =
                                        ChronoUnit.MINUTES.between(currentUnix, dateInUnix)
                                    val differenceDays = ChronoUnit.DAYS.between(
                                        currentUnix.toLocalDate().atStartOfDay(),
                                        dateInUnix.toLocalDate().atStartOfDay()
                                    )

                                    // check time
                                    if (difference > 0 && differenceDays <= 3) {
                                        val index =
                                            resultData[SPORTS[3]]!!.soon.map { league -> league.id }
                                                .indexOf(i.tournament.id)
                                        if (index != -1) { // if league already contains
                                            resultData[SPORTS[3]]!!.soon[index].list.add(
                                                Event(
                                                    i.prematchId,
                                                    i.teams[0],
                                                    i.teams[1],
                                                    "-",
                                                    "-",
                                                    "${date[2]}/${date[1]}",
                                                    "${date[3]}:${date[4]}"
                                                )
                                            )
                                        } else if (resultData[SPORTS[3]]!!.soon.size < 200) { // if we can add one league
                                            resultData[SPORTS[3]]!!.soon.add(
                                                League(
                                                    i.tournament.id,
                                                    i.tournament.name,
                                                    i.category.name,
                                                    mutableListOf(
                                                        Event(
                                                            i.prematchId,
                                                            i.teams[0],
                                                            i.teams[1],
                                                            "-",
                                                            "-",
                                                            "${date[2]}/${date[1]}",
                                                            "${date[3]}:${date[4]}"
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                    }
                                } else { // if event is live
                                    liveIds.add(i.liveId)
                                }
                            }

                            // TABLE TENNIS
                            SPORTS[4] -> {
                                if (i.liveId == 0.toLong()) { // If event not live
                                    val date =
                                        i.liveDatetime.split("T")[0].split("-") + i.liveDatetime.split(
                                            "T"
                                        )[1].split(":")
                                    val dateInUnix = LocalDateTime.of(
                                        date[0].toInt(),
                                        date[1].toInt(),
                                        date[2].toInt(),
                                        date[3].toInt(),
                                        date[4].toInt()
                                    )
                                    val difference =
                                        ChronoUnit.MINUTES.between(currentUnix, dateInUnix)
                                    val differenceDays = ChronoUnit.DAYS.between(
                                        currentUnix.toLocalDate().atStartOfDay(),
                                        dateInUnix.toLocalDate().atStartOfDay()
                                    )

                                    // check time
                                    if (difference > 0 && differenceDays <= 3) {
                                        val index =
                                            resultData[SPORTS[4]]!!.soon.map { league -> league.id }
                                                .indexOf(i.tournament.id)
                                        if (index != -1) { // if league already contains
                                            resultData[SPORTS[4]]!!.soon[index].list.add(
                                                Event(
                                                    i.prematchId,
                                                    i.teams[0],
                                                    i.teams[1],
                                                    "-",
                                                    "-",
                                                    "${date[2]}/${date[1]}",
                                                    "${date[3]}:${date[4]}"
                                                )
                                            )
                                        } else if (resultData[SPORTS[4]]!!.soon.size < 200) { // if we can add one league
                                            resultData[SPORTS[4]]!!.soon.add(
                                                League(
                                                    i.tournament.id,
                                                    i.tournament.name,
                                                    i.category.name,
                                                    mutableListOf(
                                                        Event(
                                                            i.prematchId,
                                                            i.teams[0],
                                                            i.teams[1],
                                                            "-",
                                                            "-",
                                                            "${date[2]}/${date[1]}",
                                                            "${date[3]}:${date[4]}"
                                                        )
                                                    )
                                                )
                                            )
                                        }
                                    }
                                } else { // if event is live
                                    liveIds.add(i.liveId)
                                }
                            }
                        }
                    } catch (e: NullPointerException) {
                    }
                }
                val response = Repository.playmaker.getOtherInfo(liveIds)
                if (response.isSuccessful) {
                    for (ev in response.body()!!.result) {
                        when (SPORTS.indexOf(ev.gameTitle)) {
                            0 -> {
                                val date =
                                    ev.event.gameDt.split(" ")[0].split("-") + ev.event.gameDt.split(
                                        " "
                                    )[1].split(".")[0].split(":")
                                val index = resultData[SPORTS[0]]!!.live.map { league -> league.id }
                                    .indexOf(ev.ids.tournamentId)
                                if (index != -1) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[0]]!!.live[index].list.add(
                                            Event(
                                                ev.id,
                                                ev.event.team1,
                                                ev.event.team2,
                                                score1,
                                                score2,
                                                "${date[2]}/${date[1]}",
                                                "${date[3]}:${date[4]}"
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                } else if (FOOTBALL_IDS.contains(ev.ids.tournamentId)) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[0]]!!.live.add(
                                            League(
                                                ev.ids.tournamentId,
                                                ev.tournamentTitle,
                                                ev.categoryTitle,
                                                mutableListOf(
                                                    Event(
                                                        ev.id,
                                                        ev.event.team1,
                                                        ev.event.team2,
                                                        score1,
                                                        score2,
                                                        "${date[2]}/${date[1]}",
                                                        "${date[3]}:${date[4]}"
                                                    )
                                                )
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                } else if (resultData[SPORTS[0]]!!.live.size < 200) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[0]]!!.live.add(
                                            League(
                                                ev.ids.tournamentId,
                                                ev.tournamentTitle,
                                                ev.categoryTitle,
                                                mutableListOf(
                                                    Event(
                                                        ev.id,
                                                        ev.event.team1,
                                                        ev.event.team2,
                                                        score1,
                                                        score2,
                                                        "${date[2]}/${date[1]}",
                                                        "${date[3]}:${date[4]}"
                                                    )
                                                )
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                            1 -> {
                                val date =
                                    ev.event.gameDt.split(" ")[0].split("-") + ev.event.gameDt.split(
                                        " "
                                    )[1].split(".")[0].split(":")
                                val index = resultData[SPORTS[1]]!!.live.map { league -> league.id }
                                    .indexOf(ev.ids.tournamentId)
                                if (index != -1) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[1]]!!.live[index].list.add(
                                            Event(
                                                ev.id,
                                                ev.event.team1,
                                                ev.event.team2,
                                                score1,
                                                score2,
                                                "${date[2]}/${date[1]}",
                                                "${date[3]}:${date[4]}"
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                } else if (FOOTBALL_IDS.contains(ev.ids.tournamentId)) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[1]]!!.live.add(
                                            League(
                                                ev.ids.tournamentId,
                                                ev.tournamentTitle,
                                                ev.categoryTitle,
                                                mutableListOf(
                                                    Event(
                                                        ev.id,
                                                        ev.event.team1,
                                                        ev.event.team2,
                                                        score1,
                                                        score2,
                                                        "${date[2]}/${date[1]}",
                                                        "${date[3]}:${date[4]}"
                                                    )
                                                )
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                } else if (resultData[SPORTS[1]]!!.live.size < 200) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[1]]!!.live.add(
                                            League(
                                                ev.ids.tournamentId,
                                                ev.tournamentTitle,
                                                ev.categoryTitle,
                                                mutableListOf(
                                                    Event(
                                                        ev.id,
                                                        ev.event.team1,
                                                        ev.event.team2,
                                                        score1,
                                                        score2,
                                                        "${date[2]}/${date[1]}",
                                                        "${date[3]}:${date[4]}"
                                                    )
                                                )
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                            2 -> {
                                val date =
                                    ev.event.gameDt.split(" ")[0].split("-") + ev.event.gameDt.split(
                                        " "
                                    )[1].split(".")[0].split(":")
                                val index = resultData[SPORTS[2]]!!.live.map { league -> league.id }
                                    .indexOf(ev.ids.tournamentId)
                                if (index != -1) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[2]]!!.live[index].list.add(
                                            Event(
                                                ev.id,
                                                ev.event.team1,
                                                ev.event.team2,
                                                score1,
                                                score2,
                                                "${date[2]}/${date[1]}",
                                                "${date[3]}:${date[4]}"
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                } else if (resultData[SPORTS[2]]!!.live.size < 200) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[2]]!!.live.add(
                                            League(
                                                ev.ids.tournamentId,
                                                ev.tournamentTitle,
                                                ev.categoryTitle,
                                                mutableListOf(
                                                    Event(
                                                        ev.id,
                                                        ev.event.team1,
                                                        ev.event.team2,
                                                        score1,
                                                        score2,
                                                        "${date[2]}/${date[1]}",
                                                        "${date[3]}:${date[4]}"
                                                    )
                                                )
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                            3 -> {
                                val date =
                                    ev.event.gameDt.split(" ")[0].split("-") + ev.event.gameDt.split(
                                        " "
                                    )[1].split(".")[0].split(":")
                                val index = resultData[SPORTS[3]]!!.live.map { league -> league.id }
                                    .indexOf(ev.ids.tournamentId)
                                if (index != -1) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[3]]!!.live[index].list.add(
                                            Event(
                                                ev.id,
                                                ev.event.team1,
                                                ev.event.team2,
                                                score1,
                                                score2,
                                                "${date[2]}/${date[1]}",
                                                "${date[3]}:${date[4]}"
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                } else if (resultData[SPORTS[3]]!!.live.size < 200) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[3]]!!.live.add(
                                            League(
                                                ev.ids.tournamentId,
                                                ev.tournamentTitle,
                                                ev.categoryTitle,
                                                mutableListOf(
                                                    Event(
                                                        ev.id,
                                                        ev.event.team1,
                                                        ev.event.team2,
                                                        score1,
                                                        score2,
                                                        "${date[2]}/${date[1]}",
                                                        "${date[3]}:${date[4]}"
                                                    )
                                                )
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                            4 -> {
                                val date =
                                    ev.event.gameDt.split(" ")[0].split("-") + ev.event.gameDt.split(
                                        " "
                                    )[1].split(".")[0].split(":")
                                val index = resultData[SPORTS[4]]!!.live.map { league -> league.id }
                                    .indexOf(ev.ids.tournamentId)
                                if (index != -1) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[4]]!!.live[index].list.add(
                                            Event(
                                                ev.id,
                                                ev.event.team1,
                                                ev.event.team2,
                                                score1,
                                                score2,
                                                "${date[2]}/${date[1]}",
                                                "${date[3]}:${date[4]}"
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                } else if (resultData[SPORTS[4]]!!.live.size < 200) {
                                    try {
                                        val score1 = ev.scores.total.ScoreTeam1
                                        val score2 = ev.scores.total.ScoreTeam2
                                        resultData[SPORTS[4]]!!.live.add(
                                            League(
                                                ev.ids.tournamentId,
                                                ev.tournamentTitle,
                                                ev.categoryTitle,
                                                mutableListOf(
                                                    Event(
                                                        ev.id,
                                                        ev.event.team1,
                                                        ev.event.team2,
                                                        score1,
                                                        score2,
                                                        "${date[2]}/${date[1]}",
                                                        "${date[3]}:${date[4]}"
                                                    )
                                                )
                                            )
                                        )
                                    } catch (e: Exception) {
                                    }
                                }
                            }
                        }
                    }
                } else {
                    _errors.value = allEvents.code().toString()
                }
            } else {
                _errors.value = allEvents.code().toString()
            }
            _events.value = resultData
        }
    }
}