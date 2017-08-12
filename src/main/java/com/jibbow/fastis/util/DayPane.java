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

    public enum OverlapStyle {
        Lanes,
        Stacking
    }

    public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime) {
        this.dayDate = date;
        this.dayStartTime = new SimpleObjectProperty<>(dayStartTime);
        this.dayEndTime = new SimpleObjectProperty<>(dayEndTime);

        this.appointments = new HashMap<>();

        this.dayStartTime.addListener(observable -> appointments.keySet().forEach(this::layoutAppointment));
        this.dayEndTime.addListener(observable -> appointments.keySet().forEach(this::layoutAppointment));
    }

    public void addAppointment(Appointment appointment) {
        appointments.put(appointment, createGuiElement(appointment));
        appointment.intervalProperty().addListener(observable -> {
            appointments.put(appointment, createGuiElement(appointment));
            layoutAppointment(appointment);
        });
    }

    private Pane createGuiElement(Appointment appointment) {
        Pane rendering = null;
        if(appointment.intervalProperty().get().overlaps(
                new TimeInterval(
                        LocalDateTime.of(dayDate, dayStartTime.get()),
                        LocalDateTime.of(dayDate, dayEndTime.get())))) {
            rendering = appointmentRenderer.apply(appointment);
            layoutAppointment(appointment);
            this.getChildren().add(rendering);
        }
        return rendering;
    }

    private void layoutAppointment(Appointment appointment) {
        Pane pane = appointments.get(appointment);
        if(pane == null) { return; } // no pane is associated with this appointment so nothing has to be done

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

        // implement overlapping style
        PercentPane.setLeftAnchor(pane, 0.0);
        PercentPane.setRightAnchor(pane, 0.0);
    }
}
