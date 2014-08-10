package controllers;

import models.User;
import models.utils.AppException;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import static play.data.Form.form;

/**
 * Login and Logout.
 * User: yesnault
 */
public class Admin extends Controller {

    

    /**
     * Displays the admin mainview
     *
     * @return
     */
    public static Result admin() {  
        return ok(views.html.admin.admin.render());
    }


        /**
     * Displays the admin user view
     *
     * @return
     */
    public static Result astudies() {  
        return ok(views.html.admin.studies.render());
    }

        /**
     * Displays the admin studies
     *
     * @return
     */
    public static Result auser() {  
        return ok(views.html.admin.user.render());
    }

     /**
     * Displays the admin edit page
     *
     * @return
     */
    public static Result edit() {  
        return ok(views.html.admin.edit_admin.render());
    }

        /**
     * Displays the admin lab
     *
     * @return
     */
    public static Result alab() {  
        return ok(views.html.admin.lab.render());
    }
    
    /**
     * Displays the admin lmap
     *
     * @return
     */
    public static Result mapTest() {  
        return ok(views.html.admin.mapTest.render());
    }

       /**
     * Displays konto config
     *
     * @return
     */
    public static Result konto() {  
        return ok(views.html.admin.konto.render());
    }



}