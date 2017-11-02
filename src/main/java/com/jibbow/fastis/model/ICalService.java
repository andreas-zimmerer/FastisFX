package com.jibbow.fastis.model;


import java.net.URL;
import java.util.List;

/**
 * Created by Jibbow on 10/15/17.
 */
public class ICalService {
    private final URL webURL;

    public ICalService(URL url) {
        this.webURL = url;
    }

    /**
     * Update and returns a list of available calendars in the iCal file.
     * If a calendar is not available in the file any more this calendar gets abandoned.
     * If there is a new calendar available in the file it is added to the list.
     * @return
     */
    public List<Object> getCalendars() {

    }

    /**
     * Manually trigger an update for a specific calendar behind the URL.
     * @param calendar The calendar that should be updated.
     */
    public void updateCalendar(Object calendar) {

    }
}
