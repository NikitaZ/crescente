package su.ioffe.crescente.data.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import su.ioffe.crescente.data.entity.SecurityGroupLink;
import su.ioffe.crescente.data.entity.UserAccount;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

@Singleton
@Startup
public class DatabaseSetupBean {
    // Used for direct database manipulations.
    @Resource(lookup="jdbc/crescente")
    private DataSource dataSource;

    /**
     * Persistence context.
     */
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    /**
     * Stores the password hash generator.
     */
    @Inject
    private Pbkdf2PasswordHash passwordHash;
    
    @PostConstruct
    public void init() {
        
//        Map<String, String> parameters= new HashMap<>();
//        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
//        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(Map.ofEntries(Map.entry("Pbkdf2PasswordHash.Iterations", "3072"),
                Map.entry("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512"),
                Map.entry("Pbkdf2PasswordHash.SaltSizeBytes", "64")));

//        Left as example, we do not need raw db manipulation so far.
//        executeUpdate(dataSource, "CREATE TABLE caller(name VARCHAR(64) PRIMARY KEY, password VARCHAR(255))");
//        executeUpdate(dataSource, "CREATE TABLE caller_groups(caller_name VARCHAR(64), group_name VARCHAR(64))");
//
//        executeUpdate(dataSource, "INSERT INTO caller VALUES('Joe', '" + passwordHash.generate("secret1".toCharArray()) + "')");
//
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Joe', 'foo')");
//        executeUpdate(dataSource, "INSERT INTO caller_groups VALUES('Joe', 'bar')");

        // If there are no users, create admin user.
        // I don't want to introduce dependency on UserController here,
        // although probably it doesn't matter.
        if (em.createNamedQuery(UserAccount.FIND_ALL, UserAccount.class).getResultList().isEmpty())
        {
            UserAccount adminUser = new UserAccount("admin");
            adminUser.setPasswordHash(passwordHash.generate("admin".toCharArray()));
            em.persist(adminUser);

            SecurityGroupLink group = new SecurityGroupLink();
            group.setGroupName("admin");
            group.setUserAccountName("admin");
            em.persist(group);
        }
    }

    // Left as an example.
    /**
     * Drops the tables before instance is removed by the container.
     */
    @PreDestroy
    public void destroy() {
//    	try {
//    		executeUpdate(dataSource, "DROP TABLE caller");
//    		executeUpdate(dataSource, "DROP TABLE caller_groups");
//    	} catch (Exception e) {
//    		// silently ignore, concerns in-memory database
//    	}
    }

    /*
    Executes the SQL statement in this PreparedStatement object against the database it is pointing to.
     */
    private void executeUpdate(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
           throw new IllegalStateException(e);
        }
    }
    
}
