/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dao.TransactionDao;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import models.Transaction;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import utils.SessionManager;

/**
 *
 * @author bombrunt
 */
public class Logout extends Controller {
    public static Result logout(){
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            TransactionDao.ajouterTransaction(new Transaction(0, date, "deconnexion", SessionManager.get("utilisateur"),null));
            SessionManager.closeSession();
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne : "+ex.getMessage(),"/"));
        }
        return redirect("/");
    }
}
