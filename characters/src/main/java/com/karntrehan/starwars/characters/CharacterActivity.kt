package com.karntrehan.starwars.characters

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.transaction
import com.karntrehan.starwars.characters.search.CharacterSearchFragment

class CharacterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        supportFragmentManager.transaction(allowStateLoss = true) {
            replace(R.id.flContainer,
                    CharacterSearchFragment.newInstance(),
                    CharacterSearchFragment.TAG)
        }
    }
}
