package support;

public class UsersRights {

    public static final int ADMIN = 0;
    public static final int SIMPLE_USER = 1;
    public static final int USER_WITH_PLAIN_PASSWORD = 2;
    public static final int LOCKED_USER = 3;
    public static final int LOCK_USERNAME = 4;
    public static final int EMPTY = 5;
    public static final int EMPTY_LOCK_USERNAME = 6;

    private static String[] items = {
            "Administrator",
            "Simple user with complex password",
            "Simple user with plain password",
            "Locked user",
            "Simple user with locked username",
            "Empty user",
            "Empty user with locked username",
        };

    public static String[] getItems() {
        return items;
    }

    public static String accountType(int rights) {
        return items[rights];
    }
}
