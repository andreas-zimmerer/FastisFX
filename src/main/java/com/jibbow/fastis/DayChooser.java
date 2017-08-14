package com.jibbow.fastis;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

/**
 * Created by Jibbow on 8/14/17.
 */
public class DayChooser extends VBox {

    protected ObjectProperty<LocalDate> selectedDateProperty;
    protected ObjectProperty<Month> displayedMonth;
    protected int displayedYear;


    /**
     * A control for choosing between dates. A month calendar is displayed and the user
     * can click on a date. Easy to use.
     *
     *    --------------------
     *    | <     Month    > |
     *    --------------------
     *    |    1  3  4  5  6 |
     *    | 7  8  9 10 11 12 |
     *    |        ...       |
     *    --------------------
     *
     * @param selectedDate A date property which gets adjusted when the user selects a new date.
     */
    public DayChooser(ObjectProperty<LocalDate> selectedDate) {
        this.getStylesheets().add(DayChooser.class.getClassLoader().getResource("css/DayChooser.css").toString());
        this.selectedDateProperty = selectedDate;
        this.displayedMonth = new SimpleObjectProperty<>(selectedDateProperty.get().getMonth());
        this.displayedYear = selectedDateProperty.get().getYear();

        // Add a header to the DayChooser for switching between months
        final BorderPane header = new BorderPane();
        final Button btnMonthLeft = new Button("<");
        final Button btnMonthRight = new Button(">");
        header.setLeft(btnMonthLeft);
        header.setRight(btnMonthRight);
        final Label lblMonth = new Label(selectedDate.get().getMonth().name());
        lblMonth.getStyleClass().add("daychooser-label-month");
        // set to current month when label is clicked
        lblMonth.setOnMouseClicked(event -> {
            displayedYear = selectedDateProperty.get().getYear();
            displayedMonth.set(selectedDateProperty.get().getMonth());
        });
        header.setCenter(lblMonth);
        this.getChildren().add(header);

        // create a GridPane that holds every single day
        final GridPane monthPane = new GridPane();
        monthPane.getStyleClass().add("daychooser-monthgrid");
        VBox.setVgrow(monthPane, Priority.ALWAYS);
        populateMonthPane(monthPane, displayedMonth.get(), displayedYear);
        this.getChildren().add(monthPane);

        // highlight the current date when selectedDate is changed and display the correct month
        selectedDateProperty.addListener(observable -> {
            displayedMonth.set(selectedDateProperty.get().getMonth());
            displayedYear = selectedDateProperty.get().getYear();
            populateMonthPane(monthPane, displayedMonth.get(), displayedYear);
            lblMonth.setText(displayedMonth.get().name());
        });

        // update displayed month when switching
        displayedMonth.addListener(observable -> {
            lblMonth.setText(displayedMonth.get().name());
            populateMonthPane(monthPane, displayedMonth.get(), displayedYear);
        });

        // switch between month with left and right buttons
        btnMonthLeft.setOnAction(event -> {
            if(displayedMonth.get() == Month.JANUARY) {
                displayedYear--;
            }
            displayedMonth.set(displayedMonth.get().minus(1));
        });
        btnMonthRight.setOnAction(event -> {
            if(displayedMonth.get() == Month.DECEMBER) {
                displayedYear++;
            }
            displayedMonth.set(displayedMonth.get().plus(1));
        });
    }

    /**
     * Creates and positions every node onto the GridPane for the given month and year.
     * Uses 7 (weekdays) columns and (max.) 6 rows (weeks). The rows and columns are created on fly or
     * are reused.
     *
     * @param monthPane The GradPane that is used for populating the DayNodes.
     * @param month The month that should be displayed.
     * @param year The year that should be displayed.
     */
    private void populateMonthPane(GridPane monthPane, Month month, int year) {
        monthPane.getChildren().clear();

        int currentRow = 0;
        for(LocalDate d = LocalDate.of(year, month, 1);
                d.getMonthValue() == month.getValue();
                d = d.plusDays(1)) {
            Node dayNode = renderDayItem(d);
            final LocalDate currentDate = d;
            dayNode.setOnMouseClicked(event -> selectedDateProperty.set(currentDate));

            int column = d.getDayOfWeek().getValue();
            monthPane.add(dayNode, column, currentRow);

            if(column == 7) {
                currentRow++;
            }
        }
    }

    /**
     * Creates a node for a given day. Styling should be done via CSS, but
     * appropriate CSS classes are assigned to each node.
     * @param date A given date the node is associated with.
     * @return A node that displays the day of month.
     */
    private Node renderDayItem(LocalDate date) {
        Label lblDate = new Label(""+date.getDayOfMonth());
        lblDate.getStyleClass().add("daychooser-day");
        if(date.getDayOfWeek() == DayOfWeek.SATURDAY
                || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            lblDate.getStyleClass().add("daychooser-weekend");
        } else {
            lblDate.getStyleClass().add("daychooser-weekday");
        }
        if(date.equals(selectedDateProperty.get())) {
            lblDate.getStyleClass().add("daychooser-selected-day");
        }

        return lblDate;
    }

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDateProperty;
    }
}
