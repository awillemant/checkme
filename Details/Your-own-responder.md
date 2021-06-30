# How to build your own responder? #

## Overriding the configuration file ##

The default configuration file is named checkme_routes.properties, it is placed in the resources directory of the common checkme dependency and here is what you can configure in it :

    route.appserver.path=/appserver
    route.appserver.class=io.checkme.surv.WatchApp
    
    route.jdbc.path=/jdbc
    route.jdbc.class=io.checkme.surv.jdbc.WatchJDBC
    route.jdbc.config.propertiesPath=jdbc.properties
    route.jdbc.config.urlKey=jdbc.dataSource.url
    route.jdbc.config.userKey=jdbc.dataSource.username
    route.jdbc.config.passwordKey=jdbc.dataSource.password
    route.jdbc.config.driverKey=jdbc.dataSource.driver

    route.specificRequestJDBC.path=/sqlRequest
    route.specificRequestJDBC.class=io.checkme.surv.jdbc.WatchSpecificRequestJDBC
    route.specificRequestJDBC.config.propertiesPath=jdbc.properties
    route.specificRequestJDBC.config.urlKey=jdbc.dataSource.url
    route.specificRequestJDBC.config.userKey=jdbc.dataSource.username
    route.specificRequestJDBC.config.passwordKey=jdbc.dataSource.password
    route.specificRequestJDBC.config.driverKey=jdbc.dataSource.driver
    route.specificRequestJDBC.config.sqlRequest=SELECT 0
    route.specificRequestJDBC.config.sqlIntegerResult=0

    route.repository.path=/repository
    route.repository.class=io.checkme.surv.file.WatchSimpleFile
    route.repository.config.fileToTest={the path to the file to test}

    route.specificFileWithDate.path=/fileWithDate
    route.specificFileWithDate.class=io.checkme.surv.file.WatchSpecificFileWithDate
    route.specificFileWithDate.config.templateFileToTest={the path to the file to test with %DATE% in the name of the file}
    route.specificFileWithDate.config.delayType=DAY
    route.specificFileWithDate.config.delayValue=-1
    route.specificFileWithDate.config.dateFormat=dd-MM-yy

    route.okHTTP.path=/okHTTP
    route.okHTTP.class=io.checkme.surv.WatchHTTP
    route.okHTTP.config.url={The address of a server using the checkme}/checkme/appserver
    route.okHTTP.config.handler-class=OkHttpResponseHandler

 *  each key contains three parts:
    
     *  the prefix "route"
     *  an identifier for your responder
     *  a suffix "path", "class" or "config.xxx", every key with the suffix "config.xxx" will populate a map inside the responder
 *  for each identifier, you have to indicate the path for your responder (suffix : path)
 *  for each identifier, you have to indicate the class for your responder (suffix : class)

You can override the configuration by creating your own checkme_routes.properties in the root of the classpath, or by reconfiguring the servlet in your web.xml like this:

    <servlet>
        <servlet-name>checkme</servlet-name>
        <servlet-class>io.checkme.servlet.checkmeServlet</servlet-class>
        <init-param>
            <param-name>routesProperties</param-name>
            <param-value>your_routes.properties</param-value>
        </init-param>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>checkme</servlet-name>
        <url-pattern>/checkme/*</url-pattern>
    </servlet-mapping>

## Create your own class ##

Once you configured the path for your own class, you just have to follow a few rules:

 *  Your class should extend io.checkme.surv.AbstractCheckme
    
 *  Your class should have a constructor with one argument : the path linked to your responder. This constructor has to call the super constructor with the same argument