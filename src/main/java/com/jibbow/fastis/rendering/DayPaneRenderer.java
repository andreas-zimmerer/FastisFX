package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.util.PercentPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.Map;

/**
 * Created by Jibbow on 8/11/17.
 */
public class DayPaneRenderer implements AppointmentRenderer {

    public enum OverlapStyle {
        FLEX,
        LANES,
        STACKING
    }

    private OverlapStyle style;


    public DayPaneRenderer() {
        this(OverlapStyle.FLEX);
    }

    public DayPaneRenderer(OverlapStyle style) {
        this.style = style;
    }

    @Override
    public Pane createGuiElement(Appointment appointment) {
        Pane p = new Pane();
        p.getChildren().add(new Label("Appointment"));
        p.setStyle("-fx-background-color: red");
        return p;
    }

    @Override
    public void layoutAppointments(Map<Appointment, Pane> guiElements) {
        // implement overlapping style
        guiElements.values().forEach(pane -> {
            if(pane != null) {
                PercentPane.setLeftAnchor(pane, 0.0);
                PercentPane.setRightAnchor(pane, 0.0);
            }
        });
    }
}
