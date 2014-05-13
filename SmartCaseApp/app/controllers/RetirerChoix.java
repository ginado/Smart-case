package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;

/**
 *
 * @author bombrunt
 */
public class RetirerChoix extends Controller {
    static public Result choisir() {
        return ok(views.html.retirer_choix.render());
    }
}
