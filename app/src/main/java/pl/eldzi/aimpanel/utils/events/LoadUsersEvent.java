package pl.eldzi.aimpanel.utils.events;

/**
 * Created by ElDzi on 2016-06-17.
 */
public class LoadUsersEvent {
    private Object obj;

    public LoadUsersEvent(Object obj) {
        this.obj = obj;
    }


    public Object getResult() {
        return obj;
    }
}
