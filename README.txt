download glassfish zip from glassfish.org
unzip it at some location, in will unpack to glassfish6 directory
  unzip ./glassfish-6.2.5.zip

NOTE: Maybe it is a good idea to unzip it into our project, 'glassfish6' is added to .gitignore
(or symlink it, i.e.  ln -s ../glassfish6 )

Download h2 as a platform independent zip from https://www.h2database.com/html/download.html
(say, https://github.com/h2database/h2database/releases/download/version-2.1.212/h2-2022-04-09.zip)
unzip it to glassfish6 so that it would have h2 directory next to javadb and imq
  cd glassfish6
  unzip ~/Downloads/h2-2022-04-09.zip   
Now install h2.jar into glassfish via
  cp h2/bin/h2-2.1.212.jar ./glassfish/modules/h2.jar
(we change the name to h2.jar so that we always overwrite the same file)

Restart glassfish.

Run
  glassfish6/bin/asadmin add-resources ../src/main/resources/app-server-resources.xml 
this will add connection pool and datasource.

Ping the datasource (via Ping button at 'Edit JDBC Connection Pool' for mydemofullweb-h2-pool at admin console)
If it succeeds the db file will be created at
glassfish6/glassfish/domains/domain1/config/deploy/mydemofullweb.mv.db


stop server, edit glassfish6/glassfish/domains/domain1/domain.xml
at 
  <configs>
    <config name="server-config">
      <system-property name="JMS_PROVIDER_PORT" description="Port Number that JMS Service will listen for remote clients connection." value="7676"></system-property>
      <http-service>
        <access-log></access-log>
        <virtual-server network-listeners="http-listener-1,http-listener-2" id="server"></virtual-server>
add 
   default-web-module="mydemofullweb-1.0-SNAPSHOT"
so it becomes   
     <virtual-server default-web-module="mydemofullweb-1.0-SNAPSHOT" network-listeners="http-listener-1,http-listener-2" id="server"></virtual-server>



In order to connect IDEA to DB: 
   on the 'Database' tab of Idea add new data source:
      choose H2, "embedded" mode (by clicking on 'remote' in blue) and enter PATH:
   the path to the .mv.db file above, i.e. 
      /Users/<name>/.../glassfish6/glassfish/domains/domain1/config/deploy/mydemofullweb.mv.db
   So that URL becomes
     jdbc:h2:/Users/.../glassfish6/glassfish/domains/domain1/config/deploy/mydemofullweb

   Do not click 'download missing driver files', use 'h2/bin/h2-2.1.212.jar' instead.
   (Idea now downloads 2.1.210 which doesn't seem to be fully compatible)

   Use username and password from mydemofullweb/src/main/resources/app-server-resources.xml 
   (both mydemofullweb for now)

   Click 'Test Connection' at the bottom, it should succeed and say:
        DBMS: H2 (ver. 2.1.212 (2022-04-09))
        Case sensitivity: plain=upper, delimited=exact
        Driver: H2 JDBC Driver (ver. 2.1.212 (2022-04-09), JDBC4.2)
        Ping: 14 ms

   IMPORTANT: on 'advanced' tab at 'expert' check 'use JDBC metadata for intraspection'
   - H2 integration with IDEA seems to be a bit broken, it uses TABLE_CATALOG instead of INFORMATION_SCHEMA_CATALOG_NAME

   now under PUBLIC you should see tables and USERACCOUNT.

    As said in (https://blog.jetbrains.com/idea/2021/02/creating-a-simple-jakarta-persistence-application/)
    its a good idea to connect this datasource to our persistence unit by right-clicking on the 'default' persistence unit
    in the Persistence tool window (View -> Tool Windows -> Persistence)
    and clicking Assign Data Sources… then selecting our myDB database from the drop-down menu.
    This step is required for the IntelliJ IDEA code completion

Useful commands
---------------

build via
  mvn clean install

  
deploy (from glassfish6)
  glassfish6/bin/asadmin deploy target/mydemofullweb-1.0-SNAPSHOT.war

list installed applications (from glassfish6)
  glassfish6/bin/asadmin list-components

server log is located at
  glassfish6/glassfish/domains/domain1/logs/server.log

redeploy
  glassfish6/bin/asadmin redeploy ../mydemofullweb/target/mydemofullweb-1.0-SNAPSHOT.war

undeploy
  glassfish6/bin/asadmin undeploy mydemofullweb-1.0-SNAPSHOT

restart server
  glassfish6/bin/asadmin stop-domain && glassfish6/bin/asadmin start-domain
