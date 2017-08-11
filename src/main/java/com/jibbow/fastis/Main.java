package com.jibbow.fastis;

import com.jibbow.fastis.util.DayPane;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Parent;
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
        DayPane root1 =  new DayPane(LocalDate.now(), LocalTime.MIN, LocalTime.MAX);
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.setTitle("Fastis");
        stage.setMinWidth(420.0);
        stage.setMinHeight(500.0);
        stage.show();

        root1.addAppointment(new Appointment(LocalDateTime.now().withHour(12), LocalDateTime.now().withHour(14)));
        root1.setStyle("-fx-background-color: green");
    }
}


