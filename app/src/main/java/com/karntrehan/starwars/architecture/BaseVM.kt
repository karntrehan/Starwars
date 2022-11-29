package com.karntrehan.starwars.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karntrehan.starwars.extensions.hide
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseVM : ViewModel() {

    val disposable = CompositeDisposable()

    //Loading mutable livedata
    protected val _loading by lazy { MutableStateFlow(false) }

    //Expose loading state as livedata instead of mutable
    val loading by lazy { _loading.asStateFlow() }

    //Error mutable livedata
    val _error by lazy { MutableStateFlow<Throwable?>(null) }

    //Expose error state as livedata instead of mutable
    val error by lazy { _error.asStateFlow() }

    protected fun handleError(err: Throwable) {
        _loading.hide()
        _error.value = err
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
    }

    fun errorHandled() {
        _error.value = null
    }

    protected fun launchSafely(coroutine: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
            try {
                coroutine()
            } catch (error: Throwable) {
                handleError(error)
            }
        }
    }
}