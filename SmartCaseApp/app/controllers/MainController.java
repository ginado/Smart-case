package controllers;

import dao.UtilisateurDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;

public class MainController extends Controller {
    
    public static Result index() {
        //return ok(views.html.index.render("Smart with case"));
        Utilisateur user;
        try {
            user = UtilisateurDAO.getUtilisateur("meignanm@ensimag.fr");
        } catch (SQLException ex) {
            return ok(views.html.index.render(ex.getMessage()));
        }
        return ok(views.html.index.render("Hello from " + user.getPrenom()));
    }
    
}
