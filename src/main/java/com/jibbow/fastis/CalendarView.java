package com.jibbow.fastis;

import javafx.beans.property.ObjectProperty;
import javafx.scene.layout.GridPane;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jibbow on 8/13/17.
 */
public abstract class CalendarView extends GridPane {
    protected List<Calendar> calendars = new LinkedList<>();
    ObjectProperty<LocalDate> dateProperty;

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public ObjectProperty<LocalDate> getDate() {
        return dateProperty;
    }
}
