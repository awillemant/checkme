# The default responders #

## Application server responder ##

This is a very simple code that writes "OK" in the outputstream of the HTTPServletResponse. If your application server is up, you should get "OK" when calling its corresponding URL. The associated class is "**wl.checkme.surv.WatchApp**". Example :

    route.appserver.path=/appserver
    route.appserver.class=io.checkme.surv.WatchApp

## JDBC responder ##

This responder looks for the jdbc parameters, provided in the checkme\_routes.properties file :

1.  it looks for the jdbc.properties file in the root of the classpath (route.jdbc.config.propertiesPath in the properties file)
2.  it reads the following keys :
    
     *  jdbc.dataSource.url (route.jdbc.config.urlKey in the properties file)
     *  jdbc.dataSource.username (route.jdbc.config.userKey in the properties file)
     *  jdbc.dataSource.password (route.jdbc.config.passwordKey in the properties file)
     *  jdbc.dataSource.driver (route.jdbc.config.driverKey in the properties file)

Once he finds the parameters, it connects to the database then executes the request "SELECT 1" and verify the results. If the result is 1, then it responds "OK". The associated class is "**wl.checkme.surv.jdbc.WatchSimpleJDBC**". Example :

    route.jdbc.path=/jdbc
    route.jdbc.class=io.checkme.surv.jdbc.WatchJDBC
    route.jdbc.config.propertiesPath=jdbc.properties
    route.jdbc.config.urlKey=jdbc.dataSource.url
    route.jdbc.config.userKey=jdbc.dataSource.username
    route.jdbc.config.passwordKey=jdbc.dataSource.password
    route.jdbc.config.driverKey=jdbc.dataSource.driver

3.  in addition to the previous simple JDBC responder, another one exists, for which you can configure both the request and the result. It needs the same parameters as the simple JDBC responder, and two more :

     *  The request (route.specificRequestJDBC.config.sqlRequest in the properties file)
     *  The result, an integer (route.specificRequestJDBC.config.sqlIntegerResult in the properties file)

Once he finds the parameters, it connects to the database then executes the configured request and verify the results. If the result of the request is the same as the one configured, then it responds "OK". The associated class is "**io.checkme.surv.jdbc.WatchSpecificRequestJDBC**". Example :

    route.specificRequestJDBC.path=/sqlRequest
    route.specificRequestJDBC.class=io.checkme.surv.jdbc.WatchSpecificRequestJDBC
    route.specificRequestJDBC.config.propertiesPath=jdbc.properties
    route.specificRequestJDBC.config.urlKey=jdbc.dataSource.url
    route.specificRequestJDBC.config.userKey=jdbc.dataSource.username
    route.specificRequestJDBC.config.passwordKey=jdbc.dataSource.password
    route.specificRequestJDBC.config.driverKey=jdbc.dataSource.driver
    route.specificRequestJDBC.config.sqlRequest=SELECT 0
    route.specificRequestJDBC.config.sqlIntegerResult=0

The responder works with MySQL, H2, SQL Server and PostGre

## File Responder ##

1.  This responder checks the existence of a file. 

     *  It needs the absolute path to the file as a parameter (route.repository.config.fileToTest in the properties file)

Once it gets the path to the file, the responder tries an "exists()" method on the file, to check if it is present. If it is, then it responds "OK". The associated class is "**io.checkme.surv.file.WatchSimpleFile**". Example :

    route.repository.path=/repository
    route.repository.class=io.checkme.surv.file.WatchSimpleFile
    route.repository.config.fileToTest={the absolute path to the file to test}

2.  Same as the JDBC responder, the File responder also provides another service, which is the File with Date Responder. It takes as parameters :

     *  The absolute path to the file, with the position where the date is supposed to be in the title marked with "%DATE%"
        (route.specificFileWithDate.config.templateFileToTest in the properties file)
     *  The delay type for the file : DAY, HOUR, MINUTE or SECOND (route.specificFileWithDate.config.delayType in the properties 
        file)
     *  The delay value, an integer (route.specificFileWithDate.config.delayValue in the properties file)
     *  The date format of the date, in the file name, for example "dd-MM-yy" (route.specificFileWithDate.config.dateFormat in 
        the properties file)

Once it gets the dates parameters, the responder creates a string of the date and replaces the tag in the file name with this string, then it wworks the same way as the simple File Responder, it checks the existence of the file at the path and answers "OK" if it exists. The associated class is "**io.checkme.surv.file.WatchSpecificFileWithDate**". Example :

    route.specificFileWithDate.path=/fileWithDate
    route.specificFileWithDate.class=io.checkme.surv.file.WatchSpecificFileWithDate
    route.specificFileWithDate.config.templateFileToTest={the absolute path to the file to test with %DATE% in the name of the file}
    route.specificFileWithDate.config.delayType=DAY
    route.specificFileWithDate.config.delayValue=-1
    route.specificFileWithDate.config.dateFormat=dd-MM-yy

## HTTP Responder ##

1.  This responder allows you to check an http or https address. It takes as parameters :

     *  The URL address of the page you want to check (route.http.config.url in the properties file)
     *  The handler class you want the responder to use, depending on the content of the page and on the element you want to 
        check in the content of the page (route.http.config.handler-class in the properties file)

2.  Its first checks if the response code of the request is 200, it then can be used in three different manners : 

Using the furnished "OKHttpResponseHandler", the responder checks the content of the page, and answers "OK" if the content of the page it checks is also "OK" : The goal of this responder is to verify if another webapp is active by checking the [Application Server Responder](https://gitlab.kazan.priv.atos.fr/checkme/checkme-wiki-1-migrated/wikis/Details/The-responders#application-server-responder) of the aimed webapp. It means that in order to work well, both webapps needs to use the checkme dependency. The url address to check you will give in the properties needs to end with "/checkme/appserver". 
The associated class for this responder is "**io.checkme.surv.WatchHTTP**", and the handler to put in the properties is the "**OkHttpResponseHandler**". Example :

    route.okHTTP.path=/okHTTP
    route.okHTTP.class=io.checkme.surv.WatchHTTP
    route.okHTTP.config.url={The address of a server using the checkme}/checkme/appserver
    route.okHTTP.config.handler-class=OkHttpResponseHandler

The second way to use the reponder allows you to create a simple handler to check the presence of a string in the content of a page by extending the AbstractSimpleHttpHandler class. If the string is found in the source code of the page, the responder retruns true. You'll need to implement two methods and to add the constructor method of a LOGGER. Here is how your class is supposed to look :

    public class ExampleHttpResponseHandler extends AbstractSimpleHttpHandler {

        private static final Logger LOGGER = LoggerFactory.getLogger(ExempleHttpResponseHandler.class);

        @Override
        public void logErrorMessage(Exception e) {
            LOGGER.error("Failed to reach Example :", e);
        }

        @Override
        public String getExpectedContent() {
            return "The string you want to verify the presence of";
        }
    }


You can also create your own handler, all by yourself, by implementing the interface "HttpResponseHandler"; Then, you have to override the "handle" boolean method (which takes a HttpURLConnection as parameter) and write down a code that will check what you want to verify in this page. A method is available,  which uses the connection and returns a String of the content of the page. It reads the content of the page line by line, and stops either when the line is null (The content has been entirely read). The using of this method is not mandatory, it's here to ease the creation of the handler. You can also handle the HTTPUrlConnection yourself. Return true if the requirements were met, and false if not. The associated class for this responder is "io.checkme.surv.WatchHTTP", and the handler to put in the properties is the handler class that you created.

To make the HTTP Responder more configurable, we added an optional key that you are not forced to use, which is the time out parameter. By default, it is set at 5 000 milliseconds, or 5 seconds. 
You can override it by adding the following to your properties file : 

    route.okHTTP.config.timeout=10000  (You need to put it in milliseconds, so it corresponds 10 seconds here)

# The additional responders #

## ElasticSearch, CalDav, Vidal, Keycloak Handlers ##

These 4 handlers are available by using the "checkme-http-handlers" dependency of the group "checkme". To get them working, you also need to use the basic checkme dependency, resulting in your pom.xml needing the following written inside :

    <dependency>
        <groupId>checkme</groupId>
        <artifactId>checkme</artifactId>
        <version>${version}</version>
    </dependency>
    <dependency>
        <groupId>checkme</groupId>
        <artifactId>checkme-http-handlers</artifactId>
        <version>${version}</version>
    </dependency>

These handlers works with [the HTTP Responder](https://gitlab.kazan.priv.atos.fr/checkme/checkme-wiki-1-migrated/wikis/Details/The-responders#http-responder).
It means that to use them, you just need to configure a HTTP responder, and provide one of the 3 handler as the handler-class key in the properties file. Also be careful to indicate the right URLs in the properties file. Now let's see how each one of these handlers works :

*  The CalDav Handler analyses the response of a GET request done by the responder on the "*/.web/" endpoint of your local CalDav installation, and checks if the title of the page is "Web interface for Radicale"

*  The ElasticSearch Handler analyses the response of a GET request done by the responder on the "*/moteur-recherche" endpoint of your local ElasticSearch installation, then parse the Json content of the page, and verifies if the tagline of the Json shema is "You Kow, for Search".

* The Vidal Handler analyses the response of a GET request done by the responder on the "http://apirest-dev.vidal.fr/rest/api/version" page, and then watches if the content string contains the "Current Data and Application versions" string. 

*  The Keycloak Handler analyses the response of a GET request done by the responder on the "*/auth/" endpoint of your local Keycloak installation, and checks if the title of the page is "Welcome to Keycloak"

Here is an example of a configuration :

    route.toVidal.path=/checkVidal
    route.toVidal.class=io.checkme.surv.WatchHTTP
    route.toVidal.config.url=http://apirest-dev.vidal.fr/rest/api/version
    route.toVidal.config.handler-class=io.checkme.surv.handlers.VidalHttpResponseHandler

## MongoDB Checkme Responder ##

This checkme responder is available by adding the "checkme-mongodb" dependency to your project, additionnaly to the basic checkme dependency. So be sure your pom.xml contains the following :

    <dependency>
        <groupId>checkme</groupId>
        <artifactId>checkme</artifactId>
        <version>${version}</version>
    </dependency>
    <dependency>
        <groupId>checkme</groupId>
        <artifactId>checkme-mongodb</artifactId>
        <version>${version}</version>
    </dependency>

1.  The responder reads the following keys :

    *  the URI to connect to a mongo instance (route.toMongo.config.url in the properties file)
    *  the name of the database you want to get the collections names of (route.toMongo.config.database in the properties file)
    *  the collections names, sorted and separated by a semi-column (route.toMongo.config.collections in the properties file)

2.  The way this responder works is quite simple. When you call it, the responder will try to connect to a mongo instance using the uri that you will provide in the properties file, it will then list the collections in the database using the database name you will furnish in the properties file. Once it gets the collections names, it creates a string of the names separated by semi-column and compares it to the one you will furnish, composed of the expected collections names separated by a semi-column. If at least every collection name you provided is found in the mongoDB database, the responder returns true. Be careful as the comparison also takes the matching case in account. The associated class for this responder is "**io.checkme.surv.WatchMongoDB**"

Example :

    route.toMongo.path=/toMongo
    route.toMongo.class=io.checkme.surv.WatchMongoDB
    route.toMongo.config.url=mongodb://username:password@localhost:27017/database-name
    route.toMongo.config.database=database-name
    route.toMongo.config.collections=collectionA;collectionB;collectionC
