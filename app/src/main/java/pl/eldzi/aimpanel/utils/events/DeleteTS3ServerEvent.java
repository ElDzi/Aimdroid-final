package pl.eldzi.aimpanel.utils.events;

/**
 * Created by ElDzi on 2016-06-17.
 */
public class DeleteTS3ServerEvent {
    private Object obj;

    public DeleteTS3ServerEvent(Object obj) {
        this.obj = obj;
    }

    public Object getResult() {
        return obj;
    }
}

