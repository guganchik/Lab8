package commands;

import app.CollectionManager;
import collections.Coordinates;
import collections.Vehicle;
import collections.VehicleType;
import data.Request;
import data.Response;
import java.util.Scanner;
import app.UserManager;
import collections.User;
import utils.Const;


/**
 * Команда Add
 */
public class Add implements ICommand{
    private final CollectionManager collectionManager;

    public Add(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        
        if (request.object == null) {
            return new Response(request.getCommand(), Const.ERROR);
        } else {
            Vehicle vehicle = (Vehicle) request.getObject();
            vehicle.setOwner(user.getLogin());
            if (collectionManager.add(vehicle)) {
                return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), true);
            } else {
                return new Response(request.getCommand(), Const.ERROR);
            }
        }
    }
    
    @Override
    public String toString() {
        return "add {element} - Add a new element to the collection";
    }
}

