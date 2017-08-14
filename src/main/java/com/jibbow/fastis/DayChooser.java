package com.jibbow.fastis;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Created by Jibbow on 8/14/17.
 */
public class DayChooser extends VBox {

    ObjectProperty<LocalDate> selectedDateProperty;

    public DayChooser(ObjectProperty<LocalDate> selectedDate) {
        this.getStylesheets().add(DayChooser.class.getClassLoader().getResource("css/DayChooser.css").toString());
        this.selectedDateProperty = selectedDate;

        BorderPane header = new BorderPane();
        Button btnMonthLeft = new Button("<");
        Button btnMonthRight = new Button(">");
        header.setLeft(btnMonthLeft);
        header.setRight(btnMonthRight);
        this.getChildren().add(header);


        GridPane monthPane = new GridPane();
        monthPane.getStyleClass().add("daychooser-monthgrid");
        VBox.setVgrow(monthPane, Priority.ALWAYS);
        populateMonthPane(monthPane);
        this.getChildren().add(monthPane);

        selectedDateProperty.addListener(observable -> populateMonthPane(monthPane));
    }

    private void populateMonthPane(GridPane monthPane) {
        monthPane.getChildren().clear();

        int currentRow = 0;
        for(LocalDate d = selectedDateProperty.get().withDayOfMonth(1);
                d.getMonthValue() == selectedDateProperty.get().getMonthValue();
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
