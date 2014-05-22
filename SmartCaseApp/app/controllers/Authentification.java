package controllers;

import dao.TransactionDao;
import dao.UtilisateurDAO;
import exceptions.MotDePasseErrone;
import exceptions.UtilisateurInexistant;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import models.Transaction;
import models.Utilisateur;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import static play.mvc.Results.ok;
import static play.mvc.Results.redirect;
import utils.Security;
import utils.SessionManager;

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
            java.sql.Date date = new Date(Calendar.getInstance().getTimeInMillis());
            TransactionDao.ajouterTransaction(new Transaction(0, date,"connexion", utilisateur.getAdresseMail(),-1));
        } catch (SQLException ex) {
            return ok(views.html.error.render("Erreur interne.","/"));
        } catch (MotDePasseErrone ex) {
            return ok(views.html.index.render(ex.getMessage()));
        } catch (UtilisateurInexistant ex) {
            return ok(views.html.index.render(ex.getMessage()));
        }
        SessionManager.addSession("utilisateur", utilisateur.getAdresseMail());
        return redirect("/main");
    }
    
}
