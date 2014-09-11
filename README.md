cat-walk
========

I'm frequently asked to provide example code or take tests when interviewing for new contract work. One recent test/task was to build something that consumes data from thecatapi.com and displays it to the user - here is the result, it took 3 hours from opening the IDE to being ready to publish.

Example cat app that pulls from The Cat API and displays a picture of a cat. Tap for more cats!

A few notes on anyone looking through the code:
- There are some TODOs in place to illustrate what could be done differently
- It was built in Eclipse using ANT, so that anyone can check out the project and build it (Android Studio would have led to problems if anyone reviewing is using Eclipse
- It was written in 3 hours. The target was to have something that could be placed on the play store in 3 hours, so there was a conscious effort to be up against the clock.
- There aren't any unit tests written, even though I have experience with JUnit, Robolectric and Mockito. I can explain why on the phone

Online demo app can be found at: https://play.google.com/store/apps/details?id=net.rdyonline.catwalk
