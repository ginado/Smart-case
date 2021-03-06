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
        
        //Fetching the lockers
        Collection<Casier> casiers;
        try {
            casiers = CasierDao.getCasiers();
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
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
                return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
            }
            return ok(views.html.accueil.render(utilisateur,"Tous les casiers sont pleins, impossible de déposer un objet."));
        } else {
            SessionManager.addSession("casier",String.valueOf(casierLibre.getIdCasier()));
            
            if(!debugVerrou) {
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
        Integer id = Integer.parseInt(SessionManager.get("casier"));
        Integer poids = -1;
        Casier casier;
	try {
	    casier = CasierDao.getCasier(SessionManager.get("casier"));
	} catch (SQLException ex) {
	    return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
	}


        try {
	    if(!debugSenseur){
		poids = SerialClass.peserCasier(id);
		if (poids < seuil) {
		    return ok(views.html.deposer.render(casier));
		}
	    }
            if (!debugVerrou) {
                SerialClass.fermerCasier(id);
            }

        } catch (Exception exception) {
            return ok(views.html.error.render("Erreur interne de connexion matériel","/main"));
        }
        
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        Utilisateur utilisateur;
        try {
            utilisateur = UtilisateurDAO.getUtilisateur(SessionManager.get("utilisateur"));
            TransactionDao.ajouterTransaction(new Transaction(0, date,"depot",utilisateur.getAdresseMail(),id));
            CasierDao.remplirCasier(id,poids);
            UtilisateurDAO.ajouterCredit(utilisateur.getAdresseMail(),1);
	    utilisateur.ajouterCredit(1);
            return ok(views.html.accueil.render(utilisateur,"Votre dépot a bien été pris en compte. Pensez a fermer la porte !"));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/main"));
        }
    }
}

