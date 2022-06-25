package commands;

import app.CollectionManager;
import app.UserManager;
import collections.User;
import collections.Vehicle;
import data.Request;
import data.Response;
import java.util.Scanner;
import utils.Const;
/**
 * Команда RemoveById
 */
public class RemoveById implements ICommand {
    private final CollectionManager collectionManager;


    public RemoveById(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        
        try {
            int id = Integer.parseInt(request.getArgs()[0]);
            if (this.collectionManager.removeById(id, user.getLogin())) {
                return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), true);
            } else {
                return new Response(request.getCommand(), Const.ERROR);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        catch (NumberFormatException e) {
            return new Response(request.getCommand(), Const.ERROR);
        }
    }
    
    @Override
    public String toString() {
        return "remove_by_id id - Remove element from collection by its id";
    }
}
