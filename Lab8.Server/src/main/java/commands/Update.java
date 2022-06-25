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
public class Update implements ICommand{

    private final CollectionManager collectionManager;
    public Update(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        
        if (request.object == null) {
            return new Response(request.getCommand(), Const.ERROR);
        } else {
            
            Vehicle updateVehicle = null;
            try {
                int id = Integer.parseInt(request.getArgs()[0]);
                updateVehicle = collectionManager.getById(id);
                if (updateVehicle == null) {
                    return new Response(request.getCommand(), Const.ERROR);
                }
            }
            catch (ArrayIndexOutOfBoundsException e) {
                return new Response(request.getCommand(), Const.ERROR);
            }
            catch (NumberFormatException e) {
                return new Response(request.getCommand(), Const.ERROR);
            }
            
            Vehicle vehicle = (Vehicle) request.getObject();
            vehicle.setOwner(user.getLogin());
            if (collectionManager.updateElement(updateVehicle, vehicle, user.getLogin())) {
                return new Response(request.getCommand(), Const.SUCCESS, collectionManager.getCollection(), true);
            } else {
                return new Response(request.getCommand(), Const.ERROR);
            }
        }
    }
    
    @Override
    public String toString() {
        return "update id {element} - Update the value of the collection element whose id is equal to the given one";
    }
}
