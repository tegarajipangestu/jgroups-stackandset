package client;

import org.jgroups.*;
import org.jgroups.util.Util;
import set.ReplSet;
import stack.ReplStack;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by tegar on 25/10/15.
 */

public class SimpleClient extends ReceiverAdapter {
    JChannel channel;
    ReplStack<String> replStack;
    ReplSet <String> replSet;
    List<Address> members;


    public SimpleClient()
    {
        members = new ArrayList<>();
//        replStack = new ReplStack<>();
//        replSet = new ReplSet<>();
    }

    @Override
    public void viewAccepted(View new_view) {
        members.clear();
        members.addAll(new_view.getMembers());
        if (members.size()==1)
        {
            if (channel.getClusterName().equals("StackCluster"))
                replStack = new ReplStack<>();
            else if (channel.getClusterName().equals("SetCluster"))
                replSet = new ReplSet<>();
        }
        if (!members.isEmpty() && (channel.getAddress() != null && !channel.getAddress().equals(members.get(0))))
        {
            try {
                if (replStack==null && channel.getClusterName().equals("StackCluster"))
                    channel.getState(null,10000);
                else if (replSet==null && channel.getClusterName().equals("SetCluster"))
                    channel.getState(null,10000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("** view: " + new_view);
    }

    @Override
    public void receive(Message msg) {
        String line = msg.getObject().toString();
        if (channel.getClusterName().equals("StackCluster"))
        {
            if (line.substring(0, line.indexOf(' ')).equals("push")) {
                replStack.push(line.substring(line.indexOf(' ')+1));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("pop")) {
                String poppedString = (String) replStack.pop();
                System.out.println("Popped String : "+poppedString);
            }
        }
        if (channel.getClusterName().equals("SetCluster"))
        {
            if (line.substring(0, line.indexOf(' ')).equals("add")) {
                replSet.add(line.substring(line.indexOf(' ')+1));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("remove")) {
                if (replSet.remove(line.substring(line.indexOf(' '))))
                    System.out.println(line.substring(line.indexOf(' '))+" has been removed");
            }
        }
        System.out.println(msg.getSrc() + ": " + msg.getObject());
    }


    public static void main(String[] args) throws Exception {
        new SimpleClient().start();
    }

    private void start() throws Exception {
        channel = new JChannel();

        channel.setReceiver(this);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type \"stack\" to stack and type \"set\" to set ");
        String line=in.readLine().toLowerCase();
        if (line.equals("stack"))
        {
            channel.connect("StackCluster");
            eventLoopStack();
        }
        else if (line.equals("set"))
        {
            channel.connect("SetCluster");
            eventLoopSet();
        }
        else
        {
            System.err.println("Unknown Data type");
        }
        channel.close();
    }

    public void getState(OutputStream output) throws Exception {
        List<String> list = new ArrayList<>();
        if (channel.getClusterName().equals("StackCluster"))
        {
            synchronized(list) {
//                ReplStack<String> temp = replStack;
                Iterator<String> iter = replStack.getIterator();
                while (iter.hasNext())
                {
                    list.add(iter.next());
                }
//                dos.flush();
                Util.objectToStream(list, new ObjectOutputStream(output));
                System.out.println("wrote " + replStack.size() + " elements");
            }
        }
        else if (channel.getClusterName().equals("SetCluster"))
        {
            synchronized(list) {
//                ReplSet<String> temp = replSet;
                Iterator<String> iter = replSet.getIterator();
                while(iter.hasNext())
                {
                    String s = iter.next();
                    list.add(s);
//                    dos.writeChars(s);
                }
//                dos.flush();
                Util.objectToStream(list, new ObjectOutputStream(output));
                System.out.println("wrote " + list.size() + " elements");
            }
        }
    }

    public void setState(InputStream input) throws Exception {
//        System.out.println(Util.objectFromStream(new ObjectInputStream(input)).toString());
        List<String> temp = new ArrayList<>();
        if (channel.getClusterName().equals("StackCluster"))
        {
            replStack = new ReplStack<>();
            synchronized(replStack) {
                temp = (List<String>)Util.objectFromStream(new ObjectInputStream(input));
                Iterator<String> iter = temp.iterator();
                while (iter.hasNext())
                {
                    String s = iter.next();
                    replStack.push(s);
                }
            }
        }
        else if (channel.getClusterName().equals("SetCluster"))
        {
            replSet = new ReplSet<>();
            synchronized(replSet) {
                temp = (List<String>)Util.objectFromStream(new ObjectInputStream(input));
                Iterator<String> iter = temp.iterator();
                while (iter.hasNext())
                {
                    String s = iter.next();
                    replSet.add(s);
                }
            }
        }
        System.out.println("Getting " + temp.size() + " elements");
    }


    public void eventLoopSet() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = in.readLine();
        while (!line.equals("quit"))
        {
            try {
                if (line.substring(0, line.indexOf(' ')).equals("add")) {
                    sendMessage(line);
                    replSet.add(line.substring(line.indexOf(' ')+1));
                }
                else if (line.substring(0, line.indexOf(' ')).equals("remove")) {
                    sendMessage(line);
                    if (replSet.remove(line.substring(line.indexOf(' ')+1)))
                        System.out.println(line.substring(line.indexOf(' ')+1)+" has been removed");
                    else
                        System.err.println(line.substring(line.indexOf(' ')+1)+" cannot removed");
                }
                else if (line.substring(0, line.indexOf(' ')).equals("contains")) {
                    if (replSet.contains(line.substring(line.indexOf(' ')+1)))
                        System.out.println(line.substring(line.indexOf(' ')+1)+" belongs in set");
                    else
                        System.err.println(line.substring(line.indexOf(' ')+1)+" not belongs in set");
                }
            }
            catch (StringIndexOutOfBoundsException e )
            {
                System.err.println("Method unknown");
            }
            finally {
                line = in.readLine();
            }
        }

    }

    public void sendMessage(String line) throws Exception {
        Message msg=new Message(null, null, line);
        channel.send(msg);
    }

    public void eventLoopStack() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = in.readLine();
        while (!line.equals("quit"))
        {
            try {
                if (line.contains("push")) {
                    sendMessage(line);
                    replStack.push(line.substring(line.indexOf(' ') + 1));
                }
                else if (line.equals("pop")) {
                    sendMessage(line);
                    String poppedString = (String) replStack.pop();
                }
                else if (line.equals("top")) {
                    System.out.println(replStack.top());
                }
            }
            catch (Exception e)
            {
                if (e.equals(new StringIndexOutOfBoundsException()))
                    System.err.println("Method unknown");
                if (e.equals(new java.util.EmptyStackException()))
                    System.err.println("Empty stack");
                else System.err.println(e.toString());
            }
            finally {
                line = in.readLine();
            }
        }

    }

}
