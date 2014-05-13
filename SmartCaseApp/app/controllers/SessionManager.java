package controllers;

import play.Logger;
import play.mvc.Http;
import play.mvc.Http.Session;

public class SessionManager {

    public static void addSession(String key, String value) {
        if(value != null) {
            Session session = Http.Context.current().session();
            session.put(key,value);
        } else {
            Logger.info("Value for " + key + " is null");
        }
    }

    public static String get(String key) {

        Session session = Http.Context.current().session();
        final String value = session.get(key);
        return value;
    }
    
    public static void closeSession(){
        Http.Context.current().session().clear();
    }

}
