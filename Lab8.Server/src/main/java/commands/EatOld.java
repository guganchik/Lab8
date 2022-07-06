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
 * Команда Update
 */
public class EatOld implements ICommand{

    private final CollectionManager collectionManager;
    public EatOld(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        
        Vehicle vehicle1 = null;
        Vehicle vehicle2 = null;
        try {
            int id1 = Integer.parseInt(request.getArgs()[0]);
            vehicle1 = collectionManager.getById(id1);
            if (vehicle1 == null) {
                return new Response(request.getCommand(), Const.ERROR);
            }
            int id2 = Integer.parseInt(request.getArgs()[1]);
            vehicle2 = collectionManager.getById(id2);
            if (vehicle2 == null) {
                return new Response(request.getCommand(), Const.ERROR);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        catch (NumberFormatException e) {
            return new Response(request.getCommand(), Const.ERROR);
        }

        if (collectionManager.eatElementOld(vehicle1, vehicle2, user.getLogin())) {
            return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), true);
        } else {
            return new Response(request.getCommand(), Const.ERROR);
        }
    }
    
    @Override
    public String toString() {
        return "update id {element} - Update the value of the collection element whose id is equal to the given one";
    }
}
