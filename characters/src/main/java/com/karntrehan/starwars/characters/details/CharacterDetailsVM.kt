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
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CharacterDetailsVM(private val repo: CharacterDetailsContract.Repo) : BaseVM() {

    private val characterDetails = MutableLiveData<CharacterDetailsModel>()

    private var specieName: String? = null
    private var specieLanguage: String? = null
    private val specieDetails = mutableListOf<SpeciesDetailsModel>()

    fun getCharacterDetails(url: String): LiveData<CharacterDetailsModel> {
        if (characterDetails.value == null) {

            _loading.show()

            repo.getCharacterDetails(url)
                    //Subscribe on io thread
                    .subscribeOn(Schedulers.io())
                    //Move to computation thread
                    .observeOn(Schedulers.computation())
                    .map {
                        //convert character's height from cm to feet and inches
                        val details = it.copy(heightFtInches = cmToFeet(it.heightCentimeters))
                        //Pass the details to livedata
                        characterDetails.postValue(details)
                        //Pass details along the chain
                        return@map details
                    }

                    .observeOn(Schedulers.io())
                    //Iterate through all the character species urls
                    .flatMapObservable { details -> Observable.fromIterable(details.speciesUrl) }
                    //Get the details for each specie
                    .flatMapSingle { speciesUrl -> repo.getSpecieDetails(speciesUrl) }
                    //For each specie result, get the homeworld / planet details
                    .flatMapSingle { specie ->
                        specieName = specie.name
                        specieLanguage = specie.language
                        return@flatMapSingle repo
                                .getHomeworldDetails(specie.homeworldUrl)
                                //If homeworld api fails, return a default object
                                // to allow the stream to continue.
                                .onErrorReturn { HomeworldResponseModel() }
                    }
                    .observeOn(Schedulers.computation())
                    //Combine the specie details with homeworld details
                    .map { homeworldResponse ->
                        specieDetails.add(SpeciesDetailsModel(specieName, specieLanguage,
                                homeworldResponse.name, homeworldResponse.population))
                    }
                    //Wait for all species and homeworld results
                    .toList()
                    .map {
                        //Add species and homeworld results to details
                        val newData = characterDetails.value?.copy(specieDetails = specieDetails)
                        //Pass the details to livedata
                        characterDetails.postValue(newData)
                        //Pass details along the chain
                        return@map newData
                    }

                    .observeOn(Schedulers.io())
                    //Iterate through all the character film urls
                    .flatMapObservable { details -> Observable.fromIterable(details.filmUrls) }
                    //Get details from each film
                    .flatMapSingle { filmUrl -> repo.getFilmDetails(filmUrl) }
                    //Convert response model to model required by UI
                    .map { film ->
                        FilmDetailsModel(film.title, film.releaseDate, film.openingCrawl)
                    }
                    //Wait for all film results
                    .toList()
                    .observeOn(Schedulers.computation())
                    .map { filmDetails ->
                        //Add film details to existing details
                        return@map characterDetails.value?.copy(filmDetails = filmDetails)
                    }

                    .subscribe({
                        _loading.hide()
                        //Pass complete character details to UI
                        characterDetails.postValue(it)
                    }, { handleError(it) })
                    .addTo(disposable)
        }
        return characterDetails
    }

    private fun cmToFeet(heightCentimeters: String?): Pair<String, String>? {
        return heightCentimeters
                ?.toIntOrNull()
                ?.let { Pair(it.divide(30.48), it.divide(2.54)) }
    }
}
