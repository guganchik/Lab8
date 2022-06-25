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
 * Команда RemoveGreater
 */
public class RemoveGreater implements ICommand {
    private final CollectionManager collectionManager;
    public RemoveGreater(CollectionManager collectionManager) {
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
            if (collectionManager.removeGreater(vehicle, user.getLogin())) {
                return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), true);
            } else {
                return new Response(request.getCommand(), Const.ERROR);
            }
        }
    }

    @Override
    public String toString() {
        return "remove_greater {element} - Remove from the collection all elements greater than the given";
    }

}
