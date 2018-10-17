package demo7;

import java.io.Serializable;

/**
 * demo7.Cmd class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class Cmd implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String data;

    public Cmd(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
