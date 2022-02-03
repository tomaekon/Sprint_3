import org.apache.commons.lang3.RandomStringUtils;

public class Courier {

    private String login;
    private String password;
    private String firstName;

    public String getLogin() {

        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {

        return password;
    }
    public void setPassword(String password) {

        this.password = password;
    }

    public String getFirstName() {

        return firstName;
    }
    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public Courier(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }
    public Courier(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Courier(){

    }

    public static Courier getRandom() {
        final String login = RandomStringUtils.randomAlphabetic(10);
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String firstName = RandomStringUtils.randomAlphabetic(10);
        return new Courier(login, password, firstName);
    }

}

