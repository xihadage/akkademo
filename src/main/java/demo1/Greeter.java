package demo1;

import akka.actor.UntypedActor;

/**
 * demo1.Greeter class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class Greeter extends UntypedActor {

    public static enum Msg {
        GREET, DONE;
    }
    @Override
    public void onReceive(Object msg)throws InterruptedException{
        if (msg == Msg.GREET) {
            System.out.println("Hello World!");
            Thread.sleep(1000);
            getSender().tell(Msg.DONE, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
