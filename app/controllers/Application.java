package controllers;

import java.util.Map;


// import models.LatLng;
import models.User;
import play.Logger;
import play.Routes;
import play.data.*;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.admin.submit;
import static play.data.Form.form;

/**
 * Login and Logout.
 * User: yesnault
 */
public class Application extends Controller {


    

	public static Result javascriptRoutes() {
        response().setContentType("text/javascript");
        return ok(
            Routes.javascriptRouter("myJsRoutes",
                routes.javascript.Admin.deleteBuilding(),
                routes.javascript.Admin.deleteRoom(),
                routes.javascript.Experimenter.userSearch(),
            	routes.javascript.Experimenter.saveGeneralData(),
            	routes.javascript.Experimenter.searchUsers(),
            	routes.javascript.Experimenter.saveSessions()

            )
        );
    }
    
    
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
    
    // TODO siehe model.user Zeile 550
    public static Result login() {
    	return ok(views.html.login.render(
    			form(Login.class)
    			));
    }
    
    public static Result authenticate() {
    	Form<Login> loginForm = form(Login.class).bindFromRequest(); //TODO BindfromRequest weg? Login.class weg? aber wie?
    		if (loginForm.hasErrors()) {
    			return badRequest(views.html.login.render(loginForm));
    		} else {
    			session().clear();
    			session("id", loginForm.get().email); //TODO session id = User.id 
    			return redirect (routes.Application.index());
    		}
    }
    
    public static class Login {
    	public String email;
    	public String password;
     
    
		    public String validate() {
		    	if (User.authenticate(email, password) == null) {
		    		return "Falsche Logindaten";
		    	}
		    	return null;
		    }
    
    }
    
    public static Result logout() {
        session().clear();
        flash("Erfolg", "Logout erfolgreich");
        return redirect(
            routes.Application.login()
        );
    }
}



