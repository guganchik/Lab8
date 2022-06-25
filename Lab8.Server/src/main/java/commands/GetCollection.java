package commands;

import app.CollectionManager;
import app.UserManager;
import collections.User;
import data.Request;
import data.Response;
import utils.Const;
/**
 * Команда Show
 */
public class GetCollection implements ICommand {
    private final CollectionManager collectionManager;

    public GetCollection(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), false);
    }
        
    @Override
    public String toString() {
        return "GUI";
    }
}