package io.checkme.surv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletContext;
import java.util.HashMap;
import java.util.Map;

public class CheckLDAP extends AbstractCheckme {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckLDAP.class);

    private static final String PSWD_KEY_CONFIG_KEY = "passwordKey";

    private static final String USER_KEY_CONFIG_KEY = "userKey";

    private static final String HOSTADRESS_KEY_CONFIG_KEY = "hostAdressKey";

    private static final String PORT_KEY_CONFIG_KEY = "portKey";

    private static final String SEARCHQUERYTEST_KEY_CONFIG_KEY = "searchQueryTest";

    private static final String PARTITION_KEY_CONFIG_KEY = "partitionKey";

    private static final String PROPERTIES_PATH_CONFIG_KEY = "propertiesPath";

    private HashMap<String, String> ldapEnvironment;

    private String partitionDn;

    private String searchQueryTest;

    public CheckLDAP(String path, ServletContext servletContext) {
        super(path, servletContext);
        LOGGER.debug("Building CheckLDAP with path:{}", path);
    }

    @Override
    public void refreshConfiguration() {
        loadProperties(getMandatoryConfiguration(PROPERTIES_PATH_CONFIG_KEY));
        parseLDAPProperties();
    }

    private void parseLDAPProperties() {
        ldapEnvironment = new HashMap<>();
        ldapEnvironment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnvironment.put(Context.PROVIDER_URL, getLdapUrl(getProperty(HOSTADRESS_KEY_CONFIG_KEY), getProperty(PORT_KEY_CONFIG_KEY)));
        ldapEnvironment.put(Context.SECURITY_PRINCIPAL, getProperty(USER_KEY_CONFIG_KEY));
        ldapEnvironment.put(Context.SECURITY_CREDENTIALS, getProperty(PSWD_KEY_CONFIG_KEY));

        partitionDn = getProperty(PARTITION_KEY_CONFIG_KEY);
        searchQueryTest = getMandatoryConfiguration(SEARCHQUERYTEST_KEY_CONFIG_KEY);
    }

    private String getLdapUrl(String hostAdress, String port) {
        return "ldap://" + hostAdress + ":" + port;
    }

    private NamingEnumeration<SearchResult> executeLDAPSearch() throws NamingException {
        DirContext ctx = new InitialDirContext();
        for (Map.Entry<String, String> entry : ldapEnvironment.entrySet()) {
            ctx.addToEnvironment(entry.getKey(), entry.getValue());
        }
        ctx.addToEnvironment(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        SearchControls searchControls = new SearchControls(SearchControls.SUBTREE_SCOPE, 1, 1000, null, false, true);
        return ctx.search(partitionDn, searchQueryTest, searchControls);
    }

    @Override
    public boolean isOk() {

        try {
            NamingEnumeration<SearchResult> answers = executeLDAPSearch();
            LOGGER.debug("LDAP search query test results : '{}'", answers.next());
            return true;

        } catch (NamingException e) {
            LOGGER.error("The request does not work", e);
        }
        return false;
    }

}
