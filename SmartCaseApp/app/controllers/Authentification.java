package controllers;

import dao.UtilisateurDAO;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Utilisateur;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.internalServerError;
import static play.mvc.Results.redirect;

/**
 *
 * @author bombrunt
 */
public class Authentification extends Controller {
    
    public static Result authentifier(){
        DynamicForm requestData = Form.form().bindFromRequest();
        String adresseMail = requestData.get("adresseMail");
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
        Utilisateur utilisateur;
        try {
            utilisateur=UtilisateurDAO.authentifierUtilisateur(adresseMail, hashpassword);
        } catch (SQLException ex) {
            return ok(views.html.error.render("Mauvais mot de passe pour : \""+adresseMail+"\"."));
        }
        SessionManager.addSession("utilisateur", utilisateur);
        return redirect("/");
    }
    
}
