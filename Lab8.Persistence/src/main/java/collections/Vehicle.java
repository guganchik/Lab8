package collections;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import utils.Const;

/**
 * Класс, элементы которого хранятся в коллекции
 */
public class Vehicle implements java.lang.Comparable, Serializable {
    
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float enginePower; //Значение поля должно быть больше 0
    private Long capacity; //Поле не может быть null, Значение поля должно быть больше 0
    private Double distanceTravelled; //Поле может быть null, Значение поля должно быть больше 0
    private VehicleType type; //Поле не может быть null
    private String owner; //Поле не может быть null, Строка не может быть пустой
    
    /**
     * constructor, just set fields
     * @param id                        Идентификатор
     * @param name                      имя
     * @param coordinates               координаты
     * @param enginePower               мощность двигателя
     * @param capacity                  емкость ТС
     * @param distanceTravelled         Пробег
     * @param type                      Тип ТС
     */

    public Vehicle(int id, String name, Coordinates coordinates, float enginePower, Long capacity, Double distanceTravelled, VehicleType type, String owner){
        creationDate = new Date();
        //System.out.println("creationDate: " + creationDate);
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.distanceTravelled = distanceTravelled;
        this.type = type;
        this.owner = owner;
    }

    public Vehicle(int id, String name, Coordinates coordinates, Date creationDate, float enginePower, Long capacity, Double distanceTravelled, VehicleType type, String owner){
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.enginePower = enginePower;
        this.capacity = capacity;
        this.distanceTravelled = distanceTravelled;
        this.type = type;
        this.owner = owner;
    }
    
    public Vehicle() {
    }

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) throws NullPointerException {
        if (name != null && !name.isEmpty())
            this.name = name;
        else throw new NullPointerException();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public float getEnginePower() {
        return enginePower;
    }

    public void setEnginePower(float enginePower) {
        this.enginePower = enginePower;
    }

    public Long getCapacity() {
        return capacity;
    }

    public Double getDistanceTravelled() {
        return distanceTravelled;
    }

    public VehicleType getType() {
        return type;
    }
    
    public String getTypeAsString() {
        return type.toString();
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public void incCapacity(Long extra) {
        this.capacity = this.capacity + extra;
    }

    public void setDistanceTravelled(Double distanceTravelled) {
        this.distanceTravelled = distanceTravelled;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    @Override
    public int compareTo(Object o) {
        if (this == o)
            return 0;
        if (o == null || getClass() != o.getClass())
            return -1;

        Vehicle other = (Vehicle) o;
        return (this.id-other.id);
    }

    @Override
    public String toString() {
        /**
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(timeFormat.parse(creationDate));
        } catch (Exception e) {
        */
        return "-----------------------------------------------------" + "\n" + "id=" + id + "\n" + "name=" + name + "\n" + "coordinates=" + coordinates + "\n" + "creationDate=" + Const.timeFormat.format(creationDate) + "\n" + "enginePower=" + enginePower + "\n" + "capacity=" + capacity + "\n" + "distanceTravelled=" + distanceTravelled + "\n" + "type=" + type + "\n" + "owner=" + owner + "\n" + "-----------------------------------------------------";
    }

    public static Vehicle input(Scanner scanner, Boolean script) {
        VehicleInput input = new VehicleInput(scanner, script);
        Vehicle vehicle = input.resultElement(0);
        return vehicle;
    }
    
    public static Vehicle of(String name, String x, String y, String engine, String capacity, String distance, String type) {
        Vehicle vehicle = new Vehicle();
        vehicle.creationDate = new Date();
        vehicle.name = name;
        vehicle.coordinates = new Coordinates(Float.parseFloat(x), Float.parseFloat(y));
        vehicle.enginePower = Float.parseFloat(engine);
        vehicle.capacity = Long.parseLong(capacity);
        vehicle.distanceTravelled = Double.parseDouble(distance);
        vehicle.type = VehicleType.valueOf(type);
        return vehicle;
    }
    
    public static Vehicle of(String engine, String capacity, String distance) {
        Vehicle vehicle = new Vehicle();
        vehicle.enginePower = Float.parseFloat(engine);
        vehicle.capacity = Long.parseLong(capacity);
        vehicle.distanceTravelled = Double.parseDouble(distance);
        return vehicle;
    }
    
}