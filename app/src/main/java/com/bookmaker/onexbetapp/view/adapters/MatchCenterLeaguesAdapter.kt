package com.bookmaker.onexbetapp.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bookmaker.onexbetapp.R
import com.bookmaker.onexbetapp.databinding.LeagueRvItemBinding
import com.bookmaker.onexbetapp.view.adapters.Folder.leaguesUnfoldedIds


data class League(val id: Long, var name: String, val country: String, val list: MutableList<Event>)


object Folder{
    var leaguesUnfoldedIds = mutableListOf<Long>()
}


class MatchCenterLeaguesAdapter : ListAdapter<League, MatchCenterLeaguesAdapter.LeagueViewHolder>(
    DiffCallBack()
) {

    class LeagueViewHolder(private val binding: LeagueRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bindTo(league: League){
            with(binding){
                tvLeagueName.text = league.name
                val adapter = MatchCenterGamesAdapter()
                rvGames.adapter = adapter
                tvCountryName.text = league.country
                if (leaguesUnfoldedIds.contains(league.id)){
                    btnShowMatches.setImageResource(R.drawable.arrow_up)
                    rvGames.visibility = View.VISIBLE
                    adapter.submitList(league.list)
                } else{
                    btnShowMatches.setImageResource(R.drawable.arrow_down)
                    rvGames.visibility = View.INVISIBLE
                    adapter.submitList(mutableListOf())
                }
                root.setOnClickListener {
                    if(leaguesUnfoldedIds.contains(league.id)){
                        val index = leaguesUnfoldedIds.indexOf(league.id)
                        if(index != -1){
                            leaguesUnfoldedIds.removeAt(index)
                        }
                    } else{
                        leaguesUnfoldedIds.add(league.id)
                    }
                    if (leaguesUnfoldedIds.contains(league.id)){
                        btnShowMatches.setImageResource(R.drawable.arrow_up)
                        rvGames.visibility = View.VISIBLE
                        adapter.submitList(league.list)
                    } else{
                        btnShowMatches.setImageResource(R.drawable.arrow_down)
                        rvGames.visibility = View.INVISIBLE
                        adapter.submitList(mutableListOf())
                    }
                }
            }
        }
    }


    class DiffCallBack: DiffUtil.ItemCallback<League>() {
        override fun areItemsTheSame(oldItem: League, newItem: League): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: League, newItem: League): Boolean = oldItem.name == newItem.name && oldItem.list == newItem.list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder = LeagueViewHolder(LeagueRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }
}