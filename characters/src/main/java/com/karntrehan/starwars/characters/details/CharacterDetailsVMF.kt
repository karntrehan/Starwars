package com.karntrehan.starwars.characters.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CharacterDetailsVMF(private val repo: CharacterDetailsContract.Repo) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = CharacterDetailsVM(repo) as T
}