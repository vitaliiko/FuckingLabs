package support;

public class UsersRights {

    public static final int ADMIN = 0;
    public static final int SIMPLE_USER = 1;
    public static final int USER_WITH_SIMPLE_PASSWORD = 2;
    public static final int BLOCKED_USER = 3;
    public static final int LOCK_USERNAME = 4;
    public static final int EMPTY = 5;
    public static final int LOCK_USERNAME_WITH_SIMPLE_PASSWORD = 6;
    public static final int EMPTY_SIMPLE_PASSWORD = 7;

    private static String[] items = {
            "Administrator",
            "Simple user with complex password",
            "Simple user with simple password",
            "Blocked user",
            "Simple user with locked username",
            "Empty user",
            "Locked username, simple password",
            "Empty user with simple password"
        };

    public static String[] getItems() {
        return items;
    }

    public static String accountType(int rights) {
        return items[rights];
    }
}
