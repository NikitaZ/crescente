To clone this project use
git clone https://github.com/NikitaZ/crescente.git

To update it use (once)
git pull https://github.com/NikitaZ/crescente.git
Then just
git pull

Use your GitHub user (not e-mail) as the user and an access token (settings->developer settings) as a password.
see
https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token

# Install latest Glassfish application server

Here we describe current stable version Glassfish 7 (GF7), but GF8 is feature complete already, better install it
(https://github.com/eclipse-ee4j/glassfish/releases/tag/8.0.0-M9)
Below options which we do not use refer to GF7 or even GF6.
    See
      https://github.com/eclipse-ee4j/glassfish/releases/tag/7.0.21
    download glassfish zip from glassfish.org
      https://www.eclipse.org/downloads/download.php?file=/ee4j/glassfish/glassfish-7.0.21.zip&mirror_id=1287
    unzip it at (some location) root of project, in will unpack to glassfish7 directory
      unzip ./glassfish-7.0.21.zip

While not required, it is a good idea to unzip it into our project, 'glassfish6, glassfish7, glassfish8' is added to .gitignore
(or symlink it, i.e.  ln -s ../glassfish6 ). Below we assume this a little.

# Install H2 database

Download h2 as a platform independent zip from https://www.h2database.com/html/download.html
(say, https://github.com/h2database/h2database/releases/download/version-2.1.212/h2-2022-04-09.zip
better https://github.com/h2database/h2database/releases/download/version-2.3.232/h2-2024-08-11.zip
)
unzip it to glassfish8 so that it would have h2 directory next to javadb and imq
  cd glassfish6
  unzip ~/Downloads/h2-2024-08-11.zip
Now install h2.jar into glassfish via
  cp h2/bin/h2-2.1.212.jar ./glassfish/modules/h2.jar
(we change the name to h2.jar so that we always overwrite the same file, probably this is good as it prevents us
from installing two h2 versions into same application server (GF) )

Restart glassfish.

[Example -- naive GF/H2 upgrade: (filenames as when upgrade happened, do not modify, see corresponding commit)]
<note>
    # install new db
    cp h2/bin/h2-2.3.232.jar glassfish7/glassfish/modules/h2.jar
    # copy GF configuration
    cp old_glassfish7_0_15/glassfish/domains/domain1/config/domain.xml glassfish7/glassfish/domains/domain1/config
    # copy DB files:
    cp -r old_glassfish7_0_15/glassfish/domains/domain1/config/deploy glassfish7/glassfish/domains/domain1/config
    # redeploy app
    # failed (hence need to reimport db):
    # Caused by: org.h2.mvstore.MVStoreException: The write format 2 is smaller than the supported format 3 [2.3.232/5]
    <strange> <!--todo why console backup fails??-->
        java -cp ./old_glassfish7_0_15//glassfish/modules/h2.jar org.h2.tools.Shell -url "jdbc:h2:./old_glassfish7_0_15/glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente

        Welcome to H2 Shell 2.1.212 (2022-04-09)
        sql> BACKUP TO crescente_2_1_212;
        Error: org.h2.jdbc.JdbcSQLNonTransientException: General error: "java.lang.NullPointerException: Cannot invoke ""org.h2.table.ColumnResolver.getSelect()"" because ""this.columnResolver"" is null"; SQL statement:
        BACKUP TO crescente_2_1_212 [50000-212]
    </strange>
    # below worked and produced crescente.sql dump in the current directory
    # a. export db
    java -cp ./old_glassfish7_0_15/glassfish/modules/h2.jar org.h2.tools.Script -url "jdbc:h2:./old_glassfish7_0_15/glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente -script crescente.sql
    # b. old DB doesn't work, get rid of it:
    rm glassfish7/glassfish/domains/domain1/config/deploy/crescente.*
    # c. import back
    java -cp ./glassfish7//glassfish/modules/h2.jar org.h2.tools.RunScript -url "jdbc:h2:./glassfish7/glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente -script ../crescente/src/main/sql/crescente.sql
    # d. check:
    ls -l glassfish7/glassfish/domains/domain1/config/deploy
      20480 Jan  6 15:16 crescente.mv.db
    # e. needed to start GF, run (and then restart GF)
    ./glassfish7/bin/asadmin set server-config.http-service.virtual-server.server.default-web-module=crescente-1.0-SNAPSHOT
    # f. update jdbc driver in IDEA, I just used h2...jar from H2 directory
</note>

Run
  glassfish8/bin/asadmin add-resources ./src/main/resources/app-server-resources.xml
this will add connection pool and datasource.

Ping the datasource (via Ping button at 'Edit JDBC Connection Pool' for crescente-h2-pool at admin console)
If it succeeds the db file will be created at
glassfish8/glassfish/domains/domain1/config/deploy/crescente.mv.db


stop server, edit glassfish8/glassfish/domains/domain1/domain.xml
at 
  <configs>
    <config name="server-config">
      <system-property name="JMS_PROVIDER_PORT" description="Port Number that JMS Service will listen for remote clients connection." value="7676"></system-property>
      <http-service>
        <access-log></access-log>
        <virtual-server network-listeners="http-listener-1,http-listener-2" id="server"></virtual-server>
add 
   default-web-module="crescente-1.0-SNAPSHOT"
so it becomes   
     <virtual-server default-web-module="crescente-1.0-SNAPSHOT" network-listeners="http-listener-1,http-listener-2" id="server"></virtual-server>
# above is an example of editing domain.xml settings file manually. It is actually not needed, see the 'set' command below.
# (BUT for it to work (take place) one needs to restart the server, if domain.xml has been edited, server needs to be restarted)


In order to connect IDEA to DB: 
   on the 'Database' tab of Idea add new data source:
      choose H2, "embedded" mode (by clicking on 'remote' in blue) and enter PATH:
   the path to the .mv.db file above, i.e. 
      /Users/<name>/.../glassfish8/glassfish/domains/domain1/config/deploy/crescente.mv.db
   So that URL becomes
     jdbc:h2:/Users/.../glassfish8/glassfish/domains/domain1/config/deploy/crescente

   // Do not click 'download missing driver files', use 'h2/bin/h2-2.1.212.jar' instead.
   // (Idea now downloads 2.1.210 which doesn't seem to be fully compatible)
   Update: Now idea downloads 2.2.220 which is ok.
   TODO: upgrade to a never H2 (current is aug 2 2024)

   Use username and password from crescente/src/main/resources/app-server-resources.xml
   (both crescente for now)

   Click 'Test Connection' at the bottom, it should succeed and say:
        DBMS: H2 (ver. 2.1.212 (2022-04-09))
        Case sensitivity: plain=upper, delimited=exact
        Driver: H2 JDBC Driver (ver. 2.1.212 (2022-04-09), JDBC4.2)
        Ping: 14 ms

        DBMS: H2 (ver. 2.2.220 (2023-07-04))
        Case sensitivity: plain=upper, delimited=exact
        Driver: H2 JDBC Driver (ver. 2.2.220 (2023-07-04), JDBC4.2)

        Ping: 10 ms
    (ping was 16 -> 11 -> 10 -> 10.. (above is warmed up))


   //IMPORTANT: on 'advanced' tab at 'Expert options' check 'Introspect using JDBC metadata'
   //- H2 integration with IDEA seems to be a bit broken, it uses TABLE_CATALOG instead of INFORMATION_SCHEMA_CATALOG_NAME
   The above seems to be not needed anymore

   now under PUBLIC you should see tables USERACCOUNT and SECURITYGROUPLINK provided that the app has been deployed

[IMPORTANT:]
    As said in (https://blog.jetbrains.com/idea/2021/02/creating-a-simple-jakarta-persistence-application/)
    its a good idea to connect this datasource to our persistence unit by right-clicking on the 'default' persistence unit
    in the 'Persistence tool' window of Idea (View -> Tool Windows -> Persistence)
    and clicking Assign Data Sources… then selecting our myDB database from the drop-down menu.
    This step is required for the IntelliJ IDEA code completion

    Exporting database:
     // uncompressed sql file
     java -cp ./glassfish/modules/h2.jar org.h2.tools.Script -url "jdbc:h2:./glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente -script crescente.sql
     // compressed sql file
     java -cp ./glassfish/modules/h2.jar org.h2.tools.Script -url "jdbc:h2:./glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente -script crescente.zip -options compression zip

     Quote: The SCRIPT command locks objects while it is running. Admin rights are required to execute this command.

    Importing database:
        java -cp ./glassfish/modules/h2.jar org.h2.tools.RunScript -url "jdbc:h2:./glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente -script ../crescente/src/main/sql/crescente.sql

    // For compressed one use
        java -cp ./glassfish/modules/h2.jar org.h2.tools.RunScript -url "jdbc:h2:./glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente -script test.zip -options compression zip

     or via h2 Shell, etc
        RUNSCRIPT FROM 'backup.sql'
     wow! even this is possible:
        RUNSCRIPT FROM 'classpath:/com/acme/test.sql'


[VERY IMPORTANT FOR PRODUCTION]
    There is also BACKUP command and RESTORE command as well as Backup/Restore tools, tools can work only offline, but
    BACKUP maybe performed online.
            BACKUP TO 'backup.zip'
    produces a zip with crescente.mv.db inside.
    Quote: "The resulting backup is transactionally consistent, meaning the consistency and atomicity rules apply".
    See
        https://www.h2database.com/html/tutorial.html#upgrade_backup_restore
    and
        https://www.h2database.com/html/commands.html#backup
    Quote:
        BACKUP TO fileNameString
        Backs up the database files to a .zip file. Objects are not locked, but the backup is transactionally consistent
        because the transaction log is also copied. Admin rights are required to execute this command.


[SSH Tunnels:]
ssh -L 9999:localhost:4848 <user>@<prod_host> # access production admin via localhost:9999
ssh -R 8888:localhost:8080 <user>@<prod_host> # publish local GF as prodhost:8888 - say for a demo
the last needs /etc/ssh/sshd_config: GateWayForwarding = yes

GF related system setup:
sudo sysctl -w net.ipv4.ip_unprivileged_port_start=443

----

GF:
(the config is in glassfish8/glassfish/domains/domain1/config/domain.xml)
# increase default value (8192) to support long redirects (on long URLs)
./bin/asadmin set server-config.network-config.protocols.protocol.http-listener-1.http.header-buffer-length-bytes=16384
./bin/asadmin set server-config.network-config.protocols.protocol.http-listener-2.http.header-buffer-length-bytes=16384

[TODO do not work now!]
./bin/asadmin set server-config.network-config.protocols.protocol.ws-http-listener.http.compression=on
./bin/asadmin set server-config.network-config.protocols.protocol.ws-http-listener.http.compressable-mime-type=text/html,text/xml,text/plain,text/css,text/javascript,application/json
[

nikitazinoviev@Nikitas-MacBook-Pro glassfish8 % ./bin/asadmin set server-config.network-config.protocols.protocol.ws-http-listener.http.compression=on
remote failure: No configuration found for server-config.network-config.protocols.protocol.ws-http-listener.http
Command set failed.
nikitazinoviev@Nikitas-MacBook-Pro glassfish8 %
./bin/asadmin set server-config.network-config.protocols.protocol.ws-http-listener.http.compressable-mime-type=text/html,text/xml,text/plain,text/css,text/javascript,application/json
remote failure: No configuration found for server-config.network-config.protocols.protocol.ws-http-listener.http
]

# default web module
./bin/asadmin set server-config.http-service.virtual-server.server.default-web-module=crescente-1.0-SNAPSHOT
# IMPORTANT!
# in case of a war in ear use above: crescente-ear-1.0-SNAPSHOT#crescente-1.0-SNAPSHOT.war

IMPT! disabled http2 for https
./bin/asadmin set configs.config.server-config.network-config.protocols.protocol.http-listener-2.http.http2-enabled=false
see
https://github.com/payara/Payara/issues/2625
and
https://github.com/eclipse-ee4j/grizzly/issues/2111 which seems still open!
[TODO - doublecheck, especially for GFv8, in progress, I didn't apply above to GF8]


Useful commands
---------------

build via
  mvn clean install

  
deploy
  glassfish8/bin/asadmin deploy target/crescente-1.0-SNAPSHOT.war

list installed applications
  glassfish8/bin/asadmin list-components

server log is located at
  glassfish8/glassfish/domains/domain1/logs/server.log

redeploy
  glassfish8/bin/asadmin redeploy --name crescente-1.0-SNAPSHOT target/crescente-1.0-SNAPSHOT.war
  or just use
  glassfish8/bin/asadmin deploy --force=true target/crescente-1.0-SNAPSHOT.war
  both seem to do the same as
  glassfish8/bin/asadmin undeploy crescente-1.0-SNAPSHOT && glassfish6/bin/asadmin deploy target/crescente-1.0-SNAPSHOT.war


undeploy
  glassfish8/bin/asadmin undeploy crescente-1.0-SNAPSHOT

restart server
  glassfish8/bin/asadmin stop-domain && glassfish8/bin/asadmin start-domain

easiest way to rebuild and deploy [development, is default] and [production]
  mvn clean install && ./glassfish8/bin/asadmin deploy --force=true target/crescente-1.0-SNAPSHOT.war

  mvn clean install -Pproduction && ./glassfish8/bin/asadmin deploy --force=true target/crescente-1.0-SNAPSHOT.war


If there are no users in the database, DatabaseSetup singleton EJB creates a user 'admin' with 'admin' password and administration rights,
use it to create actual admin users, test them and then delete the 'admin' user. This is partly because it is hard to
calculate password hash via the same algorithm outside the server, although of course, we need only to calculate it once.
one password hash for 'admin' is
PBKDF2WithHmacSHA512:3072:CCDMAnF2/zhBrkR+8KvRv56AP+ZmDCmXIUVGlP0mQyZjwy9lqIGZXkwq7dzCazchX9iuOIHdGfoxMkpraDKnKg==:+uXwQ4/zqSbs/QYJheYoTMfV68qCiKL2wlRKUZiieaU=

How to connect to DB from the command line:
  java -cp ./glassfish/modules/h2.jar org.h2.tools.Shell -url "jdbc:h2:./glassfish/domains/domain1/config/deploy/crescente;AUTO_SERVER=TRUE" -user crescente -password crescente
adding
  -sql "alter table USERACCOUNT add PASSWORDHASH CHARACTER VARYING(1000000000);"
executes SQL and exits.

  (These should never be needed normally:)
To update a user password to 'admin' run
  update USERACCOUNT set PASSWORDHASH='PBKDF2WithHmacSHA512:3072:CCDMAnF2/zhBrkR+8KvRv56AP+ZmDCmXIUVGlP0mQyZjwy9lqIGZXkwq7dzCazchX9iuOIHdGfoxMkpraDKnKg==:+uXwQ4/zqSbs/QYJheYoTMfV68qCiKL2wlRKUZiieaU=' where NAME='some user'
To give admin rights to 'username' run
  INSERT INTO PUBLIC.SECURITYGROUPLINK (ID, GROUPNAME, USERACCOUNTNAME) VALUES (2, 'admin', 'username');


We cannot connect IDEA to production DB as it is run in embedded mode. But may be this is a good thing that we cannot.
[IMPORTNANT]
Also h2/bin/h2.sh starts admin console to which we can connect with a browser to it (at 8182?).

TODO:
Q: should we use 'cargo' maven plugin to deploy application via maven? see tutorial examples (topmost pom file)


<note>
Upgrade from GF 7.0.21 to GF 8
    unzip ~/Downloads/glassfish-8.0.0-M9.zip
    cp -r glassfish7/h2 glassfish8
    cp glassfish7/glassfish/modules/h2.jar glassfish8/glassfish/modules
    cp -r glassfish7/glassfish/domains/domain1/config/deploy glassfish8/glassfish/domains/domain1/config/
    <apply GF settings via asadmin as described above>
</note>


# Debugging in IDEA
While it should be possible right-away if a Glassfish Debug configuration will be used,
I couldn't find it and used just the normal 'remote debug configuration', it works perfectly fine, maybe even better,
but I think one needs to add to Glassfish JVM options in config/domain.xml the options which IDEA suggests, say, under Xmx
        <jvm-options>-Xmx512m</jvm-options>
add
        <jvm-options>-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005</jvm-options>

  Books:
========
- this one has been republished recently with Jakarta EE 10 in mind!
Some comments refer to this book as "JSF and Java EE8" book.

The Definitive Guide to Jakarta Faces in Jakarta EE 10: Building Java-Based Enterprise Web Applications
cover
Author(s): Bauke Scholtz, Arjan Tijms

Next one looks promising, haven't looked at it yet.
- Definitive Guide to Security in Jakarta EE - Securing Java-based Enterprise Applications with Jakarta Security, Authorization, Authentication and More
cover
Author(s): Arjan Tijms, Teo Bais, Werner Keil
Publisher: Springer, Year: 2022

I also love "Mastering EJB 4th edition" book -- it is still great!