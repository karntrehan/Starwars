package com.karntrehan.starwars.characters.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karntrehan.starwars.architecture.BaseVM
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CharacterDetailsVM(private val repo: CharacterDetailsContract.Repo) : BaseVM() {

    private val characterDetails = MutableLiveData<CharacterDetailsModel>()

    private val TAG = "CharacterDetailsVM"

    fun getCharacterDetails(url: String): LiveData<CharacterDetailsModel> {
        if (characterDetails.value == null) {
            repo.getCharacterDetails(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .subscribe({
                        Log.d(TAG, it.toString())
                    }, { handleError(it) })
                    .addTo(disposable)
        }
        return characterDetails
    }

}

