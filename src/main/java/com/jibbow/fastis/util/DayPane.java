package com.jibbow.fastis.util;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.AppointmentsRenderFactory;
import javafx.beans.property.*;
import javafx.scene.layout.Pane;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Created by Jibbow on 8/11/17.
 */
public class DayPane extends PercentPane {
    LocalDate dayDate;
    ObjectProperty<LocalTime> dayStartTime;
    ObjectProperty<LocalTime> dayEndTime;
    Map<Appointment, Pane> appointments;

    Function<Appointment, Pane> appointmentRenderer = new AppointmentsRenderFactory()::renderDayVew;

    public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime) {
        this.dayDate = date;
        this.dayStartTime = new SimpleObjectProperty<>(dayStartTime);
        this.dayEndTime = new SimpleObjectProperty<>(dayEndTime);

        this.appointments = new HashMap<>();

        this.dayStartTime.addListener(observable -> appointments.forEach(this::layoutAppointment));
        this.dayEndTime.addListener(observable -> appointments.forEach(this::layoutAppointment));
    }

    public void addAppointment(Appointment appointment) {
        Pane rendering = appointmentRenderer.apply(appointment);
        appointments.put(appointment, rendering);
        layoutAppointment(appointment, rendering);
        this.getChildren().add(rendering);

        appointment.startDateTimeProperty().addListener(observable -> layoutAppointment(appointment, rendering));
        appointment.endDateTimeProperty().addListener(observable -> layoutAppointment(appointment, rendering));
    }

    private void layoutAppointment(Appointment appointment, Pane pane) {
        long minutesPerDay = Duration.between(dayStartTime.get(), dayEndTime.get()).toMinutes();

        if(appointment.startDateTimeProperty().get().isBefore(LocalDateTime.of(dayDate, dayStartTime.get()))) {
            PercentPane.setTopAnchor(pane, 0.0);
        } else {
            PercentPane.setTopAnchor(pane,
                    (double)Duration.between(dayStartTime.get(), appointment.startDateTimeProperty().get().toLocalTime()).toMinutes()
                            / minutesPerDay);
        }
        if(appointment.endDateTimeProperty().get().isAfter(LocalDateTime.of(dayDate, dayEndTime.get()))) {
            PercentPane.setBottomAnchor(pane, 0.0);
        } else {
            PercentPane.setBottomAnchor(pane,
                    (double)Duration.between(appointment.endDateTimeProperty().get().toLocalTime(), dayEndTime.get()).toMinutes()
                            / minutesPerDay);
        }
    }
}
