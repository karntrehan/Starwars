package com.karntrehan.starwars.characters.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karntrehan.starwars.architecture.BaseVM
import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import com.karntrehan.starwars.extensions.hide
import com.karntrehan.starwars.extensions.show
import io.reactivex.Single
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CharacterSearchVM(private val repo: CharacterSearchContract.Repo) : BaseVM() {

    private val _characters = MutableLiveData<List<CharacterSearchModel>>()
    val characters: LiveData<List<CharacterSearchModel>> by lazy { _characters }

    private var nextPageUrl: String? = ""
    private var processing: Boolean = false

    private val _paginationLoading = MutableLiveData<Boolean>()
    val paginationLoading: LiveData<Boolean> by lazy { _paginationLoading }

    private val initialAPI = "people"

    fun initialLoad() {
        if (_characters.value != null && !_characters.value.isNullOrEmpty()) return

        _loading.show()
        getCharacters(url = initialAPI, resetItems = true)
    }

    private fun getCharacters(url: String, resetItems: Boolean) {
        if (processing) return

        processing = true
        handleCharactersObs(repo.characters(url), resetItems)
    }

    /**
     * Handle the characters returned from the API
     *
     * resetItems clears the current contents.
     * */
    private fun handleCharactersObs(
        charactersObs: Single<RemoteResponse<List<CharacterSearchModel>>>,
        resetItems: Boolean
    ) {
        charactersObs
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .map { response ->
                nextPageUrl = response.next
                return@map response.results
            }
            .map { searchModels ->
                appendOrSetResults(resetItems, _characters.value, searchModels)
            }
            .subscribe({
                _loading.hide()
                _paginationLoading.hide()
                _characters.postValue(it)
                processing = false
            }, {
                handleError(it)
                processing = false
            })
            .addTo(disposable)
    }

    private fun appendOrSetResults(
        resetItems: Boolean,
        existingData: List<CharacterSearchModel>?,
        newData: List<CharacterSearchModel>
    ): List<CharacterSearchModel> {
        val finalData = mutableListOf<CharacterSearchModel>()
        if (resetItems || existingData.isNullOrEmpty())
            finalData.addAll(newData)
        else {
            finalData.addAll(existingData)
            finalData.addAll(newData)
        }
        return finalData
    }

    fun loadNextPage() {
        nextPageUrl?.run {
            _paginationLoading.show()
            getCharacters(this, false)
        }
    }

    fun searchCharacter(query: String?) {
        if (query.isNullOrEmpty()) return

        _loading.show()
        handleCharactersObs(repo.searchCharacter(query), true)
    }

    fun refreshCharacters() {
        _loading.show()
        getCharacters(url = initialAPI, resetItems = true)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setCharacter(characters: List<CharacterSearchModel>?) {
        _characters.postValue(characters)
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun getInitialApi() = initialAPI

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    fun setNextPageUrl(url: String?) {
        nextPageUrl = url
    }
}

