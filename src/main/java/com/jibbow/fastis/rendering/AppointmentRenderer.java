package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import javafx.scene.layout.Pane;

import java.util.Map;

/**
 * Created by Jibbow on 8/12/17.
 */
public interface AppointmentRenderer {

    Pane createGuiElement(Appointment appointment);

    void layoutAppointments(Map<Appointment, Pane> guiElements);
}
