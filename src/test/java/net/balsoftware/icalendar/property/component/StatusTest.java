package net.balsoftware.icalendar.property.component;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.balsoftware.icalendar.properties.component.descriptive.Status;
import net.balsoftware.icalendar.properties.component.descriptive.Status.StatusType;

public class StatusTest
{
    @Test
    public void canParseStatus()
    {
        String content = "STATUS:NEEDS-ACTION";
        Status madeProperty = Status.parse(content);
        assertEquals(content, madeProperty.toString());
        Status expectedProperty = Status.parse("NEEDS-ACTION");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(madeProperty.getValue(), StatusType.NEEDS_ACTION);
    }
}
