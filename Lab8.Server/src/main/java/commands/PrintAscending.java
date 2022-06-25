package commands;

import app.CollectionManager;
import app.UserManager;
import collections.User;
import collections.Vehicle;
import data.Request;
import data.Response;
import java.util.Scanner;
/**
 * Команда PrintAscending
 */
public class PrintAscending implements ICommand{
    private final CollectionManager collectionManager;

    public PrintAscending(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    
    @Override
    public Response execute(Request request) {
        User user = UserManager.getInstance().get(request.getLogin(), request.getHash());
        if (user == null) {
            return new Response(request.getCommand(), null, "To execute commands login first!\n\n");
        }
        
        String output =  "======== Executing command (PrintAscending) =========\n"
                + collectionManager.printAscending()
                + "======== Operation success (PrintAscending) =========\n\n";
        return new Response(request.getCommand(), null, output);
    }

    @Override
    public String toString() {
        return "print_ascending - Display the elements of the collection in ascending order";
    }
}
