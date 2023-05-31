package com.bookmaker.onexbetapp.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bookmaker.onexbetapp.R
import com.bookmaker.onexbetapp.common.systemUtils.sharedPrefs
import com.bookmaker.onexbetapp.databinding.FragmentMainBinding

class MainFragment: Fragment(R.layout.fragment_main) {
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.btnFonSite.setOnClickListener {
            startActivity(Intent(requireActivity(), WebActivity::class.java))
        }
        binding.btnMatchCenter.setOnClickListener {
            startActivity(Intent(requireActivity(), MatchCenterActivity::class.java))
        }
        if (regionIsRu && sharedPrefs.link != ""){
            binding.cvMainScreen.visibility = View.VISIBLE
        } else{
            binding.cvMainScreen.visibility = View.GONE
        }
        return binding.root
    }
}