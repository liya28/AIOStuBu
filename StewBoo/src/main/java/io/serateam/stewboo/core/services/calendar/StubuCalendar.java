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

    public void removeEntryById(String entryId)
    {
        if (entryId == null)
        {
            System.err.println("Calendar: Cannot remove entry. Entry ID is null");
            return;
        }

        StubuCalendarEntry entryToRemove = null;

        // Find the matching entry
        for (StubuCalendarEntry calendarEntry : calendarEntryList)
        {
            if (entryId.equals(calendarEntry.getId()))
            {
                System.out.println("Calendar: Calendar entry found! " + calendarEntry.getId());
                entryToRemove = calendarEntry;
                break;
            }
        }

        // Remove the entry if found
        if (entryToRemove != null)
        {
            if(calendarEntryList.remove(entryToRemove))
            {
                System.out.println("Calendar: Entry removed successfully!");
            }
        }
        else
        {
            System.err.println("Calendar: Cannot remove entry. Entry not found.");
        }
    }
}
