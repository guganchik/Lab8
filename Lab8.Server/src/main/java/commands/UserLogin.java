package commands;

import app.UserManager;
import collections.User;
import commands.ICommand;
import data.Request;
import data.Response;
import utils.Const;

public class UserLogin implements ICommand{

    public UserLogin() {
    }
    
    @Override
    public Response execute(Request request) {
        if (request.object == null) {
            return new Response(request.getCommand(), Const.ERROR);
        } else {
            User user = (User) request.getObject();
            User auser = UserManager.getInstance().get(user.getLogin(), user.getHash());
            if (auser == null) {
                return new Response(request.getCommand(), Const.ERROR_203);
            } else {
                return new Response(request.getCommand(), Const.SUCCESS, user, false);
            }
        }
    }
    
    @Override
    public String toString() {
        return "user_login - Authorize with login and password";
    }
}

