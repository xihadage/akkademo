package demo9;

/**
 * RealData class
 *
 * @author Administrator
 * @date 2018/10/17
 */
public class RealData implements Data {
    protected String data;

    public RealData(String data) {
        //利用sleep方法来表示RealData构造过程是非常缓慢的
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.data = data;
    }

    @Override
    public String getResult() {
        return data;
    }
}
