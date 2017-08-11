package com.jibbow.fastis;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Created by Jibbow on 8/11/17.
 */
public class AppointmentsRenderFactory {
    public Pane renderDayVew(Appointment appointment) {
        Pane p = new Pane();
        p.getChildren().add(new Label("Appointment"));
        p.setStyle("-fx-background-color: red");
        return p;
    }
}
