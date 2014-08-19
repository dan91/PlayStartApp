package controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import com.sun.mail.iap.Response;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;
import models.Participation;
import models.RegisteredMapper;
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
	
	public static Result getICS(){
			final Map<String, String[]> values = request().body().asFormUrlEncoded();
			final String experiment_name = values.get("experiment_name")[0];
			final String date = values.get("date")[0];
	        final String start_time = values.get("start_time")[0];
	        final String end_time = values.get("end_time")[0];
	        
	        response().setHeader ("Content-Disposition", "attachment;filename="+experiment_name+".ics");
			response().setContentType("text/calendar");
	        
			
			
	        Calendar start = new GregorianCalendar();
			// Zum Überblick das Format:

			// Achtung der Monat beginnt bei 0 = Januar :D :D :D
	        int year = Integer.parseInt(date.split(".")[2]);
	        int month = Integer.parseInt(date.split(".")[1])   -1;
	        int day = Integer.parseInt(date.split(".")[0]);
	        
			// YYYY-M-D-H-M
	        //Uhrzeit im Format: HH:00 deswegen erstmal die Stunden holen: start_time.split(":")[0]  
	        // dann die Minuten start_time.split(":")[1]
			start.set(year,month, day, Integer.parseInt(start_time.split(":")[0]), Integer.parseInt(start_time.split(":")[1]));

			Calendar end = new GregorianCalendar();
			end.set(year, month, day, Integer.parseInt(end_time.split(":")[0]), Integer.parseInt(end_time.split(":")[1]));
			DateTime startTime = new DateTime(start.getTime());
			DateTime endTime = new DateTime(end.getTime());

			// Create event
			VEvent session = new VEvent(startTime, endTime,
					"Ende Teaminterne Deadline");

			net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
			// add product Id
			cal.getProperties().add(
					new ProdId("-// Tom Außenhofer mit ICal4j V1.0//DE"));
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

			// save event in test.ics file

			// play hat es sonst nicht erkannt
			
			try {
				
				
				CalendarOutputter outputter = new CalendarOutputter();
				outputter.setValidating(false);
				
				File file = new File(experiment_name);
				FileWriter fw = new FileWriter(file.getAbsoluteFile());
				
				outputter.output(cal, fw);
				
				fw.close();
				
				Logger.info(file.toString());
				
				return ok(file);
			} catch (IOException | ValidationException e) {
				// TODO Auto-generated catch block
				return ok(e.toString());
			}
		
		
	}
	

	public static Result available() {
		return ok(views.html.proband.available.render());
	}

	public static Result completed() {
		return ok(views.html.proband.completed.render());
	}

//	public static void main(String[] args) throws Exception {
//
//		Calendar start = new GregorianCalendar();
//		// Zum Überblick das Format:
//
//		// Achtung der Monat beginnt bei 0 = Januar :D :D :D
//		// YYYY-M-D-H-M
//		start.set(2014, 7, 15, 17, 0);
//
//		Calendar end = new GregorianCalendar();
//		end.set(2014, 7, 15, 23, 55);
//		DateTime startTime = new DateTime(start.getTime());
//		DateTime endTime = new DateTime(end.getTime());
//
//		// Create event
//		VEvent eightHourEvent = new VEvent(startTime, endTime,
//				"Ende Teaminterne Deadline");
//
//		net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
//		// add product Id
//		cal.getProperties().add(
//				new ProdId("-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN"));
//		cal.getProperties().add(Version.VERSION_2_0);
//		cal.getProperties().add(CalScale.GREGORIAN);
//
//		// generate unique identifier
//		UidGenerator ug = new UidGenerator("uidGen");
//		Uid uid = ug.generateUid();
//
//		eightHourEvent.getProperties().add(uid);
//
//		// add event in ical4j calendar
//		cal.getComponents().add(eightHourEvent);
//		System.out.println(cal.toString());
//
//		// save event in test.ics file
//
//		// play hat es sonst nicht erkannt
//		net.fortuna.ical4j.data.CalendarOutputter out = new net.fortuna.ical4j.data.CalendarOutputter();
//		out.output(cal, new FileOutputStream("C:\\TeaminterneDeadline.ics"));
//
//	}

	/**
	 * Registration form for people who are no students, but want to participate
	 * in studies.
	 * 
	 */

}