package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

//TODO verschiedene Loginbereiche sichern

//we can start protecting actions with authentication. Play allows us to do this using action composition. 
//Action composition is the ability to compose multiple actions together in a chain. 
//Each action can do something to the request before delegating to the next action, 
//and can also modify the result. An action can also decide not to pass the request onto the next action, and instead generate the result itself.

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("email");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
}