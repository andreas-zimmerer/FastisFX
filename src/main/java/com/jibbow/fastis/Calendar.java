package com.jibbow.fastis;

import com.jibbow.fastis.util.TimeInterval;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Jibbow on 8/13/17.
 */
public class Calendar {
    public List<Appointment> getAppointmentsFor(LocalDate date) {
        throw new NotImplementedException();
    }

    public List<Appointment> getAppointmentsBetween(TimeInterval interval) {
        throw new NotImplementedException();
    }
}
