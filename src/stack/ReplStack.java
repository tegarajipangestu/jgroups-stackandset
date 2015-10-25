package stack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

    public T pop()
    {
        return stack.pop();
    }

    public T top()
    {
        return stack.peek();
    }

    public int size()
    {
        return stack.size();
    }

    public boolean isEmpty()
    {
        return stack.isEmpty();
    }
}
