package net.balsoftware.icalendar;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import net.balsoftware.icalendar.content.ContentLineStrategy;
import net.balsoftware.icalendar.content.Orderer;
import net.balsoftware.icalendar.utilities.ICalendarUtilities;

/**
 * <p>Base class for parent calendar components.</p>
 * 
 * <p>The order of the children from {@link #childrenUnmodifiable()} equals the order they were added.
 * Adding children is not exposed by the implementation, but rather handled internally.  When a {@link VChild} has its
 * value set, it's automatically included in the collection of children by the {@link Orderer}.</p>
 * 
 * <p>The {@link Orderer} requires registering listeners to child properties.</p>
 * 
 * @author David Bal
 */
public abstract class VParentBase<T> extends VElementBase implements VParent
{
    /*
     * HANDLE SORT ORDER FOR CHILD ELEMENTS
     */
    protected Orderer orderer;
    /** Return the {@link Orderer} for this {@link VParent} */
    
	@Override
	@Deprecated
	public void orderChild(VChild addedChild)
	{
		orderer.orderChild(addedChild);
	}

	@Override
	public void orderChild(int index, VChild addedChild)
	{
		orderer.orderChild(index, addedChild);
	}

//	@Override
//    public void addChild(VChild child)
//    {
//		Method setter = getSetter(child);
//		boolean isVarArgs = setter.getParameters()[0].isVarArgs();
//		System.out.println(setter);
//		try {
////			setter.invoke(this, new Object[]{child});
//			if (isVarArgs)
//			{
//				VChild[] childArray = new VChild[] { child };
//				System.out.println("isVargs:" + childArray + " " + setter);
//				Arrays.stream(setter.getParameters()).forEach(System.out::println);
//				setter.invoke(this, (Object) childArray);
//			} else
//			{
//				setter.invoke(this, child);
//			}
//			orderer.orderChild(child);
//		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
//			e.printStackTrace();
//		}
//    }
	@Override
    public void addChild(VChild child)
    {
		Method setter = getSetter(child);
		boolean isList = List.class.isAssignableFrom(setter.getParameters()[0].getType());
		System.out.println(setter);
		try {
			if (isList)
			{
				Method getter = getGetter(child);
				List<VChild> list = (List<VChild>) getter.invoke(this);
				if (list == null)
				{
					list = (List<VChild>) getter.getReturnType().newInstance();
					list.add(child);
					setter.invoke(this, list);
				} else
				{
					list.add(child);					
				}
			} else
			{
				setter.invoke(this, child);
			}
			orderer.orderChild(child);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
			e.printStackTrace();
		}
    }
	@Override
	public VChild removeChild(VChild child)
	{
		throw new RuntimeException("not implemented yet");
	}
	public T withChild(VChild child)
	{
		addChild(child);
		return (T) this;
	}
	
    protected Method getSetter(VElement element)
    {
    	if (SETTERS.get(element) == null)
    	{
    		System.out.println("add setter");
    		System.out.println(ICalendarUtilities.collectSetterMap(getClass()).size());
    		SETTERS.putAll(ICalendarUtilities.collectSetterMap(getClass()));
    	}
    	return SETTERS.get(element.getClass());
//		if (method != null) return method;
//    	Class<? extends Object> listKey = Array.newInstance(element.getClass(), 0).getClass();
//    	System.out.println("listKey:" + listKey);
//		return SETTERS.get(listKey);
    }
    
    protected Method getGetter(VElement element)
    {
    	if (GETTERS.get(element) == null)
    	{
    		System.out.println("add Getter");
    		System.out.println(ICalendarUtilities.collectGetterMap(getClass()).size());
    		GETTERS.putAll(ICalendarUtilities.collectGetterMap(getClass()));
    	}
    	return GETTERS.get(element.getClass());
//		if (method != null) return method;
//    	Class<? extends Object> listKey = Array.newInstance(element.getClass(), 0).getClass();
//    	System.out.println("listKey:" + listKey);
//		return SETTERS.get(listKey);
    }
	
    /* Strategy to build iCalendar content lines */
    protected ContentLineStrategy contentLineGenerator;
        
    @Override
	public List<VChild> childrenUnmodifiable()
    {
    	return orderer.childrenUnmodifiable();
    }
    
    @Override
    public void copyInto(VElement destination)
    {
        childrenUnmodifiable().forEach((childSource) -> 
        {
        	try {
				VChild newChild = childSource.getClass().newInstance();
				childSource.copyInto(newChild);
				addChild(newChild);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException e) {
				e.printStackTrace();
			}
        });
    }
    
	@Override
    public List<String> errors()
    {
        return childrenUnmodifiable().stream()
                .flatMap(c -> c.errors().stream())
                .collect(Collectors.toList());
    }


    @Override
    public String toString()
    {
        if (contentLineGenerator == null)
        {
            throw new RuntimeException("Can't produce content lines because contentLineGenerator isn't set");  // contentLineGenerator MUST be set by subclasses
        }
        return contentLineGenerator.execute();
    }
    
    // Note: can't check equals or hashCode of parents - causes stack overflow
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VParent testObj = (VParent) obj;
        
        Collection<VChild> c1 = childrenUnmodifiable();
        Collection<VChild> c2 = testObj.childrenUnmodifiable();
        if (c1.size() == c2.size())
        {
            Iterator<VChild> i1 = childrenUnmodifiable().iterator();
            Iterator<VChild> i2 = testObj.childrenUnmodifiable().iterator();
            for (int i=0; i<c1.size(); i++)
            {
                VChild child1 = i1.next();
                VChild child2 = i2.next();
                if (! child1.equals(child2))
                {
                    return false;
                }
            }
        } else
        {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        for (VChild child : childrenUnmodifiable())
        {
            result = prime * result + child.hashCode();
        }
        return result;
    }
}
