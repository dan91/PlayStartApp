package controllers;

import java.sql.SQLException;
import java.util.Map;

import models.Building;
import models.Experiment;
import models.Room;
import models.User;
import play.Logger;
import play.Routes;
import play.data.Form;
import play.data.validation.Constraints;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;
import views.html.admin.lab;
import static play.data.Form.form;

/**
 * Login and Logout.
 * User: yesnault
 */
public class Admin extends Controller {

    

    /**
     * Displays the admin mainview
     *
     * @return
     */
    public static Result admin() {  
    //	try {
        return ok(views.html.admin.admin.render());
    //	} catch (SQLException e) {
    //		return badRequest(e.toString());
    //	}
    }

    
    
    /**
     * Displays the user_edit  view 
     * @TODO wieso gibts hier den fehler von eclipse?
     * @return
     * 
     * G
     */
    public static Result user_edit(Long id) throws SQLException { 
    	Form<User> editForm = form(User.class);
    	return ok(views.html.admin.user_edit.render(User.byId(id)));
    }
    
    
//    public static Result user_edit_Safe(Long id) throws SQLException { 
//    	   
//        final String name = values.get("userName")[0];
//        final String email = values.get("userEmail")[0];
//        final String sstart = values.get("userEmail")[0];
//        final String course = values.get("userEmail")[0];
//
//        
//        // zeigt in der console an ob der server es bekommen hat || geht hier was durch?
//        Logger.info(name);
//        try {
//        	User.add(name, email, sstart, course);
//        	
//			return ok(lab.render(Building.all(),Room.all(),Building.count()));
//			return ok(views.html.admin.user_edit.render(User.byId(id)));
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			return badRequest(e.toString());
//		}
//    	
//    }


        /**
     * Displays the admin user view
     *
     * @return
     */
    public static Result astudies() {  
        return ok(views.html.admin.studies.render());
    }

        /**
     * Displays the admin studies
     *
     * @return
     */
    public static Result auser() {  
        return ok(views.html.admin.user.render());
    }

     /**
     * Displays the admin edit page
     *
     * @return
     */
    public static Result edit() {  
        return ok(views.html.admin.edit_admin.render());
    }

        /**
     * Displays the admin lab
     *
     * @return
     */
    public static Result lab() {  
        try {
			return ok(views.html.admin.lab.render(Building.all(),Room.all(),Building.count()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return badRequest(e.toString());
		}
    }

/**
*Gets the coordinates from client (admin) for a new room
*
*@return
*/
    public static Result save(){
        
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final String name = values.get("buildingName")[0];
        final String description = values.get("pac-input")[0];
        
        // Float ist zu klein, schneidet die Hälfte ab!!!
        final double lat = Double.parseDouble(values.get("latFld")[0]);
        final double lng = Double.parseDouble(values.get("lngFld")[0]);
        
        
        
        String created = "Lat: "+lat;
        
        
        // zeigt in der console an ob der server es bekommen hat
        Logger.info(created);
        try {
        	Building.add(name, description, lat, lng);
        	
			return ok(lab.render(Building.all(),Room.all(),Building.count()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return badRequest(e.toString());
		}
    }
    
    
    
    
    public static Result saveEditBuilding(){
        
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        
        final Long id = Long.parseLong(values.get("id")[0]);
        final String name = values.get("name")[0];
        final String description = values.get("desc")[0];
        
        // Float ist zu klein, schneidet die Hälfte ab!!!
        final double lat = Double.parseDouble(values.get("lat")[0]);
        final double lng = Double.parseDouble(values.get("lng")[0]);
        

        // zeigt in der console an ob der server es bekommen hat
        Logger.info("Building with ID: "+id +" will be updated with 'Name:' "+
        name+", 'Description:' "+description+", 'Lat:' "+lat+", 'Lng:' "+lng);
        
        
        
      
        try {
        	Building.update(id, name, description, lat, lng);
			return ok("Änderungen gespeichert!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return badRequest(e.toString());
		}
    }
    
    
    
    public static Result deleteBuilding(){
    	
    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final Long idToDelete = Long.parseLong(values.get("id")[0]);
        
        final String nameToDelete = values.get("name")[0];
        
    	
    	
    	
    	Boolean hasRoomsInUse = null;
		try {
			hasRoomsInUse = Building.checkRoomsUsedInSession(idToDelete);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			return badRequest("ERROR CHECKING IF ROOMS IN USE:\n"+e1.toString());
		}
    		
			
    		if(!hasRoomsInUse){
    			try{
		    			Building.delete(idToDelete);
		    			return ok("Das Gebäude "+nameToDelete+" wurde ins Archiv verschoben!\n"
		        				+ "Es können nun keine Studien mehr in ihm stattfinden.");
    			} catch (SQLException e){
    					return badRequest("BUILDING TO ARCHIVE ERROR! "+e.toString());
    			}
    		}	
    		else if(hasRoomsInUse)
    			return badRequest("In diesem Gebäude finden noch Studien statt, deren Sessions noch nicht abgelaufen sind!");
    		else{
    			return badRequest("BOOLEAN CHECKING IF ROOMS IN USE == NULL: 'SOMETHING WENT VERY WRONG' ");
    		}
    		
		
    	
    }
    
    
    public static Result saveNewRoom(){
        
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final String name = values.get("roomName")[0];
        final String description = values.get("roomDescription")[0];
        final Long building_id = Long.parseLong(values.get("building_id")[0]);
        
        String created = name+"  ;  "+building_id+"  ;  "+description;
        
        
        // zeigt in der console an ob der server es bekommen hat
        Logger.info(created);
        try {
        	long newId = Room.add(name, description, building_id);
        	
			return ok(String.valueOf(newId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return badRequest("Fehler beim Anlegen des Raumes:\n"+ e.toString());
		}
    }
    
    public static Result saveEditRoom(){
        
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        
        final Long id = Long.parseLong(values.get("roomEdit_id")[0]);

        final String name = values.get("roomEditName")[0];
        final String description = values.get("roomEditDescription")[0];
        
        
        try {
        	Room.update(id, name, description);
			return ok(lab.render(Building.all(),Room.all(),Building.count()));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return badRequest(e.toString());
		}
    }
    
    
    public static Result deleteRoom(){
    	
    	final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final Long idToDelete = Long.parseLong(values.get("id")[0]);
        
        final String nameToDelete = values.get("name")[0];
        
    	
    	Boolean roomInUse = null ;
    	try {
    		roomInUse = Room.checkRoomInUse(idToDelete);
    		
    		if(roomInUse)
    			return badRequest("In diesem Raum finden noch Studien statt, deren Sessions noch nicht abgelaufen sind!");
    		
    		else{
    			Room.delete(idToDelete);
    			return ok("Der Raum "+nameToDelete+" wurde ins Archiv verschoben.\n"
    					+ "In ihm können keine weiteren Studien mehr stattfinden");
    		}
    		
		} catch (SQLException e) {
			return badRequest(e.toString());
		}
    	
    }


    
    /**
     * Displays the admin lmap
     *
     * @return
     */
    public static Result mapTest() {  
        return ok(views.html.admin.mapTest.render());
    }

       /**
     * Displays konto config
     *
     * @return
     */
    public static Result konto() {  
        return ok(views.html.admin.konto.render());
    }



}