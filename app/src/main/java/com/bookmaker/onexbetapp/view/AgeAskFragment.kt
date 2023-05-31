package com.bookmaker.onexbetapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bookmaker.onexbetapp.R
import com.bookmaker.onexbetapp.common.systemUtils.sharedPrefs
import com.bookmaker.onexbetapp.databinding.FragmentAgeAskBinding

class AgeAskFragment: Fragment(
    R.layout.fragment_age_ask) {
    private lateinit var binding: FragmentAgeAskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentAgeAskBinding.inflate(inflater, container, false)
        binding.tvRefutAge.setOnClickListener {
            closeApp()
        }
        binding.tvConfirmAge.setOnClickListener {
            sharedPrefs.ageAskPassed = true
            navigateTo(R.id.conditionsAskFragment)
        }
        return binding.root
    }
}