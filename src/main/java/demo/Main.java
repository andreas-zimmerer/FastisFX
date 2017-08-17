package demo;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.Calendar;
import com.jibbow.fastis.WeekCalendarView;
import com.jibbow.fastis.util.TimeInterval;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Jibbow on 8/11/17.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Calendar cal = new Calendar();
        Appointment app = new Appointment(new TimeInterval(LocalDateTime.now().withHour(12).minusDays(1), LocalDateTime.now().withHour(14).minusDays(1)),"Meeting");
        //root1.dayPane.addAppointment(app);
        app.intervalProperty().set(new TimeInterval(LocalDateTime.now().withHour(12), LocalDateTime.now().withHour(14)));
        app.intervalProperty().set(new TimeInterval(LocalDateTime.now().withHour(9), LocalDateTime.now().withHour(11)));
        //Appointment app2 = new Appointment(new TimeInterval(LocalDateTime.now().withHour(10), LocalDateTime.now().withHour(15)));
        //root1.dayPane.addAppointment(app2);
        Appointment app3 = new Appointment(new TimeInterval(LocalDateTime.now().withHour(16), LocalDateTime.now().withHour(18)), "Phone call");
        //root1.dayPane.addAppointment(app3);
        //Appointment appfullday = new Appointment(true, LocalDate.now());
        //root1.dayPane.addAppointment(appfullday);
        cal.addAll(app);


        WeekCalendarView root1 =  new WeekCalendarView(LocalDate.now(), cal);
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.setTitle("Fastis");
        stage.setMinWidth(100);
        stage.setMinHeight(100);
        stage.show();

cal.add(app3);
    }
}


