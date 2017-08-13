package com.jibbow.fastis;

import com.jibbow.fastis.util.DayPane;
import com.jibbow.fastis.util.PercentPane;
import com.jibbow.fastis.util.TimeAxis;
import com.jibbow.fastis.util.TimeIndicator;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Created by Jibbow on 8/12/17.
 */
public class DayView extends GridPane {

    AnchorPane headerPane;
    PercentPane allDayPane;
    DayPane dayPane;

    public DayView() {
        this.getStylesheets().add(DayView.class.getClassLoader().getResource("css/DayView.css").toString());

        this.headerPane = new AnchorPane();
        this.headerPane.getStyleClass().add("headerpane");
        this.allDayPane = new PercentPane();
        this.allDayPane.getStyleClass().add("alldaypane");
        this.dayPane = new DayPane(LocalDate.now());
        this.dayPane.getStyleClass().add("daypane");


        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setHmin(USE_COMPUTED_SIZE);
        scrollPane.setHvalue(USE_COMPUTED_SIZE);
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border

        // holds column for time labels on the left side and the DayPane on the right side
        GridPane dayPaneHolder = new GridPane();
        ColumnConstraints timeColumn = new ColumnConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, HPos.LEFT, false);
        ColumnConstraints appointmentscolumn = new ColumnConstraints(100,100,Double.POSITIVE_INFINITY,Priority.ALWAYS,HPos.CENTER,true);
        dayPaneHolder.getColumnConstraints().addAll(timeColumn, appointmentscolumn);
        RowConstraints rowConstraint = new RowConstraints(USE_PREF_SIZE,USE_COMPUTED_SIZE,Double.POSITIVE_INFINITY,Priority.ALWAYS,VPos.TOP,true);
        dayPaneHolder.getRowConstraints().add(rowConstraint);

        TimeAxis timeAxis = new TimeAxis(LocalTime.MIN, LocalTime.MAX, Duration.ofMinutes(60));

        dayPaneHolder.add(timeAxis, 0, 0);
        dayPaneHolder.add(new TimeIndicator(dayPane),1,0);
        scrollPane.setContent(dayPaneHolder);


        RowConstraints headerRow = new RowConstraints(50, 50, 50);
        RowConstraints allDayRow = new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.TOP, false);
        RowConstraints dayPaneRow = new RowConstraints(150, 500, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true);
        this.getRowConstraints().addAll(headerRow, allDayRow, dayPaneRow);
        ColumnConstraints columnConstraints = new ColumnConstraints(100, 200, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.LEFT, true);
        this.getColumnConstraints().add(columnConstraints);

        // ordering is important:
        this.add(scrollPane, 0 , 2);
        this.add(headerPane, 0, 0, 1,2);
        this.add(allDayPane, 0 , 1);

        populateHeaderPane(headerPane);
        populateAllDayPane(allDayPane);
    }


    private void populateAllDayPane(Pane allDayPane) {
        Label day = new Label("display all day appointments");
        allDayPane.getChildren().add(day);
    }

    private void populateHeaderPane(Pane header) {
        Label day = new Label("Mo");
        header.getChildren().add(day);
    }
}
