package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.util.PercentPane;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
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
    public Region createGuiElement(Appointment appointment) {
        BorderPane p = new BorderPane();
        p.getStyleClass().add("appointment");

        VBox content = new VBox();

        Label starttime = new Label(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(appointment.startTimeProperty()));
        starttime.getStyleClass().add("lblStartTime");
        content.getChildren().add(starttime);
        Label title = new Label("Title");
        content.getChildren().add(title);

        Pane leftBar = new Pane();
        leftBar.getStyleClass().add("leftbar");
        leftBar.setPrefWidth(5.0);

        p.setLeft(leftBar);
        p.setCenter(content);
        p.setMargin(content, new Insets(0.0, 5.0, 0.0, 5.0));
        return p;
    }

    @Override
    public void layoutAppointments(Map<Appointment, Region> guiElements) {
        // implement overlapping style
        guiElements.values().forEach(region -> {
            if(region != null) {
                PercentPane.setLeftAnchor(region, 0.0);
                PercentPane.setRightAnchor(region, 0.0);
            }
        });
    }
}
