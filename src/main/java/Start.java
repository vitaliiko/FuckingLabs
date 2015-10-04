import GI.AuthenticationGI;

public class Start {

    public static void main(String[] args) {
        try {
            new AuthenticationGI();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        new WorkspaceGI(new User("Vova", "Ivanov", "vovan", "qwerty", UsersRights.ADMIN), new Controller());
    }
}
