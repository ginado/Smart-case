package controllers;

import play.mvc.Controller;
import play.mvc.Result;

/**
 *
 * @author bombrunt
 */
public class EchangerChoix extends Controller {
    static public Result choisir() {
        return ok(views.html.echanger_choix.render());
    }
}
