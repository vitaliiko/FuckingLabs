package support;

public class UsersRights {

    public static final int ADMIN = 0;
    public static final int SIMPLE_USER = 1;
    public static final int USER_WITH_PLAIN_PASSWORD = 2;
    public static final int LOCKED_USER = 3;
    public static final int LOCK_USERNAME = 4;
    public static final int WITHOUT_PASSWORD = 5;
    public static final int LOCK_USERNAME_WITHOUT_PASS = 6;

    public static String accountType(int rights) {
        switch (rights) {
            case 0: return "Administrator";
            case 1: return "Simple user with complex password";
            case 2: return "Simple user with plain password";
            case 4: return "Simple user with locked username";
            default: return null;
        }
    }
}
