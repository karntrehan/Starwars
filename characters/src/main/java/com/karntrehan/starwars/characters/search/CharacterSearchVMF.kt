package com.karntrehan.starwars.characters.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class CharacterSearchVMF(
        private val repo: CharacterSearchContract.Repo?) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CharacterSearchVM(repo) as T
    }
}