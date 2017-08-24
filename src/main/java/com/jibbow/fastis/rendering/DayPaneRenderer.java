package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.util.PercentPane;
import com.jibbow.fastis.util.TimeInterval;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

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
        this(OverlapStyle.STACKING);
    }

    public DayPaneRenderer(OverlapStyle style) {
        this.style = style;
    }

    @Override
    public Region createAppointmentElement(Appointment appointment) {
        if(appointment.isFullDayProperty().get()) {
            Pane p = new Pane();
            p.getStylesheets().add(DayPaneRenderer.class.getClassLoader().getResource("css/Appointment.css").toString());
            p.getStyleClass().add("appointment-fullday");
            return p;
        } else {
            BorderPane p = new BorderPane();
            p.getStylesheets().add(DayPaneRenderer.class.getClassLoader().getResource("css/Appointment.css").toString());
            p.getStyleClass().add("appointment");

            VBox content = new VBox();

            Label starttime = new Label(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(appointment.startTimeProperty()));
            starttime.getStyleClass().add("lblStartTime");
            content.getChildren().add(starttime);
            Label title = new Label(appointment.titleProperty().get());
            content.getChildren().add(title);

            Pane leftBar = new Pane();
            leftBar.getStyleClass().add("leftbar");
            leftBar.setPrefWidth(5.0);

            p.setLeft(leftBar);
            p.setCenter(content);
            p.setMargin(content, new Insets(0.0, 5.0, 0.0, 5.0));
            return p;
        }
    }

    @Override
    public void layoutAppointments(Map<Appointment, Region> guiElements) {
        switch (style) {
            case FLEX:
                layoutAppointmentsFlex(guiElements);
                break;
            case STACKING:
                layoutAppointmentsStacking(guiElements);
                break;
            case LANES:
                break;
        }
    }

    protected void layoutAppointmentsFlex(Map<Appointment, Region> guiElements) {
        List<Appointment> sortedAppointments = guiElements.keySet().stream().sorted(
                (o1, o2) -> {
                    // order appointments: 1. ends first 2. starts first
                    int ending = -(int) Duration.between(o1.endTimeProperty(), o2.endTimeProperty()).toMinutes();
                    if(ending != 0) return ending;
                    int start = -(int) Duration.between(o1.startTimeProperty(), o2.startTimeProperty()).toMinutes();
                    return start;
                })
                .collect(Collectors.toList());

        // associates every appointment with a "column" and the number of parallel appointments
        List<Integer> columnIndex = new ArrayList<>(sortedAppointments.size());
        List<Integer> columnsParallel = new ArrayList<>(sortedAppointments.size());

        // calculate columnIndex and columnsParallel
        for(int i = 0; i<sortedAppointments.size();i++) {
            Appointment app = sortedAppointments.get(i);
            Region reg = guiElements.get(app);
            if (reg != null) { // only if the current appointment is displayed
                columnIndex.add(i, 0); // align left
                columnsParallel.add(i, 1); // number of parallel appointments -> 1 = full width

                boolean newColumn = true; // whether a new column has to be allocated
                for (int a = 0; a < i; a++) { // check all other appointments that have already been processed
                    if (sortedAppointments.get(a).intervalProperty().get().overlaps(app.intervalProperty().get())) { // we have a collision
                        if (columnIndex.get(a) > columnIndex.get(i)) {
                            // current appointment can fit left of 'a, so no new column has to be allocated
                            columnsParallel.set(i, Math.max(columnsParallel.get(a), columnsParallel.get(i)));
                            newColumn = false;
                        } else {
                            // find a suitable position for the appointment
                            columnIndex.set(i, Math.max(columnIndex.get(a) + 1, columnIndex.get(i)));
                            if (newColumn) {
                                // allocate a new column
                                columnsParallel.set(i, Math.max(columnsParallel.get(a) + 1, columnsParallel.get(i)));
                                columnsParallel.set(a, columnsParallel.get(a) + 1);
                            }
                        }
                    }
                }
            }
        }

        // set layout of all appointments according to columnIndex and columnsParallel
        int i = 0;
        for(Appointment app : sortedAppointments) {
            Region reg = guiElements.get(app);
            if (reg != null) {
                // get current values
                int colIndex = columnIndex.get(i);
                int colParallel = columnsParallel.get(i);

                // calculate horizontal position
                double left = (double)colIndex / colParallel;
                double right = 1.0 - (double) (colIndex + 1) / colParallel;

                // set position
                PercentPane.setLeftAnchor(reg, left);
                PercentPane.setRightAnchor(reg, right);
            }
            i++;
        }
    }

    protected void layoutAppointmentsStacking(Map<Appointment, Region> guiElements) {
        List<Appointment> sortedAppointments = guiElements.keySet().stream().sorted(
                (o1, o2) -> -(int) Duration.between(o1.startTimeProperty(), o2.startTimeProperty()).toMinutes())
                .collect(Collectors.toList());

        int numberoffulldayapp = 0;
        ListIterator<Appointment> iterator = sortedAppointments.listIterator();
        for (int index = 0; iterator.hasNext(); index++) {
            Appointment a = iterator.next();
            if (a.isFullDayProperty().get()) {
                if (guiElements.get(a) != null) {
                    PercentPane.setLeftAnchor(guiElements.get(a), 0.0);
                    guiElements.get(a).setMaxWidth(10.0);
                    guiElements.get(a).setMinWidth(10.0);
                    numberoffulldayapp++;
                }
            } else {
                // get overlapping appointments before
                final TimeInterval interval = a.intervalProperty().get();
                List<Appointment> stack = sortedAppointments.stream().limit(index).filter(appointment ->
                        appointment.intervalProperty().get().overlaps(interval)
                ).filter(appointment -> !appointment.isFullDayProperty().get())
                        .collect(Collectors.toList());

                // increase the right margin of all stacked appointments, so that the new one is "on top"
                for (int i = 0; i < stack.size(); i++) {
                    if (guiElements.get(stack.get(i)) != null) {
                        PercentPane.setLeftAnchor(guiElements.get(stack.get(i)), i * 0.1);
                        PercentPane.setRightAnchor(guiElements.get(stack.get(i)), 0.1 * (stack.size() - i));
                    }
                }
                // new appointment is on top
                if (guiElements.get(a) != null) {
                    PercentPane.setRightAnchor(guiElements.get(a), 0.0);
                    PercentPane.setLeftAnchor(guiElements.get(a), 0.1 * stack.size());
                    guiElements.get(a).toFront();
                }
            }
        }
    }
}
