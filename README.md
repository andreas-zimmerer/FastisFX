This software is licensed under GNU GPL v3.0  
See LICENSE.txt for further information.

# Fastis
Fastis (Latin for "calendar") is a fully featured calendar control for JavaFX. It is _still under heavy development_ and _APIs may change_.  
If you like the project please leave a ⭐! 🎉🎉

It provides a `WeekView` and a `DayView`. A `MonthView` will be added in the near future.  
All styling is done via CSS and the visual appearance and layout can be fully adjusted with custom renderers.

## Usage

### Custom GUI components
Most GUI components can be customized by using a custom factories.
Even the layout of overlapping appointments can be completely customized.

For styling and layouting appointments an abstract factory exists called `AbstractAppointmentFactory`.
There are also two default concrete implementations: 
 - `FlexAppointmentFactory` layouts overlapping appointments in columns.  
   Here is an example of the `FLEX` layout:  
   ![FLEX layout style](screenshots/layout_flex.jpg)
   
   
 - `StackingAppointmentFactory` layouts overlapping appointments above each other (but 'stacked')
 
They both use the same `createAppointment(...)` implementation and therefore have the same style. But they differ
in how overlapping appointments are layouted.  
You can also implement your own `AppointmentFactory`. The default style implementation can be found in `AppointmentRenderer`.

## Screenshots

### DayView
![DayView](screenshots/DayView.jpg)

### WeekView
![WeekView](screenshots/WeekView.jpg)

