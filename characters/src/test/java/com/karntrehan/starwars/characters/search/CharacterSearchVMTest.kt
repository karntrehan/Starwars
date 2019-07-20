package com.karntrehan.starwars.characters.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.karntrehan.starwars.architecture.RemoteResponse
import com.karntrehan.starwars.architecture.TestingUtils
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import com.karntrehan.starwars.characters.utils.TrampolineSchedulerRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.lang.reflect.Type

@RunWith(RobolectricTestRunner::class)
class CharacterSearchVMTest {

    //Ensure livedata executions happen immediately
    @get:Rule
    var ruleInstant = InstantTaskExecutorRule()

    //Ensure rx executions happen immediately
    @get:Rule
    var ruleRx = TrampolineSchedulerRule()

    //Class to test
    private lateinit var viewModel: CharacterSearchVM
    //Mock the repo
    private val repo = mock<CharacterSearchContract.Repo>()

    //Mock the loading and error observers
    private val loadingObs = mock<Observer<Boolean>>()
    private val errorObs = mock<Observer<Throwable>>()

    //Mock the pagination and character list observers
    private val paginationObs = mock<Observer<Boolean>>()
    private val charactersObs = mock<Observer<List<CharacterSearchModel>>>()

    private val gson = Gson()

    private val initialSuccessData = mockInitialData()

    @Before
    fun setUp() {
        //Setup the viewmodel with mocked repo and test scheduler
        viewModel = CharacterSearchVM(repo = repo)

        //Attach observers to loading and error
        viewModel.loading.observeForever(loadingObs)
        viewModel.error.observeForever(errorObs)

        //Attach observers to pagination and characters list
        viewModel.paginationLoading.observeForever(paginationObs)
        viewModel.characters.observeForever(charactersObs)

    }

    @Test
    fun testInitialLoad() {
        //TODO
    }

    @Test
    fun testLoadNextValid() {
        //TODO
    }

    @Test
    fun testSearchCharacter() {
        //TODO
    }

    @Test
    fun testRefresh() {
        //TODO
    }


    //region region: Utils
    private fun pm(message: String) {
        println("\nCharacter search verified: $message")
    }

    private fun mockInitialData(): RemoteResponse<List<CharacterSearchModel>> {
        val responseModelToken: Type = object : TypeToken<RemoteResponse<List<CharacterSearchModel>>>() {}.type
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/search/initial_load"),
            responseModelToken
        )
    }

    private fun mockLukeSearch(): RemoteResponse<List<CharacterSearchModel>> {
        val responseModelToken: Type = object : TypeToken<RemoteResponse<List<CharacterSearchModel>>>() {}.type
        return gson.fromJson(
            TestingUtils.getResponseFromJson("/search/luke_search"),
            responseModelToken
        )
    }

    //endregion

}