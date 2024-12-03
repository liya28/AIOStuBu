package io.serateam.stewboo.core.services.calendar;

import java.time.Duration;
import java.time.LocalDateTime;

public class StubuCalendarEntry
{
    /*
    * The fields of the StubuCalendarEntry class comply with RFC 2445 Section 4.8
    * to ensure adherence to industry standards.
    * Refer: https://www.rfc-editor.org/rfc/rfc2445
    *
    * - RAFAEL A. MENDOZA
    * */

    // FIELDS
    private final String id;            // RFC 2445 4.8.4.7 UNIQUE IDENTIFIER
    private String title;               // RFC 2445 4.8.1.12 SUMMARY
    private LocalDateTime startDate;    // RFC 2445 4.8.2.4 DATE/TIME START
    private LocalDateTime endDate;      // RFC 2445 4.8.2.2 DATE/TIME END
    private String location;            // RFC 2445 4.8.1.7 LOCATION
    private boolean fullDay;
    private Duration minimumDuration;
    private String recurrenceRule;      // RFC 2445 4.8.5.4 RECURRENCE RULE

    // CONSTRUCTOR
    public StubuCalendarEntry
    (
            String id,
            String title,
            LocalDateTime startAsLocalDateTime,
            LocalDateTime endAsLocalDateTime,
            String location,
            boolean fullDay,
            Duration minimumDuration,
            String recurrenceRule
    )
    {
        this.id = id;
        this.title = title;
        this.startDate = startAsLocalDateTime;
        this.endDate = endAsLocalDateTime;
        this.location = location;
        this.fullDay = fullDay;
        this.minimumDuration = minimumDuration;
        this.recurrenceRule = recurrenceRule;
    }

    // METHODS

    // region Getters

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

    public Duration getMinimumDuration()
    {
        return minimumDuration;
    }

    // endregion

    // region Setters

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

    public void setMinimumDuration(Duration minimumDuration)
    {
        this.minimumDuration = minimumDuration;
    }

    public void setRecurrenceRule(String recurrenceRule)
    {
        this.recurrenceRule = recurrenceRule;
    }

    // endregion
}

