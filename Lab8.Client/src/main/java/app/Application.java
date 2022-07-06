package app;

import anim.EatOperation;
import anim.MoveOperation;
import anim.StopOperation;
import collections.User;
import collections.Vehicle;
import common.InputCheck;
import data.CollectionInfo;
import data.Request;
import data.Response;

import frame.LoginFrame;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import frame.MainFrame;
import frame.MessageDialog;
import frame.RegisterFrame;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import static java.util.ResourceBundle.Control.TTL_DONT_CACHE;
import java.util.TreeSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import utils.Const;

public class Application implements Observer {
    
    private static Application instance;
    private User user = null;

    public static Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }            

    public static String[] LANGS = new String[] {"en_US", "ru_RU", "fi_FI", "ca_ES"};
    private String lang = "";
    private Locale locale;
    private DateFormat dateFormat;
    private ResourceBundle resourceBundle;
    
    public static String[] COLUMNS = new String[] {"id", "name", "x", "y", "engine", "capacity", "distance", "speed", "date", "type", "owner"};
    
    private LoginFrame loginFrame;
    private RegisterFrame registerFrame;
    private MainFrame mainFrame;
    
    private NetworkProvider networkProvider;

    //private TreeSet<Vehicle> collection;
    

    public Application() {
        setLang(0);
    }
    
    public void start(String address, int port) {
        
        loginFrame = new LoginFrame();
        registerFrame = new RegisterFrame();
        mainFrame = new MainFrame();
        
        try {
            networkProvider = new NetworkProvider(address, port);
            networkProvider.addObserver(this);
            networkProvider.startListener();
        } catch (IOException ex) {
            Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //mainFrame.show();
        loginFrame.show();
        
        /****************************************************/
        //user = User.of("1", getHash("1"));
        //mainFrame.updateUser(user);
        
        //Request request = new Request("get_collection", null, null, user.getLogin(), user.getHash());
        //networkProvider.send(request);
        /****************************************************/
        
        System.out.println("===== Application started! Server on " + address + ":" + port + " =====");
    }

    
    public LoginFrame getLoginFrame() {
        return loginFrame;
    }

    public RegisterFrame getRegisterFrame() {
        return registerFrame;
    }
    
    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public User getUser() {
        return user;
    }
    
    public boolean userLogin(String login, String password) {
        int result = InputCheck.checkCredentials(login, password);
        if (result != Const.SUCCESS) {
            showErrorDialog(result);
            return false;
        } else {
            User auser = User.of(login, getHash(password));
            Request request = new Request("user_login", null, auser, null, null);
            networkProvider.send(request);
            return true;
        }
    }
    
    public void userHaveNoLogin() {
        loginFrame.hide();
        registerFrame.show();
    }
    
    public boolean userRegister(String login, String password) {
        int result = InputCheck.checkCredentials(login, password);
        if (result != Const.SUCCESS) {
            showErrorDialog(result);
            return false;
        } else {
        User auser = User.of(login, getHash(password));
        Request request = new Request("user_register", null, auser, null, null);
        networkProvider.send(request);
            return true;
        }
    }

    public boolean deleteVehicle(String id, String owner) {
        if (!owner.equals(user.getLogin())) {
            showErrorDialog(Const.ERROR_301);
            return false;
        } else {
            Request request;
            String[] args = new String[1];
            args[0] = id;
            request = new Request("remove_by_id", args, null, user.getLogin(), user.getHash());
            networkProvider.send(request);
            showInfoDialog();
            return true;
        }
    }
    
    public boolean saveVehicle(String id, String name, String x, String y, String engine, String capacity, String distance, String speed, String type, String owner) {
        int result = InputCheck.checkVehicle(name, x, y, engine, capacity, distance, speed, type);
        if (result != Const.SUCCESS) {
            showErrorDialog(result);
            return false;
        } else if (!id.equals("") && !owner.equals(user.getLogin())) {
            showErrorDialog(Const.ERROR_301);
            return false;
        } else {
            Request request;
            Vehicle vehicle;
            if (id.equals("")) {
                vehicle = Vehicle.of(name, x, y, engine, capacity, distance, speed, type);
                request = new Request("add", null, vehicle, user.getLogin(), user.getHash());
            } else {
                //int aid = Integer.parseInt(id);
                vehicle = Vehicle.of(name, x, y, engine, capacity, distance, speed, type);
                //vehicle.setId(aid);
                String[] args = new String[1];
                args[0] = id;
                request = new Request("update", args, vehicle, user.getLogin(), user.getHash());
            }
            networkProvider.send(request);
            showInfoDialog();
            return true;
        }
    }
    
    public boolean eatVehicle(int id1, int id2) {
        Request request;
        String[] args = new String[2];
        args[0] = String.valueOf(id1);
        args[1] = String.valueOf(id2);
        request = new Request("eat", args, null, user.getLogin(), user.getHash());
        networkProvider.send(request);
        return true;
    }
    
    public boolean animateMove(MoveOperation moveOperation) {
        Request request;
        request = new Request("move", null, moveOperation, user.getLogin(), user.getHash());
        networkProvider.send(request);
        return true;
    }
    
    public boolean animateStop(StopOperation stopOperation) {
        Request request;
        request = new Request("stop", null, stopOperation, user.getLogin(), user.getHash());
        networkProvider.send(request);
        return true;
    }

    public boolean animateEat(EatOperation eatOperation) {
        Request request;
        request = new Request("eat", null, eatOperation, user.getLogin(), user.getHash());
        networkProvider.send(request);
        return true;
    }
    
    public boolean sendCommand(String command, String[] args, boolean confirm) {
        int result;
        if (command.equals("info")) {
            Request request = new Request(command, null, null, user.getLogin(), user.getHash());
            networkProvider.send(request);
        } else if (command.equals("max_by_id")) {
            Request request = new Request(command, null, null, user.getLogin(), user.getHash());
            networkProvider.send(request);
        } else if (command.equals("get_collection")) {
            Request request = new Request(command, null, null, user.getLogin(), user.getHash());
            networkProvider.send(request);
        } else if (command.equals("clear")) {
            Request request = new Request(command, null, null, user.getLogin(), user.getHash());
            networkProvider.send(request);
            if (confirm) {
                showInfoDialog();
            }
        } else if (command.equals("remove_greater") || command.equals("remove_lower")) {
            result = InputCheck.checkEnginePower(args[0]);
            if (result != Const.SUCCESS) {
                showErrorDialog(result);
                return false;
            }
            result = InputCheck.checkCapacity(args[1]);
            if (result != Const.SUCCESS) {
                showErrorDialog(result);
                return false;
            }
            result = InputCheck.checkDistanceTravelled(args[2]);
            if (result != Const.SUCCESS) {
                showErrorDialog(result);
                return false;
            }
            Vehicle vehicle = Vehicle.of(args[0], args[1], args[2]);
            Request request = new Request(command, null, vehicle, user.getLogin(), user.getHash());
            networkProvider.send(request);
            if (confirm) {
                showInfoDialog();
            }
        } else if (command.equals("add_if_max")) {
            result = InputCheck.checkVehicle(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            if (result != Const.SUCCESS) {
                showErrorDialog(result);
                return false;
            }
            Vehicle vehicle = Vehicle.of(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            Request request = new Request(command, null, vehicle, user.getLogin(), user.getHash());
            networkProvider.send(request);
            if (confirm) {
                showInfoDialog();
            }
        }
        return true;
    }
    
    public void showCollectionInfoDialog(CollectionInfo collectionInfo) {
        String text = 
            getLocalizedString("info.class") + collectionInfo.getType() + "\n" +
            getLocalizedString("info.date") + collectionInfo.getDate() + "\n" +
            getLocalizedString("info.size") + collectionInfo.getSize();
        new MessageDialog(mainFrame.getFrame(), getLocalizedString("info.title"), text);
    }
    
    public void showVehicleInfoDialog(Vehicle vehicle) {
        String text = 
            "id=" + vehicle.getId() + "\n" +
            "name=" + vehicle.getName() + "\n" +
            "x=" + vehicle.getCoordinates().getX() + "\n" +
            "y=" + vehicle.getCoordinates().getY() + "\n" +
            "engine=" + vehicle.getEnginePower() + "\n" +
            "capacity=" + vehicle.getCapacity() + "\n" +
            "distance=" + vehicle.getDistanceTravelled() + "\n" +
            "speed=" + vehicle.getSpeed() + "\n" +
            "date=" + vehicle.getCreationDate() + "\n" +
            "type=" + vehicle.getTypeAsString() + "\n" +
            "owner=" + vehicle.getOwner();
        new MessageDialog(mainFrame.getFrame(), getLocalizedString("vehicle.title"), text);
    }
    
    
    /**
    public TreeSet<Vehicle> getCollection() {
        return collection;
    }
    */
    
    public void showErrorDialog(int code) {
        JOptionPane.showMessageDialog(new JFrame(), getLocalizedString("error.code." + code), getLocalizedString("error"),
            JOptionPane.ERROR_MESSAGE);    
    }
    
    public void showInfoDialog() {
        JOptionPane.showMessageDialog(new JFrame(), getLocalizedString("success"), getLocalizedString("information"),
            JOptionPane.INFORMATION_MESSAGE);    
    }
    

    
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof NetworkProvider && arg instanceof Response) {
            Response response = (Response)arg;
            //System.out.println(response.toString());
            if (response.getResult() == Const.SUCCESS) {
                if (response.getCommand().equals("user_login") || response.getCommand().equals("user_register")) {
                    user = (User)response.getObject();
                    Request request = new Request("get_collection", null, null, user.getLogin(), user.getHash());
                    networkProvider.send(request);
                    mainFrame.updateUser(user);
                    registerFrame.hide();
                    loginFrame.hide();
                    mainFrame.show();
                } else if (
                        response.getCommand().equals("get_collection") || 
                        response.getCommand().equals("remove_by_id") ||
                        response.getCommand().equals("update") ||
                        response.getCommand().equals("clear") || 
                        response.getCommand().equals("remove_greater") || 
                        response.getCommand().equals("remove_lower") ||
                        response.getCommand().equals("add_if_max") ||
                        response.getCommand().equals("add")
                        ) {
                    TreeSet<Vehicle> collection = (TreeSet<Vehicle>)response.getObject();
                    mainFrame.updateCollection(collection);
                    /**
                } else if (response.getCommand().equals("add")) {
                    //Vehicle vehicle = (Vehicle)response.getObject();
                    //collection.add(vehicle);
                    mainFrame.updateCollection(collection);
                    */
                } else if (response.getCommand().equals("max_by_id")) {
                    if (response.getObject() != null) {
                        showVehicleInfoDialog((Vehicle)response.getObject());
                    }
                } else if (response.getCommand().equals("info")) {
                    showCollectionInfoDialog((CollectionInfo)response.getObject());
                } else if (response.getCommand().equals("move")) {
                    mainFrame.animateMove((MoveOperation)response.getObject());
                } else if (response.getCommand().equals("stop")) {
                    mainFrame.animateStop((StopOperation)response.getObject());
                } else if (response.getCommand().equals("eat")) {
                    mainFrame.animateEat((EatOperation)response.getObject());
                }
            } else {
                showErrorDialog(response.getResult());
            }
        }
    }
    
    public static String getHash(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Critical error! SHA-256 algotithm is not found!");
            System.exit(1);
        }
        byte[] encoded = Base64.getEncoder().encode(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        return new String(encoded);
    }

    public String getLocalizedString(String key) {
        return resourceBundle.getString(key);
    }

    public Locale getLocale() {
        return locale;
    }

    public DateFormat getDateFormat() {
        return dateFormat;
    }
    
    public void setLang(int index) {
        if (!lang.equals(LANGS[index])) {
            lang = LANGS[index];
            locale = new Locale(lang);
            //ResourceBundle.clearCache();
            //resourceBundle = ResourceBundle.getBundle("i18n", locale);
            resourceBundle = ResourceBundle.getBundle("i18n_"+locale.getLanguage(), new ResourceBundle.Control() {
                @Override
                public long getTimeToLive(String arg0, Locale arg1) {
                    return TTL_DONT_CACHE;
                }
            });
            if (locale.getLanguage().startsWith("ru")) {
                dateFormat = DateFormat.getDateInstance();
            } else {
                dateFormat = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
            }
            if (user != null) {
                Request request = new Request("get_collection", null, null, user.getLogin(), user.getHash());
                networkProvider.send(request);
            }
        }
    }
}
