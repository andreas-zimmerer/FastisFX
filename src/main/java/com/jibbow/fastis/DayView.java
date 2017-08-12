package com.jibbow.fastis;

import com.jibbow.fastis.util.DayPane;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;

/**
 * Created by Jibbow on 8/12/17.
 */
public class DayView extends BorderPane {

    DayPane dayPane;

    public DayView() {
        this.getStylesheets().add(DayView.class.getClassLoader().getResource("css/DayView.css").toString());

        this.dayPane = new DayPane(LocalDate.now());
        this.setCenter(dayPane);
    }
}
