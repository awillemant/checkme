## Here are the dependencies imported to get the library working ##

### checkme ###

The basic library needs the following dependencies to be included in your project to work, with these versions or above : 

    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>1.7.2</version>
    <scope>provided</scope>

    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.0.1</version>
    <scope>provided</scope>


### checkme HTTP Handlers ###

The Http Handlers module will import the following dependency in addition to those of the basic checkme module :

    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.6</version>

### checkme MongoDB Handler ###

The MongoDB Handler module will import the following dependency in addition to those of the basic checkme module :

    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver</artifactId>
    <version>3.6.4</version>