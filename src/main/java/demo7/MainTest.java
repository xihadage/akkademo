package demo7;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * demo7.MainTest class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class MainTest {
    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("actor-server");

        final ActorRef handler = actorSystem.actorOf(Props.create(EventHandler. class));
        // 订阅
        actorSystem.eventStream().subscribe(handler , Evt.class);

        Thread.sleep(5000);

        final ActorRef actorRef = actorSystem.actorOf(Props.create(ExamplePersistentActor. class), "eventsourcing-processor" );

        actorRef.tell( new Cmd("CMD 1" ), null);
        actorRef.tell( new Cmd("CMD 2" ), null);
        actorRef.tell( new Cmd("CMD 3" ), null);
        actorRef.tell( "snap", null );//发送保存快照命令
        actorRef.tell( new Cmd("CMD 4" ), null);
        actorRef.tell( new Cmd("CMD 5" ), null);
        actorRef.tell( "print", null );

        Thread.sleep(5000);

        System.out.println( "Actor System Shutdown Starting..." );

        actorSystem.shutdown();
    }
}
