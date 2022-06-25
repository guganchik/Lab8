package commands;

import app.CollectionManager;
import app.DataProvider;
import app.UserManager;
import collections.User;
import data.Request;
import data.Response;
import java.util.Scanner;
import utils.Const;
/**
 * Команда Clear
 */
public class Clear implements ICommand {

    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        
        if (collectionManager.clear(user.getLogin())) {
            return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), true);
        } else {
            return new Response(request.getCommand(), Const.ERROR);
        }
    }
    

    @Override
    public String toString() {
        return "clear - Clear the collection";
    }

}
