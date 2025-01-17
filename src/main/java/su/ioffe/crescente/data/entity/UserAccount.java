package su.ioffe.crescente.data.entity;

import su.ioffe.crescente.data.info.UserAccountSummary;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.io.Serial;
import java.io.Serializable;

import static su.ioffe.crescente.data.entity.UserAccount.FIND_ALL;
import static su.ioffe.crescente.data.entity.UserAccount.FIND_BY_NAME;
import static su.ioffe.crescente.data.entity.UserAccount.PARAM_NAME;

/**
 * @author nikita.zinoviev@gmail.com
 */
@Entity
// todo why do we need this line?
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@NamedQueries({
        @NamedQuery(name = FIND_BY_NAME,
                query = "SELECT user FROM UserAccount user WHERE user.name = :" + PARAM_NAME
        ),
        @NamedQuery(name = FIND_ALL,
                query = "SELECT u from UserAccount u ORDER BY u.name"
        )
})
public class UserAccount implements Serializable, NamedEntity {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String QUERY_PREFIX = "admin.UserAccount.";
    public static final String PARAM_NAME = "name";
    public static final String FIND_BY_NAME = QUERY_PREFIX + "findByName";
    public static final String FIND_ALL = QUERY_PREFIX + "findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    /**
     * This is userName
     */
    @Column(nullable = false, unique = true)
    private String name;

    @Basic(optional = false)
    private String fullName;

    @Basic(optional = false)
    private String email;

    @Basic(optional = false)
    private String colour;

    @Basic(optional = false)
    private String passwordHash;

    @Basic(optional = false)
    @Lob
    private String pictureURL;

    public UserAccount() {
    }


    /**
     * @param name username, should be unique
     */
    public UserAccount(String name) {
        this.name = name;
    }

    public UserAccountSummary toUserAccountSummary() {
        return new UserAccountSummary(name, fullName, email, colour, pictureURL);
    }

    // todo get rid of "get", why override is commented out??
//    @Override
    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccount that = (UserAccount) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
