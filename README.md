# checkme #
[![Build Status](https://travis-ci.com/ENZOBERNARD/checkme.svg?branch=master)](https://travis-ci.com/ENZOBERNARD/checkme)
[![codecov](https://codecov.io/gh/ENZOBERNARD/checkme/branch/master/graph/badge.svg?token=BYQ6DHTR1V)](https://codecov.io/gh/ENZOBERNARD/checkme)

## Basic checkme responders. ##

### What's checkme ? ###

Checkme is an endpoint that allows you to check the communication between an application of your project and an external service used by your application. For example a database, a search-engine or even another application of your project. You will configure a path, which lead to a page where it will be written "**OK**" if the communication is working, "**KO**" or any other response (Even an error) if not.

### Getting started ###

First, ensure that your project matches the [prerequisites](Details/Prerequisites) to get checkme working.

Then, you need to put the following dependency inside your pom.xml

    <dependency>
        <groupId>checkme</groupId>
        <artifactId>checkme</artifactId>
        <version>${version}</version>
    </dependency>

Then, you have to configure a properties file called "checkme_route.properties" with the parameters associated to the checkme you want to add to your project. This properties file is composed of keys, each written on a line. Here is how it works :

*  each key contains three parts:

   *  the prefix "route"
   *  an identifier for your responder
   *  a suffix "path", "class", "config.xxx" or "header.xxx". every key with the suffix "config.xxx" will populate a map inside the responder
*  for each identifier, the path for your responder is mandatory (suffix : path)
*  for each identifier, the class for your responder is also mandatory (suffix : class)

Here is an example below, the minimum you need to get a checkme working :

    route.appserver.path=/{a path you will write after "[your server address]/checkme" to access the "OK" or "KO" page}
    route.appserver.class={the class that will do the verification of the communication}

The **appserver** part is the identifier, wich makes the link between the different keys. It means you can change it, but you need to change it for every key of the checkme you configured.

*  the suffix "config.xxx" is optional, it allows you to pass informations to the class of your responder, if you need it. Every key of the same identifier with the suffix "config.xxx" will populate a map inside the class of the responder.
*  the suffix "header.xxx" is also optional. Its purpose is to add a header in the response of the HTTP request made on a checkme. To do that, you need to add the key "header" after the identifier, followed by the name of the header you want to be added to the response. The name of the header will be "xx-checkme-**theNameYouWrote**". What you will put after the "=" will be the value assigned to the header.

Example :

    route.appserver.path=/...  
    route.appserver.class=...  
    route.appserver.config.exampleconfig=key
    route.appserver.header.exampleheader=headervalue  

The result will be that :
*  In the responder class, the method getMandatoryConfiguration("exampleconfig") will return the string "key"

*  On this responder endpoint, the HTTP response will contain the header "xx-checkme-exampleheader : headervalue".
   You can add as many headers as you want per checkme configured in the properties

If you want more details about the default configuration of the checkme, you can take a look at [this page](Details/Your own responder).

Those properties will depend on the type of service you want to verify your application communicates with :

*  For a simple checkme that checks if your server is on, [the Application Server Responder](https://gitlab.kazan.priv.atos.fr/checkme/checkme-wiki-1-migrated/wikis/Details/The-responders#application-server-responder) is what you need.

*  To check the communication with a JDBC database, you may be interest in [the JDBC Responder](https://gitlab.kazan.priv.atos.fr/checkme/checkme-wiki-1-migrated/wikis/Details/The-responders#jdbc-responder)

*  To verify the existence of a file, take a look at [the File Responder](https://gitlab.kazan.priv.atos.fr/checkme/checkme-wiki-1-migrated/wikis/Details/The-responders#file-responder)

*  To check if another server is on, or to make an HTTP request and check the content of the page, take a look at [the HTTP Responder](https://gitlab.kazan.priv.atos.fr/checkme/checkme-wiki-1-migrated/wikis/Details/The-responders#http-responder)


That's it ! Thanks to this dependency, you have one endpoint in your webapp that works without configuration needed :

*  **/checkme/appserver** that answers "OK" if your Application Server is up.

The following are pre-declared, but you need to configure them. [Here is the configuration file](Details/Your own responder) that you need to reproduce and complete to get them working, and [here is how they work](Details/The responders).
*  **/checkme/jdbc** that answers "OK" if your JDBC connection works.
*  **/checkme/sqlRequest** that answers "OK" if your custom JDBC connection works.
*  **/checkme/repository** that answers "OK" if the file you configured to be checked exists.
*  **/checkme/fileWithDate** that answers "OK" if the file with a date in his name you configured to be checked exists.
*  **/checkme/okHTTP** sthat answers "OK" if the server you configured to be checked is responding.

### Table of content ###

*  [Prerequisites](Details/Prerequisites)
*  [The default responders](Details/The responders)
*  [How to add your own responder](Details/Your own responder)