package com.karntrehan.starwars.characters.search

import com.karntrehan.starwars.characters.CharacterService

class CharacterSearchRepo(private val service: CharacterService) : CharacterSearchContract.Repo {

}