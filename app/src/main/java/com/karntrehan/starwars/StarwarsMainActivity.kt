package com.karntrehan.starwars

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.karntrehan.starwars.extensions.Activities
import com.karntrehan.starwars.extensions.intentTo

class StarwarsMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starwars_main)

        //Open the Characters activity on app launch
        startActivity(intentTo(Activities.Characters))
        finish()
    }
}
