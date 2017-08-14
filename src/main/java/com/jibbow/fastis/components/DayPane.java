package com.jibbow.fastis.components;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.rendering.DayPaneRenderer;
import com.jibbow.fastis.util.PercentPane;
import com.jibbow.fastis.util.TimeInterval;
import javafx.beans.property.*;
import javafx.scene.layout.Region;

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
    private LocalDate dayDate;
    private ObjectProperty<LocalTime> dayStartTimeProperty;
    private ObjectProperty<LocalTime> dayEndTimeProperty;
    private Map<Appointment, Region> appointments;
    private DayPaneRenderer renderer;


    public DayPane(LocalDate date) {
        this(date, LocalTime.MIN, LocalTime.MAX);
    }

    public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime) {
        this(date, dayStartTime, dayEndTime, new DayPaneRenderer());
    }

    public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime, DayPaneRenderer renderer) {
        this.dayDate = date;
        this.dayStartTimeProperty = new SimpleObjectProperty<>(dayStartTime);
        this.dayEndTimeProperty = new SimpleObjectProperty<>(dayEndTime);
        this.renderer = renderer;

        this.setPrefHeight(Duration.between(dayStartTime, dayEndTime).toMinutes() / 2);

        this.appointments = new HashMap<>();

        this.dayStartTimeProperty().addListener(observable -> {
            appointments.keySet().forEach(this::addGuiElement);
            renderer.layoutAppointments(appointments);
        });
        this.dayEndTimeProperty().addListener(observable -> {
            appointments.keySet().forEach(this::addGuiElement);
            renderer.layoutAppointments(appointments);
        });
    }


    /**
     * Adds a new appointment to the DayPane.
     * Calculates the correct position and layout and changes them if the appointment's interval
     * is updated.
     *
     * @param appointment The appointment that should be added to the DayPane.
     */
    public void addAppointment(Appointment appointment) {
        Region p = addGuiElement(appointment);
        appointments.put(appointment, p);
        renderer.layoutAppointments(appointments);

        appointment.intervalProperty().addListener(observable -> {
            appointments.put(appointment, addGuiElement(appointment));
            renderer.layoutAppointments(appointments);
        });
    }


    public void removeAppointment(Appointment appointment) {
        Region region = appointments.get(appointment);
        if(region != null) {
            this.getChildren().remove(region);
        }
        appointments.remove(appointment);
    }


    /**
     * Creates a gui element for an appointment with an associated {@link DayPaneRenderer}.
     * The gui element is automatically added to (or removed from) the DayPane.
     * According to the displayed time interval of the DayPane the relative vertical position
     * of the gui element is set. If the appointments is outside the displayed time interval no
     * gui element will be created or - if there has already been a gui element but the
     * appointment's time or the DayPanes time constraints has been changed - destroyed and
     * removed from the DayPane if this method is invoked after the change.
     *
     * @param appointment The appointment to which a gui element will be created and added to the
     *                    DayPane.
     *
     * @return  Returns null if no element has been created or if the element has been destroyed,
     *          both because the appointments datetime is outside of the time constraints of the
     *          DayPane. Otherwise the new gui element is returned.
     */
    protected Region addGuiElement(Appointment appointment) {
        // check if there is an existing gui element
        Region region = appointments.getOrDefault(appointment, null);

        // check if the appointment should be displayed
        if(appointment.intervalProperty().get().overlaps(
                new TimeInterval(
                        LocalDateTime.of(getDayDate(), dayStartTimeProperty().get()),
                        LocalDateTime.of(getDayDate(), dayEndTimeProperty().get())))) {

            if(region != null) {
                this.getChildren().remove(region);
            }
            region = renderer.createGuiElement(appointment);

            // calculate minutes per day displayed; used for calculating the percentage
            long minutesPerDay = Duration.between(dayStartTimeProperty().get(), dayEndTimeProperty().get()).toMinutes();

            if(appointment.intervalProperty().get().startsBefore(LocalDateTime.of(getDayDate(), dayStartTimeProperty().get()))) {
                PercentPane.setTopAnchor(region, 0.0);
            } else {
                PercentPane.setTopAnchor(region,
                        (double)TimeInterval.between(dayStartTimeProperty().get(), appointment.startTimeProperty()).getDuration().abs().toMinutes()
                                / minutesPerDay);
            }
            if(appointment.intervalProperty().get().endsAfter(LocalDateTime.of(getDayDate(), dayEndTimeProperty().get()))) {
                PercentPane.setBottomAnchor(region, 0.0);
            } else {
                PercentPane.setBottomAnchor(region,
                        (double)TimeInterval.between(dayEndTimeProperty().get(), appointment.endTimeProperty()).getDuration().abs().toMinutes()
                                / minutesPerDay);
            }

            this.getChildren().add(region);
            return region;
        } else {
            if(region != null) {
                // there is an existing gui element although is is not displayed -> remove it
                this.getChildren().remove(region);
            }
            return null;
        }
    }

    public LocalDate getDayDate() {
        return dayDate;
    }

    public ObjectProperty<LocalTime> dayStartTimeProperty() {
        return dayStartTimeProperty;
    }

    public ObjectProperty<LocalTime> dayEndTimeProperty() {
        return dayEndTimeProperty;
    }
}