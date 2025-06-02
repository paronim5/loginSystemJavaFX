package org.example.loginsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.query.Query;
import javafx.fxml.FXML;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * controller for signup page
 */
public class SignUpController {
    Session session = HibernateUtl.openSession();
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField passwordAgain;
    @FXML
    private TextField email;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField phoneNumber;
    @FXML
    private DatePicker birthdayDatePicker;
    @FXML
    private Button signUpButton;
    @FXML
    private Button backButton;

    private ToggleGroup sexToggleGroup;
    @FXML
    private RadioButton maleRadioButton;
    @FXML
    private RadioButton femaleRadioButton;
    @FXML
    private RadioButton otherRadioButton;
    @FXML
    private Label selectedSexLabel;
    @FXML
    private Label errorLabel;

    public Sex sex;

    /**
     * navigate to the next page (only login )
     * @param pageName
     * @param button
     * @throws IOException
     */

    private void navigateToNextPage( String pageName, Button button) throws IOException {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(pageName + ".fxml"));
            Parent root = loader.load();

            // Add CSS to the root node
            String cssPath = getClass().getResource(pageName + ".css").toExternalForm();
            root.getStylesheets().add(cssPath);

            // Get the current stage (window)
            Stage stage = (Stage) button.getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load " + pageName + " page.");
        }
    }

    /**
     * method to validate email
     * @param email user's email
     * @return return true if email is match this regex
     */
    private boolean checkEmail(String email) {
        String trimmedMail = email.trim();
        return trimmedMail.matches("(?:[a-z0-9!#$%&'*+\\=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+\\=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    }

    /**
     * method to validate password
     * @param password22
     * @return
     */
    private boolean checkPassword(String password22) {
        String password = password22.trim();
        if (password == null || password.isEmpty()) {
            errorLabel.setText("Password cannot be empty.");
            return false;
        }
        if (passwordAgain == null || passwordAgain.getText() == null || passwordAgain.getText().isEmpty()) {
            errorLabel.setText("Confirm password cannot be empty.");
            return false;
        }
         if(!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")) {
            errorLabel.setText("Create stronger password.");
            return false;
        }
         if(!password.equals(passwordAgain.getText())) {
            errorLabel.setText("Passwords do not match.");
            return false;
        }
        System.out.println("good"); // debug todo delete
            return true;
    }
    /**
     * method to check birthday (not null, not in the future, not more than 100 years )
     */
    private boolean checkBirthday(LocalDate birthday) {
        if (birthday == null) {
            errorLabel.setText("Birthday cannot be empty.");
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate hundredYearsAgo = today.minusYears(100);

        if (birthday.isAfter(today)) {
            errorLabel.setText("Birthday cannot be in the future.");
            return false;
        }

        if (birthday.isBefore(hundredYearsAgo)) {
            errorLabel.setText("Birthday cannot be more than 100 years ago.");
            return false;
        }
        System.out.println("good");
        return true;
    }

    /**
     * method to check first and last
     * @param firstname
     * @param lastname
     * @return
     */
    private boolean checkName(String firstname, String lastname) {
        firstname = firstname.trim();
        lastname = lastname.trim();
        if (firstname == null || firstname.isEmpty()) {
            errorLabel.setText("First name cannot be empty.");
            return false;
        }
        if (lastname == null || lastname.isEmpty()) {
            errorLabel.setText("Last name cannot be empty.");
            return false;
        }
        this.firstname.setText(firstLetterCapital(firstname));
        this.lastname.setText(firstLetterCapital(lastname));
        return true;
    }

    /**
     * method to check phone number
     * @param phoneNumber
     * @return
     */
    private boolean checkPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.trim();
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            errorLabel.setText("Phone number cannot be empty.");
            return false;
        }
        if(!phoneNumber.matches("^\\+\\d{1,3}\\d{1,14}$")){
            errorLabel.setText("Phone number is incorrect.");
            return false;
        }
        System.out.println("good");
        return true;
    }

    /**
     * method to check username
     * @param username
     * @return
     */
    private boolean checkUsername(String username) {
        if (username == null || username.isEmpty()) {
            errorLabel.setText("Username cannot be empty.");
            return false;
        }
        if (!userByUsername(session, username)) {
            errorLabel.setText("Username with the specified username already exists.");
            return false;
        }
        System.out.println("good");
        return true;
    }

    /**
     * method to check if the gender is not null
     * @param gender
     * @return
     */
    private boolean checkGender(String gender) {
        if (selectedSexLabel.toString().contains("None")) {
            errorLabel.setText("Gender cannot be empty.");
            return false;
        }
    System.out.println("good");
        return true;
}
    //region util methods
    private String firstLetterCapital(String name) {
        String firstLetter = name.substring(0, 1).toUpperCase();
        return firstLetter + name.substring(1);
    }
    private boolean userByUsername(Session session, String username) { //if we have user with the same username in database we can't add another one so we return false

        User user = session.createQuery("from User where username = :username", User.class).setParameter("username", username).uniqueResult();

        if (user == null){
            return true;
        }else {
            return false;
        }
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()); // ai code to convert data
    }

    /**
     * initialize method called as soon as page is loaded
     */
    @FXML
    public void initialize() { // ai code
        sexToggleGroup = new ToggleGroup();
        maleRadioButton.setToggleGroup(sexToggleGroup);
        femaleRadioButton.setToggleGroup(sexToggleGroup);
        otherRadioButton.setToggleGroup(sexToggleGroup);

        otherRadioButton.setSelected(true);
        sexToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == maleRadioButton) {
                selectedSexLabel.setText("Selected Sex: Male");
                sex = Sex.MALE;
            } else if (newValue == femaleRadioButton) {
                selectedSexLabel.setText("Selected Sex: Female");
                sex = Sex.FEMALE;
            } else if (newValue == otherRadioButton) {
                selectedSexLabel.setText("Selected Sex: Other");
                sex = Sex.OTHER;
            } else {
                selectedSexLabel.setText("Selected Sex: None");
            }
        });
    }
    //endregion

    /**
     * method to validate data from user using methods abovementioned
     * @return
     */
    private boolean validateData() {
        if (checkEmail(email.getText()) &&
                checkPassword(password.getText()) &&
                checkBirthday(birthdayDatePicker.getValue()) &&
                checkName(firstname.getText(), lastname.getText()) &&
                checkPhoneNumber(phoneNumber.getText()) &&
                checkUsername(username.getText()) &&
                checkGender(selectedSexLabel.getText())) {
            return true;
        }
return false;
    }

    /**
     * method to add user into the database if data is valid
     * @param session hibernate session
     * @return
     */
    private boolean addNewUserToDB(Session session) {
        if (validateData()) {
            Transaction transaction = null;
            try {
                transaction = session.beginTransaction();
                User user = new User();
                user.setUsername(username.getText());
                user.setPassword(password.getText());
                user.setFirstName(firstname.getText());
                user.setLastName(lastname.getText());
                user.setBirthday(convertToDate(birthdayDatePicker.getValue()));
                user.setEmail(email.getText());
                user.setRoleID(1);
                user.setSexId(1); //TODO fix this line just for test set this to male for good

                session.persist(user);
                transaction.commit();
                System.out.printf("New user created: %s\n", username.getText());
                return true;
            } catch (HibernateException e) {
                if (transaction != null) transaction.rollback();
                e.printStackTrace();
                errorLabel.setText("Failed to create user.");
                return false;
            } finally {
                session.close();
            }
        }
        return false;
    }

    /**
     * method to navigate back to the login page
     * @throws IOException
     */
    @FXML
    protected void onBackButtonClick() throws IOException {
        navigateToNextPage("login" , backButton);
    }
    /**
     * method to sign up
     */
    @FXML
    protected void onSignUpButtonClick() {
        boolean valid = addNewUserToDB(session);
        if (valid) {
            clearFields();
            errorLabel.setText("You can login now.");
        }
       // navigateToNextPage("login" , signUpButton);
    }

    /**
     * method to clear fields if user successfully signed up
     */
    private void clearFields() {
        email.setText("");
        password.setText("");
        firstname.setText("");
        lastname.setText("");
        birthdayDatePicker.setValue(null);
        maleRadioButton.setSelected(false);
        femaleRadioButton.setSelected(false);
        otherRadioButton.setSelected(true);
        phoneNumber.setText("");
        username.setText("");
        password.setText("");

    }
}
