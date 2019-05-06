package com.karntrehan.starwars.characters.details.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.karntrehan.starwars.characters.R
import com.karntrehan.starwars.characters.details.models.OpeningCrawl
import com.karntrehan.starwars.characters.details.models.ReleaseDate
import com.karntrehan.starwars.characters.details.models.Title
import kotlinx.android.synthetic.main.view_character_film_details.view.*

class FilmDetailsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater
                .from(context)
                .inflate(R.layout.view_character_film_details, this, true)
    }

    fun filmDetails(details: Triple<Title, ReleaseDate, OpeningCrawl>) {
        tvFilmName.text = details.first
        tvFilmReleaseDate.text = String.format(context.getString(R.string.released_on), details.second)
        tvFilmCrawl.text = details.third
    }

}