package io.serateam.stewboo.core.services.calendar;

import io.serateam.stewboo.core.utility.ISerializable;

import java.util.ArrayList;
import java.util.List;

public class StubuCalendar implements ISerializable
{
    // FIELDS
    private String name;
    private List<StubuCalendarEntry> calendarEntryList;

    // CONSTRUCTOR
    public StubuCalendar(String name)
    {
        this.name = name;
        calendarEntryList = new ArrayList<>();
    }

    // METHODS
    public String getName()
    {
        return name;
    }

    public List<StubuCalendarEntry> getEntryList()
    {
        return calendarEntryList;
    }

    public void addEntry(StubuCalendarEntry calendarEntry)
    {
        calendarEntryList.add(calendarEntry);
    }

    public void removeEntry(StubuCalendarEntry calendarEntry)
    {
        calendarEntryList.remove(calendarEntry);
    }
}
