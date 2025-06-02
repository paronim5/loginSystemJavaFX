package org.example.loginsystem;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;

/**
 * user class represents user entity in code
 */
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserID")
    private int id;
    @Column (name = "Email")
    private String email;
    @Column (name = "Password")
    private String password;
    @Column(name = "Username")
    private String username;
    @Column(name = "Birthday")
    private Date birthday;
    @Column(name = "RoleID")
    private int roleID;

//    private Role role;

    @Column(name = "FirstName")
    private String firstName;
    @Column(name = "LastName")
    private String lastName;
    @Column(name = "Phone")
    private String phone; //TODO phone is not writing to the database table
    @Column(name = "SexID")
    private int sexId;
//    private Sex sex ;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", birthday=" + birthday +
//                ", role=" + role +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phone='" + phone + '\'' +
//                ", sex=" + sex +
                '}';
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getSexId() {
        return sexId;
    }

    public void setSexId(int sexId) {
        this.sexId = sexId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
//
//    public Sex getSex() {
//        return sex;
//    }
//
//    public void setSex(Sex sex) {
//        this.sex = sex;
//    }
//
//    public Role getRole() {
//        return role;
//    }
//
//    public void setRole(Role role) {
//        this.role = role;
//    }

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

    /**
     * method to hash password before sending it to the database
     * @param password password to encrypt
     * @return
     */
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

    /**
     * method to check if the hashed password in the database is equaled to password user has provided
     * @param password user password from login
     * @param hashedPassword hashed password from database
     * @return
     */
    public boolean checkPassword(String password, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hashedPassword);
    }
}
