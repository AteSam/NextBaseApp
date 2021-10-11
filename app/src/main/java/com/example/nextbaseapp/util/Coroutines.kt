package com.example.nextbaseapp.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


    fun coroutine(
        work: suspend () -> Unit
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            work()
        }
    }
