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
public class Experimenter extends Controller {

   

    /**
     * Displays the registered experiments
     *
     * @return
     */
    public static Result create() {  
        return ok(views.html.experimenter.create.render());
    }


    /**
     * Displays the edit experiment view (Mitch testedit)
     *
     * @return
     */
    public static Result edit() {  
        return ok(views.html.experimenter.edit.render());
    }
    
    /**
     * Displays current and expired studies of the experimenter
     *
     * @return
     */
    public static Result myStudies() {  
        return ok(views.html.experimenter.myStudies.render());
    }
    
    /**
     * Completes current study, shows attended probands and sessions
     *
     * @return
     */
    public static Result completeStudy() {  
        return ok(views.html.experimenter.completeStudy.render());
    }


}