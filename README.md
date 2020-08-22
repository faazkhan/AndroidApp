# Movies Info Application 
Watch [Demonstration!!](https://confizpk-my.sharepoint.com/:v:/g/personal/faaz_ahmad_confiz_com/EcUsnBGoNuVAmezz8R20vf0BBPzzNZJc5eeFp3Fs_DZBgQ?e=M9JJ8M)

###### Description:
- Login screen with persistent login, where you dont need to login everytime
  - This is achieved via Shared Preferences.
- After login, a movie listing screen is shown. 
  - If the device is connected to the internet then latest movies data is fetched from https://api.androidhive.info/json/movies.json then saved into the local databases and also showed on the screen. I have used [Volley](https://developer.android.com/training/volley) library for this purpose.
  - If device is not connected to the internet then data is simply fetched from the local database.
- On clicking on any item the application takes you to detail screen for that item.

