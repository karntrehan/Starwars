package com.karntrehan.starwars.characters.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CharacterSearchVMF(private val repo: CharacterSearchContract.Repo) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = CharacterSearchVM(repo) as T
}