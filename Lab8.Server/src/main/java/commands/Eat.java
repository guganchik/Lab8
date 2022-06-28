package commands;

import anim.EatOperation;
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
public class Eat implements ICommand{

    private final CollectionManager collectionManager;
    public Eat(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        EatOperation operation = (EatOperation)request.getObject();
        
        //System.out.print("Eat ");
        
        int updateId = -1;
        int deleteId = -1;
        long capacity = operation.getVehicle1Capacity() + operation.getVehicle2Capacity();
        if (operation.getVehicle1Capacity() > operation.getVehicle2Capacity()) {
            operation.setVehicle1Capacity(capacity);
            operation.setVehicle2Capacity(0);
            updateId = operation.getVehicle1Id();
            deleteId = operation.getVehicle2Id();
        } else if (operation.getVehicle1Capacity() < operation.getVehicle2Capacity()) {
            operation.setVehicle1Capacity(0);
            operation.setVehicle2Capacity(capacity);
            updateId = operation.getVehicle2Id();
            deleteId = operation.getVehicle1Id();
        }

        if (collectionManager.eatElement(deleteId, updateId, capacity)) {
            return new Response(request.getCommand(), Const.SUCCESS, operation, true);
        } else {
            return new Response("null", Const.SUCCESS, null, false);
        }
    }
    
    @Override
    public String toString() {
        return "update id {element} - Update the value of the collection element whose id is equal to the given one";
    }
}
