package com.karntrehan.starwars.architecture

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import okio.Okio
import java.nio.charset.StandardCharsets

@VisibleForTesting(otherwise = VisibleForTesting.NONE)
object TestingUtils {

  /**
   *Helper to read a JSON file and return a JSON string
   *Note: JSON file should have the structure "/api-response/filename.json"
   * @param fileName: File's name
   * @return JSON String
   */
  @SuppressLint("NewApi")
  fun getResponseFromJson(fileName: String): String {
    val inputStream = javaClass.classLoader
        .getResourceAsStream("api-response/$fileName.json")
    val source = Okio.buffer(Okio.source(inputStream))
    return source.readString(StandardCharsets.UTF_8)
  }
}