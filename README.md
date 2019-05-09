# Star Wars app
by Karan Trehan

The app has been developed in Kotlin following the MVVM architecture pattern with a modular approach.  Koin has been used for dependency injection. Reactive streams have been used to make networking cleaner. LiveData and ViewModel from Jetpack have also been used.

The app is developed with a modular approach to support instant apps & dynamic delivery for the users. Modularity also allows us to have faster gradle build speeds, clear ownerships amongst the team & cleaner git flows. More info about modular apps can be found [here](https://medium.com/mindorks/writing-a-modular-project-on-android-304f3b09cb37).

## Decision
* Koin: This is my first experiments with Koin, and it turned out to be very straight-forward to implement. The performance is also great. Although, feature module support is something they are still working on.
* Modular: As mentioned above, modularity has various advantages to the end users as well as the development team.

## Structure
The `app` module is where the application initializes & `characters` module is where our sample screens reside. 
`CharacterActivity` holds the `CharacterSearchFragment` & `CharacterDetailsFragment`.
The packages are "by-feature" for easier access.

## Testing
I have implemented test case for the `CharacterDetailsVM`. The same can be implemented on other screens. 

## Improvements
* We can improve the UX and UI of the app. 
* This is the first time I have chained API requests (on Details screen), there could be better ways to approach this, although, the current implementation is also pretty clean.
* We can expand the tests to cover more cases. 