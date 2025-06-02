package org.example.loginsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.time.LocalDate;
import java.util.Optional;

public class AddEventController {

    @FXML private TextField eventTitleField;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker eventDatePicker;
    @FXML private CheckBox doneCheckBox;

    private CalendarEvent eventToEdit;
    private LocalDate selectedDate;
    private int userID;
    private Stage stage;

    /**
     *
     * @param date date of the clicked event
     * @param userID
     * @param stage
     * @param event what event we should display
     */
    public void initData(LocalDate date, int userID, Stage stage, CalendarEvent event) {
        this.selectedDate = date;
        this.userID =userID;
        this.stage = stage;
        this.eventToEdit = event;


initialize();
    }
/**
 * method called when Add/Edit/Delete window is appeared. Sets the valuable from method initData to the window if you would like to add event only date will be set
 */
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

    /**
     * method called when we press save button on Add/Edit/Delete window. displays alert if data is invalid or some data is missing. if we edit already existing event this method will call update method in database
     */
    @FXML
    private void saveEvent() {
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
                CalendarEvent newEvent = new CalendarEvent();
                newEvent.setName(name);
                newEvent.setDescription(description);
                newEvent.setEventDate(eventDate);
                newEvent.setUserID(userID);
                newEvent.setDone(done);

                session.save(newEvent);
            } else {
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

    /**
     * method to close the Add/Edit/Delete window
     */
    @FXML
    private void closeWindow() {
        if (stage == null) {
            stage = (Stage) eventTitleField.getScene().getWindow();
        }
        stage.close();
    }

    /**
     * method to delete event on button press. also showing an alert window to confirm delete
     */
    @FXML
    private void deleteEvent() {
        if (eventToEdit == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Event");
            alert.setHeaderText("Cannot delete event");
            alert.setContentText("There is no event to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Event");
        confirm.setHeaderText("Are you sure you want to delete this event?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Session session = HibernateUtl.openSession()) {
                session.beginTransaction();

                session.delete(eventToEdit);
                session.getTransaction().commit();

                closeWindow();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText("Failed to delete event.");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
}