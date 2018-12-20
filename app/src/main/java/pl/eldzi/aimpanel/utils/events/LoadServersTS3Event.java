package pl.eldzi.aimpanel.utils.events;

/**
 * Created by ElDzi on 2016-04-29.
 */
public class LoadServersTS3Event {
    private Object obj;

    public LoadServersTS3Event(Object obj) {
        this.obj = obj;
    }


    public Object getResult() {
        return obj;
    }
}
