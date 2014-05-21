package controllers;

import utils.Security;
import utils.SessionManager;
import dao.UtilisateurDAO;
import java.io.UnsupportedEncodingException;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import java.security.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Utilisateur;
import static play.mvc.Results.internalServerError;

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
            return ok(views.html.error.render("Veuillez verifiez la saisie de votre mot de passe."));
        String hashpassword=Security.crypt(requestData.get("adresseMail"));
                
        Utilisateur utilisateur = new Utilisateur(adresseMail, prenom, nom, hashpassword, 0);
        try {        
            UtilisateurDAO.ajouterUtilisateur(utilisateur);
        } catch (SQLException ex) {
            return ok(views.html.error.render("L'adresse \""+utilisateur.getAdresseMail()+"\" a déja été utilisé pour créer un compte."));
        }
        SessionManager.addSession("utilisateur", utilisateur.getAdresseMail());
        return redirect("/main");
    }
    
    public static Result getFormulaire() {
        return ok(views.html.inscription.render());
    }
}
