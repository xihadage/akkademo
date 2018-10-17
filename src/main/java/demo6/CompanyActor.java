package demo6;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.transactor.Coordinated;
import demo6.STMMain;
import scala.concurrent.stm.Ref;
import scala.concurrent.stm.japi.STM;

/**
 * demo6.CompanyActor class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class CompanyActor extends UntypedActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    //定义账户余额
    private Ref.View<Integer> count = STM.newRef(100);

    @Override
    public void onReceive(Object o) {
        if(o instanceof Coordinated){
            Coordinated coordinated = (Coordinated) o;
            //传递过来的参数，减多少。
            int downCount = (int) coordinated.getMessage();
            //通知employeeActor增加费用
            STMMain.employeeActor.tell(coordinated.coordinate(downCount), getSelf());
             //注意这里异常要及时处理，否则异常会一直扩散，导致回退到系统刚启动时的初始状态！
            try {
                coordinated.atomic(() -> {
                    if(count.get() < downCount) {
                        throw new RuntimeException("余额不足！");
                    }
                    //减余额
                    STM.increment(count, -downCount);
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }else if("getCount".equals(o)){
            getSender().tell(count.get(), getSelf());
        }else{
            unhandled(o);
        }
    }
}
