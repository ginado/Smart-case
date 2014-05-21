package controllers;

import dao.TransactionDao;
import utils.Security;
import utils.SessionManager;
import dao.UtilisateurDAO;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Transaction;
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
            if(utilisateur==null) {
                return ok(views.html.error.render("Mauvais mot de passe pour : \""+adresseMail+"\"."));
            }
            java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
            TransactionDao.ajouterTransaction(new Transaction(0, date,"connexion", utilisateur.getAdresseMail(),-1));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Aucun compte ayant comme email : \""+adresseMail+"\"."));
        }
        SessionManager.addSession("utilisateur", utilisateur.getAdresseMail());
        return redirect("/main");
    }
    
}