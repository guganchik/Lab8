package commands;

import anim.MoveOperation;
import anim.StopOperation;
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
public class Stop implements ICommand{

    private final CollectionManager collectionManager;
    public Stop(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), Const.ERROR);
        }
        StopOperation operation = (StopOperation)request.getObject();
        
        //System.out.print("Stop ");
        if (collectionManager.updateElementXY(operation.getVehicleId(), (float)operation.getTargetPoint().getX(), (float)operation.getTargetPoint().getY(), user.getLogin())) {
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
