package com.karntrehan.starwars.characters.details.layouts

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.karntrehan.starwars.characters.R
import com.karntrehan.starwars.characters.details.models.CharacterDetailsModel
import kotlinx.android.synthetic.main.view_character_species_details.view.*

class SpecieDetailsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0)
    : LinearLayout(context, attrs, defStyleAttr) {

    init {
        LayoutInflater
                .from(context)
                .inflate(R.layout.view_character_species_details, this, true)
    }

    fun specieDetails(detailsModel: CharacterDetailsModel.SpeciesDetailsModel) {
        tvSpeciesName.text = detailsModel.species
        tvSpeciesLanguage.text = String.format(context.getString(R.string.speak), detailsModel.language)
        tvHomeworld.text = String.format(context.getString(R.string.live_in), detailsModel.homeworld)
        tvPopulation.text = String.format(context.getString(R.string.population), detailsModel.population)
    }

}