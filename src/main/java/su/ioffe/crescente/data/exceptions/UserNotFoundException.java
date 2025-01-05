package su.ioffe.crescente.data.exceptions;

// todo check (git annotate) where (a??) static method comes from
public class UserNotFoundException extends Exception {

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String name) {
        // todo switch to Java 21-22?
        super(("Cannot find UserAccount with name '%s'".formatted(name)));
    }


}