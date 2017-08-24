package com.jibbow.fastis.rendering;

import com.jibbow.fastis.Appointment;
import javafx.scene.layout.Region;
import java.util.Map;

/**
 * This class inheriting from this interface is responsible for the visual appearance of
 * appointments inside a DayView.
 * The renderer creates the node that is displayed for each appointment and also layouts
 * the node inside the DayPane.
 */
public interface AppointmentRenderer {

    /**
     * Creates a GUI element for a given appointment. This region is then used
     * for displaying the appointment and its associated information and is therefore
     * fully responsible for the visual appearance of an appointment.
     * Position and size are calculated somewhere else.
     *
     * @param appointment   The given appointment a GUI element should be created for.
     * @return              The resulting GUI element that is displayed on the calendar.
     */
    Region createAppointmentElement(Appointment appointment);


    /**
     * Layouts the nodes for each appointment.
     * The vertical position (which corresponds to the time an appointment is scheduled)
     * is calculated by the DayPane. This method is mainly used for layouting overlapping
     * appointments and creating custom layouts. Per default every appointment's region
     * is set with:
     * PercentPane.setLeftAnchor(region, 0.0);
     * PercentPane.setRightAnchor(region, 0.0);
     * This results in having appointments that stretch the full width of a DayPane.
     * These values might be overridden within this method.
     * Furthermore, the vertical position of a node can be overridden by this method.
     *
     * @param guiElements A mapping between appointments an nodes. If an appointment
     *                    is not displayed, the node might be null! The layout of the
     *                    nodes should be modified within this method.
     */
    void layoutAppointments(Map<Appointment, Region> guiElements);
}
