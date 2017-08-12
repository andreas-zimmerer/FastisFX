package com.jibbow.fastis.util;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.rendering.DayPaneRenderer;
import javafx.beans.property.*;
import javafx.scene.layout.Pane;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jibbow on 8/11/17.
 */
public class DayPane extends PercentPane {
    LocalDate dayDate;
    ObjectProperty<LocalTime> dayStartTime;
    ObjectProperty<LocalTime> dayEndTime;
    Map<Appointment, Pane> appointments;
    DayPaneRenderer renderer = new DayPaneRenderer();


    public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime) {
        this.dayDate = date;
        this.dayStartTime = new SimpleObjectProperty<>(dayStartTime);
        this.dayEndTime = new SimpleObjectProperty<>(dayEndTime);

        this.appointments = new HashMap<>();

        this.dayStartTime.addListener(observable -> {
            appointments.keySet().forEach(this::addGuiElement);
            renderer.layoutAppointments(appointments);
        });
        this.dayEndTime.addListener(observable -> {
            appointments.keySet().forEach(this::addGuiElement);
            renderer.layoutAppointments(appointments);
        });
    }

    public void addAppointment(Appointment appointment) {
        appointments.put(appointment, addGuiElement(appointment));
        appointment.intervalProperty().addListener(observable -> {
            appointments.put(appointment, addGuiElement(appointment));
            renderer.layoutAppointments(appointments);
        });
    }

    private Pane addGuiElement(Appointment appointment) {
        Pane pane = null;
        if(appointment.intervalProperty().get().overlaps(
                new TimeInterval(
                        LocalDateTime.of(dayDate, dayStartTime.get()),
                        LocalDateTime.of(dayDate, dayEndTime.get())))) {
            pane = renderer.createGuiElement(appointment);

            // calculate minutes per day displayed; used for calculating the percentage
            long minutesPerDay = Duration.between(dayStartTime.get(), dayEndTime.get()).toMinutes();

            if(appointment.intervalProperty().get().startsBefore(LocalDateTime.of(dayDate, dayStartTime.get()))) {
                PercentPane.setTopAnchor(pane, 0.0);
            } else {
                PercentPane.setTopAnchor(pane,
                        (double)TimeInterval.between(dayStartTime.get(), appointment.startTimeProperty()).getDuration().abs().toMinutes()
                                / minutesPerDay);
            }
            if(appointment.intervalProperty().get().endsAfter(LocalDateTime.of(dayDate, dayEndTime.get()))) {
                PercentPane.setBottomAnchor(pane, 0.0);
            } else {
                PercentPane.setBottomAnchor(pane,
                        (double)TimeInterval.between(dayEndTime.get(), appointment.endTimeProperty()).getDuration().abs().toMinutes()
                                / minutesPerDay);
            }

            this.getChildren().add(pane);
        }
        return pane;
    }
}
