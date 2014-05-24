/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import play.mvc.Controller;

/**
 * 
 * @author bombrunt
 */
public abstract class ControllerCommandeArduino extends Controller {
    
    public static final boolean debugVerrou = true;
    public static final boolean debugSenseur = true;
    
    public static final int seuil = 100;
    
}
