package app;

import commands.UserLogin;
import commands.Update;
import commands.UserRegister;
import commands.RemoveGreater;
import commands.Clear;
import commands.Help;
import commands.PrintAscending;
import commands.MaxById;
import commands.ICommand;
import commands.RemoveById;
import commands.RemoveLower;
import commands.AddIfMax;
import commands.Info;
import commands.Exit;
import commands.Add;
import commands.Eat;
import commands.EatOld;
import commands.GetCollection;
import commands.Move;
import commands.Stop;
import data.Request;
import data.Response;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, который хранит все возможные команды и вызывает их по ключу.
 */
public class DataProvider {
    public static final HashMap<String, ICommand> commands = new HashMap<>();
    private CollectionManager collectionManager;
    
    public DataProvider() {
        collectionManager = new CollectionManager();
        commands.put("help", new Help());                                       
        commands.put("clear", new Clear(collectionManager));                    
        commands.put("info", new Info(collectionManager));                      // done
        commands.put("add_if_max", new AddIfMax(collectionManager));
        commands.put("remove_greater", new RemoveGreater(collectionManager));   
        commands.put("remove_lower", new RemoveLower(collectionManager));       
        commands.put("update", new Update(collectionManager));                  // GUI
        commands.put("remove_by_id", new RemoveById(collectionManager));        // GUI
        commands.put("max_by_id", new MaxById(collectionManager));              
        commands.put("print_ascending", new PrintAscending(collectionManager)); // Дублирует сортировку в таблице

        commands.put("eat1", new EatOld(collectionManager));                        // GUI
        commands.put("add", new Add(collectionManager));                        // GUI
        commands.put("user_register", new UserRegister());                      // GUI
        commands.put("user_login", new UserLogin());                            // GUI
        commands.put("get_collection", new GetCollection(collectionManager));   // GUI
        commands.put("exit", new Exit());                                       
        
        commands.put("move", new Move(collectionManager));                      // GUI
        commands.put("stop", new Stop(collectionManager));                      // GUI
        commands.put("eat", new Eat(collectionManager));                      // GUI
        
    }

    public Response execute(Request request) {
        
        /**
        try {
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException ex) {
            Logger.getLogger(NetworkProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        
        if (commands.containsKey(request.getCommand())) {
            return commands.get(request.getCommand()).execute(request);
        } else {
            return new Response(request.getCommand(), null, "Command not found!\n\n");
        }
    }
    
    /**
    public void doSave() {
        collectionManager.save();
    }
    */
    
}
