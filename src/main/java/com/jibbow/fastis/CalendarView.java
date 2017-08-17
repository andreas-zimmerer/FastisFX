package com.jibbow.fastis;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jibbow on 8/13/17.
 */
public abstract class CalendarView extends GridPane {
    protected ObservableList<Calendar> calendars = FXCollections.observableArrayList();
    ObjectProperty<LocalDate> dateProperty;

    public ObservableList<Calendar> getCalendars() {
        return calendars;
    }

    public ObjectProperty<LocalDate> getDate() {
        return dateProperty;
    }
}
