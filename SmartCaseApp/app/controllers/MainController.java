package controllers;
import play.api.*;
import scala.*;


import dao.CasierDao;
import dao.UtilisateurDAO;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Utilisateur;
import models.Casier;
import play.mvc.Controller;
import java.util.ArrayList;
import java.util.Collection;
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
    
    public static Result index2(){
      try{
        return ok(views.html.casier_tab.render(CasierDao.getCasiers()));
     } catch (SQLException ex){
            return ok(views.html.index.render(ex.getMessage()));
    }    
}
   
}
