package com.karntrehan.starwars.extensions

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Boolean>.show() = this.postValue(true)

fun MutableLiveData<Boolean>.hide() = this.postValue(false)
