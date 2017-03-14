package net.balsoftware.icalendar.properties.component.recurrence.rrule;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.balsoftware.icalendar.Elements;
import net.balsoftware.icalendar.VElement;

/**
 * Week Start
 * WKST:
 * RFC 5545 iCalendar 3.3.10, page 42
 * 
 * The WKST rule part specifies the day on which the workweek starts.
 * Valid values are MO, TU, WE, TH, FR, SA, and SU.  This is
 * significant when a WEEKLY "RRULE" has an interval greater than 1,
 * and a BYDAY rule part is specified.  This is also significant when
 * in a YEARLY "RRULE" when a BYWEEKNO rule part is specified.  The
 * default value is MO.
 */
public class WeekStart extends RRuleElementBase<DayOfWeek, WeekStart>
{
    public static final DayOfWeek DEFAULT_WEEK_START = DayOfWeek.MONDAY;
    
    void setValue(String weekStart) { parseContent(weekStart); }
    public WeekStart withValue(String weekStart) { setValue(weekStart); return this; }
    
    public WeekStart(DayOfWeek dayOfWeek)
    {
        this();
        setValue(dayOfWeek);
    }
    
    public WeekStart()
    {
        super();
        setValue(DEFAULT_WEEK_START);
    }

    public WeekStart(WeekStart source)
    {
        this();
        setValue(source.getValue());
    }
    
    @Override
    public String toString()
    {
        return RRuleElementType.enumFromClass(getClass()).toString() + "=" + getValue().toString().substring(0, 2);
    }

    @Override
    public Map<VElement, List<String>> parseContent(String content)
    {
    	String valueString = Elements.extractValue(content);
        DayOfWeek dayOfWeek = Arrays.stream(DayOfWeek.values())
            .filter(d -> d.toString().substring(0, 2).equals(valueString))
            .findAny()
            .get();
        setValue(dayOfWeek);
        return Collections.EMPTY_MAP;
    }
    
    public static WeekStart parse(String content)
    {
    	WeekStart element = new WeekStart();
    	element.parseContent(content);
        return element;
    }
}
