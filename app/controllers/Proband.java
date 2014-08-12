package controllers;

import models.User;
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
public class Proband extends Controller {

  

    /**
     * Displays the registered experiments
     *
     * @return
     */
    public static Result registered() {  
        return ok(views.html.proband.registered.render());
    }
    
    public static Result available() {  
        return ok(views.html.proband.available.render());
    }
    
    public static Result completed() {  
        return ok(views.html.proband.completed.render());
    }

    
    /**
     * Registration form for people who are no students,
     * but want to participate in studies.
     * 
     */
    
   

}