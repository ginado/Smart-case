package controllers;

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
        String hashpassword = null;
        try {
            byte[] bytesOfMessage = requestData.get("password").getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            hashpassword=thedigest.toString();    
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            return internalServerError();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            return internalServerError();
        }
                
        Utilisateur utilisateur = new Utilisateur(adresseMail, prenom, nom, hashpassword, 0);
        try {        
            UtilisateurDAO.ajouterUtilisateur(utilisateur);
        } catch (SQLException ex) {
            return ok(views.html.error.render("L'adresse \""+utilisateur.getAdresseMail()+"\" a déja été utilisé pour créer un compte."));
        }
        SessionManager.addSession("utilisateur", utilisateur);
        return redirect("/");
    }
    
    public static Result getFormulaire() {
        return ok(views.html.inscription.render());
    }
}
