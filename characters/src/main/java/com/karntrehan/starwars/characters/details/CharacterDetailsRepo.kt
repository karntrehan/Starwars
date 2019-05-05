package com.karntrehan.starwars.characters.details

import com.karntrehan.starwars.characters.CharacterService

class CharacterDetailsRepo(private val service: CharacterService) : CharacterDetailsContract.Repo {

}