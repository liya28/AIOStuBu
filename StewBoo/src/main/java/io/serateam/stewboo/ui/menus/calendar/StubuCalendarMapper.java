package io.serateam.stewboo.ui.menus.calendar;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Entry;
import io.serateam.stewboo.core.services.calendar.StubuCalendarEntry;
import io.serateam.stewboo.core.services.calendar.StubuCalendar;

class StubuCalendarMapper
{
    static StubuCalendar toStubuCalendarObject(Calendar calendar)
    {
        return new StubuCalendar(calendar.getName());
    }
    static StubuCalendarEntry toStubuCalendarEntryObject(Entry<?> entry)
    {
        // Entry tips (when doing recurrence entries):
        // isRecurring() includes the recurrence source and its copies
        // isRecurrence() only includes copy of recurrences.

        return new StubuCalendarEntry(
                entry.getId(),
                entry.getTitle(),
                entry.getStartAsLocalDateTime(),
                entry.getEndAsLocalDateTime(),
                entry.getLocation(),
                entry.isFullDay(),
                entry.getMinimumDuration(),
                entry.getRecurrenceRule()
        );
    }
}
