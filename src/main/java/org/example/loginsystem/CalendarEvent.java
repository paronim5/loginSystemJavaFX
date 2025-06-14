package org.example.loginsystem;
import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * class represents calendar event entiry from database
 */
@Entity
@Table(name = "CalendarEvent")
public class CalendarEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int eventID;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "UserID", nullable = false)
    private int userID;

    @Column(name = "EventDate", nullable = false)
    private LocalDate eventDate;

    @Column(name = "Done", columnDefinition = "bit default 0")
    private boolean done;

    // Constructors, Getters & Setters
    public CalendarEvent() {}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     *  to string for debugging reasons
     */

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "eventID=" + eventID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", userID=" + userID +
                ", eventDate=" + eventDate +
                ", done=" + done +
                '}';
    }
}