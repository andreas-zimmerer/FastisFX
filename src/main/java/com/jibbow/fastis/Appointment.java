package com.jibbow.fastis;

import com.jibbow.fastis.util.TimeInterval;
import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

/**
 * Created by Jibbow on 8/11/17.
 */
public class Appointment {
    private ObjectProperty<TimeInterval> intervalProperty;
    private StringProperty titleProperty;
    private BooleanProperty isFullDayProperty = new SimpleBooleanProperty(false);


    public Appointment(boolean isFullDay, LocalDate date) {
        this(isFullDay, date, "");
    }
    public Appointment(boolean isFullDay, LocalDate date, String title) {
        this(isFullDay, date, new SimpleStringProperty(title));
    }
    public Appointment(boolean isFullDay, LocalDate date, StringProperty title) {
        this.intervalProperty = new SimpleObjectProperty<>(new TimeInterval(date, date));
        this.titleProperty = title;
        setFullDay(true);
    }
    public Appointment(TimeInterval interval) {
        this(interval, "");
    }
    public Appointment(ObjectProperty<TimeInterval> interval) {
        this(interval, new SimpleStringProperty(""));
    }
    public Appointment(TimeInterval interval, String title) {
        this(new SimpleObjectProperty<>(interval), new SimpleStringProperty(title));
    }
    public Appointment(ObjectProperty<TimeInterval> interval, StringProperty title) {
        this.intervalProperty = interval;
        this.titleProperty = title;
        setFullDay(false);
    }


    public void setFullDay(boolean value) {
        isFullDayProperty.set(value);
        if(value) {
            intervalProperty().set(
                    new TimeInterval(
                            LocalDateTime.of(LocalDate.ofYearDay(startTimeProperty().get(ChronoField.YEAR), startTimeProperty().get(ChronoField.DAY_OF_YEAR)),
                                    LocalTime.MIN),
                            LocalDateTime.of(LocalDate.ofYearDay(endTimeProperty().get(ChronoField.YEAR), endTimeProperty().get(ChronoField.DAY_OF_YEAR)),
                                    LocalTime.MAX)
                    ));
        }
    }

    public BooleanProperty isFullDayProperty() {
        return isFullDayProperty;
    }

    public StringProperty titleProperty() {
        return titleProperty;
    }

    public ObjectProperty<TimeInterval> intervalProperty() {
        return intervalProperty;
    }

    public Temporal startTimeProperty() {
        return intervalProperty.get().getStartDateTime();
    }

    public Temporal endTimeProperty() {
        return intervalProperty.get().getEndDateTime();
    }
}
