package com.example.crescente.web;


//    Since the SecurityContext is not a named bean or implicit object,
//    we have to create a small helper bean in order to use this in EL.


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;

@Named
@ApplicationScoped
public class SecurityHelperBean {

//    Quote from JSF and Java EE 8 book:

//    Sometimes it’s necessary not only to render content on a view differently,
//    depending on what roles a caller is in, but also to take into account what other views
//    (web resources) a caller is allowed to access.
//    This comes into play, for instance, when rendering navigation menus
//    (omitting the entries for views a caller does not have access to), or rendering links or buttons that navigate
//    to views to which the caller does not have access in a special way (e.g., in red or with a lock icon next to it).

//    A traditional way to implement this is to test for the roles that the programmer knows give access to the given view.
//    While this may seem to work well, it’s often brittle in practice as it lets the code work under
//    the assumption of a specific role/view relationship without any strong guarantees that this relationship actually holds.
//    A more stable way to test whether a caller has access to a given view is quite
//    simply to test directly for exactly that; does the caller have access to this view (web resource).
//    The SecurityContext has a method that can be used for almost exactly
//    this: SecurityContext#hasAccessToWebResource().

//    There are two things to be aware of here.
//      First, the hasAccessToWebResource() method takes a web resource pattern,
//            which is the same pattern as used for the url-pattern in the web.xml fragment we looked at earlier.
//            This is close to, but not exactly the same as, the JSF view.
//            The JSF view is often specified in a mapping independent way
//            (e.g., /foo instead of /faces/foo or /foo.xhtml).
//            The web resource pattern, however, has to be the URL itself, with the mapping included.
//      Second, hasAccessToWebResource() requires us to specify the HTTP method for which we test the access.
//          This is required since in Java EE Security constraints actually apply per URL and per HTTP method.
//          For instance, a caller can have access to POST to /foo.xhtml but not to GET /foo.xhtml.
//          As we’re going to use our utility method for navigation tests, GET is typically the right HTTP method to use,
//          but we should be aware that sometimes we may need to test for another HTTP method.

//    Usage example:
//<h:link value="Go to Bar" outcome="/bar"
//    disabled="#{not security.hasAccessToWebResource('/faces/bar.xhtml')}" />
    @Inject
    private SecurityContext securityContext;

    public boolean hasAccessToWebResource(String resource) {
        return securityContext.hasAccessToWebResource(resource, "GET");
    }

//  Nikita: it maybe a good idea to introduce a similar method
//  which checks "/faces/"+view+".xhtml"
    public boolean hasAccessToView(String jsfView) {
        return hasAccessToWebResource("/faces/" + jsfView + ".xhtml");
    }


// Nikita: Given the above I am not sure if these are useful:

//    In the standard Java EE programmatic APIs there are no methods available to test whether the caller is in any of two or more roles,
//    or in all of two or more roles.
//    If this is required, utility methods such as shown in the following code can be used:
    public boolean isInAnyRole(String... roles) {
        for (String role : roles) {
            if (securityContext.isCallerInRole(role)) {
                return true;
            }
        }
        return false;
    }
    public  boolean isInAllRoles(String... roles) {
        for (String role : roles) {
            if (!securityContext.isCallerInRole(role)) {
                return false;
            }
        }
        return true;
    }
}
