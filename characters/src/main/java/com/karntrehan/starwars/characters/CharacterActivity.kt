package com.karntrehan.starwars.characters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.karntrehan.starwars.characters.details.CharacterDetailsContract
import com.karntrehan.starwars.characters.details.CharacterDetailsFragment
import com.karntrehan.starwars.characters.details.CharacterDetailsRepo
import com.karntrehan.starwars.characters.details.CharacterDetailsVM
import com.karntrehan.starwars.characters.search.CharacterSearchContract
import com.karntrehan.starwars.characters.search.CharacterSearchFragment
import com.karntrehan.starwars.characters.search.CharacterSearchRepo
import com.karntrehan.starwars.characters.search.CharacterSearchVM
import com.karntrehan.starwars.characters.search.models.CharacterSearchModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

class CharacterActivity : AppCompatActivity(), CharacterSearchFragment.CharacterNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        initDI()

        navigateTo(CharacterSearchFragment.newInstance(), CharacterSearchFragment.TAG)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 1)
            super.onBackPressed()
        else finish()
    }

    override fun showCharacterDetails(character: CharacterSearchModel) {
        navigateTo(CharacterDetailsFragment.newInstance(character), CharacterDetailsFragment.TAG)
    }

    private fun navigateTo(fragment: Fragment, tag: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContainer, fragment, tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    private fun initDI() {
        loadKoinModules(characterDetailsModule(), characterSearchModule(), characterModule())
    }

    //Details module
    private fun characterDetailsModule(): Module = module {
        viewModel { CharacterDetailsVM(get()) }
        single { characterDetailsContract(get()) }
    }

    private fun characterDetailsContract(service: CharacterService)
            : CharacterDetailsContract.Repo = CharacterDetailsRepo(service)

    //Search module
    private fun characterSearchModule(): Module = module {
        viewModel { CharacterSearchVM(get()) }
        single { characterSearchContract(get()) }
    }

    private fun characterSearchContract(service: CharacterService)
            : CharacterSearchContract.Repo = CharacterSearchRepo(service)


    //Character module
    private fun characterModule(): Module = module {
        single { characterService(get()) }
    }

    private fun characterService(retrofit: Retrofit): CharacterService = retrofit.create()
}
