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
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;

/**
 *
 * @author bombrunt
 */
public class Authentification extends Controller {
    
    public static Result authentifier(){
        DynamicForm requestData = Form.form().bindFromRequest();
        String adresseMail = requestData.get("adresseMail");
        String hashpassword=Security.crypt(requestData.get("password"));
        Utilisateur utilisateur;
        try {
            utilisateur=UtilisateurDAO.authentifierUtilisateur(adresseMail, hashpassword);
        } catch (SQLException ex) {
            return ok(views.html.error.render("Aucun compte ayant comme email : \""+adresseMail+"\"."));
        }
        if(utilisateur==null) {
            return ok(views.html.error.render("Mauvais mot de passe pour : \""+adresseMail+"\"."));
        }
        SessionManager.addSession("utilisateur", utilisateur.getAdresseMail());
        return redirect("/main");
    }
    
}
