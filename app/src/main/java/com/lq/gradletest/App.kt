package com.lq.gradletest

import android.app.Application
import com.example.lib_api.HRouter

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        HRouter.init(this)
    }
}