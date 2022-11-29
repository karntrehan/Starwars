package com.karntrehan.starwars.extensions

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

fun MutableStateFlow<Boolean>.show() {
    this.value = true
}

fun MutableStateFlow<Boolean>.hide() {
    this.value = false
}
