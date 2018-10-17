package demo5;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * demo5.WorkerActor class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class WorkerActor extends UntypedActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object o)throws InterruptedException{
        log.info("akka.future.demo5.WorkerActor.onReceive:" + o);

        if (o instanceof Integer) {
            Thread.sleep(1000);
            int i = Integer.parseInt(o.toString());
            getSender().tell(i*i, getSelf());
        } else {
            unhandled(o);
        }
    }
}
