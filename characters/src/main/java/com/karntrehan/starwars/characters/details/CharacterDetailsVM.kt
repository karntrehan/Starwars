package com.karntrehan.starwars.characters.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.karntrehan.starwars.architecture.BaseVM
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import com.karntrehan.starwars.extensions.divide
import com.karntrehan.starwars.extensions.hide
import com.karntrehan.starwars.extensions.show
import io.reactivex.Observable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers

class CharacterDetailsVM(private val repo: CharacterDetailsContract.Repo) : BaseVM() {

    private val characterDetails = MutableLiveData<CharacterDetailsModel>()

    private var specieName: String = ""
    private var specieLanguage: String = ""

    private val specieDetails = mutableListOf<CharacterDetailsModel.SpeciesDetailsModel>()

    fun getCharacterDetails(url: String): LiveData<CharacterDetailsModel> {
        if (characterDetails.value == null) {
            _loading.show()
            repo.getCharacterDetails(url)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.computation())
                    .map {
                        val details = it.copy(heightFtInches = cmToFeet(it.heightCentimeters))
                        characterDetails.postValue(details)
                        return@map details
                    }

                    .observeOn(Schedulers.io())
                    .flatMapObservable { details -> Observable.fromIterable(details.speciesUrl) }
                    .flatMapSingle { speciesUrl -> repo.getSpecieDetails(speciesUrl) }
                    .flatMapSingle { specie ->
                        specieName = specie.name
                        specieLanguage = specie.language
                        return@flatMapSingle repo.getHomeworldDetails(specie.homeworldUrl)
                    }
                    .observeOn(Schedulers.computation())
                    .map { homeworldResponse ->
                        specieDetails.add(
                                CharacterDetailsModel.SpeciesDetailsModel(
                                        specieName,
                                        specieLanguage,
                                        homeworldResponse.name,
                                        homeworldResponse.population)
                        )
                    }
                    .toList()
                    .map {
                        val newData = characterDetails.value?.copy(specieDetails = specieDetails)
                        characterDetails.postValue(newData)
                        return@map newData
                    }

                    .observeOn(Schedulers.io())
                    .flatMapObservable { details -> Observable.fromIterable(details.filmUrls) }
                    .flatMapSingle { filmUrl -> repo.getFilmDetails(filmUrl) }
                    .toList()
                    .observeOn(Schedulers.computation())
                    .map { filmsResponse ->
                        val currentValue = characterDetails.value
                        val films = mutableListOf<CharacterDetailsModel.FilmDetailsModel>()
                        filmsResponse.forEach { film ->
                            films.add(
                                    CharacterDetailsModel.FilmDetailsModel(
                                            film.title,
                                            film.releaseDate,
                                            film.openingCrawl)
                            )
                        }
                        return@map currentValue?.copy(filmDetails = films)
                    }

                    .subscribe({
                        _loading.hide()
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
