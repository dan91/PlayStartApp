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
public class Application extends Controller {

   
    /**
     * Display the login page or dashboard if connected
     *
     * @return login page or dashboard
     */
    public static Result index() {
        // Check that the email matches a confirmed user before we redirect
        // String email = ctx().session().get("email");
        // if (email != null) {
        //     User user = User.findByEmail(email);
        //     if (user != null && user.validated) {
        //         return GO_DASHBOARD;
        //     } else {
        //         Logger.debug("Clearing invalid session credentials");
        //         session().clear();
        //     }
        // }

        return ok(index.render());
    }



}