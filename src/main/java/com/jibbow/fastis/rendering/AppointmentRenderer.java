package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import javafx.scene.layout.Region;
import java.util.Map;

/**
 * Created by Jibbow on 8/12/17.
 */
public interface AppointmentRenderer {

    Region createAppointmentElement(Appointment appointment);

    void layoutAppointments(Map<Appointment, Region> guiElements);
}
