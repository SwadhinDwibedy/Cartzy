package com.example.cartzy

import android.app.Application
import com.google.firebase.FirebaseApp

class CartzyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}