package org.example.loginsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.time.LocalDate;

public class AddEventController {

    @FXML private TextField eventTitleField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker eventDatePicker;
    @FXML private CheckBox doneCheckBox;

    private CalendarEvent eventToEdit;
    private LocalDate selectedDate;
    private int userID =4;
    private Stage stage;

    // Called by CalendarController to pass data
    public void initData(LocalDate date, int userID, Stage stage, CalendarEvent event) {
        this.selectedDate = date;
        this.userID = 4;
        this.stage = stage;
        this.eventToEdit = event;


initialize();
    }

    @FXML
    public void initialize() {
        // Set default date
        eventDatePicker.setValue(selectedDate);

        // Load event data after FXML is fully loaded
        if (eventToEdit != null) {
            eventTitleField.setText(eventToEdit.getName());
            descriptionArea.setText(eventToEdit.getDescription());
            eventDatePicker.setValue(eventToEdit.getEventDate());
            doneCheckBox.setSelected(eventToEdit.isDone());
        } else {
            eventTitleField.clear();
            descriptionArea.clear();
            doneCheckBox.setSelected(false);
        }
    }

    @FXML
    private void saveEvent(ActionEvent actionEvent) {
        String name = eventTitleField.getText().trim();
        String description = descriptionArea.getText();
        LocalDate eventDate = eventDatePicker.getValue();
        boolean done = doneCheckBox.isSelected();

        if (name.isEmpty() || eventDate == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validation Error");
            alert.setHeaderText("Missing required fields.");
            alert.setContentText("Please enter at least a title and date.");
            alert.showAndWait();
            return;
        }

        try (Session session = HibernateUtl.openSession()) {
            session.beginTransaction();

            if (eventToEdit == null) {
                // ADD NEW EVENT
                CalendarEvent newEvent = new CalendarEvent();
                newEvent.setName(name);
                newEvent.setDescription(description);
                newEvent.setEventDate(eventDate);
                newEvent.setUserID(userID); // Use passed-in user ID
                newEvent.setDone(done);
                session.save(newEvent);
            } else {
                // UPDATE EXISTING EVENT
                eventToEdit.setName(name);
                eventToEdit.setDescription(description);
                eventToEdit.setEventDate(eventDate);
                eventToEdit.setDone(done);
                session.update(eventToEdit);
            }

            session.getTransaction().commit();

            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error");
            alert.setHeaderText("Failed to save event.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void closeWindow() {
        if (stage == null) {
            stage = (Stage) eventTitleField.getScene().getWindow();
        }
        stage.close();
    }
}