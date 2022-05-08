package su.ioffe.crescente.data.exceptions;

public class UserNotFoundException extends Exception {

    public static final String PREFIX = "Cannot find UserAccount with name ";

    public UserNotFoundException() {
        super();
    }

    public UserNotFoundException(String name) {
        super(wrapMessage(name));
    }

    private static String wrapMessage(String msg) {
        return msg.startsWith(PREFIX) ? msg : (PREFIX + "'" + msg + "'");
    }


}