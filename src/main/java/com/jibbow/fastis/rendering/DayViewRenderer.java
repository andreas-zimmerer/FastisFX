package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.DayChooser;
import com.jibbow.fastis.DayView;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Jibbow on 8/13/17.
 */
public class DayViewRenderer extends DayPaneRenderer {

    public Node createAllDayPane(List<Appointment> appointments) {
        AnchorPane pane = new AnchorPane();
        Label day = new Label("all day");
        pane.getChildren().add(day);
        pane.getStyleClass().add("alldaypane");
        return pane;
    }

    public Node createHeaderPane(DayView calView) {
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
        left.getStyleClass().add("day-header-button");
        left.setOnAction(event -> calView.getDate().set(calView.getDate().get().minusDays(1)));
        final Button right = new Button(">");
        right.getStyleClass().add("day-header-button");
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
}
