package com.jibbow.fastis;

import biweekly.ICalendar;
import com.jibbow.fastis.components.TimeAxis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.util.List;


/**
 * Created by Jibbow on 11/26/17.
 */
public class CalendarViewWeek extends BorderPane {
    private final ObservableList<ICalendar> calendars;
    private final GridPane dayHeader;
    private final GridPane calendarPane;
    private final TimeAxis timeAxis;

    /**
     * Creates a new CalendarView with an empty set of calendars.
     */
    public CalendarViewWeek() {
        this(FXCollections.observableArrayList());
    }

    /**
     * Creates a new CalendarView with the given calendars that will
     * be displayed in the view.
     * @param calendars A list of calendars being displayed.
     */
    public CalendarViewWeek(List<ICalendar> calendars) {
        this(FXCollections.observableList(calendars));
    }

    /**
     * Creates a new CalendarView with the given calendars that will
     * be displayed in the view.
     * @param calendars A list of calendars being displayed.
     */
    public CalendarViewWeek(ObservableList<ICalendar> calendars) {
        super();
        this.calendars = calendars;
        this.dayHeader = new GridPane();
        this.calendarPane = new GridPane();

        createLayout();
    }



    protected void createLayout() {
        this.setTop(dayHeader);
        timeAxis.widthProperty().addListener((observable, oldValue, newValue) ->
                dayHeader.setPadding(new Insets(0.0, 0.0, 0.0, (Double)newValue)));

        // ScrollPane that contains the DayPane and the TimeAxis
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);


    }



    /**
     * Returns a list of all displayed calendars.
     */
    public ObservableList<ICalendar> getCalendars() {
        return calendars;
    }
}
