package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.DayView;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.util.List;

/**
 * Created by Jibbow on 8/13/17.
 */
public class DayViewRenderer extends DayPaneRenderer {

    public Node createAllDayPane(List<Appointment> appointments) {
        AnchorPane pane = new AnchorPane();
        Label day = new Label("display all day appointments");
        pane.getChildren().add(day);
        pane.getStyleClass().add("alldaypane");
        return pane;
    }

    public Node createHeaderPane(DayView dayView) {
        AnchorPane p = new AnchorPane();
        Label day = new Label("Mo");
        p.getChildren().add(day);
        p.getStyleClass().add("headerpane");
        p.setMinHeight(50);
        return p;
    }
}
