/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.CasierDao;
import dao.TransactionDao;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import models.Casier;
import models.Transaction;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import utils.SessionManager;

/**
 *
 * @author bombrunt
 */
public class Signaler extends Controller {
    
    public static Result choisir(){
        try {
            Collection<Casier> casiers = CasierDao.getCasiers();
            return ok(views.html.choix_casier.render(casiers,"Choississez le casier dont vous voulez signaler le contenu comme étant nuisible.","Signaler","signaler"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne","/main"));
            
        }
    }
    
    
    public static Result signaler(String idCasier){
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            TransactionDao.ajouterTransaction(new Transaction(0, date, "signaler", "anonymous", Integer.parseInt(idCasier)));
        } catch (SQLException ex) {
            if(SessionManager.get("utilisateur")==null) {
                return ok(views.html.error.render("Erreur interne","/"));
            } else {
                return ok(views.html.error.render("Erreur interne","/main"));
            }
        }
        return redirect("/main");
    }
    
}
