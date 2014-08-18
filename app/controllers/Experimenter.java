package controllers;

import java.sql.SQLException;
import java.util.Map;

import models.Experiment;
import models.Filter;
import models.ProbandPoolFilter;
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
    
    public static Result saveGeneralData(int id) throws SQLException {
    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final String name = values.get("expName")[0];
        final String description = values.get("description")[0];
        final int duration = Integer.parseInt(values.get("duration")[0]);
        final int probandAmount = Integer.parseInt(values.get("probandAmount")[0]);
        final int expType = Integer.parseInt(values.get("expType")[0]);
        final float probandHours = Float.parseFloat(values.get("probandHours")[0]);
        final String sendInvitations = values.get("sendInvitations")[0];
        final String[] probandPools = values.get("probandPools");
        final int semesterFrom = Integer.parseInt(values.get("semesterFrom")[0]);
        final int semesterUntil = Integer.parseInt(values.get("semesterUntil")[0]);
        final String[] genders = values.get("gender");
        String gender = "";
        if(genders.length > 1) {
        	gender = "both";
        }
        	if(gender.length() == 1) {
        		if(genders[0].equals("male"))
        			gender = "male";
        		else
        			gender = "female";
        	}
        int filter_id = Filter.create(id, gender, semesterFrom, semesterUntil);
        	Logger.info("Filter angelegt: "+filter_id);
        for(String p : probandPools) {
        	ProbandPoolFilter.create(filter_id, p);
        }

        Experiment.update(id, name, description, duration, probandHours, probandAmount, expType);
		return ok();
    }
    
    public static Result saveFilters() throws SQLException {
		return ok();
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