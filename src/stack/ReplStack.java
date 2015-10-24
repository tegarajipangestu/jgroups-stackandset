package stack;

import java.util.Stack;

/**
 * Created by tegar on 24/10/15.
 */
public class ReplStack<T> {

    private Stack<T> stack;

    public ReplStack(){
        stack = new Stack<T>();
    }

    public void push(T element)
    {
        stack.push(element);
    }

    public T pop ()
    {
        return stack.pop();
    }

    public T top()
    {
        return stack.peek();
    }
}
