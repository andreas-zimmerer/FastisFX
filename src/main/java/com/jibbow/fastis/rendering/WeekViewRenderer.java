package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.WeekView;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Jibbow on 8/14/17.
 */
public class WeekViewRenderer extends DayPaneRenderer {
    public Node createAllDayPane(List<Appointment> appointments) {
        AnchorPane pane = new AnchorPane();
        Label day = new Label("all day");
        pane.getChildren().add(day);
        pane.getStyleClass().add("alldaypane");
        return pane;
    }

    public Node createHeaderPane(WeekView calView) {
        HBox container = new HBox();
        container.setAlignment(Pos.BOTTOM_LEFT);
        container.getStyleClass().add("headerpane");

        Label lblWeekday = new Label("KW: " + calView.getStartDate().get(ChronoField.ALIGNED_WEEK_OF_YEAR));
        lblWeekday.getStyleClass().add("label-weekday");

        Label lblDate = new Label(calView.getStartDate().toString() + "\n" + calView.getEndDate().toString());
        lblDate.getStyleClass().add("label-date");

        container.getChildren().add(lblWeekday);
        container.getChildren().add(lblDate);
        return container;
    }

    public Node createDayBackground(LocalDate date) {
        Pane p = new Pane();
        if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY))
            p.setStyle("-fx-background-color: red");
        return p;
    }

    public Node createSingleDayHeader(LocalDate date) {
        return new Label(date.toString());
    }
}
