package client;

import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import set.ReplSet;
import stack.ReplStack;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by tegar on 25/10/15.
 */

public class SimpleClient extends ReceiverAdapter {
    JChannel channel;
    String user_name=System.getProperty("user.name", "n/a");
    ReplStack<String> replStack;
    ReplSet <String> replSet;

    @Override
    public void viewAccepted(View new_view) {
        System.out.println("** view: " + new_view);
    }

    @Override
    public void receive(Message msg) {
        String line = msg.getObject().toString();
        if (!replStack.equals(null))
        {
            if (line.substring(0, line.indexOf(' ')).equals("push")) {
                replStack.push(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("pop")) {
                String poppedString = replStack.pop();
                System.out.println("Popped String : "+poppedString);
            }
        }
        System.out.println(msg.getSrc() + ": " + msg.getObject());
    }


    public static void main(String[] args) throws Exception {
        new SimpleClient().start();
    }

    private void start() throws Exception {
        channel.setReceiver(this);
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

    public void eventLoopSet()
    {

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
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("add ")
                        .append(line.substring(line.indexOf(' ')));
                sendMessage(line);
                replStack.push(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("pop")) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("add ")
                        .append(line.substring(line.indexOf(' ')));
                sendMessage(line);
                replStack.push(line.substring(line.indexOf(' ')));
            }
            else if (line.substring(0, line.indexOf(' ')).equals("top")) {
                System.out.println(replStack.top());
            }
    }

}
