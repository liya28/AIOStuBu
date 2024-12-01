package io.serateam.stewboo.core.services.calendar;

import java.time.LocalDateTime;

public class StubuCalendarEntry
{
    // FIELDS
    private String id;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String location;
    private boolean fullDay;
    private String recurrenceRule;

    // CONSTRUCTOR
    public StubuCalendarEntry(String id, String title,
                              LocalDateTime startAsLocalDateTime, LocalDateTime endAsLocalDateTime,
                              String location, boolean fullDay, String recurrenceRule)
    {
        this.id = id;
        this.title = title;
        this.startDate = startAsLocalDateTime;
        this.endDate = endAsLocalDateTime;
        this.location = location;
        this.fullDay = fullDay;
        this.recurrenceRule = recurrenceRule;
    }

    // METHODS
    public String getId()
    {
        return id;
    }

    public String getTitle()
    {
        return title;
    }

    public LocalDateTime getStartDate()
    {
        return startDate;
    }

    public LocalDateTime getEndDate()
    {
        return endDate;
    }

    public String getLocation()
    {
        return location;
    }

    public boolean isFullDay()
    {
        return fullDay;
    }

    public String getRecurrenceRule()
    {
        return recurrenceRule;
    }
}
