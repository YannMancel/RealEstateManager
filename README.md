# REAL ESTATE MANAGER

**Goal**: Create an application allowing agents to access the files of different properties from their mobile equipment.


## Phone display
<p align="middle">
     <img src="./screenshots/phone_1.png" width="30%" height="30%"> <img src="./screenshots/phone_2.png" width="30%"   height="30%">
</p>
<p align="middle">
     <img src="./screenshots/phone_3.png" width="30%" height="30%"> <img src="./screenshots/phone_4.png" width="30%" height="30%">
</p>


## Tablet display
<p align="middle">
    <img src="./screenshots/tablet_1.png" width="50%" height="50%">
</p>


## Physical data model
<p align="middle">
    <img src="./database/PDM.png" width="80%" height="80%">
</p>


## Requirements
* Computer (Windows, Mac or Linux)
* Android Studio


## Setup the project in Android studio
1. Download the project code, preferably using `git clone https://github.com/YannMancel/RealEstateManager.git`.
2. In Android Studio, select *File* | *Open...*
3. Select the project
     
     
## Compile and execute the project in Android studio
1. In Android Studio, select *Run* | *Run...*
2. Choose `app` in *Run dialog*
3. Select a device (*Available Virtual Devices* or *Connected Devices*)
4. Select `OK` in *Select Deployment Target dialog*


## Wiki
* [Android Jetpack](https://developer.android.com/jetpack)
  * [Architecture Components](https://developer.android.com/topic/libraries/architecture/)
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
    * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
    * [Navigation](https://developer.android.com/guide/navigation/)
    * [Room](https://developer.android.com/topic/libraries/architecture/room)
  * [Foundation Multidex](https://developer.android.com/studio/build/multidex.html)
* [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines)
* [Library Retrofit](https://github.com/square/retrofit)
  * [Converter Moshi](https://github.com/square/retrofit/tree/master/retrofit-converters/moshi)
  * [Adapter RxJava2](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2)
* [Library Moshi](https://github.com/square/moshi)
* [Reactive programming](http://reactivex.io/)
  * [Library RxAndroid](https://github.com/ReactiveX/RxAndroid)
  * [Library RxKotlin](https://github.com/ReactiveX/RxKotlin)
* [Library Koin](https://github.com/InsertKoinIO/koin)
* [Library Material Components](https://github.com/material-components/material-components-android)
* [Google Maps](https://cloud.google.com/maps-platform/)
  * [Maps](https://developers.google.com/maps/documentation/android-sdk/intro)
  * [Places](https://developers.google.com/places/web-service/intro)
* [Library Timber](https://github.com/JakeWharton/timber)
* [Library Glide](https://github.com/bumptech/glide)
* [Library Mockito](https://github.com/mockito/mockito)
* [Library Mockito-kotlin](https://github.com/nhaarman/mockito-kotlin)

## Troubleshooting

### No device available during the compilation and execution steps 
* If none of device is present (*Available Virtual Devices* or *Connected Devices*),
    * Either select `Create a new virtual device`
    * or connect and select your phone or tablet
     
     
## Useful
* [Download Android Studio](https://developer.android.com/studio)
* [Create a new virtual device](https://developer.android.com/studio/run/managing-avds.html)
* [Enable developer options and debugging](https://developer.android.com/studio/debug/dev-options.html#enable)