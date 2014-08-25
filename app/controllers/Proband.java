package controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
import models.Participation;
//import net.fortuna.ical4j.model.DateTime;
//import net.fortuna.ical4j.model.component.VEvent;
//import net.fortuna.ical4j.model.property.CalScale;
//import net.fortuna.ical4j.model.property.ProdId;
//import net.fortuna.ical4j.model.property.Uid;
//import net.fortuna.ical4j.model.property.Version;
//import net.fortuna.ical4j.util.UidGenerator;
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
 * Login and Logout. User: yesnault
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
	
	public static Result registerExp(int session_id) throws SQLException {
		int user_id = 1; // SESSION VARIABLE EINSETZEN
		Participation.create(session_id, user_id);
		return ok(views.html.proband.registered.render());
	}

	public static Result available() {
		return ok(views.html.proband.available.render());
	}

	public static Result completed() {
		return ok(views.html.proband.completed.render());
	}

public static Result authenticate(){
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		final String email = values.get("email")[0];
		final String password = values.get("password")[0];
//        if(User.authenticate(email, password)) {
//        	
//        }
		//session("user-id", user_id);
        
}
	public static Result getICS(){
		
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
		final String experiment_name = values.get("experiment_name")[0];
		final String date = values.get("date")[0];
        final String start_time = values.get("start_time")[0];
        final String end_time = values.get("end_time")[0];
        
        response().setHeader ("Content-Disposition", "attachment;filename="+experiment_name+".ics");
		response().setContentType("text/calendar");
        
		
		
//		 Logger.debug(String.valueOf(felder.length));
		 
		Calendar start = new GregorianCalendar(); 
		// Zum Überblick das Format:
		// YYYY-M-D-H-M 
		// Achtung der Monat beginnt bei 0 = Januar :D :D :D
        int year = Integer.parseInt(date.split("\\.")[2]);
        int month = Integer.parseInt(date.split("\\.")[1])   -1;
        int day = Integer.parseInt(date.split("\\.")[0]);
        //Uhrzeit im Format: HH:00 deswegen erstmal die Stunden holen: start_time.split(":")[0]  
        // dann die Minuten start_time.split(":")[1]
		start.set(year,month, day, Integer.parseInt(start_time.split(":")[0]), Integer.parseInt(start_time.split(":")[1]));

		
		Calendar end = new GregorianCalendar();
		end.set(year, month, day, Integer.parseInt(end_time.split(":")[0]), Integer.parseInt(end_time.split(":")[1]));
		DateTime startTime = new DateTime(start.getTime());
		DateTime endTime = new DateTime(end.getTime());
		
		// Create event
		VEvent session = new VEvent(startTime, endTime,
				"Studie: "+experiment_name);

		net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
		// add product Id
		cal.getProperties().add(
				new ProdId("-//Tom Aussenhofer mit ICal4j  V1.0//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);

		// generate unique identifier
		UidGenerator ug = null;
		try {
			ug = new UidGenerator("uidGen");
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Uid uid = ug.generateUid();

		session.getProperties().add(uid);

		// add event in ical4j calendar
		cal.getComponents().add(session);

		
		try {
			
			
			CalendarOutputter outputter = new CalendarOutputter();
			outputter.setValidating(false);
			
			File file = new File(experiment_name);
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			
			outputter.output(cal, fw);
			fw.close();
			
			
			return ok(file);
			
		} catch(Exception e){
			
			Logger.error(e.toString());
			return badRequest(e.toString());
		}

		 
//		 return ok();
	}	
		
		

}