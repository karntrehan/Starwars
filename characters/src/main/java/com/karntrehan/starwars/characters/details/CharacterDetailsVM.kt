package com.karntrehan.starwars.characters.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karntrehan.starwars.architecture.BaseVM
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.characters.details.models.FilmDetailsModel
import com.karntrehan.starwars.characters.details.models.SpeciesDetailsModel
import com.karntrehan.starwars.characters.details.models.response.HomeworldResponseModel
import com.karntrehan.starwars.extensions.divide
import com.karntrehan.starwars.extensions.hide
import com.karntrehan.starwars.extensions.show
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CharacterDetailsVM(private val repo: CharacterDetailsContract.Repo) : BaseVM() {

    private val characterDetails = MutableStateFlow<CharacterDetailsModel?>(null)

    fun getCharacterDetails(url: String): StateFlow<CharacterDetailsModel?> {
        if (characterDetails.value == null) {

            _loading.show()

            launchSafely {
                //Get the core details
                val remoteCoreDetails = repo.getCharacterDetails(url)
                //Calculate the height in ft and inches
                val coreDetails = remoteCoreDetails.copy(heightFtInches = cmToFeet(remoteCoreDetails.heightCentimeters))
                //Send the core details to the UI
                characterDetails.value = coreDetails

                loadAdditionalDetails(coreDetails)
            }
        }
        return characterDetails.asStateFlow()
    }

    private suspend fun loadAdditionalDetails(coreDetails: CharacterDetailsModel) {
        coroutineScope {
            var combinedDetails = coreDetails

            launchSafely {
                //Update the core details with species details
                combinedDetails = combinedDetails.copy(specieDetails = speciesAndHomeWorld(combinedDetails))
                //Send the core + specie details to the UI
                characterDetails.value = combinedDetails
            }

            launchSafely {
                //Update core details with film details
                combinedDetails = combinedDetails.copy(filmDetails = films(combinedDetails))
                //Send core + film details to the UI
                characterDetails.value = combinedDetails
                _loading.hide()
            }
        }
    }

    private suspend fun speciesAndHomeWorld(details: CharacterDetailsModel): List<SpeciesDetailsModel> {
        //Iterate through all the character species urls
        return withContext(Dispatchers.IO) {
            details.speciesUrl?.map { speciesUrl ->
                val specieDetails = repo.getSpecieDetails(speciesUrl)

                //If homeworld url is valid, fetch its details, else return an empty object
                val homeWorldDetails = try {
                    repo.getHomeworldDetails(specieDetails.homeworldUrl)
                } catch (t: Throwable) {
                    t.printStackTrace()
                    HomeworldResponseModel()
                }

                SpeciesDetailsModel(
                    specieDetails.name, specieDetails.language,
                    homeWorldDetails.name, homeWorldDetails.population
                )
            }?.toList()
        } ?: emptyList()
    }

    private suspend fun films(details: CharacterDetailsModel): List<FilmDetailsModel>? {
        return withContext(Dispatchers.IO) {
            //Iterate through all the film urls
            details.filmUrls?.map { filmUrl ->
                //get film details
                val film = repo.getFilmDetails(filmUrl)
                //convert to our model
                FilmDetailsModel(film.title, film.releaseDate, film.openingCrawl)
            }?.toList() ?: emptyList()
        }
    }

    fun cmToFeet(heightCentimeters: String?): Pair<String, String>? {
        return heightCentimeters
            ?.toIntOrNull()
            ?.let { Pair(it.divide(30.48), it.divide(2.54)) }
    }
}
