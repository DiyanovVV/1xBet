package com.bookmaker.onexbetapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bookmaker.onexbetapp.common.systemUtils.Constants.SPORTS
import com.bookmaker.onexbetapp.databinding.ActivityMatchCenterBinding
import com.bookmaker.onexbetapp.view.adapters.Folder
import com.bookmaker.onexbetapp.view.adapters.MatchCenterLeaguesAdapter
import com.bookmaker.onexbetapp.viewModels.MatchCenterViewModel
import com.bookmaker.onexbetapp.viewModels.Sport
import com.google.android.material.tabs.TabLayout

class MatchCenterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMatchCenterBinding
    private lateinit var viewModel: MatchCenterViewModel
    private var sport = 0
    private var time = 0
    private var soon: Boolean = false
    private lateinit var events: Map<String, Sport>
    private lateinit var adapter: MatchCenterLeaguesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatchCenterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = MatchCenterViewModel()
        viewModel.updateData()
        adapter = MatchCenterLeaguesAdapter()
        binding.rvLeagues.adapter = adapter
        binding.pbLoadStatus.max = 100
        viewModel.errors.observe(this){
            Toast.makeText(this, "Network error - $it", Toast.LENGTH_SHORT).show()
        }
        viewModel.events.observe(this){
            events = it
            soon = viewModel.soonUpdated
            viewModel.soonUpdated = false
            dataInRvUpdate()
            binding.tvLoadStatus.visibility = View.GONE
            binding.pbLoadStatus.visibility = View.GONE
            binding.rvLeagues.visibility = View.VISIBLE
            binding.tlSports.visibility = View.VISIBLE
            binding.tlTime.visibility = View.VISIBLE
        }
        binding.tlSports.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                sport = tab.position
                dataInRvUpdate(true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        binding.tlTime.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                time = tab.position
                dataInRvUpdate(true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun dataInRvUpdate(doScroll: Boolean = false){
        if (time == 0){
            adapter.submitList(events[SPORTS[sport]]!!.live)
        } else{
            if(soon || doScroll){
                soon = false
                adapter.submitList(events[SPORTS[sport]]!!.soon)
            }
        }
        if (doScroll){
            binding.rvLeagues.scrollToPosition(0)
            Folder.leaguesUnfoldedIds = mutableListOf()
        } else{
            for (i in adapter.currentList.indices){
                adapter.notifyItemChanged(i)
            }
        }
    }
}