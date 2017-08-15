package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.DayChooser;
import com.jibbow.fastis.WeekView;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.text.DateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jibbow on 8/14/17.
 */
public class WeekViewRenderer extends DayPaneRenderer {
    public Node createAllDayPane(List<Appointment> appointments) {
        AnchorPane pane = new AnchorPane();
        return pane;
    }

    public Node createHeaderPane(WeekView calView) {
        final GridPane container = new GridPane();
        container.setAlignment(Pos.BOTTOM_LEFT);
        container.getStyleClass().add("headerpane");

        final Label lblWeekday = new Label(calView.getDate().get().format(DateTimeFormatter.ofPattern("EEE")));
        lblWeekday.getStyleClass().add("label-weekday");

        final Label lblDate = new Label(calView.getDate().get().toString());
        lblDate.getStyleClass().add("label-date");
        final ContextMenu dayChooserMenu = new ContextMenu();
        final CustomMenuItem item = new CustomMenuItem(new DayChooser(calView.getDate()));
        dayChooserMenu.getStyleClass().add("day-chooser");
        item.setHideOnClick(false);
        dayChooserMenu.getItems().add(item);
        lblDate.setOnMouseClicked(event ->
                dayChooserMenu.show(lblDate,
                        lblDate.localToScreen(0, 0).getX(),
                        lblDate.localToScreen(0, 0).getY())
        );

        final Button left = new Button("<");
        left.getStyleClass().add("header-button");
        left.setOnAction(event -> calView.getDate().set(calView.getDate().get().minusDays(1)));
        final Button right = new Button(">");
        right.getStyleClass().add("header-button");
        right.setOnAction(event -> calView.getDate().set(calView.getDate().get().plusDays(1)));

        final ColumnConstraints columnWeekday = new ColumnConstraints(70);
        final ColumnConstraints columnCenter = new ColumnConstraints(20,50,Double.POSITIVE_INFINITY, Priority.ALWAYS, HPos.LEFT,true);
        final ColumnConstraints columnSwitcher = new ColumnConstraints(60);
        container.getColumnConstraints().addAll(columnWeekday, columnCenter, columnSwitcher);
        container.add(lblWeekday,0,0);
        container.add(lblDate, 1,0);
        container.add(new HBox(left, right), 2,0);
        return container;
    }

    public Node createDayBackground(LocalDate date) {
        Pane p = new Pane();
        if(date.getDayOfWeek().equals(DayOfWeek.SUNDAY)
                || date.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
            p.getStyleClass().add("day-background-weekend");
        }
        if(date.equals(LocalDate.now())) {
            p.getStyleClass().add("day-background-today");
        }
        p.getStyleClass().add("day-background");
        return p;
    }

    public Node createSingleDayHeader(LocalDate date) {
        final Label lblWeekday = new Label(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault()));
        lblWeekday.getStyleClass().add("header-weekday");
        final Label lblDate = new Label(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(date));
        lblDate.getStyleClass().add("header-date");
        VBox container = new VBox(lblWeekday, lblDate);
        container.getStyleClass().add("header-container");
        if(date.equals(LocalDate.now())) {
            container.getStyleClass().add("header-container-today");
        }
        container.setAlignment(Pos.TOP_CENTER);
        return container;
    }
}
