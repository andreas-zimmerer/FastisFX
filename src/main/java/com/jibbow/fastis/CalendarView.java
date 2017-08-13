package com.jibbow.fastis;

import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jibbow on 8/13/17.
 */
public abstract class CalendarView extends GridPane {
    protected List<Calendar> calendars = new LinkedList<>();

    public List<Calendar> getCalendars() {
        return calendars;
    }
}
