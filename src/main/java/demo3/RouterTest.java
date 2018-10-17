package demo3;

import akka.actor.*;
import akka.routing.ActorRefRoutee;
import akka.routing.RoundRobinRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;
import com.typesafe.config.ConfigFactory;
import demo2.InboxTest;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * demo3.RouterTest class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class RouterTest extends UntypedActor {

    public Router router;
    {
        ArrayList<Routee> routees = new ArrayList<Routee>();
        for(int i = 0; i < 5; i ++) {
            //借用上面的 inboxActor
            ActorRef worker = getContext().actorOf(Props.create(InboxTest.class), "worker_" + i);
            //监听
            getContext().watch(worker);
            routees.add(new ActorRefRoutee(worker));
        }
        /**
         * RoundRobinRoutingLogic: 轮询
         * BroadcastRoutingLogic: 广播
         * RandomRoutingLogic: 随机
         * SmallestMailboxRoutingLogic: 空闲
         */
        router = new Router(new RoundRobinRoutingLogic(), routees);
    }
    @Override
    public void onReceive(Object o){
        if(o instanceof InboxTest.Msg){
            //进行路由转发
            router.route(o, getSender());
        }else if(o instanceof Terminated){
            //发生中断，将该actor删除。当然这里可以参考之前的actor重启策略，进行优化，为了简单，这里仅进行删除处理
            router = router.removeRoutee(((Terminated)o).actor());
            System.out.println(((Terminated)o).actor().path() + " 该actor已经删除。router.size=" + router.routees().size());
            //没有可用actor了
            if(router.routees().size() == 0){
                System.out.print("没有可用actor了，系统关闭。");
                flag.compareAndSet(true, false);
                getContext().system().shutdown();
            }
        }else {
            unhandled(o);
        }
    }
    public  static AtomicBoolean flag = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        ActorSystem system = ActorSystem.create("strategy", ConfigFactory.load("akka.config"));
        ActorRef routerTest = system.actorOf(Props.create(RouterTest.class), "demo3.RouterTest");

        int i = 1;
        while(flag.get()){
            routerTest.tell(InboxTest.Msg.WORKING, ActorRef.noSender());
            if(i % 10 == 0) {
                routerTest.tell(InboxTest.Msg.CLOSE, ActorRef.noSender());
            }
            Thread.sleep(500);
            i ++;
        }
    }
}
