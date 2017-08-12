package com.jibbow.fastis.util;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.Duration;
import java.time.temporal.Temporal;

/**
 * Created by Jibbow on 8/12/17.
 */
public class TimeInterval {
    private ObjectProperty<Temporal> startDateTimeProperty;
    private ObjectProperty<Temporal> endDateTimeProperty;

    public TimeInterval(Temporal firstDate, Temporal lastDate) {
        this.startDateTimeProperty = new SimpleObjectProperty<>(firstDate);
        this.endDateTimeProperty = new SimpleObjectProperty<>(lastDate);
    }

    public static TimeInterval between(Temporal firstDate, Temporal lastDate) {
        return new TimeInterval(firstDate, lastDate);
    }

    public boolean overlaps(TimeInterval other) {
        return Duration.between(endDateTimeProperty().get(), other.startDateTimeProperty().get()).isNegative()
                && !Duration.between(startDateTimeProperty().get(), other.endDateTimeProperty().get()).isNegative();
    }

    public boolean startsBefore(TimeInterval other) {
        return !Duration.between(startDateTimeProperty().get(), other.startDateTimeProperty().get()).isNegative();
    }

    public boolean startsBefore(Temporal other) {
        return !Duration.between(startDateTimeProperty.get(), other).isNegative();
    }

    public boolean endsAfter(TimeInterval other) {
        return Duration.between(endDateTimeProperty().get(), other.endDateTimeProperty().get()).isNegative();
    }

    public boolean endsAfter(Temporal other) {
        return Duration.between(endDateTimeProperty.get(), other).isNegative();
    }

    public Duration getDuration() {
        return Duration.between(startDateTimeProperty.get(), endDateTimeProperty.get());
    }

    public ObjectProperty<Temporal> startDateTimeProperty() {
        return startDateTimeProperty;
    }

    public ObjectProperty<Temporal> endDateTimeProperty() {
        return endDateTimeProperty;
    }
}
