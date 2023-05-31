package com.bookmaker.onexbetapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.bookmaker.onexbetapp.common.systemUtils.sharedPrefs
import com.bookmaker.onexbetapp.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CookieManager.getInstance().setAcceptThirdPartyCookies(binding.wv, true)
        binding.wv.settings.javaScriptEnabled = true
        binding.wv.settings.loadWithOverviewMode = true
        binding.wv.settings.useWideViewPort = true
        binding.wv.settings.domStorageEnabled = true
        val deleteBottomItem = """
                                function removeElementsByClass(className){
                                    const elements = document.getElementsByClassName(className);
                                    while(elements.length > 0){
                                        elements[0].parentNode.removeChild(elements[0]);
                                    }
                                }
                                removeElementsByClass("footer-app__tile footer-app__download");
                                """.trimIndent()
        val turnOffDownloadAppOption = """
                function removeElementsByClass(className){
                                    const elements = document.getElementsByClassName(className);
                                    while(elements.length > 0){
                                        elements[0].parentNode.removeChild(elements[0]);
                                    }
                                }
                                removeElementsByClass("mobile-apps-main mobile-app__content");
                """.trimIndent()

        binding.wv.webChromeClient = object : WebChromeClient() {
        }
        binding.wv.webViewClient = object: WebViewClient(){
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                binding.pbFon.visibility = View.GONE
                binding.tvFon.visibility = View.GONE
                binding.wv.visibility = View.VISIBLE
                super.onPageFinished(view, url)
            }

            override fun onLoadResource(view: WebView?, url: String?) {
                super.onLoadResource(view, url)
                binding.wv.loadUrl("javascript:$deleteBottomItem")
                binding.wv.loadUrl("javascript:$turnOffDownloadAppOption")
            }
        }
        binding.wv.loadUrl(sharedPrefs.link)
//        binding.wv.loadUrl("https://refpa1364493.top/L?tag=s_2363387m_1234c_&site=2363387&ad=1234")
    }

    override fun onBackPressed() {
        if (binding.wv.canGoBack()){
            binding.wv.goBack()
        } else{
            super.onBackPressed()
        }
    }
}