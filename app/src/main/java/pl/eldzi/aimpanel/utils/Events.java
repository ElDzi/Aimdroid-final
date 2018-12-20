package pl.eldzi.aimpanel.utils;

import com.squareup.otto.Bus;

/**
 * Created by ElDzi on 2016-04-27.
 */
public class Events {
    private static final Bus listenerHandler = new Bus();

    public static Bus getListenerHandler() {
        return listenerHandler;
    }

}
