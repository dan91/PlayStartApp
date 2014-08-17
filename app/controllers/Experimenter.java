package controllers;

import java.sql.SQLException;

import models.Experiment;
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
     * Displays the experimenter search results
     *
     * @return
     */
    public static Result userSearch(String name, String target) {  
        return ok(views.html.experimenter.userSearchResults.render(name, target));
    }


    /**
     * Displays the edit experiment view 
     *
     * @return
     */
    public static Result edit(Long id) throws SQLException { 
    	Form<Experiment> editForm = form(Experiment.class);
    	return ok(views.html.experimenter.edit.render(Experiment.byId(id)));
    }
    
    /**
     * Displays current and expired studies of the experimenter
     *
     * @return
     * @throws SQLException 
     */
    public static Result myStudies() throws SQLException {  
			return ok(views.html.experimenter.myStudies.render(Experiment.all()));
		
		
    }
    
    public static Result save() {
    	try {
			return ok(views.html.experimenter.myStudies.render(Experiment.all()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return ok(e.toString());
		}
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