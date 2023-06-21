package com.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        System.loadLibrary("test_native")

        logError("hello world1")
    }

    external fun logError(message: String)
}