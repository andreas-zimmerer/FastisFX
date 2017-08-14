package com.jibbow.fastis.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Jibbow on 8/13/17.
 */
public class TimeAxis extends GridPane {
    private ObjectProperty<LocalTime> timeStartProperty;
    private ObjectProperty<LocalTime> timeEndProperty;
    private ObjectProperty<Duration> timeStepsProperty;
    private DateTimeFormatter formatter;


    public TimeAxis(LocalTime timeStart, LocalTime timeEnd, Duration timeSteps) {
        this(new SimpleObjectProperty<>(timeStart), new SimpleObjectProperty<>(timeEnd), new SimpleObjectProperty<>(timeSteps));
    }
    public TimeAxis(ObjectProperty<LocalTime> timeStart, ObjectProperty<LocalTime> timeEnd, ObjectProperty<Duration> timeSteps) {
        this(timeStart, timeEnd, timeSteps, DateTimeFormatter.ofPattern("HH:mm"));
    }
    public TimeAxis(LocalTime timeStart, LocalTime timeEnd, Duration timeSteps, DateTimeFormatter formatter) {
        this(new SimpleObjectProperty<>(timeStart), new SimpleObjectProperty<>(timeEnd), new SimpleObjectProperty<>(timeSteps), formatter);
    }
    public TimeAxis(ObjectProperty<LocalTime> timeStart, ObjectProperty<LocalTime> timeEnd,
                    ObjectProperty<Duration> timeSteps, DateTimeFormatter formatter) {
        this.timeStartProperty = timeStart;
        this.timeEndProperty = timeEnd;
        this.timeStepsProperty = timeSteps;
        this.formatter = formatter;

        timeStartProperty.addListener(observable -> calculateRows());
        timeEndProperty.addListener(observable -> calculateRows());
        timeStepsProperty.addListener(observable -> calculateRows());

        calculateRows();
    }


    private void calculateRows() {
        this.getChildren().clear();
        this.getRowConstraints().clear();

        for(LocalTime currentTime = timeStartProperty.get();
            currentTime.isBefore(timeEndProperty.get()); ) {

            RowConstraints row = new RowConstraints(0,0,Double.POSITIVE_INFINITY, Priority.SOMETIMES, VPos.TOP, true);
            this.getRowConstraints().add(row);

            Label indicator = new Label();
            indicator.setText(currentTime.format(formatter));
            indicator.getStyleClass().add("time-indicator-label");

            this.add(indicator, 0, this.getRowConstraints().size() - 1);

            // prevent overflows at midnight
            LocalTime newTime = currentTime.plusMinutes(timeStepsProperty.get().toMinutes());
            if(newTime.isAfter(currentTime)) {
                currentTime = newTime;
            } else {
                break;
            }
        }
    }
}
