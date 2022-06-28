package commands;

import app.CollectionManager;
import app.UserManager;
import collections.Coordinates;
import collections.User;
import collections.Vehicle;
import collections.VehicleType;
import data.Request;
import data.Response;
import java.util.Scanner;
import utils.Const;
/**
 * Команда AddIfMax
 */
public class AddIfMax implements ICommand {

    private final CollectionManager collectionManager;
    
    public AddIfMax(CollectionManager collectionManager) {
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
            if (collectionManager.addIfMax(vehicle)) {
                return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), true);
            } else {
                return new Response("null", Const.SUCCESS, null, false);
            }
        }
    }
    
    

    @Override
    public String toString() {
        return "add_if_max {element} - Add a new element to the collection if its value is greater than the value of the largest element in this collection";
    }

}
