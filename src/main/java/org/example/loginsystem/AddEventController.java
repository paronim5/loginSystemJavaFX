package org.example.loginsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;


import java.time.LocalDate;

public class AddEventController {
    @FXML private TextField eventTitleField;
    @FXML private TextArea descriptionArea;

    private LocalDate selectedDate;
    private int userID;
    private Stage stage;

    public void initData(LocalDate date, int userID, Stage stage) {
        this.selectedDate = date;
        this.userID = userID;
        this.stage = stage;
    }

    @FXML
    public void saveEvent() {
        String name = eventTitleField.getText();
        String desc = descriptionArea.getText();

        CalendarEvent event = new CalendarEvent(name, desc, userID, selectedDate, false);

        try (Session session = HibernateUtl.openSession()) {
            session.beginTransaction();
            session.save(event);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        closeWindow();
    }

    @FXML
    public void closeWindow() {
        stage.close();
    }
}