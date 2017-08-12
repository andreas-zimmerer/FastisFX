package com.jibbow.fastis;

import com.jibbow.fastis.util.DayPane;
import com.jibbow.fastis.util.PercentPane;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.time.LocalDate;

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

        // responsible for time labels and horizontal rows
        GridPane backgroundPane = new GridPane();
        backgroundPane.setGridLinesVisible(true);
        backgroundPane.addRow(0);
        backgroundPane.addRow(1);
        backgroundPane.addRow(2);

        ScrollPane scrollPane = new ScrollPane(dayPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setStyle("-fx-background-color:transparent;"); // remove gray border

        RowConstraints headerRow = new RowConstraints(50, 50, 50);
        RowConstraints allDayRow = new RowConstraints(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE, USE_PREF_SIZE, Priority.ALWAYS, VPos.TOP, false);
        RowConstraints dayPaneRow = new RowConstraints(150, 500, Double.POSITIVE_INFINITY, Priority.ALWAYS, VPos.TOP, true);
        this.getRowConstraints().addAll(headerRow, allDayRow, dayPaneRow);

        ColumnConstraints columnConstraints = new ColumnConstraints(100, 200, Double.POSITIVE_INFINITY, Priority.SOMETIMES, HPos.CENTER, true);
        this.getColumnConstraints().add(columnConstraints);

        this.add(scrollPane, 0 , 2);
        this.add(allDayPane, 0 , 1);
        this.add(headerPane, 0, 0);

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
