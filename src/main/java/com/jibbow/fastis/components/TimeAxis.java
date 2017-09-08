package com.jibbow.fastis.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
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
    private final ObjectProperty<LocalTime> timeStartProperty;
    private final ObjectProperty<LocalTime> timeEndProperty;
    private final ObjectProperty<Duration> timeStepsProperty;
    private final boolean horizontal;
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
        this.horizontal = false;

        this.getStyleClass().add("time-axis");

        getTimeStartProperty().addListener(observable -> createLabels());
        getTimeEndProperty().addListener(observable -> createLabels());
        getTimeStepsProperty().addListener(observable -> createLabels());

        createLabels();
    }


    /**
     * Creates or updates the actual layout of the time axis.
     * The layout can either be horizontal or vertical.
     * The GridView is populated with columns/rows and labels are added accordingly.
     * The labels show the time between this.timeStartProperty and this.timeEndProperty with
     * this.timeStepsProperty in between.
     * The time is formatted according to this.formatter.
     */
    private void createLabels() {
        this.getChildren().clear();
        this.getRowConstraints().clear();
        this.getColumnConstraints().clear();

        for(LocalTime currentTime = getTimeStartProperty().get();
            currentTime.isBefore(getTimeEndProperty().get()); ) {

            // create a new label with the time
            Label lblTime = new Label();
            lblTime.setText(currentTime.format(getFormatter()));
            lblTime.getStyleClass().add("time-axis-label");

            // create a new row/column and add the label to it
            if(horizontal) {
                // center the label
                lblTime.widthProperty().addListener(o -> lblTime.setTranslateX( -lblTime.widthProperty().getValue() / 2));

                ColumnConstraints column = new ColumnConstraints(0, USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.LEFT, true);
                this.getColumnConstraints().add(column);
                this.add(lblTime, this.getColumnConstraints().size() - 1, 0);
            } else {
                // center the label
                lblTime.heightProperty().addListener(o -> lblTime.setTranslateY( -lblTime.heightProperty().getValue() / 2));
                RowConstraints row = new RowConstraints(0, USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.SOMETIMES, VPos.TOP, true);
                this.getRowConstraints().add(row);
                this.add(lblTime, 0, this.getRowConstraints().size() - 1);
            }

            // prevent overflows at midnight
            LocalTime newTime = currentTime.plusMinutes(getTimeStepsProperty().get().toMinutes());
            if(newTime.isAfter(currentTime)) {
                currentTime = newTime;
            } else {
                break;
            }
        }
    }

    public ObjectProperty<LocalTime> getTimeStartProperty() {
        return timeStartProperty;
    }

    public ObjectProperty<LocalTime> getTimeEndProperty() {
        return timeEndProperty;
    }

    public ObjectProperty<Duration> getTimeStepsProperty() {
        return timeStepsProperty;
    }

    public DateTimeFormatter getFormatter() {
        return formatter;
    }

    public void setFormatter(DateTimeFormatter formatter) {
        this.formatter = formatter;
        createLabels();
    }
}
