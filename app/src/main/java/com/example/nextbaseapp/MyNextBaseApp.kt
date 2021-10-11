package com.example.nextbaseapp

import android.app.Application
import com.example.nextbaseapp.di.AppComponent
import com.example.nextbaseapp.di.DaggerAppComponent

class MyNextBaseApp : Application() {

    val appComponent: AppComponent = DaggerAppComponent.create()
}