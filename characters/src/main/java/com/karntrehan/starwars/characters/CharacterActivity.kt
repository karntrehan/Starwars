package com.karntrehan.starwars.characters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.karntrehan.starwars.characters.search.CharacterSearchContract
import com.karntrehan.starwars.characters.search.CharacterSearchFragment
import com.karntrehan.starwars.characters.search.CharacterSearchRepo
import com.karntrehan.starwars.characters.search.CharacterSearchVM
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create

class CharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        initDI()

        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.flContainer,
                    CharacterSearchFragment.newInstance(),
                    CharacterSearchFragment.TAG)
        }
    }

    private fun initDI() {
        loadKoinModules(characterSearchModule(), characterModule())
    }

    //Search module
    private fun characterSearchModule(): Module = module {
        viewModel { CharacterSearchVM(get()) }
        single { characterSearchContact(get()) }
    }
    private fun characterSearchContact(service: CharacterService)
            : CharacterSearchContract.Repo = CharacterSearchRepo(service)


    //Character module
    private fun characterModule(): Module = module {
        single { characterService(get()) }
    }
    private fun characterService(retrofit: Retrofit): CharacterService = retrofit.create()
}
