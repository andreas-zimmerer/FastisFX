package com.jibbow.fastis.components;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.rendering.AbstractAppointmentFactory;
import com.jibbow.fastis.rendering.AppointmentRenderer;
import com.jibbow.fastis.rendering.FlexAppointmentFactory;
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
 * A DayPane is a PercentPane that adds the functionality of automatically adding {@link Appointment} to it.
 * It is used in {@link com.jibbow.fastis.CalendarView} where a full day is shown.
 * A DayPane is associated with one date and only appointments that (partly) occur on this date are shown.
 * If the Interval of an added appointment changes, the DayPane will be updated.
 * Furthermore, a DayPane has a dayStartTime and a dayEndTime. Both specify the interval where appointments
 * are being displayed. This is also used for layouting the appointments to the right vertical position.
 * The DayPane takes care of displaying the right appointments and positioning the appointments vertically
 * so that they resemble the time when the appointment occurs.
 * A DayPane is strongly chained to a {@link AppointmentRenderer} which can be set in the constructor.
 * The {@link AppointmentRenderer} is responsible for creating nodes for each appointment and for layouting them.
 *
 * Generally, a DayPane is a typical view that displays exactly one day with its appointments. The appointments
 * are placed and scaled according the their interval property.
 */
public class DayPane extends PercentPane {
    private final LocalDate dayDate;
    private final ObjectProperty<LocalTime> dayStartTimeProperty;
    private final ObjectProperty<LocalTime> dayEndTimeProperty;
    private final Map<Appointment, Region> appointments;
    private final AbstractAppointmentFactory renderer;


    /**
     * Creates a new DayPane with associated with the given date. The DayPane covers the full day and
     * uses the default renderer.
     *
     * @param date The date associated with this DayPane. Only appointments for this date will be displayed.
     */
    public DayPane(LocalDate date) {
        this(date, LocalTime.MIN, LocalTime.MAX);
    }

    /**
     * Creates a new DayPane associated with the given date. The DayPane covers the interval between
     * dayStartTime and dayEndTime. Only appointments within this interval at this date are displayed.
     *
     * @param date          The date associated with this DayPane.
     * @param dayStartTime  The start value of the time interval being displayed
     * @param dayEndTime    The end value of the time interval being displayed.
     */
    public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime) {
        this(date, dayStartTime, dayEndTime, new FlexAppointmentFactory());
    }

    /**
     * Creates a new DayPane associated with the given date. The DayPane covers the interval between
     * dayStartTime and dayEndTime. Only appointments within this interval at this date are displayed.
     * Additionally a custom renderer can be specified which takes care of the visual appearance of
     * Appointments.
     *
     * @param date          The date associated with this DayPane.
     * @param dayStartTime  The start value of the time interval being displayed.
     * @param dayEndTime    The end value of the time interval being displayed.
     * @param renderer      A custom renderer that is used for displaying appointments.
     */
    public DayPane(LocalDate date, LocalTime dayStartTime, LocalTime dayEndTime, AbstractAppointmentFactory renderer) {
        this.dayDate = date;
        this.dayStartTimeProperty = new SimpleObjectProperty<>(dayStartTime);
        this.dayEndTimeProperty = new SimpleObjectProperty<>(dayEndTime);
        this.renderer = renderer;

        this.setPrefHeight(Duration.between(dayStartTime, dayEndTime).toMinutes() / 2);

        this.appointments = new HashMap<>();

        this.dayStartTimeProperty().addListener(observable -> {
            appointments.keySet().forEach(appointment -> this.getChildren().add(this.createGuiElement(appointment)));
            renderer.layoutAppointments(appointments);
        });
        this.dayEndTimeProperty().addListener(observable -> {
            appointments.keySet().forEach(appointment -> this.getChildren().add(this.createGuiElement(appointment)));
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
        Region p = createGuiElement(appointment);
        this.getChildren().add(p);
        appointments.put(appointment, p);
        renderer.layoutAppointments(appointments);

        appointment.intervalProperty().addListener(observable -> {
            appointments.put(appointment, createGuiElement(appointment));
            renderer.layoutAppointments(appointments);
        });
    }


    /**
     * Removes an appointment from the DayView so that it will not be rendered.
     *
     * @param appointment Appointment to be removed.
     */
    public void removeAppointment(Appointment appointment) {
        Region region = appointments.get(appointment);
        appointments.remove(appointment);
        if(region != null) {
            this.getChildren().remove(region);
            renderer.layoutAppointments(appointments);
        }
    }


    /**
     * Creates a gui element for an appointment with an associated {@link AppointmentRenderer}.
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
    protected Region createGuiElement(Appointment appointment) {
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
            region = renderer.createAppointment(appointment);

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

            PercentPane.setLeftAnchor(region, 0.0);
            PercentPane.setRightAnchor(region, 0.0);
            return region;
        } else {
            if(region != null) {
                // there is an existing gui element although is is not displayed -> remove it
                this.getChildren().remove(region);
            }
            return null;
        }
    }

    /**
     * Returns the date the DayPane is associated with.
     * @return  The associated date.
     */
    public LocalDate getDayDate() {
        return dayDate;
    }

    /**
     * Returns the start value of the displayed interval.
     * @return The start value of the displayed interval.
     */
    public ObjectProperty<LocalTime> dayStartTimeProperty() {
        return dayStartTimeProperty;
    }

    /**
     * Returns the end value of the displayed interval.
     * @return The end value of the displayed interval.
     */
    public ObjectProperty<LocalTime> dayEndTimeProperty() {
        return dayEndTimeProperty;
    }
}
