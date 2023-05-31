package com.bookmaker.onexbetapp.view

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.fragment.app.Fragment
import com.bookmaker.onexbetapp.R
import com.bookmaker.onexbetapp.databinding.FragmentPrivacyPolicyShowBinding

class PrivacyPolicyShowFragment: Fragment(R.layout.fragment_privacy_policy_show) {
    private lateinit var binding: FragmentPrivacyPolicyShowBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentPrivacyPolicyShowBinding.inflate(inflater, container, false)
        binding.wvPrivacyPolicy.settings.javaScriptEnabled = true
        binding.wvPrivacyPolicy.settings.loadWithOverviewMode = true
        binding.wvPrivacyPolicy.settings.useWideViewPort = true
        binding.wvPrivacyPolicy.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                val removeNotificationApp = """
                                function removeElementsByClass(className){
                                    const elements = document.getElementsByClassName(className);
                                    while(elements.length > 0){
                                        elements[0].parentNode.removeChild(elements[0]);
                                    }
                                }
                                removeElementsByClass("docs-ml-promotion docs-ml-promotion-off-screen docs-ml-promotion-shown");
                                """.trimIndent()

                val turnOffEditInterface = """
                    function removeElementsByClass(className){
                                    const elements = document.getElementsByClassName(className);
                                    elements[0].parentNode.removeChild(elements[0]);
                                }
                    removeElementsByClass("docs-ml-header");
                """.trimIndent()
                binding.pbLoadPrivacyPolicy.visibility = View.GONE
                binding.tvLoadPrivacyPolicy.visibility = View.GONE
                binding.wvPrivacyPolicy.visibility = View.VISIBLE
                view.loadUrl("javascript:$turnOffEditInterface")
                object: CountDownTimer(3000, 10){
                    override fun onTick(millisUntilFinished: Long) {
                        view.loadUrl("javascript:$removeNotificationApp")
                    }

                    override fun onFinish() {
                        view.loadUrl("javascript:$removeNotificationApp")
                    }

                }.start()
            }
        }
        binding.wvPrivacyPolicy.loadUrl("https://docs.google.com/document/d/12Rup9oaZFp0kJljmOL27e7zjUar3kbX6GZm4bLCi2HY/edit?usp=sharing")
        Log.d("test", binding.wvPrivacyPolicy.progress.toString())
        binding.ivBtnBack.setOnClickListener {
            navigateTo(R.id.conditionsAskFragment)
        }
        binding.tvTopAppName.text = "Приложение ${resources.getText(R.string.app_name)}"
        return binding.root
    }
}