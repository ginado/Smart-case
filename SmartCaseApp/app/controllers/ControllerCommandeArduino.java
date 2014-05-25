package controllers;

import play.mvc.Controller;

/**
 * Abstract interface used for Controller interacting with the Arduino
 * @author bombrunt
 */
public abstract class ControllerCommandeArduino extends Controller {
    
    public static final boolean debugVerrou = false;// true <-> no interaction with the locks
    public static final boolean debugSenseur = true;// true <-> no interaction with the sensors
    
    public static final int seuil = 280;// sensor<seuil <-> nothing is on the sensor 
    
}
