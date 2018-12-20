package pl.eldzi.aimpanel.utils.events;

/**
 * Created by ElDzi on 2016-04-29.
 */
public class ConsoleLogEvent {
    private Object obj;
    public int id;

    public ConsoleLogEvent(int id, Object obj) {
        this.obj = obj;
        this.id = id;
    }


    public Object getResult() {
        return obj;
    }
}
