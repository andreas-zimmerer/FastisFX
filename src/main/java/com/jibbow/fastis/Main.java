package com.jibbow.fastis;

import com.jibbow.fastis.util.DayPane;
import com.jibbow.fastis.util.TimeInterval;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by Jibbow on 8/11/17.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DayView root1 =  new DayView();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.setTitle("Fastis");
        stage.setMinWidth(420.0);
        stage.setMinHeight(500.0);
        stage.show();

        Appointment app = new Appointment(new TimeInterval(LocalDateTime.now().withHour(12).minusDays(1), LocalDateTime.now().withHour(14).minusDays(1)));
        root1.dayPane.addAppointment(app);
        root1.setStyle("-fx-background-color: lightgray");
        app.intervalProperty().set(new TimeInterval(LocalDateTime.now().withHour(12), LocalDateTime.now().withHour(14)));
        app.intervalProperty().set(new TimeInterval(LocalDateTime.now().withHour(9), LocalDateTime.now().withHour(14)));
    }
}


