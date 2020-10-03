# TwitterSweep
[![Java](https://img.shields.io/static/v1.svg?label=Java&color=brown&message=v11)]() [![Spring Boot](https://img.shields.io/static/v1.svg?label=Spring%20Boot&color=green&message=v2.3.4)]() [![HTML](https://img.shields.io/static/v1.svg?label=&color=orange&message=HTML)]() [![Bootstrap](https://img.shields.io/static/v1.svg?label=&color=purple&message=Bootstrap)]()  [![JavaScript](https://img.shields.io/static/v1.svg?label=&color=yellow&message=JavaScript)]() [![JQuery](https://img.shields.io/static/v1.svg?label=&color=blue&message=JQuery)]()\
[![build](https://img.shields.io/static/v1.svg?label=build&color=success&message=passing)]() [![coverage](https://img.shields.io/static/v1.svg?label=coverage&color=success&message=86%)]()

TwitterSweep is a Java Spring Boot Web Application that can stream live tweets from twitter based on given search conditions.

## Introduction
TwitterSweep accepts search terms from the end-user and connects to twitter 
via Twitter API v2 to stream live tweets from twitter that match the search terms provided by the end-user.
Search terms can be either the twitter username or certain keywords that may appear in the tweets being streamed. These search terms can be easily configured using the web app which allows for easy addition or removal of search terms.

## Set Up
TwitterSweep is build using the Eclipse IDE. It is primarily written in Java v11 and hence requires JDK v11 to be installed on the system. 
TwitterSweep is a Maven project in Eclipse and hence requires the Eclipse IDE or any capable Java Development IDE that supports Maven projects such as Apache Netbeans, IntelliJ IDEA, etc for project configuration, building and running.
Since TwitterSweep is build using Spring Boot, the Spring Tools 4 can also be used.


* JDK v11: https://www.oracle.com/java/technologies/javase-jdk11-downloads.html
* Eclipse IDE: https://www.eclipse.org/downloads/packages/installer
* Spring Tools 4: https://spring.io/tools

## Installation

* ##### Import Maven Project To Eclipse
> Select ``` File ``` > ``` Import ```\
> Select ``` Existing Maven Projects ``` under ``` Maven ```\
> Click ``` Next ```\
> Click ``` Browse ``` and browse to the root directory of the maven project where ``` pom.xml ``` is located\
> Select ``` pom.xml ``` and Click ``` Finish ```

* ##### Configure OAuth 2.0
> TwitterSweep users Twitter API v2 to gain access to twitter and stream tweets in realtime. This requires authentication using OAuth 2.0 which users a Bearer Token for authorization. Once an app is registered on the twitter API and a Bearer Token is received, it can be added to ``` application.settings ``` file under ``` src/main/resources ```\
>The ``` application.settings ``` file contains a property ``` twitter.authorization.bearer ``` to configure the bearer token.\
>Add bearer token as follows ``` twitter.authorization.bearer=$```, where ```$``` must be replaced by the bearer token.

* ##### Configure MAX_TWEETS
> The ``` app.js ``` under ``` src/main/resources/static ``` contains a javascript constant ```MAX_TWEETS``` which accepts a positive integer that controls the maximum no. of tweets being displayed in the web app.\
>```MAX_TWEETS```>=1

* ##### Build and Run
>Once the above steps are complete, the project needs to be build and run.\
>Click on the ``` Run ``` icon or press ``` ctrl + F11``` to build and run the application.

* ##### Open the Web App
>Open any browser such as Goggle Chrome or Mozilla Firefox and type ``` localhost:8080 ``` in the search bar and hit Enter.

## Web App Usage
* ##### Configure Search
>TwitterSweep Web App contains a search configurator which consists of two search bars which allow for addition and deletion of search terms.\
>The search bar on the left is used to configure twitter usernames and the one on the right is used to configure keywords.\
>To add a search term, simply type it in the corresponding search bar and press the ```  Add ``` button or hit Enter. The search term will be added and displayed below the corresponding search bar.\
>To remove a search term, simply hover the mouse pointer over it until ```X``` turns from blue to red and left click to remove it.

* ##### START and STOP
>TwitterSweep consists of two buttons ``` START ``` and ``` STOP ```.\
>Press ```START``` to start streaming live tweets from twitter.\
>Press ```STOP``` to stop streaming.\
>```NOTE:``` If no search terms are configured, but stream was started, then no tweets will be recieved.\
>Search terms can also be configured while streaming.
