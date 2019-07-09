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
        //Mock success data response from repo
        whenever(repo.characters(viewModel.getInitialApi())).doReturn(Single.just(initialSuccessData))

        //Trigger the load
        viewModel.initialLoad()

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).characters(viewModel.getInitialApi())

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI
        verify(charactersObs).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the characters are reset and not appended
        assertEquals(10, viewModel.characters.value?.size)

        pm("Initial load calls the correct functions in the repo and sets success data correctly")
    }

    @Test
    fun testLoadNextValid() {
        //Mock success data response from repo
        whenever(repo.characters(initialSuccessData.next!!)).doReturn(Single.just(initialSuccessData))

        //Setup
        viewModel.setCharacter(initialSuccessData.results)
        val nextUrl = initialSuccessData.next
        //Set valid next page url
        viewModel.setNextPageUrl(nextUrl)

        //Trigger next load
        viewModel.loadNextPage()

        //Verify pagination state changes
        verify(paginationObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).characters(initialSuccessData.next!!)

        //Verify pagination state changes again
        verify(paginationObs).onChanged(false)

        //Verify data pushed to UI
        verify(charactersObs, atLeastOnce()).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the characters are not reset and appended instead
        assertEquals(20, viewModel.characters.value?.size)

        pm("Load next with a valid next page url calls the correct functions in the repo and appends success data correctly")
    }

    @Test
    fun testSearchCharacter() {
        val queriedCharacter = "luke"
        //Mock success data response from repo
        whenever(repo.searchCharacter(queriedCharacter)).doReturn(Single.just(mockLukeSearch()))

        //Setup
        viewModel.setCharacter(initialSuccessData.results)

        //Trigger search
        viewModel.searchCharacter(queriedCharacter)

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).searchCharacter(queriedCharacter)

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI
        verify(charactersObs, atLeastOnce()).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the characters are reset and not appended
        assertEquals(1, viewModel.characters.value?.size)

        pm("Search for a character triggers correct functions in the repo and resets the results to the UI")
    }

    @Test
    fun testRefresh() {
        //Mock success data response from repo
        whenever(repo.characters(viewModel.getInitialApi())).doReturn(Single.just(initialSuccessData))

        //Trigger the load
        viewModel.refreshCharacters()

        //Verify loading state changes
        verify(loadingObs).onChanged(true)

        //Verify correct methods are called in the repo
        verify(repo).characters(viewModel.getInitialApi())

        //Verify loading state changes again
        verify(loadingObs).onChanged(false)

        //Verify data pushed to UI
        verify(charactersObs).onChanged(any())

        //Verify error is never pushed to the UI
        verify(errorObs, never()).onChanged(any())

        //Verify the characters are reset and not appended
        assertEquals(10, viewModel.characters.value?.size)

        pm("Refresh calls the correct functions in the repo and sets success data correctly")
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