package com.jibbow.fastis;

import com.jibbow.fastis.util.DayPane;
import com.jibbow.fastis.util.TimeAxis;
import com.jibbow.fastis.util.TimeIndicator;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Jibbow on 8/12/17.
 */
public class DayView extends CalendarView {

    Node headerPane;
    Node allDayPane;
    DayPane dayPane;
    TimeAxis timeAxis;
    TimeIndicator timeIndicator;

    public DayView() {
        this.getStylesheets().add(DayView.class.getClassLoader().getResource("css/DayView.css").toString());
        this.setPrefWidth(300);
        this.setPrefHeight(400);

        this.headerPane = populateHeaderPane();
        this.allDayPane = populateAllDayPane();
        this.dayPane = new DayPane(LocalDate.now());
        this.dayPane.getStyleClass().add("daypane");
        this.timeAxis = new TimeAxis(LocalTime.MIN, LocalTime.MAX, Duration.ofMinutes(60));
        this.timeIndicator = new TimeIndicator(dayPane);

        // set layout for this pane
        RowConstraints headerRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE);
        RowConstraints allDayRow = new RowConstraints(USE_PREF_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE);
        RowConstraints dayPaneRow = new RowConstraints(150, 500, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true);
        ColumnConstraints columnConstraints = new ColumnConstraints(100, 200, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.LEFT, true);
        this.getRowConstraints().addAll(headerRow, allDayRow, dayPaneRow);
        this.getColumnConstraints().add(columnConstraints);


        // ScrollPane that contains the DayPane and the TimeAxis
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border

        // holds a column for the TimeAxis on the left side and the DayPane on the right side
        GridPane dayPaneHolder = new GridPane();
        ColumnConstraints timeColumn = new ColumnConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, false);
        ColumnConstraints appointmentsColumn = new ColumnConstraints(100,100,Double.POSITIVE_INFINITY,Priority.ALWAYS,HPos.CENTER,true);
        RowConstraints rowConstraint = new RowConstraints(USE_PREF_SIZE,USE_COMPUTED_SIZE,Double.POSITIVE_INFINITY,Priority.ALWAYS,VPos.TOP,true);
        dayPaneHolder.getColumnConstraints().addAll(timeColumn, appointmentsColumn);
        dayPaneHolder.getRowConstraints().add(rowConstraint);


        dayPaneHolder.add(timeAxis, 0, 0);
        dayPaneHolder.add(timeIndicator,1,0);
        scrollPane.setContent(dayPaneHolder);

        // ordering is important:
        this.add(scrollPane, 0, 2);
        this.add(allDayPane, 0, 1);
        this.add(headerPane, 0, 0);
    }


    private Node populateAllDayPane() {
        AnchorPane pane = new AnchorPane();
        Label day = new Label("display all day appointments");
        pane.getChildren().add(day);
        pane.getStyleClass().add("alldaypane");
        return pane;
    }

    private Node populateHeaderPane() {
        AnchorPane p = new AnchorPane();
        Label day = new Label("Mo");
        p.getChildren().add(day);
        p.getStyleClass().add("headerpane");
        p.setMinHeight(50);
        return p;
    }
}
