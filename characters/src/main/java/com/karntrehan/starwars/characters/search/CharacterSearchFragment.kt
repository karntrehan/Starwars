package com.karntrehan.starwars.characters.search

import android.os.Bundle
import android.view.View
import com.karntrehan.starwars.architecture.BaseFragment
import com.karntrehan.starwars.characters.R
import kotlinx.android.synthetic.main.fragment_search_character.*
import org.koin.android.ext.android.inject

class CharacterSearchFragment : BaseFragment() {

    override val layout = R.layout.fragment_search_character

    override val viewModel: CharacterSearchVM by inject()

    companion object {
        const val TAG = "CharacterSearchFragment"
        fun newInstance() = CharacterSearchFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar(toolbar)
    }

    override fun hideLoading() {
        srlCharacters.isRefreshing = false
    }

    override fun showLoading() {
        srlCharacters.isRefreshing = true
    }
}
