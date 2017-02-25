package net.balsoftware.component;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import net.balsoftware.components.VComponent;
import net.balsoftware.components.VDateTimeEnd;
import net.balsoftware.components.VEvent;
import net.balsoftware.components.VFreeBusy;
import net.balsoftware.properties.component.time.DateTimeEnd;
import net.balsoftware.properties.component.time.DateTimeStart;

/**
 * Test following components:
 * @see VEvent
 * @see VFreeBusy
 * 
 * for the following properties:
 * @see DateTimeEnd
 * 
 * @author David Bal
 *
 */
public class DateTimeEndTest
{
    @Test
    public void canBuildLastModified() throws InstantiationException, IllegalAccessException
    {
        List<VDateTimeEnd<?>> components = Arrays.asList(
                new VEvent()
                        .withDateTimeEnd(DateTimeEnd.parse("20160306T080000Z")),
                new VFreeBusy()
                        .withDateTimeEnd(DateTimeEnd.parse("20160306T080000Z"))
                );
        
        for (VDateTimeEnd<?> builtComponent : components)
        {
            String componentName = builtComponent.name();            
            String expectedContent = "BEGIN:" + componentName + System.lineSeparator() +
                    "DTEND:20160306T080000Z" + System.lineSeparator() +
                    "END:" + componentName;
                    
            VComponent parsedComponent = builtComponent.getClass().newInstance();
            parsedComponent.parseContent(expectedContent);
            
            assertEquals(parsedComponent, builtComponent);
            assertEquals(expectedContent, builtComponent.toString());            
        }
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType()
    {
        Thread.setDefaultUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        new VEvent()
                .withDateTimeStart(LocalDate.of(1997, 3, 1))
                .withDateTimeEnd("20160306T080000Z");
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType2()
    {
        Thread.setDefaultUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
       VEvent v = new VEvent()
                .withDateTimeEnd("20160306T080000Z")
                .withDateTimeStart(LocalDate.of(1997, 3, 1));
    }
    
    @Test (expected = DateTimeException.class)
    public void canCatchWrongDateType3()
    {
        Thread.setDefaultUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        VEvent builtComponent = new VEvent();
        builtComponent.setDateTimeEnd(new DateTimeEnd(LocalDateTime.of(2016, 3, 6, 8, 0)));
        builtComponent.setDateTimeStart(new DateTimeStart(LocalDate.of(1997, 3, 1)));
    }
}
