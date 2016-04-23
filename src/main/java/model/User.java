package model;

import java.io.Serializable;

public class User implements Serializable {

    private String firstName;
    private String lastName;
    private String login;
    private String password;
    private int rights;
    private String telephoneNum;
    private String mailAddress;
    private String alphabet = "";
    private Integer day = 1;
    private Integer month = 1;
    private Integer year = 1990;
    private int startUpCount;
    private String ubsSerial;

    public User(String firstName, String lastName, String login, char[] password,
                String telephoneNum, String mailAddress, int rights) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = String.valueOf(password);
        this.rights = rights;
        this.telephoneNum = telephoneNum;
        this.mailAddress = mailAddress;
    }

    public User(String firstName, String lastName, String login, String password, int rights) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.login = login;
        this.password = CustomMessageDigest.hashText(password);
        this.rights = rights;
    }

    public User(String firstName, String lastName, String login, char[] password, int rights) {
        this(firstName, lastName, login, String.valueOf(password), rights);
    }

    public User(String login, int rights) {
        this.login = login;
        this.rights = rights;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(char[] password) {
        this.password = CustomMessageDigest.hashText(password);
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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public int getStartUpCount() {
        return startUpCount;
    }

    public void setStartUpCount(int startUpCount) {
        this.startUpCount = startUpCount;
    }

    public String getUbsSerial() {
        return ubsSerial;
    }

    public void setUbsSerial(String ubsSerial) {
        this.ubsSerial = ubsSerial;
    }

    public boolean isMatches(String userName, char[] password) {
        return this.login.equals(userName) && this.password.equals(CustomMessageDigest.hashText(password));
    }

    public boolean isPasswordsMatches(char[] password) {
        return this.password.equals(CustomMessageDigest.hashText(password));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return login.toLowerCase().equals(user.login.toLowerCase());
    }

    @Override
    public int hashCode() {
        return login.hashCode();
    }
}
