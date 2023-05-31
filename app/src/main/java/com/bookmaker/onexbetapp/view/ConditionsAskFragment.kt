package com.bookmaker.onexbetapp.view

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bookmaker.onexbetapp.R
import com.bookmaker.onexbetapp.common.systemUtils.sharedPrefs
import com.bookmaker.onexbetapp.databinding.FragmentConditionsAskBinding
import com.bookmaker.onexbetapp.view.utils.UnitSpan

class ConditionsAskFragment: Fragment(R.layout.fragment_conditions_ask) {
    private lateinit var binding: FragmentConditionsAskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentConditionsAskBinding.inflate(inflater, container, false)

        val textFromTv = binding.tvConditions.text
        val spannedText = SpannableString(textFromTv)
        val textPrivacyPolicy = "политику конфиденциальности"
        val textLicenseAgreement = "лицензионное соглашение"
        var indexOfSpanningText = spannedText.indexOf(textPrivacyPolicy)
        spannedText.setSpan(UnitSpan(requireContext()){navigateTo(R.id.privacyPolicyShowFragment)}, indexOfSpanningText, indexOfSpanningText + textPrivacyPolicy.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        indexOfSpanningText = spannedText.indexOf(textLicenseAgreement)
        spannedText.setSpan(UnitSpan(requireContext()){navigateTo(R.id.licenseAgreementShowFragment)}, indexOfSpanningText, indexOfSpanningText + textLicenseAgreement.length, Spanned.SPAN_EXCLUSIVE_INCLUSIVE)
        binding.tvConditions.text = spannedText
        binding.tvConditions.movementMethod = LinkMovementMethod.getInstance()
        binding.btnAccept.setOnClickListener {
            sharedPrefs.rulesConfirm = true
            Log.d("test", sharedPrefs.rulesConfirm.toString())
            navigateTo(R.id.mainFragment)
        }
        binding.btnDismiss.setOnClickListener {
            closeApp()
        }
        return binding.root
    }
}