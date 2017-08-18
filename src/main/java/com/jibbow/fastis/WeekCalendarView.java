package com.jibbow.fastis;

import com.jibbow.fastis.components.DayPane;
import com.jibbow.fastis.components.TimeAxis;
import com.jibbow.fastis.components.TimeIndicator;
import com.jibbow.fastis.rendering.WeekViewRenderer;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

/**
 * Created by Jibbow on 8/12/17.
 */
public class WeekCalendarView extends CalendarView {
    private static final int dayPaneMinWidth = 50;
    protected LocalTime dayStartTime;
    protected LocalTime dayEndTime;

    protected int numberOfDays;
    protected Pane weekHeaderContainer;
    protected GridPane dayHeadersContainer;
    protected GridPane dayPanesContainer;
    protected Pane timeAxisContainer;
    protected WeekViewRenderer renderer;


    /**
     * Creates a new WeekCalendar displaying the current week (7 days) and with a new empty calendar.
     */
    public WeekCalendarView() {
        this(LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()), new Calendar());
    }
    public WeekCalendarView(LocalDate dateBegin, Calendar... calendar) {
        this(new SimpleObjectProperty<>(dateBegin), calendar);
    }

    public WeekCalendarView(ObjectProperty<LocalDate> dateBegin, Calendar... calendar) {
        this(dateBegin, 7, calendar);
    }

    public WeekCalendarView(ObjectProperty<LocalDate> dateBegin, int numberOfDays, Calendar... calendar) {
        this(dateBegin, numberOfDays, LocalTime.MIN, LocalTime.MAX, new WeekViewRenderer(), calendar);
    }

    public WeekCalendarView(ObjectProperty<LocalDate> dateBegin, int numberOfDays, LocalTime dayStartTime, LocalTime dayEndTime, WeekViewRenderer renderer, Calendar... calendar) {
        this.getStylesheets().add(WeekCalendarView.class.getClassLoader().getResource("css/WeekView.css").toString());
        this.dateProperty = dateBegin;
        this.numberOfDays = numberOfDays;
        this.dayStartTime = dayStartTime;
        this.dayEndTime = dayEndTime;
        this.renderer = renderer;
        for (int i = 0; i < calendar.length; i++) {
            this.getCalendars().add(calendar[i]);
        }

        setLayout();
        setContent();

        getDate().addListener(observable -> {
            setContent();
        });

        getCalendars().addListener((InvalidationListener) observable -> {
            setContent();
        });
    }

    private void setLayout() {
        // set layout for this pane
        final RowConstraints headerRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // HEADER FOR FULL WEEK
        final RowConstraints allDayRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // SINGLE DAY HEADER AND ALL DAY APPOINTMENTS
        final RowConstraints calendarRow = new RowConstraints(
                150, 500, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // CALENDAR
        final ColumnConstraints columnConstraints = new ColumnConstraints(
                400, 600, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.LEFT, true); // FULL WIDTH
        this.getRowConstraints().addAll(headerRow, allDayRow, calendarRow);
        this.getColumnConstraints().add(columnConstraints);
        this.getStyleClass().add("weekview");


        // create a container for the week header
        Pane weekHeaderContainer = new StackPane();
        weekHeaderContainer.getStyleClass().add("weekview-header-container");
        this.weekHeaderContainer = weekHeaderContainer;

        // ScrollPane that contains the DayPane and the TimeAxis
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("weekview-scrollpane");
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // the ScrollPane holds a GridPane with two columns: one for the TimeAxis and one for the calendar
        final GridPane scrollPaneContent = new GridPane();
        scrollPaneContent.getStyleClass().add("weekview-scrollpane-content");
        final ColumnConstraints timeColumn = new ColumnConstraints(
                USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, false); // TIME COLUMN
        final ColumnConstraints calendarColumn = new ColumnConstraints(
                USE_PREF_SIZE, USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT, true); // CALENDAR COLUMN
        final RowConstraints rowConstraint = new RowConstraints(
                USE_PREF_SIZE, USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // FULL HEIGHT
        scrollPaneContent.getColumnConstraints().addAll(timeColumn, calendarColumn);
        scrollPaneContent.getRowConstraints().addAll(rowConstraint);
        scrollPane.setContent(scrollPaneContent);

        // create a container for the TimeAxis
        Pane timeAxisContainer = new StackPane();
        timeAxisContainer.getStyleClass().add("weekview-timeaxis-container");
        scrollPaneContent.add(timeAxisContainer, 0, 0);
        this.timeAxisContainer = timeAxisContainer;

        // set up a GridPane that holds all the DayPanes and a GridPane for the headers and full day appointments
        final GridPane dayPaneContainer = new GridPane();
        dayPaneContainer.getStyleClass().add("weekview-daypane-container");
        final GridPane dayHeaderContainer = new GridPane();
        dayHeaderContainer.getStyleClass().add("weekview-day-header-container");
        for (int i = 0; i < numberOfDays; i++) {
            final ColumnConstraints appointmentsColumn = new ColumnConstraints(
                    USE_PREF_SIZE, dayPaneMinWidth, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true);
            dayPaneContainer.getColumnConstraints().add(appointmentsColumn);
            dayHeaderContainer.getColumnConstraints().add(appointmentsColumn);
        }
        final RowConstraints singleDayHeaderRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // PANE FOR A DAILY HEADER
        final RowConstraints singleDayAppointmentsRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // PANE FOR ALL DAY APPOINTMENTS
        dayHeaderContainer.getRowConstraints().addAll(singleDayHeaderRow, singleDayAppointmentsRow);
        timeAxisContainer.widthProperty().addListener(observable -> {
            dayHeaderContainer.setPadding(new Insets(0, 17 /*scrollbar*/, 0, timeAxisContainer.getWidth() + 1));
        });
        final RowConstraints dayPanesRow = new RowConstraints(
                USE_PREF_SIZE, USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // FULL HEIGHT
        dayPaneContainer.getRowConstraints().add(dayPanesRow);
        this.dayPanesContainer = dayPaneContainer;
        this.dayHeadersContainer = dayHeaderContainer;
        scrollPaneContent.add(dayPaneContainer, 1, 0);

        // ordering is important:
        this.add(scrollPane, 0, 2);
        this.add(dayHeaderContainer, 0, 1);
        this.add(weekHeaderContainer, 0, 0);
    }


    private void setContent() {
        this.weekHeaderContainer.getChildren().clear();
        this.timeAxisContainer.getChildren().clear();
        this.dayHeadersContainer.getChildren().clear();
        this.dayPanesContainer.getChildren().clear();

        this.timeAxisContainer.getChildren().add(new TimeAxis(LocalTime.MIN, LocalTime.MAX, Duration.ofHours(1)));
        this.weekHeaderContainer.getChildren().add(renderer.createHeaderPane(this));


        // create a new column for every day and add a DayPane as well as a AllDayPane to it
        for (int i = 0; i < numberOfDays; i++) {
            final LocalDate currentDate = dateProperty.get().plusDays(i);

            //final List<FilteredList<Appointment>> allAppointments =
            //        calendars.stream().map(appointments -> appointments.getAppointmentsFor(currentDate)).collect(Collectors.toList());


            // populate header pane for each day
            final Node dayHeader = renderer.createSingleDayHeader(currentDate);
            dayHeadersContainer.add(dayHeader, i, 0);

            // populate pane for all-day appointments
            final Node allDay = renderer.createAllDayPane(getCalendars().parallelStream()
                    .flatMap(appointments -> appointments.stream())
                    .filter(appointment -> appointment.isFullDayProperty().get()).collect(Collectors.toList()));
            dayHeadersContainer.add(allDay, i, 1);


            // create a background for each day
            final Node dayBackground = renderer.createDayBackground(currentDate);
            dayPanesContainer.add(dayBackground, i, 0);

            // create a new DayPane for each day
            final DayPane dp = new DayPane(currentDate, dayStartTime, dayEndTime, renderer);
            final TimeIndicator indicator = new TimeIndicator(dp);
            dayPanesContainer.add(indicator, i, 0);
            // populate DayPane
            // add ALL appointments (those that are not on this date will not be displayed, but this makes sense if
            // one of the appointments changes it interval)
            getCalendars().stream().flatMap(appointments -> appointments.stream()).forEach(a -> dp.addAppointment(a));

            // update DayPane when calendar changes
            getCalendars().forEach(appointments -> appointments.addListener((ListChangeListener<Appointment>) c -> {
                while(c.next()) {
                    for(Appointment a : c.getAddedSubList()) {
                        dp.addAppointment(a);
                    }
                    for(Appointment a : c.getRemoved()) {
                        dp.removeAppointment(a);
                    }
                }
            }));
        }
    }



    private void populateSingleDay() {

    }

    public LocalDate getStartDate() {
        return dateProperty.get();
    }

    public LocalDate getEndDate() {
        return dateProperty.get().plusDays(numberOfDays - 1);
    }
}
