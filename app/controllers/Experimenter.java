package controllers;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import models.Assignment;
import models.ExcludedExperiment;
import models.Experiment;
import models.Filter;
import models.Participation;
import models.ProbandPoolFilter;
import models.Session;
import models.User;
import play.Logger;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.libs.Json;
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
			return ok(views.html.experimenter.myStudies.render(Experiment.all(),""));
    }
    
    public static Result searchUsers() throws SQLException, JsonProcessingException {
    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
    	final String[] genders = values.get("gender");
    	final String[] probandPools = values.get("probandPools");
        //final String[] excludedExp = values.get("excludedExp");
        final int semesterFrom = Integer.parseInt(values.get("semesterFrom")[0]);
        final int semesterUntil = Integer.parseInt(values.get("semesterUntil")[0]);
        final Integer probandHours = Integer.parseInt(values.get("probandHours")[0]);
        final String[] course = values.get("course");
        if(genders == null || probandPools == null || probandHours == null || course == null)
        	return ok("");
        String gender = "";
        if(genders.length > 1) {
        	gender = "both";
        }
        else {
			gender = genders[0];
    	}
    	Logger.info(genders[0]);
    	String n = User.search(gender, probandPools, semesterFrom, semesterUntil, course, probandHours);
		return ok(n);
    }
    
    public static Result myStudiesUpdated(String param) throws SQLException {  
		return ok(views.html.experimenter.myStudies.render(Experiment.all(), param));
	
	
}
    
    public static Result saveGeneralData(int id) throws SQLException {

    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
    	Logger.info(values.toString());
        final String name = values.get("expName")[0];
        final String description = values.get("description")[0];
        final int duration = Integer.parseInt(values.get("duration")[0]);
        final int probandAmount = Integer.parseInt(values.get("probandAmount")[0]);
        final int expType = Integer.parseInt(values.get("expType")[0]);
        final float probandHours = Float.parseFloat(values.get("probandHours")[0]);
        final int building = Integer.parseInt(values.get("buildings")[0]);
        final int defaultRoom_id = Integer.parseInt(values.get("room_"+building)[0]);
        final String sendInvitations = values.get("sendInvitations")[0];
        final String[] probandPools = values.get("probandPools");
        final String[] excludedExp = values.get("excludedExp");
        final int semesterFrom = Integer.parseInt(values.get("semesterFrom")[0]);
        final int semesterUntil = Integer.parseInt(values.get("semesterUntil")[0]);
        final String[] genders = values.get("gender");
        final String[] assignments = values.get("assignment");
        
        String gender = "";
        if(genders.length > 1) {
        	gender = "both";
        }
        else {
			gender = genders[0];
    	}
    	Logger.info(genders[0]);
    	
        
        int exp_id = 0;
        if(id == 0) {
            exp_id = Experiment.create(name, description, duration, probandHours, probandAmount, expType, defaultRoom_id);
        }
        else {
        	exp_id = id;
        	Experiment.update(exp_id, name, description, duration, probandHours, probandAmount, expType, defaultRoom_id);
        	Assignment.deleteByExperimentId(exp_id);
        }
        int filter_id = Filter.create(exp_id, gender, semesterFrom, semesterUntil);
    	Logger.info("Filter angelegt: "+filter_id);
    	for(String p : probandPools) {
    		ProbandPoolFilter.create(filter_id, p);
    	}
    	if(excludedExp != null) {
	    	for(String p : excludedExp) {
	    		ExcludedExperiment.create(filter_id, p);
	    	}
    	}
        for(String user_id : assignments) {
        	int right = Integer.parseInt(values.get("assignment_rights_"+user_id)[0]);
        	int user_id_int = Integer.parseInt(user_id);
        	Assignment.create(exp_id, user_id_int, right);
        }
        String ret_str = String.valueOf(exp_id);
		return ok(ret_str);
    }
    
    public static Result saveSessions(int id) throws SQLException {
    	JsonNode values = request().body().asJson();
		int room_id = values.path("room").asInt();
    	JsonNode events = values.path("events");
    	Iterator<JsonNode> i = events.elements();
    	if(id != 0)
    		Session.deleteByExperimentId(id);
    	while(i.hasNext()) {
    		JsonNode event = i.next();
    		long datetime = event.path("datetime").asLong();
	    	Iterator<JsonNode> i2 = event.path("participations").elements();
	    	int session_id = Session.create(id, datetime, room_id);
	    	while(i2.hasNext()) {
	    		JsonNode part = i2.next();
	    		int user_id = part.path("user_id").asInt();
	    		Logger.info("-- "+user_id);
	    		Participation.create(session_id, user_id);
	    	}
	    	
    	}

    	return ok();
    }
    
    public static Result jsonByExperimentId(long id) throws SQLException, JsonProcessingException {
    	String n = Session.jsonByExperimentId(id);
		return ok(n);
    }
    
    public static Result getExperiments() throws SQLException, JsonProcessingException {
    	String query = request().body().asFormUrlEncoded().get("query")[0];
    	
    	String n = Experiment.jsonByString(query);
		return ok(n);
    }
    
    /**
     * Completes current study, shows attended probands and sessions
     *
     * @return
     */
    public static Result confirmParticipation(int experimentId) {  
        return ok(views.html.experimenter.confirmParticipation.render(experimentId));
    }
    
    public static Result saveParticipation() throws SQLException {
    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
    	Logger.info(values.toString());
        final String[] partics = values.get("partics");
        for(String p : partics) {
        	Double proband_hours = Double.parseDouble(values.get("proband_hours_"+p)[0]);
        	int was_present = 1;
        	try {
        	String was_present_form = values.get("was_present_"+p)[0];
        	} catch (NullPointerException e) {
        		was_present = 0;
        	}
        	
        	int p_id = Integer.parseInt(p);
        	Participation.update(p_id, proband_hours, was_present);
        	
        }

        return ok(views.html.experimenter.myStudies.render(Experiment.all(),"successedit"));
    }



}