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

    public StubuCalendarEntry findEntryById(String entryId)
    {
        for (StubuCalendarEntry calendarEntry : calendarEntryList)
        {
            if (entryId.equals(calendarEntry.getId()))
            {
                System.out.println("Calendar: Calendar entry found! " + calendarEntry.getId());
                return calendarEntry;
            }
        }
        return null;
    }

    public void removeEntryById(String entryId)
    {
        if (entryId == null)
        {
            System.err.println("Calendar: Cannot remove entry. Entry ID is null");
            return;
        }

        // ConcurrentModificationException may be thrown when we manually iterate
        // over a collection and removing them at the same time.
        // Thus, we will opt to the collect-then-remove strategy.
        List<StubuCalendarEntry> entriesToRemove = new ArrayList<>();

        for (StubuCalendarEntry calendarEntry : calendarEntryList)
        {
            if (entryId.equals(calendarEntry.getId()))
            {
                System.out.println("Calendar: Calendar entry found! " + calendarEntry.getId());
                entriesToRemove.add(calendarEntry);
            }
        }

        // Remove collected entries
        calendarEntryList.removeAll(entriesToRemove);
        System.out.println("Calendar: All matching entries removed successfully!");
    }
}
