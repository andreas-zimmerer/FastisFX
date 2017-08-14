package com.jibbow.fastis;

import com.jibbow.fastis.util.TimeInterval;
import javafx.collections.FXCollections;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jibbow on 8/13/17.
 */
public class Calendar extends ModifiableObservableListBase<Appointment> {

    ObservableList<Appointment> appointments = FXCollections.observableList(new LinkedList<>());


    public List<Appointment> getAppointmentsFor(LocalDate date) {
        return getAppointmentsBetween(new TimeInterval(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX)));
    }

    public List<Appointment> getAppointmentsBetween(TimeInterval interval) {
        return new FilteredList<Appointment>(appointments, appointment -> appointment.intervalProperty().get().overlaps(interval));
    }



    @Override
    public Appointment get(int index) {
        return appointments.get(index);
    }

    @Override
    public int size() {
        return appointments.size();
    }

    @Override
    protected void doAdd(int index, Appointment element) {
        appointments.add(index, element);
    }

    @Override
    protected Appointment doSet(int index, Appointment element) {
        return appointments.set(index, element);
    }

    @Override
    protected Appointment doRemove(int index) {
        return appointments.remove(index);
    }
}
