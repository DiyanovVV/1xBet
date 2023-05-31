package com.bookmaker.onexbetapp.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bookmaker.onexbetapp.databinding.GameRvItemBinding

data class Event(val id: Long, val team1: String, val team2: String, var score1: String, var score2: String, val date: String, val time: String)
class MatchCenterGamesAdapter: ListAdapter<Event, MatchCenterGamesAdapter.EventViewHolder>(
    DiffCallback()
) {

    class EventViewHolder(private val binding: GameRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindTo(event: Event){
            with(binding){
                tvTeam1Name.text = event.team1
                tvTeam2Name.text = event.team2
                tvDate.text = event.date
                tvTime.text = event.time
                tvScore1.text = event.score1
                tvScore2.text = event.score2
            }
        }
    }

    class DiffCallback: DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean = oldItem.score1 == newItem.score1 && oldItem.score2 == newItem.score2

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder = EventViewHolder(
        GameRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}