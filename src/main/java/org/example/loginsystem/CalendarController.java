package org.example.loginsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class CalendarController {

    @FXML private GridPane calendarGrid;
    @FXML private Label monthYearLabel;
    @FXML private MenuItem prevMonthMenuItem;
    @FXML private MenuItem todayMenuItem;
    @FXML private MenuItem nextMonthMenuItem;
    @FXML private MenuItem addEventMenuItem;

    private YearMonth currentYearMonth;
    private int currentUserID ; // TODO: Replace with real logged-in user ID

    /**
     * initializes the calendar when the FXML is loaded.
     */
    public void initializeCalendar() {
        currentYearMonth = YearMonth.now();
        populateCalendar();

        // Set up menu actions
        prevMonthMenuItem.setOnAction(e -> changeMonth(-1));
        todayMenuItem.setOnAction(e -> showCurrentMonth());
        nextMonthMenuItem.setOnAction(e -> changeMonth(1));
        addEventMenuItem.setOnAction(e -> showAddEventDialog());
    }

    /**
     * @param currentUserID
     * setter for currentUserId
     */
    public void setCurrentUserID(int currentUserID) {
        this.currentUserID = currentUserID;
    }

    /**
     * populates the calendar grid for the current month/year starts from monday.
     */
    private void populateCalendar() {
        //ai code
        calendarGrid.getChildren().clear();
        monthYearLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());
        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (int i = 0; i < 7; i++) {
            Label header = new Label(dayNames[i]);
            header.setStyle("-fx-font-weight: bold; -fx-text-fill: #00FFAA;");
            GridPane.setRowIndex(header, 0);
            GridPane.setColumnIndex(header, i);
            calendarGrid.add(header, i, 0);
        }
        LocalDate firstDay = currentYearMonth.atDay(1);
        int firstDayOfWeekValue = firstDay.getDayOfWeek().getValue(); // Monday=1 ... Sunday=7
        int firstDayColumn = (firstDayOfWeekValue + 6) % 7; // Map Monday to column 0

        int row = 1;
        int col = firstDayColumn;

        for (int day = 1; day <= currentYearMonth.lengthOfMonth(); day++, col++) {
            if (col > 6) {
                col = 0;
                row++;
            }

            LocalDate date = LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), day);
            CalendarEvent event = getEventForDate(date, currentUserID);

            VBox dayBox = createDayCell(date, event);
            calendarGrid.add(dayBox, col, row);
        }
    }
    /**
     * creates a cell to display a single calendar day.
     */
    private VBox createDayCell(LocalDate date, CalendarEvent event) {
        VBox dayBox = new VBox(5);
        dayBox.setStyle("-fx-alignment: center;");
        dayBox.setPrefSize(80, 60);
        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.setStyle("-fx-font-size: 14px;");
        if (event != null) {
            Label eventLabel = new Label("EVENT");
            eventLabel.setStyle("-fx-text-fill: black; -fx-font-size: 10px;");
            dayBox.getChildren().addAll(dayLabel, eventLabel);
            if (event.isDone()) {
                dayBox.setStyle(
                        "-fx-background-color: violet; -fx-alignment: center; -fx-padding: 5px;"
                );
            } else {
                dayBox.setStyle(
                        "-fx-background-color: yellow; -fx-alignment: center; -fx-padding: 5px;"
                );
            }
        } else {
            dayBox.getChildren().add(dayLabel);
        }

        dayBox.setOnMouseClicked(e -> {
            if (event != null) {
                showAddEventWindow(date, currentUserID, event); // show edit window
            } else {
                showAddEventWindow(date, currentUserID, null); // show add new event window
            }
        });
        return dayBox;
    }

    /**
     * fetch user event for a specific date(slow)
     */
    private CalendarEvent getEventForDate(LocalDate date, int userId) {
        try (Session session = HibernateUtl.openSession()) {
            Query<CalendarEvent> query = session.createQuery(
                    "FROM CalendarEvent WHERE eventDate = :date AND userID = :userId",
                    CalendarEvent.class
            );
            query.setParameter("date", date);
            query.setParameter("userId", userId);
            List<CalendarEvent> results = query.list();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * changes the displayed month
     */
    private void changeMonth(int monthsToAdd) {
        currentYearMonth = currentYearMonth.plusMonths(monthsToAdd);
        populateCalendar();
    }

    /**
     * resets the calendar to the current month.
     */
    private void showCurrentMonth() {
        currentYearMonth = YearMonth.now();
        populateCalendar();
    }

    /**
     * opens the add-event dialog with month date as default.
     */
    private void showAddEventDialog() {
        LocalDate selectedDate = LocalDate.now();
        showAddEventWindow(selectedDate, currentUserID, null);
    }

    /**
     * shows the add/edit event dialog.
     */
    private void showAddEventWindow(LocalDate selectedDate, int currentUserID, CalendarEvent eventToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addEvent.fxml"));
            Parent root = loader.load();

            String cssPath = getClass().getResource("addEvent" + ".css").toExternalForm();
            root.getStylesheets().add(cssPath);

            AddEventController controller = loader.getController();

            Stage stage = new Stage();
// this must be after stage is loaded otherwise data will load after you close window or will not load at all
            controller.initData(selectedDate, currentUserID, stage, eventToEdit);


            stage.setTitle((eventToEdit == null ? "Add" : "Edit") + " Event - " + selectedDate);
            stage.setScene(new Scene(root, 400, 320));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();



            populateCalendar(); // refresh after save

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not open add/edit event window.");
            alert.setContentText("Please try again later.");
            alert.showAndWait();
        }
    }
}