package com.karntrehan.starwars.architecture

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.karntrehan.starwars.extensions.hide
import io.reactivex.disposables.CompositeDisposable

abstract class BaseVM : ViewModel() {

  val disposable = CompositeDisposable()

  //Loading mutable livedata
  protected val _loading: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
  //Expose loading state as livedata instead of mutable
  val loading: LiveData<Boolean> by lazy { _loading }

  //Error mutable livedata
  val _error: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }
  //Expose error state as livedata instead of mutable
  val error: LiveData<Throwable> by lazy { _error }

  protected fun handleError(err: Throwable) {
    _loading.hide()
    _error.postValue(err)
  }

  override fun onCleared() {
    super.onCleared()
    disposable.dispose()
  }

  fun errorHandled() {
    _error.value = null
  }

}