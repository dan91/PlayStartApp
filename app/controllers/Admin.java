package controllers;

import java.util.Map;

import models.Building;
import models.Experiment;
import models.User;
import play.Logger;
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
        return ok(views.html.admin.admin.render());
    }


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
        return ok(views.html.admin.lab.render(Building.all()));
    }

/**
*Gets the coordinates from client (admin) for a new room
*
*@return
*/
    public static Result save(){
        
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        final String lat = values.get("latFld")[0];;
        final String fld = values.get("lngFld")[0];
        
        String created = "Lat: "+lat+"  Lng: "+fld;
         
        //return ok(submit.render(created));
        
        return ok(lab.render(Building.all()));
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