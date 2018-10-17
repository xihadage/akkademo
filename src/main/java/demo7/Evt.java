package demo7;

import java.io.Serializable;

/**
 * demo7.Evt class
 *
 * @author Administrator
 * @date 2018/10/16
 */
public class Evt implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String data;
    private final String uuid;

    public Evt(String data, String uuid) {
        this.data = data;
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getData() {
        return data;
    }
}
