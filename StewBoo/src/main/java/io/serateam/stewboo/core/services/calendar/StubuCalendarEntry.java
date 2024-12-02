package io.serateam.stewboo.core.services.calendar;

import java.time.LocalDateTime;

public class StubuCalendarEntry
{
    // FIELDS
    private final String id;
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

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setStartDate(LocalDateTime startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate)
    {
        this.endDate = endDate;
    }

    public void setLocation(String location)
    {

        this.location = location;
    }

    public void setFullDay(boolean fullDay)
    {
        this.fullDay = fullDay;
    }

    public void setRecurrenceRule(String recurrenceRule)
    {
        this.recurrenceRule = recurrenceRule;
    }
}

