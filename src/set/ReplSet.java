package set;

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
    public void contains(T element)
    {
        set.contains(element);
    }
    public void eventLoop()
    {

    }
}
