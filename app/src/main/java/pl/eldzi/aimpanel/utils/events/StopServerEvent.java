package pl.eldzi.aimpanel.utils.events;

/**
 * Created by ElDzi on 2016-04-29.
 */
public class StopServerEvent {
    private Object obj;
    public int id;

    public StopServerEvent(int id, Object obj) {
        this.obj = obj;
        this.id = id;
    }


    public Object getResult() {
        return obj;
    }
}
