package com.bookmaker.onexbetapp.view

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.bookmaker.onexbetapp.R
import com.bookmaker.onexbetapp.databinding.FragmentLicenseAgreementShowBinding

class LicenseAgreementShowFragment: Fragment(R.layout.fragment_license_agreement_show) {
    private lateinit var binding: FragmentLicenseAgreementShowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentLicenseAgreementShowBinding.inflate(inflater, container, false)
        binding.ivBtnBack.setOnClickListener {
            navigateTo(R.id.conditionsAskFragment)
        }
        binding.tvTopAppName.text = "Приложение ${resources.getText(R.string.app_name)}"
        return binding.root
    }
}