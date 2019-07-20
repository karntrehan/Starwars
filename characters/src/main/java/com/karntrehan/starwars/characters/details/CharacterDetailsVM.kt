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
import com.karntrehan.starwars.extensions.isValid
import com.karntrehan.starwars.extensions.show
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Error

class CharacterDetailsVM(private val repo: CharacterDetailsContract.Repo) : BaseVM() {

    private val characterDetails = MutableLiveData<CharacterDetailsModel>()

    fun getCharacterDetails(url: String): LiveData<CharacterDetailsModel> {
        if (characterDetails.value == null) {

            _loading.show()

            launchSafely {
                //Get the core details
                val remoteCoreDetails = repo.getCharacterDetails(url)
                //Calculate the height in ft and inches
                var coreDetails = remoteCoreDetails.copy(heightFtInches = cmToFeet(remoteCoreDetails.heightCentimeters))
                //Send the core details to the UI
                characterDetails.postValue(coreDetails)

                //Fire a request to get species and homeworlds
                val speciesDetails = speciesAndHomeWorld(coreDetails)
                //Update the core details with species details
                coreDetails = coreDetails.copy(specieDetails = speciesDetails)
                //Send the core + specie details to the UI
                characterDetails.postValue(coreDetails)

                //Fire a request to get films
                val films = films(coreDetails)
                //Update core details with film details
                coreDetails = coreDetails.copy(filmDetails = films)
                //Send core + film details to the UI
                characterDetails.postValue(coreDetails)

                _loading.hide()
            }
        }
        return characterDetails
    }

    private suspend fun speciesAndHomeWorld(details: CharacterDetailsModel): List<SpeciesDetailsModel> {
        //Iterate through all the character species urls
        return withContext(Dispatchers.IO) {
            details.speciesUrl?.map { speciesUrl ->
                val specieDetails = repo.getSpecieDetails(speciesUrl)

                //If homeworld url is valid, fetch its details, else return an empty object
                val homeWorldDetails = if (specieDetails.homeworldUrl.isValid())
                    repo.getHomeworldDetails(specieDetails.homeworldUrl)
                else HomeworldResponseModel()

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
