# Contact Info Application

- Login screen with persistent login, where you dont need to login everytime
  - This is achieved via Shared Preferences.
- After login, a contact listing screen is shown. If the data already exist in the local database then that data is fetch and shown. Otherwise data is fetched from https://api.androidhive.info/contacts/ then saved into the local databases and also showed on the screen.
- On clicking on any item the application takes you to detail screen for that item.
