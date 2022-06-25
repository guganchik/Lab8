package app;


import dao.DataAccessDriver;
import data.Request;
import data.Response;
import java.io.File;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("Usage for launching server:  java -jar Lab6.Server.jar port password");
        System.out.println("Argument values:");
        System.out.println("port - port number for listeting connections, for example 30000");
        System.out.println("password - password to access DB");
        System.out.println("================== Starting Server ==================");

        if (null == args || args.length == 0) {
            System.out.println("Port is not specified");
            System.exit(1);
        }

        int port=0;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("Port must be a number!");
            System.exit(1);
        }
        
        if (args.length == 1) {
            System.out.println("Password is not specified");
            System.exit(1);
        }
        
        DataAccessDriver.getInstance().setPassword(args[1]);
        if (DataAccessDriver.getInstance().getConnection() == null) {
            System.out.println("Couldn't connect to DB");
            System.exit(1);
        }
        
        DataProvider dataProvider = new DataProvider();

        try {
            NetworkProvider networkProvider = new NetworkProvider(port);
            
            System.out.print("Type 'exit' to shutdown server: ");
            
            ThreadPoolExecutor readExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
            ThreadPoolExecutor processingExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
            
            while (true) {
                //readExecutor.execute(() -> {
                    Request request = networkProvider.receive();
                    if (request != null) {
                        processingExecutor.execute(() -> {
                            Response response = dataProvider.execute(request);
                            networkProvider.send(request.getClient(), response);
                        });
                    }
                //});
                if (System.in.available() > 0) {
                    String command = new Scanner(System.in).nextLine();
                    if (command.equals("exit")) {
                        readExecutor.shutdown();
                        processingExecutor.shutdown();
                        System.exit(0);
                    } else if (command.equals("clients")) {
                        networkProvider.printClients();
                    } else {
                        System.out.println("Command not found!\n");
                    }
                    System.out.print("Type 'exit' to shutdown server: ");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
