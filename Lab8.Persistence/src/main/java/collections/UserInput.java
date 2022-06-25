package collections;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserInput {
    
    private String login;
    private String password;

    private Scanner input;
    private boolean script;
    
    public UserInput(Scanner input, boolean silent) {
        this.input = input;
        this.script = silent;
    }

    public boolean loginInput() {
        while (true) {
            if (!script) 
                System.out.print("Enter login: ");
            login = input.nextLine();
            if (login.trim().length() == 0) {
                System.out.println("Invalid input. Login must not be empty!");
            } else if (login.trim().length() > 10) {
                System.out.println("Invalid input. Login must not more than 10 symbols!");
            } else {
                return true;
            }
        }
    }

    public boolean passwordInput() {
        while (true) {
            if (!script) 
                System.out.print("Enter password: ");
            password = input.nextLine();
            if (password.trim().length() > 20) {
                System.out.println("Invalid input. Password must not more than 20 symbols!");
            } else {
                return true;
            }
        }
    }

    public User resultElement(Integer id) {
        if (loginInput()
            && passwordInput()
                ) {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException ex) {
                System.out.println("Critical error! SHA-256 algotithm is not found!");
                System.exit(1);
            }
            byte[] encoded = Base64.getEncoder().encode(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
            String hash = new String(encoded);
            //System.out.println(hash);
            return User.of(login, hash);
        } else {
            return null;
        }
    }
}
