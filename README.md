This software is licensed under GNU GPL v3.0  
See LICENSE.txt for further information.

# Fastis
Fastis (Latin for "calendar") is a fully featured calendar control for JavaFX. It is _still under heavy development_ and _APIs may change_.  
If you like the project please leave a ⭐! 🎉🎉

It provides a `WeekView` and a `DayView`. A `MonthView` will be added in the near future.  
All styling is done via CSS and the visual appearance and layout can be fully adjusted with custom renderers.

## Usage

### Custom renderer
Most GUI components can be customized by using a custom renderer which overrides the default render methods.
Even the layout of overlapping appointments can be completely customized.
```java
public class CustomRenderer extends WeekViewRenderer {
    @Override
    public Region createAppointmentElement(Appointment appointment) { ... }
    
    @Override
    public void layoutAppointments(Map<Appointment, Region> guiElements) { ... }
    
    @Override
    public Node createAllDayPane(List<Appointment> appointments) { ... }
    
    @Override
    public Node createHeaderPane(WeekView calView) { ... }
    
    @Override
    public Node createDayBackground(LocalDate date) { ... }
    
    @Override
    public Node createSingleDayHeader(LocalDate date) { ... }
}
```

The `CustomRenderer` can now be plugged into a new `WeekView` when instantiating it.

## Screenshots

### DayView
![DayView](screenshots/DayView.jpg)

### WeekView
![WeekView](screenshots/WeekView.jpg)

Both `DayView` and `WeekView` support different layout styles for overlapping appointments (e.g. `FLEX` and `STACKING`).  
Here is an example for the `FLEX` layout style:

![FLEX layout style](screenshots/layout_flex.jpg)