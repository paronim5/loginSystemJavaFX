package org.example.loginsystem;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column (name = "name")
    private String email;
    @Column (name = "password")
    private String password;

    public User() {
    }

    public User( String email, String password) {
        this.email = email;
        this.password = hashPassword(password);
    }
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(email, user.email) && Objects.equals(password, user.password);
    }



    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    private String hashPassword(String password) {
//region hash password
//        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
//        keyGenerator.init(256);
//
//        SecretKey secretKey = keyGenerator.generateKey();
//
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
//
//        byte[] bytesEncypted = cipher.doFinal(password.getBytes());
//
//        String encryptedTextUsingBase64 = Base64.getEncoder().encodeToString(bytesEncypted);
//
//        System.out.println(encryptedTextUsingBase64);
//
//        cipher.init(Cipher.DECRYPT_MODE, secretKey);
//
//        byte[] bytesDecypted = cipher.doFinal(Base64.getDecoder().decode(encryptedTextUsingBase64));
//
//        String plainText2 = new String(bytesDecypted);
//
//        System.out.println(plainText2);
//
//        //now lets use hash
//
//        MessageDigest digest = MessageDigest.getInstance("SHA-256");
//        byte[] hashOfPasswordByte = digest.digest(password.getBytes());
//
 //endregion
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //System.out.println(encoder.encode(password)); //for database
        return encoder.encode(password);
    }
    public boolean checkPassword(String password, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hashedPassword);
    }
}
