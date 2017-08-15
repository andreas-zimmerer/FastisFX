package com.jibbow.fastis;

import com.jibbow.fastis.rendering.WeekViewRenderer;
import com.jibbow.fastis.components.DayPane;
import com.jibbow.fastis.components.TimeAxis;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jibbow on 8/12/17.
 */
public class WeekView extends CalendarView {
    int numberOfDays;

    Pane weekHeaderContainer;
    GridPane dayHeadersContainer;
    GridPane dayPanesContainer;
    Pane timeAxisContainer;
    WeekViewRenderer renderer;


    public WeekView(LocalDate dateBegin, Calendar... calendar) {
        this(new SimpleObjectProperty<>(dateBegin), calendar);
    }
    public WeekView(ObjectProperty<LocalDate> dateBegin, Calendar... calendar) {
        this(dateBegin, 7, calendar);
    }
    public WeekView(ObjectProperty<LocalDate> dateBegin, int numberOfDays, Calendar... calendar) {
        this(dateBegin, numberOfDays, new WeekViewRenderer(), calendar);
    }
    public WeekView(ObjectProperty<LocalDate> dateBegin, int numberOfDays, WeekViewRenderer renderer, Calendar... calendar) {
        this.getStylesheets().add(WeekView.class.getClassLoader().getResource("css/WeekView.css").toString());
        this.dateProperty = dateBegin;
        this.numberOfDays = numberOfDays;
        this.renderer = renderer;
        for (int i = 0; i < calendar.length; i++) {
            this.getCalendars().add(calendar[i]);
        }

        getDate().addListener(observable -> {
            setContent();
        });

        setLayout();
        setContent();
    }

    private void setLayout() {
        // set layout for this pane
        final RowConstraints headerRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // HEADER FOR FULL WEEK
        final RowConstraints allDayRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // SINGLE DAY HEADER AND ALL DAY APPOINTMENTS
        final RowConstraints dayPaneRow = new RowConstraints(
                150, 500, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // CALENDAR
        final ColumnConstraints columnConstraints = new ColumnConstraints(
                400, 600, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.LEFT, true); // FULL WIDTH
        this.getRowConstraints().addAll(headerRow, allDayRow, dayPaneRow);
        this.getColumnConstraints().add(columnConstraints);


        // create a container for the week header
        Pane weekHeaderPaneContainer = new StackPane();
        this.weekHeaderContainer = weekHeaderPaneContainer;

        // ScrollPane that contains the DayPane and the TimeAxis
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // the ScrollPane holds a GridPane with two columns: one for the TimeAxis and one for the calendar
        final GridPane scrollPaneContent = new GridPane();
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
        scrollPaneContent.add(timeAxisContainer, 0, 0);
        this.timeAxisContainer = timeAxisContainer;

        // set up a GridPane that holds all the DayPanes and a GridPane for the headers and full day appointments
        final GridPane dayPaneContainer = new GridPane();
        final GridPane dayHeaderPaneContainer = new GridPane();
        for(int i=0;i<numberOfDays;i++) {
            final ColumnConstraints appointmentsColumn = new ColumnConstraints(
                    50, 100, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true);
            dayPaneContainer.getColumnConstraints().add(appointmentsColumn);
            dayHeaderPaneContainer.getColumnConstraints().add(appointmentsColumn);
        }
        final RowConstraints singleDayHeaderRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // PANE FOR A DAILY HEADER
        final RowConstraints singleDayAppointmentsRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // PANE FOR ALL DAY APPOINTMENTS
        dayHeaderPaneContainer.getRowConstraints().addAll(singleDayHeaderRow, singleDayAppointmentsRow);
        dayHeaderPaneContainer.setPadding(new Insets(0,17,0, 10)); // has to be there because the scrollpane takes away some space
        this.dayPanesContainer = dayPaneContainer;
        this.dayHeadersContainer = dayHeaderPaneContainer;
        scrollPaneContent.add(dayPaneContainer, 1, 0);

        // ordering is important:
        this.add(scrollPane, 0, 2);
        this.add(dayHeaderPaneContainer, 0, 1);
        this.add(weekHeaderPaneContainer, 0, 0);
    }


    private void setContent() {
        this.weekHeaderContainer.getChildren().clear();
        this.timeAxisContainer.getChildren().clear();
        this.dayHeadersContainer.getChildren().clear();
        this.dayPanesContainer.getChildren().clear();

        this.timeAxisContainer.getChildren().add(new TimeAxis(LocalTime.MIN, LocalTime.MAX, Duration.ofHours(1)));
        this.weekHeaderContainer.getChildren().add(renderer.createHeaderPane(this));


        // create a new column for every day and add a DayPane as well as a AllDayPane to it
        for(int i=0;i<numberOfDays;i++) {
            final LocalDate currentDate = dateProperty.get().plusDays(i);

            List<Appointment> allAppointments = calendars.stream()
                    .flatMap(cal -> cal.getAppointmentsFor(dateProperty.get()).stream())
                    .collect(Collectors.toList());


            // populate header pane for each day
            final Node dayHeader = renderer.createSingleDayHeader(currentDate);
            dayHeadersContainer.add(dayHeader, i, 0);

            // populate pane for all-day appointments
            final Node allDay = renderer.createAllDayPane(allAppointments.parallelStream()
                    .filter(appointment -> appointment.isFullDayProperty().get()).collect(Collectors.toList()));
            dayHeadersContainer.add(allDay, i, 1);



            // create a background for each day
            final Node dayBackground = renderer.createDayBackground(currentDate);
            dayPanesContainer.add(dayBackground, i, 0);

            // create a new DayPane for each day
            final DayPane dp = new DayPane(currentDate);
            dayPanesContainer.add(dp, i, 0);
            // populate DayPane
            allAppointments.forEach(a -> dp.addAppointment(a));
        }
    }

    public LocalDate getStartDate() {
        return dateProperty.get();
    }

    public LocalDate getEndDate() {
        return dateProperty.get().plusDays(numberOfDays - 1);
    }
}
