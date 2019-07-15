package com.karntrehan.starwars.characters.search

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.karntrehan.starwars.architecture.BaseVM
import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import com.karntrehan.starwars.extensions.hide
import com.karntrehan.starwars.extensions.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        launchSafely {
            handleCharacters(remoteCharacters(url), resetItems)
        }
    }

    private suspend fun remoteCharacters(url: String) =
        withContext(Dispatchers.IO) {
            repo.characters(url)
        }

    /**
     * Handle the characters returned from the API
     *
     * resetItems clears the current contents.
     * */
    private suspend fun handleCharacters(
        response: RemoteResponse<List<CharacterSearchModel>>,
        resetItems: Boolean
    ) = withContext(Dispatchers.Default) {
        nextPageUrl = response.next
        val results = response.results
        if (!results.isNullOrEmpty()) {
            _characters.postValue(appendOrSetResults(resetItems, _characters.value, results))
            _loading.hide()
            _paginationLoading.hide()
            processing = false
        }
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
        launchSafely {
            handleCharacters(searchedRemoteCharacters(query), true)
        }
    }

    private suspend fun searchedRemoteCharacters(query: String) =
        withContext(Dispatchers.IO) {
            repo.searchCharacter(query)
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