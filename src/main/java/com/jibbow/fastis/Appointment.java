package com.jibbow.fastis;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDateTime;

/**
 * Created by Jibbow on 8/11/17.
 */
public class Appointment {
    private ObjectProperty<LocalDateTime> startDateTimeProperty;
    private ObjectProperty<LocalDateTime> endDateTimeProperty;


    public Appointment(LocalDateTime startTime, LocalDateTime endTime) {
        this.startDateTimeProperty = new SimpleObjectProperty<>(startTime);
        this.endDateTimeProperty = new SimpleObjectProperty<>(endTime);
    }

    public ObjectProperty<LocalDateTime> startDateTimeProperty() {
        return startDateTimeProperty;
    }

    public ObjectProperty<LocalDateTime> endDateTimeProperty() {
        return endDateTimeProperty;
    }
}
