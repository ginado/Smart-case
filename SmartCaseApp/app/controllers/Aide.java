/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 *
 * @author bombrunt
 */
public class Aide extends Controller {
    public static Result aide() {
        return ok(views.html.aide.render());
    }
    
}
