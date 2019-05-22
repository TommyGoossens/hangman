package hangmanshared.utility;

public class PasswordChecker {

    public PasswordChecker() {
    }

    public boolean passwordMeetsRequirements(String password, String password2) {
        return passwordMeetsLength(password) && doPasswordsMatch(password, password2) && !passWordIsEmpty(password);
    }

    private boolean passWordIsEmpty(String password) {
        return password.isEmpty();
    }

    private boolean doPasswordsMatch(String password, String password2) {
        return password.equals(password2);
    }

    private boolean passwordMeetsLength(String password) {
        return password.length() >= 6 && password.length() <= 24;
    }
}
