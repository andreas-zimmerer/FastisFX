package com.jibbow.fastis;

import com.jibbow.fastis.rendering.DayViewRenderer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;

/**
 * Created by Jibbow on 8/12/17.
 */
public class DayCalendarView extends WeekCalendarView {


    public DayCalendarView() {
        this(LocalDate.now(), new Calendar());
    }
    public DayCalendarView(LocalDate date, Calendar... calendar) {
        this(new SimpleObjectProperty<>(date), calendar);
    }

    public DayCalendarView(ObjectProperty<LocalDate> date, Calendar... calendar) {
        this(date, new DayViewRenderer(), calendar);
    }

    public DayCalendarView(ObjectProperty<LocalDate> date, DayViewRenderer renderer, Calendar... calendar) {
        super(date, 1, renderer, calendar);
    }
}
