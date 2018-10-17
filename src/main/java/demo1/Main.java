package demo1;

import demo1.HelloWorld;

/**
 * demo1.Main class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class Main {
    public static void main(String[] args) {
        akka.Main.main(new String[] { HelloWorld.class.getName() });
    }
}
