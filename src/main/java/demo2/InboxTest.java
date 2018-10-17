package demo2;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * demo2.InboxTest class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class InboxTest extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    public enum Msg{
        WORKING, DONE, CLOSE;
    }
    @Override
    public void onReceive(Object o){
        if(o == Msg.WORKING){
            log.info("i am working.");
        }else if(o == Msg.DONE){
            log.info("i am done");
        }else if(o == Msg.CLOSE){
            log.info("i am close.");
            //告诉消息发送者我要关闭了
            getSender().tell(Msg.CLOSE, getSelf());
            //关闭自己
            getContext().stop(getSelf());
        }else{
            unhandled(o);
        }
    }
    public static void main(String [] args){
        ActorSystem system = ActorSystem.create("inbox", ConfigFactory.load("akka.conf"));
        ActorRef inboxTest = system.actorOf(Props.create(InboxTest.class), "demo2.InboxTest");

        Inbox inbox = Inbox.create(system);
        //监听一个actor
        inbox.watch(inboxTest);

        //通过inbox来发送消息
        inbox.send(inboxTest, Msg.WORKING);
        inbox.send(inboxTest, Msg.DONE);
        inbox.send(inboxTest, Msg.CLOSE);

        while(true){
            try {
                Object receive = inbox.receive(Duration.create(1, TimeUnit.SECONDS));
                //收到的inbox的消息
                if(receive == Msg.CLOSE){
                    System.out.println("inboxTextActor is closing");
                    //中断 ，和线程一个概念
                }else if(receive instanceof  Terminated){
                    System.out.println("inboxTextActor is closed");
                    system.shutdown();
                    break;
                }else {
                    System.out.println(receive);
                }
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
    }
}
