# jitter
Web frontend and Scraper for Twitter written in Java 11 using Spring+Thymeleaf.
This program contains a Twitter scraper that loads the official website and gets relevant information. 
It also includes a simple web frontend that is easily replacable.
All tweets and accounts can be cached in a postgres database either on machine, in memory, or remotely.

## Features
- Simple web frontend
- Dark theme
- No Twitter API token required
- Locally pin accounts
- Custom timeline without account
- Drop in replacement for twitter.com
- Minimal Javascript
- Mobile Support

## Screenshot
![twitter account](https://github.com/mgurga/jitter/blob/master/docs/twitter.png?raw=true)

## Setup your own instance
To build jitter you will need git, maven, and java 11 installed.
```
git clone https://github.com/mgurga/jitter
cd jitter
mvn -Dmaven.test.skip=true clean package spring-boot:repackage
java -jar target/jitter-X.X.X-SNAPSHOT.jar
```
To set a database edit the [application.properties](https://github.com/mgurga/jitter/blob/master/src/main/resources/application.properties).
By default the program will use a in memory database that will be wiped on program exit.
Uncomment the last line to clear the database if incorrect data is inputed or the program has been updated.

## Credits
- [zedeus/nitter](https://github.com/zedeus/nitter) - This project was based on nitter by zedeus, I wrote jitter because it did not have custom timeline support and was written in Nim which I don't know.
- [Google](https://fonts.google.com/icons) for the icons.
- [Twitter](https://twitter.com/) for the wonderful website to parse through. :)
