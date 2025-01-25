# Warframe Marketplace Android App
A mobile trade market app for the popular third person MMO Warframe. Pulling data from warframe.market the app displays item information to the user that might be part of the set.
The goal of this project was to introduce myself to Kotlin and Android mobile app development. 

## Features
- Kotlin
- Warframe.Market API integration
- JSON to data class
- User authentication
- SharedPreferences storage
- XML Layouts
- Navigation Drawers
- Recycler View
- Fragments
- Item sort by search

### How it works
- Upon initial app load, an API GET request is sent to pull a list of all items available on warframe.market and is saved as a global variable for future use. 
- SharedPreferences is checked to see if user credentials are stored, if they are display the items list page, else display the login page.
#### Currently there are no features implemented with user authentication. In future updates, in order to make account changes / posts we will need user authentication which is why it's temporarily setup.   
- On the items list page, display all the items as card views in a recycler view that were pulled from the initial GET request.
- If a specific item is clicked, send another GET request to pull the clicked item's information, parse the retrieved information and setup a model to pass to the item details fragment.
- The item details fragment will then display the:
  - Item image
  - Item description
  - Other items (if part of a set) and their associated images
  - List of prices and the sellers (sorted by ascending order)
  - Information link which will redirect the user to the item's wiki page

## Getting Started
- To be added in a future update
