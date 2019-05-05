package com.karntrehan.starwars.extensions

import android.content.Intent

/*Inspired from Plaid's ActivityHelper
https://github.com/android/plaid/blob/776abd95f02b1e34360a3fef149baa7d9d4b23a0/core/src/main/java/io/plaidapp/core/util/ActivityHelper.kt#L1*/


private const val PACKAGE_NAME = "com.karntrehan.starwars"

fun intentTo(navigatableActivity: NavigatableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
            PACKAGE_NAME,
            navigatableActivity.className)
}

interface NavigatableActivity {
    val className: String
}

object Activities {

    object Characters : NavigatableActivity {
        override val className = "$PACKAGE_NAME.characters.CharacterActivity"
    }

}