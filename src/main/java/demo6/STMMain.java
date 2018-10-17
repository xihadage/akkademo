package demo6;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.pattern.Patterns;
import akka.transactor.Coordinated;
import akka.util.Timeout;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;

import java.util.concurrent.TimeUnit;

/**
 * demo6.STMMain class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class STMMain {
    public static ActorRef companyActor = null;
    public static ActorRef employeeActor = null;


    public static void main(String [] args) throws Exception {
        ActorSystem system = ActorSystem.create("stm", ConfigFactory.load("akka.conf"));
        companyActor = system.actorOf(Props.create(CompanyActor.class), "demo6.CompanyActor");
        employeeActor = system.actorOf(Props.create(EmployeeActor.class), "demo6.EmployeeActor");

        Timeout timeout = new Timeout(1, TimeUnit.SECONDS);

        for(int i = 0 ; i < 23; i ++){
            companyActor.tell(new Coordinated(i, timeout), ActorRef.noSender());

            Thread.sleep(200);

            Object companyCount =  Await.result(Patterns.ask(companyActor, "getCount", timeout), timeout.duration());
            Object employeeCount = Await.result(Patterns.ask(employeeActor, "getCount", timeout), timeout.duration());

            System.out.println("companyCount = " + companyCount + ";employeeCount = " + employeeCount);
            System.out.println("-----------------------");
        }

    }
}
