package com.bookmaker.onexbetapp.common.systemUtils

import android.app.Application
import com.bookmaker.onexbetapp.common.systemUtils.Constants.YANDEXMETRICA_KEY
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig

val sharedPrefs by lazy {
    SharedPreferences(App.instance)
}

class App: Application() {
    companion object{
        lateinit var instance: App
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        val config = YandexMetricaConfig.newConfigBuilder(YANDEXMETRICA_KEY).build()
        YandexMetrica.activate(applicationContext, config)
        YandexMetrica.enableActivityAutoTracking(this)
    }
}