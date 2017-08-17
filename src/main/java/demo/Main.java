package demo;

import com.jibbow.fastis.Appointment;
import com.jibbow.fastis.Calendar;
import com.jibbow.fastis.CalendarView;
import com.jibbow.fastis.WeekCalendarView;
import com.jibbow.fastis.util.TimeInterval;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.function.Consumer;

/**
 * Created by Jibbow on 8/11/17.
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        WeekCalendarView root1 =  new WeekCalendarView(LocalDate.now(), new Calendar());
        Stage stage = new Stage();
        stage.setScene(new Scene(root1));
        stage.setTitle("Fastis");
        stage.setMinWidth(100);
        stage.setMinHeight(100);
        stage.show();

        testProgram(root1);
    }


    private void testProgram(CalendarView calendarView) {
        System.out.println("clearing all calendars");
        calendarView.getCalendars().clear();

        System.out.println("adding a new calendar with two appointments");
        Appointment app1 = new Appointment(new TimeInterval(LocalDateTime.now(), LocalDateTime.now().plusHours(2)), "Appointment1");
        Appointment app2 = new Appointment(new TimeInterval(LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(5)), "Appointment2");
        Calendar cal1 = new Calendar(app1, app2);
        calendarView.getCalendars().add(cal1);

        System.out.println("adding a third appointment to this calendar");
        Appointment app3 = new Appointment(new TimeInterval(LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(6)), "Appointment3");
        cal1.add(app3);

        System.out.println("removing the first appointment");
        cal1.remove(app1);

        System.out.println("adding a new empty calendar");
        Calendar cal2 = new Calendar();
        calendarView.getCalendars().add(cal2);

        System.out.println("adding the first appointment again to the second calendar");
        cal2.add(app1);

        System.out.println("adding the second appointment also to the second calendar");
        cal2.add(app2);

        System.out.println("moving the second appointment 4 hours into the past");
        app2.intervalProperty().set(new TimeInterval(LocalDateTime.now().minusHours(4), LocalDateTime.now().minusHours(3)));

        // doesn't work yet
        System.out.println("moving the first appointment to the next day");
        app1.intervalProperty().set(new TimeInterval(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(2).plusDays(1)));
    }



    /*private class MethodTask extends Task<Void> {
        Queue<Runnable> function;
        int sleep;
        public MethodTask(int sleep, Runnable... function) {
            for(Runnable r : function) {
                this.function.add(r);
            }
            this.sleep = sleep;

            this.setOnSucceeded(event -> function.run());
        }

        public void start() {
            new Thread(this).start();
        }

        @Override
        protected Void call() throws Exception {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
            }
            return null;
        }
    }*/
}


