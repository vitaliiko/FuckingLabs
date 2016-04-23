package model;

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
            "Администратор",
            "Пользователь со сложным паролем",
            "Пользователь с простым паролем",
            "Заблокированный пользователь",
            "Пользователь с заблокированным логином",
            "Пустой пользователь",
            "Заблокированный логин и простой пароль",
            "Пустой пользователь с простым паролем"
        };

    public static String[] getItems() {
        return items;
    }

    public static String accountType(int rights) {
        return items[rights];
    }
}
