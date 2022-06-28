package app;


import collections.Vehicle;
import collections.VehicleType;
import collections.Coordinates;
import dao.DataAccessDriver;
import data.CollectionInfo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import utils.Const;
import java.util.stream.Collectors;

import java.util.*;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Класс управления коллекцией. Здесь происходят все изменения коллекции.
 */
public class CollectionManager {

    private final TreeSet<Vehicle> collection;
    static Date collectionDate = new Date();
    
    ThreadPoolExecutor processingExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
    
    //public static final Calendar tzUTC = Calendar.getInstance(TimeZone.getTimeZone("UTC")); 
    public CollectionManager() {
        collection = new TreeSet();
        readCollectionFromDB();
    }
    
    private synchronized boolean readCollectionFromDB() {
        boolean success = false;
        collection.clear();
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            pst = con.prepareStatement("SELECT \"id\", \"name\", \"creationDate\", \"vehicleType\", \"x\", \"y\", \"enginePower\", \"capacity\", \"distanceTravelled\", \"owner\", \"speed\" FROM \"vehicles\"");
            rs = pst.executeQuery();
            Vehicle vehicle;
            while (rs.next()) {
                vehicle = new Vehicle(
                        rs.getInt("id"),
                        rs.getString("name"),
                        new Coordinates(rs.getFloat("x"), rs.getFloat("y")),
                        new Date(rs.getTimestamp("creationDate").getTime()),
                        rs.getFloat("enginePower"),
                        rs.getLong("capacity"),
                        rs.getDouble("distanceTravelled"),
                        rs.getFloat("speed"),
                        VehicleType.valueOf(rs.getString("vehicleType")),
                        rs.getString("owner")
                );
                collection.add(vehicle);
            }
            success = true;
            rs.close(); rs = null;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while selecting vehicles");
        } finally {
            if (rs != null) { try { rs.close(); } catch (Exception e) { } rs = null; }
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
    
    private int getNextVehicleIdFromDB() {
        int result = 0;
        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            pst = con.prepareStatement("SELECT NEXTVAL('vehicle_id') AS id");
            rs = pst.executeQuery();
            if (rs.next()) {
                result = rs.getInt("id");
            }
            rs.close(); rs = null;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while selecting next vehicle id");
        } finally {
            if (rs != null) { try { rs.close(); } catch (Exception e) { } rs = null; }
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return result;
    }
    
    private boolean insertVehicleToDB(Vehicle vehicle) {
        boolean success = false;
        
        vehicle.setId(getNextVehicleIdFromDB());
        Instant instant = vehicle.getCreationDate().toInstant();
        Timestamp ts = instant != null ? Timestamp.from(instant) : null;
        
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            String sql = "INSERT INTO \"vehicles\" (\"id\", \"name\", \"creationDate\", \"vehicleType\", \"x\", \"y\", \"enginePower\", \"capacity\", \"distanceTravelled\", \"owner\", \"speed\") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            pst.setInt(1, vehicle.getId());
            pst.setString(2, vehicle.getName());
            pst.setTimestamp(3, ts);
            pst.setString(4, vehicle.getTypeAsString());
            pst.setFloat(5, vehicle.getCoordinates().getX());
            pst.setFloat(6, vehicle.getCoordinates().getY());
            pst.setFloat(7, vehicle.getEnginePower());
            pst.setLong(8, vehicle.getCapacity());
            pst.setDouble(9, vehicle.getDistanceTravelled());
            pst.setString(10, vehicle.getOwner());
            pst.setFloat(11, vehicle.getSpeed());
            pst.executeUpdate();
            success = true;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while adding vehicle");
        } finally {
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
    
    private boolean updateVehicleInDB(int id, Vehicle vehicle) {
        boolean success = false;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            String sql = "UPDATE \"vehicles\" SET \"name\"=?, \"vehicleType\"=?, \"x\"=?, \"y\"=?, \"enginePower\"=?, \"capacity\"=?, \"distanceTravelled\"=?, \"speed\"=? WHERE \"id\"=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, vehicle.getName());
            pst.setString(2, vehicle.getTypeAsString());
            pst.setFloat(3, vehicle.getCoordinates().getX());
            pst.setFloat(4, vehicle.getCoordinates().getY());
            pst.setFloat(5, vehicle.getEnginePower());
            pst.setLong(6, vehicle.getCapacity());
            pst.setDouble(7, vehicle.getDistanceTravelled());
            pst.setFloat(8, vehicle.getSpeed());
            pst.setInt(9, id);
            pst.executeUpdate();
            success = true;
            
            System.out.println("Vehile id:" + id + " x:" + vehicle.getCoordinates().getX() + " y:" + vehicle.getCoordinates().getY());
            
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while adding vehicle");
        } finally {
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
    
    
    
    private boolean deleteByOwnerFromDB(String owner) {
        boolean success = false;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            String sql = "DELETE FROM \"vehicles\" WHERE \"owner\"=?";
            pst = con.prepareStatement(sql);
            pst.setString(1, owner);
            pst.executeUpdate();
            success = true;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while deleting by owner");
        } finally {
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
    
    private boolean deleteByIdFromDB(int id, String owner) {
        boolean success = false;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            String sql = "DELETE FROM \"vehicles\" WHERE \"id\"=? AND \"owner\"=?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, id);
            pst.setString(2, owner);
            pst.executeUpdate();
            success = true;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while deleting by id");
        } finally {
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
    
    private boolean deleteGreaterFromDB(float enginePower, Long capacity, Double distanceTravelled, String owner) {
        boolean success = false;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            String sql = "DELETE FROM \"vehicles\" WHERE \"enginePower\">? AND \"capacity\">? AND \"distanceTravelled\">? AND \"owner\"=?";
            pst = con.prepareStatement(sql);
            pst.setFloat(1, enginePower);
            pst.setLong(2, capacity);
            pst.setDouble(3, distanceTravelled);
            pst.setString(4, owner);
            pst.executeUpdate();
            success = true;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while deleting greater");
        } finally {
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
    

    private boolean deleteLowerFromDB(float enginePower, Long capacity, Double distanceTravelled, String owner) {
        boolean success = false;
        Connection con = null;
        PreparedStatement pst = null;
        try {
            con = DataAccessDriver.getInstance().getConnection();
            String sql = "DELETE FROM \"vehicles\" WHERE \"enginePower\"<? AND \"capacity\"<? AND \"distanceTravelled\"<? AND \"owner\"=?";
            pst = con.prepareStatement(sql);
            pst.setFloat(1, enginePower);
            pst.setLong(2, capacity);
            pst.setDouble(3, distanceTravelled);
            pst.setString(4, owner);
            pst.executeUpdate();
            success = true;
            pst.close(); pst = null;
            con.close(); con = null;
        } catch (Exception e) {
            System.out.println("Error while deleting lower");
        } finally {
            if (pst != null) { try { pst.close(); } catch (Exception e) { } pst = null; }
            if (con != null) { try { con.close();} catch (Exception e) { } con = null; }
        }
        return success;
    }
    
    

    /**
     * Найти элемент по его id
     */
    public synchronized Vehicle getById(int id) {
        for(Vehicle v: collection) {
            if (v.getId() == id) {
                return v;
            }
        }
        return null;
    }
    
    /**
     *
     * @return Коллекция
     */
    public synchronized TreeSet<Vehicle> getCollection() {
        return collection;
    }
    
    /**
     * Добавить элемент в коллекцию
     */
    public synchronized boolean add(Vehicle vehicle) {
        if (insertVehicleToDB(vehicle)) {
            collection.add(vehicle);
            return true;
        }
        return false;
    }
    
    /**
     * Добавить элемент в коллекцию если  его EnginePower и Capacity больше максимального значения данных полей в этой коллекции
     */
    public synchronized boolean addIfMax(Vehicle element) {
        Iterator<Vehicle> itr = collection.iterator();
        while (itr.hasNext()) {
            Vehicle v = itr.next();
            if (v.getEnginePower() > element.getEnginePower() || v.getCapacity() > element.getCapacity()) {
                break;
            } else if(v.getEnginePower() < element.getEnginePower() && v.getCapacity() < element.getCapacity() && !itr.hasNext()) {
               if (insertVehicleToDB(element)) {
                   collection.add(element);
                   return true;
               } else {
                   return false;
               }
            }
        }
        return false;
    }
    
    /**
     * Удалить все элементы пользователя
     */
    public synchronized boolean clear(String owner) {
        if (deleteByOwnerFromDB(owner)) {
            readCollectionFromDB();
            return true;
        }
        return false;
    }
    
    /**
     * Информация о коллекции
     * @return
     */
    public synchronized CollectionInfo info() {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Const.timeFormat.parse(String.valueOf(collectionDate)));
        } catch (Exception e) {
        }
        return new CollectionInfo(collection.getClass(), calendar.getTime(), collection.size());
        
        /**
        return  "Type - " + collection.getClass() + "\n" +
                "Creation date - " +  calendar.getTime() + "\n"+
                "Amount of elements - " + collection.size();
        */
    }

    /**
     * Вывести элемент с максимальным Id
     */
    public synchronized Vehicle getMaxById() {
        return collection
                .stream()
                .max(Comparator.comparing(Vehicle::getId))
                .orElse(null);
    }

    /**
     * Вывести элементы коллекции в порядке возрастания
     * @return
     */
    public synchronized String printAscending() {
        if (collection.isEmpty()) {
            return "Collection is empty!\n";
        } else {
            return collection
                .stream()
                .sorted(Comparator.comparingInt(vehicle -> vehicle.getId()))
                .map(Vehicle::toString)
                .reduce("", (result, vehicle) -> result + vehicle + "\n");
        }
    }

    /**
     * Удалить элемент по его id
     */
    public synchronized boolean removeById(int id, String owner) {
        Vehicle avehicle = getById(id);
        if (avehicle == null || !avehicle.getOwner().equals(owner)) {
            return false;
        }
        if (deleteByIdFromDB(id, owner)) {
            collection.removeIf(vehicle -> vehicle.getId() == id && vehicle.getOwner().equals(owner));
            return true;
        }
        return false;
    }
    
    
    /**
     * Удалить элементы больше заданного
     */
    public synchronized boolean removeGreater(Vehicle element, String owner) {
        if (deleteGreaterFromDB(element.getEnginePower(), element.getCapacity(), element.getDistanceTravelled(), owner)) {
            readCollectionFromDB();
            //collection.removeIf(v -> v.getDistanceTravelled() > element.getDistanceTravelled() && v.getCapacity() > element.getCapacity() && v.getEnginePower() > element.getEnginePower());
            return true;
        }
        return false;
    }

    /**
     * Удалить элементы меньше заданного
     */
    public synchronized boolean removeLower(Vehicle element, String owner) {
        if (deleteLowerFromDB(element.getEnginePower(), element.getCapacity(), element.getDistanceTravelled(), owner)) {
            readCollectionFromDB();
            //collection.removeIf(v -> v.getDistanceTravelled() < element.getDistanceTravelled() && v.getCapacity() < element.getCapacity() && v.getEnginePower() < element.getEnginePower());
            return true;
        }
        return false;
    }
    
    
    /**
     * Вывести элементы коллекции
     * @return 
     */
    public synchronized String show() {
        if (collection.isEmpty()) {
            return "Collection is empty!\n";
        } else {
            return collection
                .stream()
                .map(Vehicle::toString)
                .reduce("", (result, vehicle) -> result + vehicle + "\n");
        }
    }
    


    /**
     * Обновить элемент
     */
    public synchronized boolean updateElement(Vehicle v1, Vehicle v2, String owner) {
        if (!v1.getOwner().equals(owner)) {
            return false;
        }
        if (updateVehicleInDB(v1.getId(), v2)) {
            v1.setName(v2.getName());
            v1.setCoordinates(v2.getCoordinates());
            v1.setEnginePower(v2.getEnginePower());
            v1.setCapacity(v2.getCapacity());
            v1.setDistanceTravelled(v2.getDistanceTravelled());
            v1.setSpeed(v2.getSpeed());
            v1.setType(v2.getType());
            return true;
        }
        return false;
    }
    
    public synchronized boolean eatElementOld(Vehicle v1, Vehicle v2, String owner) {
        if (!v1.getOwner().equals(owner)) {
            return false;
        }
        if (!v2.getOwner().equals(owner)) {
            return false;
        }
        if (v1.getCapacity() > v2.getCapacity()) {
            v1.incCapacity(v2.getCapacity());
            updateVehicleInDB(v1.getId(), v1);
            deleteByIdFromDB(v2.getId(), owner);
        } else {
            v2.incCapacity(v1.getCapacity());
            updateVehicleInDB(v2.getId(), v2);
            deleteByIdFromDB(v1.getId(), owner);
        }
        readCollectionFromDB();
        return true;
    }

    public synchronized boolean updateElementXY(int id, float x, float y, String owner) {
        Vehicle avehicle = getById(id);
        if (avehicle == null || !avehicle.getOwner().equals(owner)) {
            return false;
        }
        avehicle.getCoordinates().setX(x);
        avehicle.getCoordinates().setY(y);
        
        processingExecutor.execute(() -> {
            updateVehicleInDB(avehicle.getId(), avehicle);
        });
        return true;
    }
    
    public synchronized boolean eatElement(int id1, long capacity1, int id2, long capacity2) {
        int updateId = 0;
        int deleteId = 0;
        if (capacity1 > capacity2) {
            capacity1 = capacity1 + capacity2;
            capacity2 = 0;
            updateId = id1;
            deleteId = id2;
        } else if (capacity1 < capacity2) {
            capacity2 = capacity1 + capacity2;
            capacity1 = 0;
            updateId = id2;
            deleteId = id1;
        }
        
        
        /**
        
        
        Vehicle updateVehicle = null;
        Vehicle deleteVehicle = null;
        for(Vehicle vehicle: collection) {
            if (vehicle.getId() == updateId) {
                updateVehicle = vehicle;
            }
            if (vehicle.getId() == deleteId) {
                deleteVehicle = vehicle;
            }
        }
        
        if (updateVehicle != null)
        

        
        Vehicle avehicle = getById(id);
        if (avehicle == null || !avehicle.getOwner().equals(owner)) {
            return false;
        }
        avehicle.getCoordinates().setX(x);
        avehicle.getCoordinates().setY(y);
        
        processingExecutor.execute(() -> {
            updateVehicleInDB(avehicle.getId(), avehicle);
        });
        */
        return true;
    }
    
}
