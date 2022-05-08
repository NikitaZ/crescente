package su.ioffe.crescente.web;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;
import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@ApplicationScoped
@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/faces/Login.xhtml",
                errorPage = ""  // no need for error page as errors are displayed on login page
        )
)
@DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/crescente",
        callerQuery = "select PASSWORDHASH from USERACCOUNT where NAME = ?",
        groupsQuery = "select GROUPNAME from SECURITYGROUPLINK where USERACCOUNTNAME = ?",
        hashAlgorithm = Pbkdf2PasswordHash.class,
        hashAlgorithmParameters = {
            "Pbkdf2PasswordHash.Iterations=3072",
            "Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512",
            "Pbkdf2PasswordHash.SaltSizeBytes=64"
        }
//        ,        priority = 100
)
public class SecurityConfigurationBean {
}

// Here go several screens of my comments which may be useful.

/* We may use expressions for properties, see tutorial/examples

// Database Definition for built-in DatabaseIdentityStore
@DatabaseIdentityStoreDefinition(
    callerQuery = "#{'select password from caller where name = ?'}",
    groupsQuery = "select group_name from caller_groups where caller_name = ?",
    hashAlgorithm = Pbkdf2PasswordHash.class,
    priorityExpression = "#{100}",
    hashAlgorithmParameters = {
        "Pbkdf2PasswordHash.Iterations=3072",
        "${applicationConfig.dyna}"
    }
)

@BasicAuthenticationMechanismDefinition(
        realmName = "file"
)

@ApplicationScoped
@Named
public class ApplicationConfig {

     public String[] getDyna() {
         return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
     }

}
*/

/*
spec:
        https://javaee.github.io/security-spec/spec/jsr375-spec.pdf
        only it clearly describes that one can use @DatabaseIdentityStoreDefinition ONLY to provide groups
        and that multiple identity stores are scanned for groups

misc:
        https://blog.payara.fish/securing-a-rest-service
        https://www.baeldung.com/java-ee-oauth2-implementation?utm_content=99762918&utm_medium=social&utm_source=twitter&hss_channel=tw-2599580401

        books:

        see "Chapter 13: Security" of
        "The Definitive Guide to JSF in Java EE 8: Building Web Applications with JavaServer Faces"
        by Bauke Scholtz Arjan Tijms, 2018
        what is interesting, it describes @EmbeddedIdentityStoreDefinition which may be useful for local development

        samples:
        https://github.com/javaee-samples/vendoree-samples/payara

        note:
        one can also use jdbc-realm
        https://medium.com/@ismetserhat/jdbc-realm-for-user-authentication-on-payara-840b16be5133
        but that would mean we store hashes of password in the DB

        useful tests for JSF:
        #{request.isUserInRole('**')}
        A special role that we can test for is the “**” role which is an alternative for the
        #{not empty request.userPrincipal} check.

<h:link value="Go to Bar" outcome="/bar"         disabled="#{not securityHelperBean.hasAccessToWebResource('/bar.xhtml')}" />
*/

/*

    I have

    <welcome-file-list>
        <welcome-file>faces/Index.xhtml</welcome-file>
    </welcome-file-list>


    security constraint. But as the 'welcome' page becomes secured as well it seems to enter endless security check loop.
It is stuck to AuthenticationStatus.SEND_CONTINUE status. Is this the expected behavior?

I had to overcome this by unsecuring the landing page of redirectURI by
    <security-constraint>
    <web-resource-collection>
        <web-resource-name>First Page</web-resource-name>
        <url-pattern>/faces/Index.xhtml</url-pattern>
    </web-resource-collection>
    </security-constraint>
The welcome page had to have a "please, log in" link to a secured page in case user is not authenticated.
Later on, I overcome this with a JSF phase listener redirecting to a secured page if
securityContext.getCallerPrincipal() is null. I also added auxiliary action to load user data to a session bean if the user
just authenticated. I guess I could use a servlet for this as well making it the target of the redirectURI.

*/



/*
I've made roles work with
@DatabaseIdentityStoreDefinition(
dataSourceLookup = "jdbc/",
groupsQuery = "SELECT name FROM groups WHERE user_name = ?",
useFor = {IdentityStore.ValidationType.PROVIDE_GROUPS}
)
as it seems to be too cumbersome to set claims with Google Cloud.
There seems to be no UI at Google for it, one has to use google Firebase API and it seems we need to set "groups" claim
to a json array of strings with desired groups.
 */

/*
Hi, what is the reason of adding extra redirectURI.contains(request.getServletPath()) check?
it breaks existing applications, (I just debugged it),
if redirectURI is "https://localhost:8181/myapp/" and a welcome file is specified, i.e.

faces/About.xhtml

request.getServletPath() returns "/faces" and this check fails. In case of index.html it returns "/index.html".
Setting welcome-file to "/" makes authentication work again, but breaks user experience.

I don't think I want to set redirectURI to https://localhost:8181/myapp/faces/About.xhtml,
it breaks the idea of welcome-file, if it changes one also have to change configuration at openID provider (Google Cloud in my case).
Or we have to add a servlet for redirect URI and then forward to proper page which again reinvents welcome-file. Please, advice!
 */
