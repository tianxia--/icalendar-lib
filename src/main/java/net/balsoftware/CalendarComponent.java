package net.balsoftware;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.balsoftware.components.VComponentBase;
import net.balsoftware.components.VEvent;
import net.balsoftware.components.VTodo;
import net.balsoftware.properties.PropertyType;

/**
 * <p>Enumerated type containing all the {@link VChild} elements that can be in a {@link VCalendar}</p>
 * 
 * @author David Bal
 *
 */
// TODO - SPLIT INTO TWO ENUMS - ONE FOR COMPONENTS, ONE FOR VCALENDAR PROPERTIES
public enum CalendarComponent
{
    // MAIN COMPONENTS
    VEVENT ("VEVENT",
    		

            VEvent.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            VEvent e = new VEvent();
//            e.parseContent(unfoldedLines);
//            vCalendar.getVEvents().add(e);
//            return e;
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
        	throw new RuntimeException("not implemented");
        }

		@Override
		public List<Method> propertyGetters()
		{
        	throw new RuntimeException("not implemented");
		}
    },
    VTODO ("VTODO",
//            Arrays.asList(PropertyType.ATTACHMENT, PropertyType.ATTENDEE, PropertyType.CATEGORIES,
//            PropertyType.CLASSIFICATION, PropertyType.COMMENT, PropertyType.CONTACT, PropertyType.DATE_TIME_COMPLETED,
//            PropertyType.DATE_TIME_CREATED, PropertyType.DATE_TIME_DUE, PropertyType.DATE_TIME_STAMP,
//            PropertyType.DATE_TIME_START, PropertyType.DESCRIPTION, PropertyType.DURATION,
//            PropertyType.EXCEPTION_DATE_TIMES, PropertyType.GEOGRAPHIC_POSITION,
//            PropertyType.LAST_MODIFIED, PropertyType.LOCATION,  PropertyType.NON_STANDARD, PropertyType.ORGANIZER,
//            PropertyType.PERCENT_COMPLETE, PropertyType.PRIORITY, PropertyType.RECURRENCE_DATE_TIMES,
//            PropertyType.RECURRENCE_IDENTIFIER, PropertyType.RELATED_TO, PropertyType.RECURRENCE_RULE,
//            PropertyType.REQUEST_STATUS, PropertyType.RESOURCES, PropertyType.SEQUENCE, PropertyType.STATUS,
//            PropertyType.SUMMARY, PropertyType.UNIQUE_IDENTIFIER, PropertyType.UNIFORM_RESOURCE_LOCATOR),
//            true,
            VTodo.class)
    {
//        @Override
//        public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines)
//        {
//            VTodo e = new VTodo();
//            e.parseContent(unfoldedLines);
//            vCalendar.getVTodos().add(e);
//            return e;
//        }

        @Override
        public void copyChild(VChild child, VCalendar destination)
        {
        	throw new RuntimeException("not implemented");
        }

		@Override
		public List<Method> propertyGetters()
		{
        	throw new RuntimeException("not implemented");
		}
    };

    // Map to match up name to enum
    private static Map<String, CalendarComponent> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, CalendarComponent> makeEnumFromNameMap()
    {
        Map<String, CalendarComponent> map = new HashMap<>();
        CalendarComponent[] values = CalendarComponent.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static CalendarComponent enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends VElement>, CalendarComponent> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends VElement>, CalendarComponent> makeEnumFromClassMap()
    {
        Map<Class<? extends VElement>, CalendarComponent> map = new HashMap<>();
        CalendarComponent[] values = CalendarComponent.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static CalendarComponent enumFromClass(Class<? extends VElement> myClass)
    {
        return enumFromClassMap.get(myClass);
    }
    
    private Class<? extends VElement> myClass;
    public Class<? extends VElement> getElementClass() { return myClass; }
    
    private String name;
    @Override
    public String toString() { return name; }
    
    private List<PropertyType> allowedProperties;
    public List<PropertyType> allowedProperties() { return allowedProperties; }

//    private boolean isCalendarElement;
//    public boolean isCalendarElement() { return isCalendarElement; }
    
    /*
     * CONSTRUCTOR
     */
    CalendarComponent(String name, Class<? extends VElement> myClass)
    {
        this.name = name;
//        this.allowedProperties = allowedProperties;
//        this.isCalendarElement = isCalendarElement;
        this.myClass = myClass;
    }

//    abstract public List<? extends VComponent> getComponents(VCalendar vCalendar);

    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
//    abstract public VElement parse(VCalendar vCalendar, Iterator<String> unfoldedLines);
    
    abstract public void copyChild(VChild child, VCalendar destination);
//    {
//        throw new RuntimeException("not implemented");
//    }
    /** return's list of property getter methods */
    abstract public List<Method> propertyGetters();
}
