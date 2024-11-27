package io.serateam.stewboo.core.services.calendar;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;

import java.util.ArrayList;
import java.util.List;

public class CalendarEvents
{
    List<Entry<?>> eventEntries = new ArrayList<>();

    void addEntry(Entry<?> entry)
    {
        eventEntries.add(entry);
    }
    void removeEntry(Entry<?> entry)
    {
        eventEntries.remove(entry);
    }


}
