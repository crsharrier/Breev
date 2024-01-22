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


![Screenshot_20240122-101010](https://github.com/crsharrier/Breev/assets/99554423/ab2b6a9d-4b96-4648-90cc-6c94af142814)
![Screenshot_20240122-102456](https://github.com/crsharrier/Breev/assets/99554423/672139fd-a259-45c9-b914-2b9bdbb966c7)
![Screenshot_20240122-101032](https://github.com/crsharrier/Breev/assets/99554423/78e251de-0d30-4d8a-be7a-bcecd92627aa)  
(^ history page still to be implemented ^)
![Screenshot_20240122-101019](https://github.com/crsharrier/Breev/assets/99554423/f7157eb1-7c4f-4879-a1b8-07d32d78cef8)
![Screenshot_20240122-102208](https://github.com/crsharrier/Breev/assets/99554423/74271fc2-2150-43af-9630-87005af09d4e)
![Screenshot_20240122-102211](https://github.com/crsharrier/Breev/assets/99554423/e95bb309-dbc6-422e-960b-7026b4d0abd9)  

![Screenshot_20240122-102328](https://github.com/crsharrier/Breev/assets/99554423/b067b4d3-61bb-4ff8-920b-e7092fe168c1)
![Screenshot_20240122-102442](https://github.com/crsharrier/Breev/assets/99554423/6d2e6c88-db97-44e7-8d10-e5ab80ffffbd)
