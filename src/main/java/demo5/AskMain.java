package demo5;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.pattern.Patterns;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Future;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * demo5.AskMain class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class AskMain {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef printActor = system.actorOf(Props.create(PrintActor.class), "demo5.PrintActor");
        ActorRef workerActor = system.actorOf(Props.create(WorkerActor.class), "demo5.WorkerActor");

        //等等future返回
        Future<Object> future = Patterns.ask(workerActor, 5, 1000);
        Object result =Await.result(future, Duration.create(3, TimeUnit.SECONDS));
        System.out.println("result:" + result);

        //不等待返回值，直接重定向到其他actor，有返回值来的时候将会重定向到printActor
        Future<Object> future1 = Patterns.ask(workerActor, 8, 1000);
        Patterns.pipe(future1, system.dispatcher()).to(printActor);


        workerActor.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }
}
