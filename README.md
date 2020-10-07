# exampleBookStore
## A simple example to demonstrate part of my knowledge :coffee: :computer: :coffee: :iphone: :coffee: :sparkles:


  - [x] Book Store consists on showing a simple 2-column list of available books about Android development. Using google’s api for books, the app should fetch and display the Thumbnail of a few books at a time and load more as the user scroll’s through the list. 
Rest API: https://developers.google.com/books/docs/v1/getting_started#REST

  - [x] UI Tests with espresso
  
  - [x] androidTest: Mocked API with espresso and Mirror. Mirror uses reflection to create mocked response, so some classes need to be in JAVA (like Retrofit singleton and model classes). No big problem at all. The other files are in Kotlin. UIAutomator its responsible for do orientation changes during the tests.
  
  ```
   // tests
    testImplementation 'junit:junit:4.13'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    androidTestImplementation 'androidx.test:runner:1.3.0'
    androidTestImplementation 'androidx.test:rules:1.3.0'
    androidTestImplementation 'androidx.test.espresso:espresso-contrib:3.3.0'
    androidTestImplementation 'androidx.test.uiautomator:uiautomator:2.2.0'
    androidTestImplementation "com.squareup.okhttp3:mockwebserver:4.9.0"
    androidTestImplementation "net.vidageek:mirror:1.6.1"


    //OkHttp
    implementation ("com.squareup.okhttp3:okhttp:4.9.0"){
        force = true //API 19 support
    }
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.0'
  ```


  - [x] The app shows both, a thumbnail and the title 
  
  - [x] Currently using filtered request to minimize bandwidth consumption (https://developers.google.com/books/docs/v1/using#filtering)
  
    ```JAVA
    public static final String FIELDS = "kind,items(id,volumeInfo/title,volumeInfo/authors,volumeInfo/publisher,volumeInfo/publishedDate,volumeInfo/description,volumeInfo/imageLinks(smallThumbnail)searchInfo(textSnippet),saleInfo/buyLink)";
    ```
  
  - [x] The app should be usable in both Android phone and tablet.
  
  - [x] The list should also have a button to filter/show only books that the user has set as favorite.

  - [x] When the user clicks on one of the books, the app should present a detailed view displaying the most relevant information of the book: Title, Author, Description and, if available, a Buy link.

  - [x] In the detail view, the user can also favorite or unfavorite a book. This option should be stored locally so it persists through each app usage.
  
  - [x] Clicking on the Buy link should open the link on browser.
