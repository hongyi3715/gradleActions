package com.lq.gradletest

import android.app.Application
import com.lq.lib_api.HRouter

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        HRouter.init(this)
    }
}