package com.karntrehan.starwars.characters.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.karntrehan.starwars.architecture.TestingUtils
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.FilmDetailsModel
import com.karntrehan.starwars.characters.details.models.SpeciesDetailsModel
import com.karntrehan.starwars.characters.details.models.response.FilmResponseModel
import com.karntrehan.starwars.characters.details.models.response.HomeworldResponseModel
import com.karntrehan.starwars.characters.details.models.response.SpeciesResponseModel
import com.karntrehan.starwars.characters.utils.TrampolineSchedulerRule
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CharacterDetailsVMTest {

    //Ensure livedata executions happen immediately
    @get:Rule
    var ruleInstant = InstantTaskExecutorRule()

    //Ensure rx executions happen immediately
    @get:Rule
    var ruleRx = TrampolineSchedulerRule()

    //Class to test
    private lateinit var viewModel: CharacterDetailsVM
    //Mock the repo
    private val repo = mock<CharacterDetailsContract.Repo>()

    //Mock the loading and error observers
    private val loadingObs = mock<Observer<Boolean>>()
    private val errorObs = mock<Observer<Throwable>>()
    private val gson = Gson()

    private val baseUrl = "https://swapi.co/api/"
    private val characterUrl = "${baseUrl}people/1/"
    private val speciesUrl = "${baseUrl}species/1/"
    private val homeworldUrl = "${baseUrl}planets/9/"
    private val filmUrl2 = "${baseUrl}films/2/"
    private val filmUrl6 = "${baseUrl}films/6/"

    @Before
    fun setUp() {
        //Setup the viewmodel with mocked repo and test scheduler
        viewModel = CharacterDetailsVM(repo = repo)

        //Attach observers to loading and error
        viewModel.loading.observeForever(loadingObs)
        viewModel.error.observeForever(errorObs)
    }

    @Test
    fun testSuccessfulRemoteLoad() {
        //TODO
    }

    @Test
    fun testPartialRemoteLoad() {
       //TODO
    }

    @Test
    fun testCmToFeetValid() {
        val expected = Pair("5.65", "67.72")
        assertEquals(expected, viewModel.cmToFeet("172"))
        pm("Height is correctly converted from cm to feet and inches for valid digits")
    }

    @Test
    fun testCmToFeetInvalid() {
        assertEquals(null, viewModel.cmToFeet("unknown"))
        pm("Height is correctly handled for invalid digits")
    }

    @Test
    fun testCmToFeetEmpty() {
        assertEquals(null, viewModel.cmToFeet(""))
        pm("Height is correctly handled for empty input")
    }

    //region region: Utils
    private fun pm(message: String) {
        println("\nCharacter details verified: $message")
    }

    private fun mockCharacterDetails(): CharacterDetailsModel {
        return gson.fromJson(
                TestingUtils.getResponseFromJson("/details/character_details"),
                CharacterDetailsModel::class.java
        )
    }

    private fun mockCharacterSpecies(): SpeciesResponseModel {
        return gson.fromJson(
                TestingUtils.getResponseFromJson("/details/character_species"),
                SpeciesResponseModel::class.java
        )
    }

    private fun mockCharacterHomeworld(): HomeworldResponseModel {
        return gson.fromJson(
                TestingUtils.getResponseFromJson("/details/character_homeworld"),
                HomeworldResponseModel::class.java
        )
    }

    private fun mockCharacterFilm2(): FilmResponseModel {
        return gson.fromJson(
                TestingUtils.getResponseFromJson("/details/character_film_2"),
                FilmResponseModel::class.java
        )
    }

    private fun mockCharacterFilm6(): FilmResponseModel {
        return gson.fromJson(
                TestingUtils.getResponseFromJson("/details/character_film_6"),
                FilmResponseModel::class.java
        )
    }

    private fun mockCharacterSpeciesWithNoHomeworld(): SpeciesResponseModel {
        return gson.fromJson(
                TestingUtils.getResponseFromJson("/details/character_species_no_homeworld"),
                SpeciesResponseModel::class.java
        )
    }


    private fun lukeSkywalkerSpecieDetails() =
            SpeciesDetailsModel("Human", "Galactic Basic", "Coruscant", "1000000000000")

    private fun lukeSkywalkerSpecieDetailsWithoutHomeWorld() =
            SpeciesDetailsModel("Human", "Galactic Basic", "", "")

    private fun film2Details() = FilmDetailsModel("The Empire Strikes Back", "1980-05-17",
            "It is a dark time for the\r\nRebellion. Although the Death\r\nStar has been destroyed,\r\nImperial troops have driven the\r\nRebel forces from their hidden\r\nbase and pursued them across\r\nthe galaxy.\r\n\r\nEvading the dreaded Imperial\r\nStarfleet, a group of freedom\r\nfighters led by Luke Skywalker\r\nhas established a new secret\r\nbase on the remote ice world\r\nof Hoth.\r\n\r\nThe evil lord Darth Vader,\r\nobsessed with finding young\r\nSkywalker, has dispatched\r\nthousands of remote probes into\r\nthe far reaches of space....")


    private fun film6Details() = FilmDetailsModel("Revenge of the Sith", "2005-05-19",
            "War! The Republic is crumbling\r\nunder attacks by the ruthless\r\nSith Lord, Count Dooku.\r\nThere are heroes on both sides.\r\nEvil is everywhere.\r\n\r\nIn a stunning move, the\r\nfiendish droid leader, General\r\nGrievous, has swept into the\r\nRepublic capital and kidnapped\r\nChancellor Palpatine, leader of\r\nthe Galactic Senate.\r\n\r\nAs the Separatist Droid Army\r\nattempts to flee the besieged\r\ncapital with their valuable\r\nhostage, two Jedi Knights lead a\r\ndesperate mission to rescue the\r\ncaptive Chancellor....")

    //endregion

}