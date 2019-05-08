package com.karntrehan.starwars.characters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.karntrehan.starwars.characters.details.CharacterDetailsFragment
import com.karntrehan.starwars.characters.search.CharacterSearchFragment
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel

//Koin setup
private val loadCharacterDependencies by lazy { CharacterDH.init() }

private fun injectCharacterDependencies() = loadCharacterDependencies

class CharacterActivity : AppCompatActivity(), CharacterSearchFragment.CharacterNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        injectCharacterDependencies()

        displayFragment(CharacterSearchFragment.newInstance(), CharacterSearchFragment.TAG)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            super.onBackPressed()
        else finish()
    }

    //Character navigator interactions
    override fun showCharacterDetails(character: CharacterSearchModel) {
        displayFragment(CharacterDetailsFragment.newInstance(character), CharacterDetailsFragment.TAG)
    }

    private fun displayFragment(fragment: Fragment, tag: String) {
        if (supportFragmentManager.findFragmentByTag(tag) != null) return

        supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContainer, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }
}
