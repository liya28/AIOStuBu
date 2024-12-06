package io.serateam.stewboo.core.services.calendar;

import java.util.ArrayList;
import java.util.List;

public class StubuCalendarList
{
    // FIELDS
    private static List<StubuCalendar> calendars;
    private static StubuCalendarList instance;

    // CONSTRUCTORS & INITIALIZERS
    private StubuCalendarList() {}

    public static StubuCalendarList getInstance()
    {
        if(instance == null)
        {
            instance = new StubuCalendarList();
            calendars = new ArrayList<>();
        }
        return instance;
    }

    // METHODS
    public List<StubuCalendar> getCalendars()
    {
        return calendars;
    }

    public void addCalendar(StubuCalendar calendar)
    {
        calendars.add(calendar);
    }

    public void removeCalendar(StubuCalendar calendar)
    {
        calendars.remove(calendar);
    }
}
