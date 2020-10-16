# exampleBookStore

## On tablets (portrait/landscape) and handphones on landscape
![Image of App1](https://i.imgur.com/2Mp52oSm.jpg)  &nbsp;&nbsp;&nbsp; ![Image of App1](https://i.imgur.com/Nn0QwiWm.jpg) 

## Only handphones on portrait
![Image of App1](https://i.imgur.com/a06HeUbm.png)  &nbsp;&nbsp;&nbsp; ![Image of App1](https://i.imgur.com/2BHaGJLm.jpg)

## A simple example to demonstrate part of my knowledge :coffee: :computer: :coffee: :iphone: :coffee: :sparkles:


  - [x] Book Store consists on showing a simple **2-column list of available bookListDTO about Android development**. Using google’s api for bookListDTO, the app should fetch and display the Thumbnail of a few bookListDTO at a time and load more as the user scroll’s through the list. 
Rest API: https://developers.google.com/bookListDTO/docs/v1/getting_started#REST

  - [x] UI Tests with espresso. androidTest uses Mocked API throught MockWebServer and Mirror. Mirror uses reflection to create mocked responses, so Retrofit singleton class need to be in JAVA to work properly. No big problem at all. The other files are in Kotlin. UIAutomator its responsible for do orientation changes during the tests. Interceptor and OkHttp are used for logging the results of the API requests on Logcat.
  
  ```
   /// tests
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    androidTestImplementation 'androidx.test:runner:1.3.0'
    androidTestImplementation 'androidx.test:rules:1.3.0'
    androidTestImplementation 'androidx.test.espresso:espresso-contrib:3.3.0'
    androidTestImplementation 'androidx.test.uiautomator:uiautomator:2.2.0'
    androidTestImplementation "com.squareup.okhttp3:mockwebserver:3.4.2" // SDk 16 support 3.4.2
    androidTestImplementation "net.vidageek:mirror:1.6.1"
  ```


  - [x] The app shows both, a thumbnail and the title 
  
  - [x] Currently using "fields" to minimize bandwidth consumption (https://developers.google.com/bookListDTO/docs/v1/performance)
  
    ```JAVA
    public static final String FIELDS = "kind,bookDTOS(id,volumeInfoDTO/title,volumeInfoDTO/authors,volumeInfoDTO/publisher,volumeInfoDTO/publishedDate,volumeInfoDTO/description,volumeInfoDTO/imageLinksDTO(smallThumbnail)searchInfoDTO(textSnippet),saleInfoDTO/buyLink)";
    ```
  
  - [x] The app should be usable in both Android phone and tablet.
  
  - [x] The list should also have a button to filter/show only bookListDTO that the user has set as favorite.

  - [x] When the user clicks on one of the bookListDTO, the app should present a detailed view displaying the most relevant information of the book: Title, Author, Description and, if available, a Buy link.

  - [x] In the detail view, the user can also favorite or unfavorite a book. This option should be stored locally so it persists through each app usage.
  
  - [x] Clicking on the Buy link should open the link on browser.



## Changelog
  - Retrocompatibility
    - **app (flavor normal)** minSdk changed from 21 to **16**
    - **androidTest (flavor espressoTest)** minSdk changed from 21 to **19**. _(**UiAutomator** requires minSdk 19)_
    - Product Flavors created to make everything more explicit

app build.gradle:

```
    ...
    productFlavors {

        // App supports api 16
        normal {
            dimension "v1"
        }

        // Automated tests only works on API 19 and higher
        espressoTest {
            minSdkVersion 19
        }
    }
    
    ...
    // SDK 16 support (do not update this)
    implementation 'com.squareup.retrofit2:retrofit:2.3.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:3.4.2'
    ...
    // tests
    ...
    androidTestImplementation "com.squareup.okhttp3:mockwebserver:3.4.2" // SDk 16 support 3.4.2
    ...
```
