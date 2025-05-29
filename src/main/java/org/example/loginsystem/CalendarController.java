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
    private int currentUserID = 4; // TODO: Replace with real logged-in user ID

    /**
     * Initializes the calendar when the FXML is loaded.
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
     * Populates the calendar grid for the current month/year.
     */
    private void populateCalendar() {
        calendarGrid.getChildren().clear();

        // Update month/year label
        monthYearLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        // Add day headers (Monday - Sunday)
        for (int i = 0; i < 7; i++) {
            DayOfWeek dayOfWeek = DayOfWeek.of(((i + 1) % 7) + 1); // Start week on Monday
            Text header = new Text(dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()));
            GridPane.setRowIndex(header, 0);
            GridPane.setColumnIndex(header, i);
            calendarGrid.add(header, i, 0);
        }

        // First day of the month
        LocalDate firstDay = currentYearMonth.atDay(1);
        int firstDayCol = (firstDay.getDayOfWeek().getValue() % 7); // Adjust for Monday start
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Fill in the calendar grid
        for (int day = 1, row = 1, col = firstDayCol; day <= daysInMonth; day++, col++) {
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
     * Creates a cell (VBox) representing a single calendar day.
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
            dayBox.setStyle("-fx-background-color: yellow; -fx-alignment: center; -fx-padding: 5px;");
        } else {
            dayBox.getChildren().add(dayLabel);
        }

        // On click: open add/edit event window
        dayBox.setOnMouseClicked(e -> {
            if (event != null) {
                showAddEventWindow(date, currentUserID, event); // Edit event
            } else {
                showAddEventWindow(date, currentUserID, null); // Add new event
            }
        });

        return dayBox;
    }

    /**
     * Fetches a single event for a given date and user.
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
     * Changes the displayed month by a number of months.
     */
    private void changeMonth(int monthsToAdd) {
        currentYearMonth = currentYearMonth.plusMonths(monthsToAdd);
        populateCalendar();
    }

    /**
     * Resets the calendar to the current month.
     */
    private void showCurrentMonth() {
        currentYearMonth = YearMonth.now();
        populateCalendar();
    }

    /**
     * Opens the add-event dialog with today's date as default.
     */
    private void showAddEventDialog() {
        LocalDate selectedDate = LocalDate.now();
        showAddEventWindow(selectedDate, currentUserID, null);
    }

    /**
     * Shows the add/edit event dialog.
     */
    private void showAddEventWindow(LocalDate selectedDate, int currentUserID, CalendarEvent eventToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addEvent.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle((eventToEdit == null ? "Add" : "Edit") + " Event - " + selectedDate);
            stage.setScene(new Scene(root, 400, 320));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            AddEventController controller = loader.getController();
            controller.initData(selectedDate, currentUserID, stage, eventToEdit);

            populateCalendar(); // Refresh after save

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