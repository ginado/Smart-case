package controllers;

import dao.TransactionDao;
import dao.UtilisateurDAO;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import models.Transaction;
import models.Utilisateur;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Security;
import utils.SessionManager;

/**
 *
 * @author bombrunt
 */
public class InscriptionUtilisateur extends Controller {
    
    public static Result Inscrire() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String adresseMail = requestData.get("adresseMail");
        String prenom = requestData.get("prenom");
        String nom = requestData.get("nom");
        if(requestData.get("password_confirmation").compareTo(requestData.get("password"))!=0)
            return ok(views.html.error.render("Veuillez verifiez la saisie de votre mot de passe.","/inscription"));
        String hashpassword=Security.crypt(requestData.get("password"));
                
        Utilisateur utilisateur = new Utilisateur(adresseMail, prenom, nom, hashpassword, 0);
        try {        
            UtilisateurDAO.ajouterUtilisateur(utilisateur);
        } catch (SQLException ex) {
            return ok(views.html.error.render("L'adresse \""+utilisateur.getAdresseMail()+"\" a déja été utilisé pour créer un compte.","/inscription"));
        }
        SessionManager.addSession("utilisateur", utilisateur.getAdresseMail());
        java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
        try {
            TransactionDao.ajouterTransaction(new Transaction(0,date,"inscription",utilisateur.getAdresseMail(),-1));
            TransactionDao.ajouterTransaction(new Transaction(0,date,"connexion",utilisateur.getAdresseMail(),-1));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne :"+ex.getMessage(),"/"));
        }
        return redirect("/main");
    }
    
    public static Result getFormulaire() {
        return ok(views.html.inscription.render());
    }
}
