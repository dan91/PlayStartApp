package controllers.account;

import controllers.Application;
import models.User;
import models.utils.AppException;
import models.utils.Hash;
import models.utils.Mail;
import org.apache.commons.mail.EmailException;
import play.Configuration;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.Result;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static play.data.Form.form;

/**
 * Signup to PlayStartApp : save and send confirm mail.
 * <p/>
 * User: yesnault
 * Date: 31/01/12
 */
public class Signup extends Controller {

    /**
     * Display the create form.
     *
     * @return create form
     */
    

    public static Result register() {
        return ok(views.html.account.signup.register.render());
    }

}
