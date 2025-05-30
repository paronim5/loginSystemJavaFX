package org.example.loginsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.hibernate.query.Query;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.io.IOException;

public class LoginController {
    Session session = HibernateUtl.openSession();
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;
    @FXML
    private Button signUpButton;
    @FXML
    private Button loginButton;

    @FXML
    protected void onSubmitButtonClick() {
//        System.out.println(login(session, email.getText(), password.getText()));
//        if (login(session, email.getText(), password.getText())) {
//            email.setText("");
//            password.setText("");
//        } else {
//            errorLabel.setText("User not found");
//        }
        try {
            int login = login(session, email.getText(), password.getText(), username.getText());
            if (login != -1) {
                email.setText("");
                password.setText("");

            }
            navigateToNextPage("calendar", loginButton, login);
        }catch(UserNotFoundException e) {
            errorLabel.setText(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private User findUserByEmail(Session session, String email) throws UserNotFoundException{

        User user = session.createQuery("from User where email = :email", User.class).setParameter("email", email).uniqueResult();

         if (user == null){
             throw new UserNotFoundException("User with email "+ email +" was not found in database.");
         }else {
             return user;
         }
    }

    private int login(Session session, String email, String password, String username ) throws UserNotFoundException{
       // Transaction tx = session.beginTransaction();
        User user = findUserByEmail(session, email);

        if (user.getEmail().equals(email) && user.checkPassword(password, user.getPassword()) && user.getUsername().equals(username)){
            return user.getId();
        }else {
            return -1;
        }
    }

    private void navigateToNextPage(String pageName, Button button) throws IOException {
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
            Scene scene = new Scene(root);
            stage.setScene(scene);
          //  stage.setTitle(pageName.replaceFirst("^[a-z]", c -> c.toString().toUpperCase()) + " Page");

            // Check if the pageName is "calendar" and call initializeCalendar
            if (pageName.equals("calendar")) {
                CalendarController controller = loader.getController();
                if (controller != null) {
                    controller.initializeCalendar();
                } else {
                    System.err.println("CalendarController is null.");
                }
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load " + pageName + " page.");
        }
    }

    private void navigateToNextPage(String pageName, Button button, int userId) throws IOException {
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
            Scene scene = new Scene(root);
            stage.setScene(scene);
            //  stage.setTitle(pageName.replaceFirst("^[a-z]", c -> c.toString().toUpperCase()) + " Page");

            // Check if the pageName is "calendar" and call initializeCalendar
            if (pageName.equals("calendar")) {
                CalendarController controller = loader.getController();
                if (controller != null) {
                    controller.setCurrentUserID(userId);
                    controller.initializeCalendar();

                } else {
                    System.err.println("CalendarController is null.");
                }
            }

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to load " + pageName + " page.");
        }
    }
    @FXML
    protected void onSignUpButtonClick() throws IOException {

        navigateToNextPage("signUp" , signUpButton);
    }

}