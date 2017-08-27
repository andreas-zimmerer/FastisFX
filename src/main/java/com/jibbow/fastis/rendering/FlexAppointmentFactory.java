package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.util.PercentPane;
import javafx.scene.layout.Region;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Jibbow on 8/27/17.
 */
public class FlexAppointmentFactory implements AbstractAppointmentFactory {


    @Override
    public Region createAppointment(Appointment appointment) {
        return AppointmentRenderer.createAppointment(appointment);
    }


    @Override
    public void layoutAppointments(Map<Appointment, Region> guiElements) {
        List<Appointment> sortedAppointments = guiElements.keySet().stream().sorted(
                (o1, o2) -> {
                    // order appointments: 1. ends first 2. starts first
                    int ending = -(int) Duration.between(o1.endTimeProperty(), o2.endTimeProperty()).toMinutes();
                    if (ending != 0) return ending;
                    int start = -(int) Duration.between(o1.startTimeProperty(), o2.startTimeProperty()).toMinutes();
                    return start;
                })
                .collect(Collectors.toList());

        // associates every appointment with a "column" and the number of parallel appointments
        List<Integer> columnIndex = new ArrayList<>(sortedAppointments.size());
        List<Integer> columnsParallel = new ArrayList<>(sortedAppointments.size());

        // calculate columnIndex and columnsParallel
        for (int i = 0; i < sortedAppointments.size(); i++) {
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
        for (Appointment app : sortedAppointments) {
            Region reg = guiElements.get(app);
            if (reg != null) {
                // get current values
                int colIndex = columnIndex.get(i);
                int colParallel = columnsParallel.get(i);

                // calculate horizontal position
                double left = (double) colIndex / colParallel;
                double right = 1.0 - (double) (colIndex + 1) / colParallel;

                // set position
                PercentPane.setLeftAnchor(reg, left);
                PercentPane.setRightAnchor(reg, right);
            }
            i++;
        }
    }
}
