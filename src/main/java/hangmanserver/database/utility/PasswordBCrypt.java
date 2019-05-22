package hangmanserver.database.utility;

import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.logging.Logger;

public class PasswordBCrypt {
    public PasswordBCrypt() {
    }

    public boolean verifyHash(String plainTextPW, String hashedPW){
        return BCrypt.checkpw(plainTextPW,hashedPW);
    }

    public String enCryptPassWord(String plainTextPW){
        return BCrypt.hashpw(plainTextPW,BCrypt.gensalt());
    }
}
