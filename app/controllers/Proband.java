package controllers;

import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import net.fortuna.ical4j.model.DateTime;
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
	

	public static Result available() {
		return ok(views.html.proband.available.render());
	}

	public static Result completed() {
		return ok(views.html.proband.completed.render());
	}

	public static void main(String[] args) throws Exception {

		Calendar start = new GregorianCalendar();
		// Zum Überblick das Format:

		// Achtung der Monat beginnt bei 0 = Januar :D :D :D
		// YYYY-M-D-H-M
		start.set(2014, 7, 15, 17, 0);

		Calendar end = new GregorianCalendar();
		end.set(2014, 7, 15, 23, 55);
		DateTime startTime = new DateTime(start.getTime());
		DateTime endTime = new DateTime(end.getTime());

		// Create event
		VEvent eightHourEvent = new VEvent(startTime, endTime,
				"Ende Teaminterne Deadline");

		net.fortuna.ical4j.model.Calendar cal = new net.fortuna.ical4j.model.Calendar();
		// add product Id
		cal.getProperties().add(
				new ProdId("-//Mozilla.org/NONSGML Mozilla Calendar V1.1//EN"));
		cal.getProperties().add(Version.VERSION_2_0);
		cal.getProperties().add(CalScale.GREGORIAN);

		// generate unique identifier
		UidGenerator ug = new UidGenerator("uidGen");
		Uid uid = ug.generateUid();

		eightHourEvent.getProperties().add(uid);

		// add event in ical4j calendar
		cal.getComponents().add(eightHourEvent);
		System.out.println(cal.toString());

		// save event in test.ics file

		// play hat es sonst nicht erkannt
		net.fortuna.ical4j.data.CalendarOutputter out = new net.fortuna.ical4j.data.CalendarOutputter();
		out.output(cal, new FileOutputStream("C:\\TeaminterneDeadline.ics"));

	}

	/**
	 * Registration form for people who are no students, but want to participate
	 * in studies.
	 * 
	 */

}