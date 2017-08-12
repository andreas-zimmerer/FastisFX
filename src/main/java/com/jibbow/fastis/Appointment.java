package com.jibbow.fastis;

import com.jibbow.fastis.util.TimeInterval;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.temporal.Temporal;

/**
 * Created by Jibbow on 8/11/17.
 */
public class Appointment {
    private ObjectProperty<TimeInterval> intervalProperty;


    public Appointment(TimeInterval interval) {
        this.intervalProperty = new SimpleObjectProperty<>(interval);
    }

    public ObjectProperty<TimeInterval> intervalProperty() {
        return intervalProperty;
    }

    public  ObjectProperty<Temporal> startTimeProperty() {
        return intervalProperty.get().startDateTimeProperty();
    }

    public ObjectProperty<Temporal> endTimeProperty() {
        return intervalProperty.get().endDateTimeProperty();
    }
}
