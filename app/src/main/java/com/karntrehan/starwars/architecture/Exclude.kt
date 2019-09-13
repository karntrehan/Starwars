package com.karntrehan.starwars.architecture

/**
 * Fields annotated with @Exclude will not be parsed in retrofit network requests
 *
 **/
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@MustBeDocumented
annotation class Exclude