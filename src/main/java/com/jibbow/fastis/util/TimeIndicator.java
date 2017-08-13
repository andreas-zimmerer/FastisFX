package com.jibbow.fastis.util;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Jibbow on 8/13/17.
 */
public class TimeIndicator extends PercentPane {

    Node indicator;
    ObjectProperty<LocalTime> startTime;
    ObjectProperty<LocalTime> endTime;
    LocalDate date;

    public TimeIndicator(DayPane dayPane) {
        this(dayPane, new Indicator());
    }

    public TimeIndicator(DayPane dayPane, Node indicator) {
        AnchorPane.setLeftAnchor(dayPane, 0.0);
        AnchorPane.setTopAnchor(dayPane, 0.0);
        AnchorPane.setRightAnchor(dayPane, 0.0);
        AnchorPane.setBottomAnchor(dayPane, 0.0);
        this.getChildren().add(dayPane);

        this.startTime = dayPane.dayStartTimeProperty();
        this.endTime = dayPane.dayEndTimeProperty();
        this.date = dayPane.getDayDate();

        this.indicator = indicator;
        AnchorPane.setLeftAnchor(indicator, 0.0);
        AnchorPane.setRightAnchor(indicator, 0.0);
        this.getChildren().add(indicator);

        // update position if the DayPane's time window changes
        startTime.addListener(observable -> setIndicatorPosition(indicator));
        endTime.addListener(observable -> setIndicatorPosition(indicator));

        // updates the position every minute
        Timeline indicatorupdate = new Timeline(new KeyFrame(javafx.util.Duration.minutes(1), actionEvent -> setIndicatorPosition(indicator)));
        indicatorupdate.setCycleCount(Animation.INDEFINITE);
        indicatorupdate.play();

        // initial position
        setIndicatorPosition(indicator);
    }


    /**
     * Sets the position of a given indicator according to the current time of
     * the hosts machine. If the current time is not inside the time window of
     * the DayPane the indicator will be removed from the stage. If it is inside
     * the time window the indicator will be shown. It does not update the
     * position when time passes by.
     * @param indicator The indicator node that should be shown on the stage.
     */
    private void setIndicatorPosition(Node indicator) {
        if (LocalTime.now().isBefore(startTime.get())
                || LocalTime.now().isAfter(endTime.get())
                || !LocalDate.now().isEqual(date)) {
            this.getChildren().remove(indicator);
        } else {
            if (!this.getChildren().contains(indicator)) {
                this.getChildren().add(indicator);
            }
        }
        int totalMinutes = (int) Duration.between(startTime.get(), endTime.get()).toMinutes();
        int offsetMinutes = LocalTime.now().minusMinutes(startTime.get().toSecondOfDay() / 60).toSecondOfDay() / 60;

        PercentPane.setTopAnchor(indicator, (double) offsetMinutes / totalMinutes);
    }

    private static class Indicator extends StackPane {
        public Indicator() {
            Circle c = new Circle(5);
            Line l = new Line();
            l.setStartX(0);
            l.endXProperty().bind(this.widthProperty().subtract(10));
            c.getStyleClass().add("timeindicator");
            l.getStyleClass().add("timeindicator");

            StackPane.setAlignment(c, Pos.CENTER_LEFT);
            this.getChildren().add(l);
            this.getChildren().add(c);
            this.translateYProperty().bind(this.heightProperty().divide(2).negate());
        }
    }
}
