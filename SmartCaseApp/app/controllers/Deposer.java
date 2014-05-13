package controllers;

import dao.CasierDao;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Casier;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;

/**
 *
 * @author bombrunt
 */
public class Deposer extends Controller{
    public static Result deposer(){
        Collection<Casier> casiers;
        try {
            casiers = CasierDao.getCasiers();
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne."));
        }
        //Search for an mpty locker
        Casier casierLibre = null;
        Casier casiercourant = null;
        Iterator<Casier> itCasier = casiers.iterator();
        while(itCasier.hasNext()) {
            casiercourant=itCasier.next();
            if(!casiercourant.estPlein()){
                casierLibre=casiercourant;
            }
        }
        
        if(casierLibre==null) {
            return ok(views.html.error.render("Tout les casiers sont occupés, désolé :) ."));
        } else {
            SessionManager.addSession("casier",String.valueOf(casierLibre.getIdCasier()));
            return ok(views.html.deposer.render(casierLibre));
        }
    }
    
    public static Result deposerFin(){
        try {
            CasierDao.remplirCasier(Integer.parseInt(SessionManager.get("casier")),100);
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne."));
        }
        return redirect("/main");
    }
}

