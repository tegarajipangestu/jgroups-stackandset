package set;

import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * Created by tegar on 24/10/15.
 */

public class ReplSet<T> {
    private Set<T> set;

    public ReplSet()
    {
        set = new HashSet<T>();
    }
    public void add(T element)
    {
        set.add(element);
    }
    public boolean contains(T element)
    {
        return set.contains(element);
    }
    public boolean remove(T element)
    {
        return set.remove(element);
    }

    public int size()
    {
        return set.size();
    }

    public boolean isEmpty()
    {
        return set.isEmpty();
    }

    public Iterator<T> getIterator()
    {
        return set.iterator();
    }
}
