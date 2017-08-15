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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jibbow on 8/12/17.
 */
public class WeekView extends CalendarView {
    int numberOfDays = 7; // show one week per default

    Node weekHeaderPane;
    List<Node> dayHeaderPanes;
    List<Node> allDayAppointmentsPanes;
    List<DayPane> dayPanes;
    TimeAxis timeAxis;
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
        this.dayHeaderPanes = new ArrayList<>(numberOfDays);
        this.dayPanes = new ArrayList<>(numberOfDays);
        this.allDayAppointmentsPanes = new ArrayList<>(numberOfDays);

        // set layout for this pane
        final RowConstraints headerRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // HEADER
        final RowConstraints allDayRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // PANE FOR ALL DAY APPOINTMENTS
        final RowConstraints dayPaneRow = new RowConstraints(
                150, 500, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // CALENDAR
        final ColumnConstraints columnConstraints = new ColumnConstraints(
                400, 600, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.LEFT, true); // FULL
        this.getRowConstraints().addAll(headerRow, allDayRow, dayPaneRow);
        this.getColumnConstraints().add(columnConstraints);

        getDate().addListener(observable -> {
            setContent();
        });

        setContent();
    }


    private void setContent() {
        this.getChildren().clear();

        this.timeAxis = new TimeAxis(LocalTime.MIN, LocalTime.MAX, Duration.ofHours(1));
        this.weekHeaderPane = renderer.createHeaderPane(this);



        // ScrollPane that contains the DayPane and the TimeAxis
        final ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // holds a column for the TimeAxis on the left side and the DayPanes on the right side
        final GridPane dayPaneHolder = new GridPane();
        final ColumnConstraints timeColumn = new ColumnConstraints(
                USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, false); // TIME COLUMN
        final RowConstraints rowConstraint = new RowConstraints(
                USE_PREF_SIZE, USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // FULL
        dayPaneHolder.getRowConstraints().add(rowConstraint);
        dayPaneHolder.getColumnConstraints().add(timeColumn);

        // set layout for singleDayHeader
        final GridPane singleDayHeader = new GridPane();
        final RowConstraints singleDayHeaderRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // PANE FOR A DAILY HEADER
        final RowConstraints singleDayAppointmentsRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // PANE FOR ALL DAY APPOINTMENTS
        singleDayHeader.getRowConstraints().addAll(singleDayHeaderRow, singleDayAppointmentsRow);
        singleDayHeader.getStyleClass().add("all-day-pane-background");
        final ColumnConstraints timeColumnAllDay = new ColumnConstraints(); // additional space on the left side aligning it with the time axis
        timeColumnAllDay.minWidthProperty().bind(timeAxis.widthProperty());
        singleDayHeader.getColumnConstraints().add(timeColumnAllDay);
        singleDayHeader.setPadding(new Insets(0,17,0, 1)); // has to be there because the scrollpane takes away some space

        // create a new column for every day and add a DayPane as well as a AllDayPane to it
        for(int i=0;i<numberOfDays;i++) {
            final LocalDate currentDate = dateProperty.get().plusDays(i);

            List<Appointment> allAppointments = calendars.stream()
                    .flatMap(cal -> cal.getAppointmentsFor(dateProperty.get()).stream())
                    .collect(Collectors.toList());

            // create a new column
            final ColumnConstraints appointmentsColumn = new ColumnConstraints(
                    50, 100, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true);
            dayPaneHolder.getColumnConstraints().add(appointmentsColumn);
            singleDayHeader.getColumnConstraints().add(appointmentsColumn);

            // populate header pane for each day
            final Node dayHeader = renderer.createSingleDayHeader(currentDate);
            singleDayHeader.add(dayHeader, i+1, 0);
            dayHeaderPanes.add(dayHeader);

            // populate pane for all-day appointments
            final Node allDay = renderer.createAllDayPane(allAppointments.parallelStream()
                    .filter(appointment -> appointment.isFullDayProperty().get()).collect(Collectors.toList()));
            singleDayHeader.add(allDay, i+1, 1);
            allDayAppointmentsPanes.add(allDay);


            // create a background for each day
            final Node dayBackground = renderer.createDayBackground(currentDate);
            dayPaneHolder.add(dayBackground, i+1, 0);

            // create a new DayPane for each day
            final DayPane dp = new DayPane(currentDate);
            dayPaneHolder.add(dp, i+1, 0);
            // populate DayPane
            allAppointments.forEach(a -> dp.addAppointment(a));


            dayPanes.add(dp);
        }

        dayPaneHolder.add(timeAxis, 0, 0);
        //dayPaneHolder.add(timeIndicator, 1, 0);
        scrollPane.setContent(dayPaneHolder);

        GridPane.setColumnSpan(weekHeaderPane, numberOfDays);

        // ordering is important:
        this.add(scrollPane, 0, 2);
        this.add(singleDayHeader, 0, 1);
        this.add(weekHeaderPane, 0, 0);
    }

    public LocalDate getStartDate() {
        return dateProperty.get();
    }

    public LocalDate getEndDate() {
        return dateProperty.get().plusDays(numberOfDays - 1);
    }
}
