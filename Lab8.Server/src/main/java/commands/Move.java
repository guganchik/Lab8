package commands;

import anim.MoveOperation;
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
public class Move implements ICommand{

    private final CollectionManager collectionManager;
    public Move(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        MoveOperation operation = (MoveOperation)request.getObject();
        
        //System.out.print("Move ");
        if (collectionManager.updateElementXY(operation.getVehicleId(), (float)operation.getStartPoint().getX(), (float)operation.getStartPoint().getY(), user.getLogin())) {
            return new Response(request.getCommand(), Const.SUCCESS, operation, true);
        } else {
            return new Response(request.getCommand(), Const.ERROR);
        }
    }
    
    @Override
    public String toString() {
        return "";
    }
}
