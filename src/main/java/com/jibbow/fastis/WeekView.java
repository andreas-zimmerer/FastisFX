package com.jibbow.fastis;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;

/**
 * Created by Jibbow on 8/12/17.
 */
public class WeekView extends CalendarView {
    IntegerProperty numberOfDaysProperty = new SimpleIntegerProperty(7); // show one week per default
    ObjectProperty<LocalDate> dateBeginProperty;

    GridPane weekPane = new GridPane();
    GridPane allDayPane = new GridPane();
    GridPane timeIndicatorPane = new GridPane();

    public WeekView(LocalDate dateBegin) {
        this.dateBeginProperty = new SimpleObjectProperty<>(dateBegin);

        dateBeginProperty.addListener((observable, oldValue, newValue) -> populateDayViews(oldValue, newValue));
    }


    private void populateDayViews(LocalDate oldValue, LocalDate newValue) {
        if(oldValue == null || newValue == null) {
            weekPane.getChildren().clear();
        }


    }
}
