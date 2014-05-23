package controllers;

import dao.CasierDao;
import dao.TransactionDao;
import dao.UtilisateurDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import models.Casier;
import models.Transaction;
import models.Utilisateur;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import utils.SessionManager;
import arduino.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bombrunt
 */
public class Deposer extends ControllerCommandeArduino{
    public static Result deposer(){
        Collection<Casier> casiers;
        try {
            casiers = CasierDao.getCasiers();
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/main"));
        }
        //Search for an empty locker
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
            Utilisateur utilisateur;
            try {
                utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            } catch (SQLException ex) {
                return ok(views.html.error.render("Erreur interne","/main"));
            }
            return ok(views.html.accueil.render(utilisateur,"Tous les casiers sont pleins, impossible de déposer un objet."));
        } else {
            SessionManager.addSession("casier",String.valueOf(casierLibre.getIdCasier()));
            
            if(connecteArduino) {
                try {
                    SerialClass.ouvrirCasier(casierLibre.getIdCasier());
                } catch (Exception ex) {
                    return ok(views.html.error.render("Erreur interne de connexion matériel","/main"));
                }
            }
            
	    return ok(views.html.deposer.render(casierLibre));
        }
    }
    
    public static Result deposerFin(){
        int id = Integer.parseInt(SessionManager.get("casier"));
        int poids = -1;
        
        try {
            if (connecteArduino) {
                SerialClass.fermerCasier(id);
                poids = SerialClass.peserCasier(id);
                if (poids < seuil) {
                    return redirect("/deposerfin/");
                }
            }
        } catch (Exception exception) {
            return ok(views.html.error.render("Erreur interne de connexion matériel","/main"));
        }
        
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            TransactionDao.ajouterTransaction(new Transaction(0, date,"depot",SessionManager.get("utilisateur"),id));
            CasierDao.remplirCasier(id,poids);
            UtilisateurDAO.ajouterCredit(SessionManager.get("utilisateur"),1);
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne."+ex.getMessage(),"/main"));
        }
        return redirect("/main");
    }
}

