package commands;

import app.UserManager;
import collections.User;
import data.Request;
import data.Response;
import utils.Const;

public class UserRegister implements ICommand{

    public UserRegister() {
    }
    
    @Override
    public Response execute(Request request) {
        if (request.object == null) {
            return new Response(request.getCommand(), Const.ERROR);
        } else {
            User user = (User) request.getObject();
            User auser = UserManager.getInstance().get(user.getLogin());
            
            if (auser != null) {
                return new Response(request.getCommand(), Const.ERROR_204);
            }
            if (auser == null && UserManager.getInstance().register(user)) {
                return new Response(request.getCommand(), Const.SUCCESS, user, false);
            } else {
                return new Response(request.getCommand(), Const.ERROR);
            }
        }
    }
    
    @Override
    public String toString() {
        return "user_register - Register new user";
    }
}

