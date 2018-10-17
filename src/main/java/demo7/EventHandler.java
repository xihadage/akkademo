package demo7;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.alibaba.fastjson.JSON;

/**
 * demo7.EventHandler class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class EventHandler extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    @Override
    public void onReceive(Object msg ) throws Exception {
        log.info( "Handled Event: " + JSON.toJSONString(msg));
    }
}
