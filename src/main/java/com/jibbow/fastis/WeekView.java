package com.jibbow.fastis;

import com.jibbow.fastis.rendering.WeekViewRenderer;
import com.jibbow.fastis.util.DayPane;
import com.jibbow.fastis.util.TimeAxis;
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

/**
 * Created by Jibbow on 8/12/17.
 */
public class WeekView extends CalendarView {
    int numberOfDays = 7; // show one week per default

    Node headerPane;
    List<Node> allDayPanes;
    List<DayPane> dayPane;
    TimeAxis timeAxis;

    WeekViewRenderer renderer = new WeekViewRenderer();

    public WeekView(LocalDate dateBegin) {
        this.getStylesheets().add(WeekView.class.getClassLoader().getResource("css/WeekView.css").toString());
        this.dateProperty = new SimpleObjectProperty<>(dateBegin);

        this.timeAxis = new TimeAxis(LocalTime.MIN, LocalTime.MAX, Duration.ofMinutes(30));
        this.allDayPanes = new ArrayList<>(numberOfDays);
        this.headerPane = renderer.createHeaderPane(this);

        setLayout();
    }


    private void setLayout() {
        // set layout for this pane
        RowConstraints headerRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // HEADER
        RowConstraints allDayRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE); // ALLDAY
        RowConstraints dayPaneRow = new RowConstraints(150, 500, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // CALENDAR
        ColumnConstraints columnConstraints = new ColumnConstraints(400, 600, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.LEFT, true); // FULL
        this.getRowConstraints().addAll(headerRow, allDayRow, dayPaneRow);
        this.getColumnConstraints().add(columnConstraints);

        // ScrollPane that contains the DayPane and the TimeAxis
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // holds a column for the TimeAxis on the left side and the DayPanes on the right side
        GridPane dayPaneHolder = new GridPane();
        GridPane allDayPane = new GridPane();
        ColumnConstraints timeColumn = new ColumnConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, false); // TIME COLUMN
        RowConstraints rowConstraint = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true); // FULL
        dayPaneHolder.getRowConstraints().add(rowConstraint);
        dayPaneHolder.getColumnConstraints().add(timeColumn);

        // set layout for allDayPane
        allDayPane.getStyleClass().add("all-day-pane-background");
        ColumnConstraints timeColumnAllDay = new ColumnConstraints(); // additional space on the left side aligning it with the time axis
        timeColumnAllDay.minWidthProperty().bind(timeAxis.widthProperty());
        allDayPane.getColumnConstraints().add(timeColumnAllDay);
        allDayPane.setPadding(new Insets(0,17,0, 1)); // has to be there because the scrollpane takes away some space

        // create a new column for every day and add a DayPane as well as a AllDayPane to it
        for(int i=0;i<numberOfDays;i++) {
            ColumnConstraints appointmentsColumn = new ColumnConstraints(50, 100, Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.CENTER, true);
            dayPaneHolder.getColumnConstraints().add(appointmentsColumn);
            allDayPane.getColumnConstraints().add(appointmentsColumn);

            Node allDay = renderer.createAllDayPane(new LinkedList<>());
            allDayPane.add(allDay, i+1, 0);
            allDayPanes.add(allDay);

            dayPaneHolder.add(new DayPane(dateProperty.get()), i+1, 0);
        }

        dayPaneHolder.add(timeAxis, 0, 0);
        //dayPaneHolder.add(timeIndicator, 1, 0);
        scrollPane.setContent(dayPaneHolder);

        GridPane.setColumnSpan(headerPane, numberOfDays);

        // ordering is important:
        this.add(scrollPane, 0, 2);
        this.add(allDayPane, 0, 1);
        this.add(headerPane, 0, 0);
    }
}
