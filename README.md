### Breev
#### A timer app based on Wim Hof's breathing exercise
Original Wim Hof exercise: https://www.youtube.com/watch?v=tybOi4hjZFQ

Inspired by the official app suddenly becoming really expensive, I decided to make my own app for those odd occasions I decide to sit down and do a breathing exercise! Although this would certainly not be the most efficient use of time if my primary concern was to actually do breathing exercises, it was in fact a great project for learning about app architecture - just the right-sized challenge to keep me engaged.

Concepts implemented:
  - Model-View-ViewModel architecture (MVVM)
  - Mixed object-oriented elements with Jetpack Compose's declarative paradigm: in order to allow BreevBlob and SettingsWidget to morph between navigation destinations, I implemented them as classes which are instantiated inside the BreevAppState class. Each class draws the element using a @Composable draw() member function, and exposes functions for animating the size and visibility.
  - Persistence of settings and data using DataStore 

TO DO:
  - Custom graph on history page. This is the perfect excuse to finally delve deeper into the lower-level drawing APIs I have been avoiding...

Next time:
  - Set up for TDD from the outset. Takes more time to get the ball rolling but will save you so many Log() statement debugging sessions down the line, future Christian.