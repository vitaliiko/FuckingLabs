package support;

import java.io.Serializable;
import java.util.Arrays;

public class User implements Serializable {

    private String name;
    private String surname;
    private String userName;
    private char[] password;
    private int rights;
    private String telephoneNum;
    private String mailAddress;
    private String alphabet = "";

    public User(String name, String surname, String userName, String password, int rights) {
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.password = password.toCharArray();
        this.rights = rights;
    }

    public User(String name, String surname, String userName, char[] password, int rights) {
        this.name = name;
        this.surname = surname;
        this.userName = userName;
        this.password = password;
        this.rights = rights;
    }

    public User(String userName, int rights) {
        this.userName = userName;
        this.rights = rights;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public int getRights() {
        return rights;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public boolean isMatches(String userName, char[] password) {
        return this.userName.equals(userName) && Arrays.equals(this.password, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return userName.toLowerCase().equals(user.userName.toLowerCase());
    }

    @Override
    public int hashCode() {
        int result = userName.hashCode();
        result = 31 * result + Arrays.hashCode(password);
        return result;
    }
}
