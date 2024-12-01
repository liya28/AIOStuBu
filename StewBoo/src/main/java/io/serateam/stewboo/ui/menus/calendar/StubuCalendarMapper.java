package io.serateam.stewboo.ui.menus.calendar;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import io.serateam.stewboo.core.services.calendar.StubuCalendarEntry;
import io.serateam.stewboo.core.services.calendar.StubuCalendar;

public class StubuCalendarMapper
{
    public static StubuCalendar toStubuCalendarObject(Calendar calendar)
    {
        return new StubuCalendar(calendar.getName());
    }
    public static StubuCalendarEntry toStubuCalendarEntryObject(Entry<?> entry)
    {
        return new StubuCalendarEntry(
                entry.getId(),
                entry.getTitle(),
                entry.getStartAsLocalDateTime(),
                entry.getEndAsLocalDateTime(),
                entry.getLocation(),
                entry.isFullDay(),
                entry.getRecurrenceRule()
        );
    }
}
