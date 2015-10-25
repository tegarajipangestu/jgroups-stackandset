package client;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.Util;
import set.ReplSet;
import stack.ReplStack;

import java.io.*;

/**
 * Created by tegar on 25/10/15.
 */

public class SimpleClient extends ReceiverAdapter {
    JChannel channel;
    ReplStack<String> replStack;
    ReplSet <String> replSet;

    @Override
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    @Override
    public void receive(Message msg) {
        String line = msg.getObject().toString();
        if (channel.getClusterName().equals("StackCluster"))
        {
            if (line.substring(0, line.indexOf(' ')).equals("push")) {
                replStack.push(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("pop")) {
                String poppedString = replStack.pop();
                System.out.println("Popped String : "+poppedString);
            }
        }
        if (channel.getClusterName().equals("SetCluster"))
        {
            if (line.substring(0, line.indexOf(' ')).equals("add")) {
                replSet.add(line.substring(line.indexOf(' ')));
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
        channel.setReceiver(this);
        channel.getState(null,0);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        channel=new JChannel();
        System.out.println("Type \"stack\" to stack and type \"set\" to set ");
        String line=in.readLine().toLowerCase();
        if (line.equals("stack"))
        {
            channel.connect("StackCluster");
            replStack = new ReplStack<>();
            eventLoopStack();
        }
        else
        {
            channel.connect("SetCluster");
            replSet = new ReplSet<>();
            eventLoopSet();
        }
        channel.close();
    }

    public void getState(OutputStream output) throws Exception {
        if (channel.getClusterName().equals("StackCluster"))
        {
            synchronized(replStack) {
                Util.objectToStream(replStack, new DataOutputStream(output));
            }
        }
        else if (channel.getClusterName().equals("SetCluster"))
        {
            synchronized(replSet) {
                Util.objectToStream(replSet, new DataOutputStream(output));
            }
        }
    }

    public void setState(InputStream input) throws Exception {
        if (channel.getClusterName().equals("StackCluster"))
        {
            synchronized(replStack) {
                replStack = (ReplStack<String>)Util.objectFromStream(new DataInputStream(input));
            }
        }
        else if (channel.getClusterName().equals("SetCluster"))
        {
            synchronized(replSet) {
                replSet = (ReplSet<String>)Util.objectFromStream(new DataInputStream(input));
            }
        }
    }


    public void eventLoopSet() throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String line = in.readLine();
        while (!line.equals("quit"))
            if (line.substring(0, line.indexOf(' ')).equals("add")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("add ")
                        .append(line.substring(line.indexOf(' ')));
                sendMessage(line);
                replSet.add(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("remove")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("add ")
                        .append(line.substring(line.indexOf(' ')));
                sendMessage(line);
                replSet.add(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("top")) {
                System.out.println(replStack.top());
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
            if (line.substring(0, line.indexOf(' ')).equals("push")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("add ")
                        .append(line.substring(line.indexOf(' ')));
                sendMessage(line);
                replStack.push(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("pop")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("add ")
                        .append(line.substring(line.indexOf(' ')));
                sendMessage(line);
                replStack.push(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("top")) {
                System.out.println(replStack.top());
            }
    }

}
