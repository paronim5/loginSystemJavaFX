package org.example.loginsystem;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.YearMonth;

public class CalendarController {
    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthYearLabel;

    @FXML
    private MenuItem prevMonthMenuItem;

    @FXML
    private MenuItem todayMenuItem;

    @FXML
    private MenuItem nextMonthMenuItem;

    private YearMonth currentYearMonth;

    public void initializeCalendar() {
        currentYearMonth = YearMonth.now();
        populateCalendar();

        // Add event handlers
        prevMonthMenuItem.setOnAction(e -> changeMonth(-1));
        todayMenuItem.setOnAction(e -> showCurrentMonth());
        nextMonthMenuItem.setOnAction(e -> changeMonth(1));
    }

    private void populateCalendar() {
        calendarGrid.getChildren().clear();

        // Display the month and year
        monthYearLabel.setText(currentYearMonth.getMonth().toString() + " " + currentYearMonth.getYear());

        // Fill in the day headers
        for (int i = 0; i < 7; i++) {
            Text dayOfWeek = new Text(LocalDate.of(currentYearMonth.getYear(), currentYearMonth.getMonthValue(), 1).plusDays(i).getDayOfWeek().toString().substring(0, 3));
            dayOfWeek.setFill(Color.GREEN);
            GridPane.setRowIndex(dayOfWeek, 0);
            GridPane.setColumnIndex(dayOfWeek, i);
            calendarGrid.add(dayOfWeek, i, 0);
        }

        // Determine the first day of the month and the number of days in the month
        int firstDayOfMonth = currentYearMonth.atDay(1).getDayOfWeek().getValue() % 7;
        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Populate the grid with days
        for (int day = 1, row = 1, col = firstDayOfMonth; day <= daysInMonth; day++, col++) {
            if (col > 6) {
                col = 0;
                row++;
            }
            Text dayText = new Text(String.valueOf(day));
            dayText.setFill(Color.GREEN);
            GridPane.setRowIndex(dayText, row);
            GridPane.setColumnIndex(dayText, col);
            calendarGrid.add(dayText, col, row);
        }
    }

    private void changeMonth(int monthsToAdd) {
        currentYearMonth = currentYearMonth.plusMonths(monthsToAdd);
        populateCalendar();
    }

    private void showCurrentMonth() {
        currentYearMonth = YearMonth.now();
        populateCalendar();
    }
}
